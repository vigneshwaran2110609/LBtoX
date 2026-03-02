package com.example.LBtoX.controllers;
import com.example.LBtoX.DTO.TwitterTokenResponse;
import com.example.LBtoX.DTO.TwitterUserResponse;
import com.example.LBtoX.models.LetterboxdProfile;
import com.example.LBtoX.services.LetterboxdProfileService;
import com.example.LBtoX.services.TwitterCredentialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.security.MessageDigest;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173"}, allowCredentials = "true")
public class ProfileController {

    @Autowired
    private TwitterCredentialService credentialService;

    @Autowired
    private LetterboxdProfileService letterboxdProfileService;

    @Value("${twitter.api.key}")
    private String clientId;

    @Value("${twitter.api.secret}")
    private String clientSecret;

    @Value("${twitter.callback.url}")
    private String redirectUri;

    private String letterboxdIdVar;

    @PostMapping("/profile")
    public ResponseEntity<?> saveLetterboxdProfile(@RequestBody Map<String, String> request) {
        String letterboxdId = request.get("letterboxdId");

        if (letterboxdId == null || letterboxdId.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "letterboxdId is required"));
        }

        LetterboxdProfile profile = new LetterboxdProfile(letterboxdId);
        letterboxdProfileService.saveLetterboxdProfile(profile);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Letterboxd profile saved successfully");
        response.put("letterboxdId", letterboxdId);
        response.put("status", "SAVED");
        this.letterboxdIdVar = letterboxdId;
        return ResponseEntity.ok(response);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(
            @RequestParam String code,
            @RequestParam String state) {

        String codeVerifier = new String(Base64.getUrlDecoder().decode(state));

        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        WebClient webClient = WebClient.builder().build();

        TwitterTokenResponse tokenResponse =
            webClient.post()
                    .uri("https://api.x.com/2/oauth2/token")
                    .header(HttpHeaders.CONTENT_TYPE,
                            MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .header(HttpHeaders.AUTHORIZATION,
                            "Basic " + encodedCredentials)
                    .body(BodyInserters
                            .fromFormData("grant_type", "authorization_code")
                            .with("code", code)
                            .with("redirect_uri", redirectUri)
                            .with("code_verifier", codeVerifier)
                    )
                    .retrieve()
                    .bodyToMono(TwitterTokenResponse.class)
                    .block();
        
        TwitterUserResponse userResponse =
        webClient.get()
                .uri("https://api.x.com/2/users/me")
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + tokenResponse.getAccessToken())
                .retrieve()
                .bodyToMono(TwitterUserResponse.class)
                .block();

        String twitterId = userResponse.getData().getId();
        String username = userResponse.getData().getUsername();

        credentialService.saveOrUpdateCredentials(
            this.letterboxdIdVar,
            tokenResponse,
            twitterId,
            username
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:3000/success"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/{letterboxdId}/twitter/authorize")
    public ResponseEntity<?> authorizeTwitter(
            @PathVariable String letterboxdId,
            HttpSession session) throws Exception {

        // 1️⃣ Generate code_verifier
        SecureRandom secureRandom = new SecureRandom();
        byte[] code = new byte[32];
        secureRandom.nextBytes(code);
        String codeVerifier = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(code);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(codeVerifier.getBytes());
        String codeChallenge = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(hash);

        String state = Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier.getBytes());

        // 4️⃣ Build authorization URL
        String url = UriComponentsBuilder
                .fromUriString("https://x.com/i/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "tweet.read tweet.write users.read offline.access")
                .queryParam("state", state)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .build()
                .encode()
                .toUriString();
        // System.out.println("Redirecting to: " + url);
        System.out.println("Authorize Session ID: " + session.getId());
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(new URI(url))
                .build();
    }
}
