package com.example.LBtoX.services;
import org.springframework.stereotype.Service;
import com.example.LBtoX.DTO.TwitterTokenResponse;
import com.example.LBtoX.models.TwitterCredential;
import com.example.LBtoX.repositories.TwitterCredentialRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Value;

@Service
public class TwitterCredentialService {

    private final TwitterCredentialRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    @Value("${twitter.api.key}")
    private String clientId;
    @Value("${twitter.api.secret}")
    private String clientSecret;

    public TwitterCredentialService(TwitterCredentialRepository repository) {
        this.repository = repository;
    }

    public void saveOrUpdateCredentials(String letterboxdId, TwitterTokenResponse tokenResponse, String twitterId, String userName) {

        Optional<TwitterCredential> optional =
                repository.findByLetterboxdId(letterboxdId);

        TwitterCredential credential =
                optional.orElse(new TwitterCredential());

        credential.setLetterboxdId(letterboxdId);
        credential.setAccessToken(tokenResponse.getAccessToken());
        credential.setRefreshToken(tokenResponse.getRefreshToken());
        credential.setScope(tokenResponse.getScope());

        ZonedDateTime expiresAt =
                ZonedDateTime.now().plusSeconds(tokenResponse.getExpiresIn());

        credential.setExpiresAt(expiresAt);
        credential.setUpdatedAt(ZonedDateTime.now());

        if (credential.getCreatedAt() == null) {
            credential.setCreatedAt(ZonedDateTime.now());
        }
        credential.setTwitterId(twitterId);
        credential.setTwitterHandle(userName);
        repository.save(credential);
    }

    public String postTweet(String letterboxdId, String tweetText) throws Exception {

        TwitterCredential credential = getActiveCredential(letterboxdId);
        // refreshAccessToken(credential);
        if (credential.getExpiresAt().isBefore(ZonedDateTime.now())) {
            refreshAccessToken(credential);
        }
        System.out.println("here 1");
        String body = objectMapper.writeValueAsString(
                java.util.Map.of("text", tweetText)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.x.com/2/tweets"))
                .header("Authorization", "Bearer " + credential.getAccessToken())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        System.out.println("here 12");
        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("here 13");
        System.out.println(response.statusCode());
        if (response.statusCode() == 402){
                System.out.println("Payment Required !!!");
        }
        else if (response.statusCode() != 201 && response.statusCode() != 200) {
                System.out.println("Error");
        }
        System.out.println("here 10");
        return response.body();
    }

    private TwitterCredential getActiveCredential(String letterboxdId) {
        Optional<TwitterCredential> optional =
                repository.findByLetterboxdIdAndIsActiveTrue(letterboxdId);

        return optional.orElseThrow(() ->
                new RuntimeException("No active Twitter credential found"));
    }

    private void refreshAccessToken(TwitterCredential credential) throws Exception {

        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        String refreshBody =
                "grant_type=refresh_token" +
                "&refresh_token=" + URLEncoder.encode(
                    credential.getRefreshToken(), StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.x.com/2/oauth2/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic " + encodedCredentials)
            .POST(HttpRequest.BodyPublishers.ofString(refreshBody))
            .build();

        HttpResponse<String> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
        throw new RuntimeException(
                "Failed to refresh token: " + response.statusCode() + " " + response.body());
        }

        JsonNode json = objectMapper.readTree(response.body());

        credential.setAccessToken(json.get("access_token").asText());

        if (json.has("refresh_token")) {
                credential.setRefreshToken(json.get("refresh_token").asText());
        }

        int expiresIn = json.get("expires_in").asInt();
        credential.setExpiresAt(ZonedDateTime.now().plusSeconds(expiresIn));
        repository.save(credential);
        System.out.println("DOne saving");
        }    
}
