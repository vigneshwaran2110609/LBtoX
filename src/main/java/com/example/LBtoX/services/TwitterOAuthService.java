package com.example.LBtoX.services;

import com.example.LBtoX.models.TwitterCredential;
import com.example.LBtoX.repositories.TwitterCredentialRepository;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class TwitterOAuthService {

    @Value("${twitter.api.key}")
    private String apiKey;

    @Value("${twitter.api.secret}")
    private String apiSecret;

    @Value("${twitter.callback.url}")
    private String callbackUrl;

    private final TwitterCredentialRepository twitterCredentialRepository;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TWITTER_OAUTH_TOKEN_URL = "https://api.twitter.com/2/oauth2/token";
    private static final String TWITTER_OAUTH_AUTHORIZE_URL = "https://twitter.com/i/oauth2/authorize";

    public TwitterOAuthService(TwitterCredentialRepository twitterCredentialRepository) {
        this.twitterCredentialRepository = twitterCredentialRepository;
    }

    public String generateAuthorizationUrl(String letterboxdId) {
        
        String state = UUID.randomUUID().toString();
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);
        
        TwitterCredential cred = twitterCredentialRepository.findByLetterboxdId(letterboxdId)
                .orElseGet(() -> {
                    TwitterCredential t = new TwitterCredential();
                    t.setLetterboxdId(letterboxdId);
                    t.setCreatedAt(ZonedDateTime.now());
                    return t;
                });
        cred.setOauthState(state);
        cred.setCodeVerifier(codeVerifier);
        twitterCredentialRepository.save(cred);

        String authUrl = TWITTER_OAUTH_AUTHORIZE_URL +
                "?response_type=code" +
                "&client_id=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(callbackUrl, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode("tweet.read tweet.write users.read offline.access", StandardCharsets.UTF_8) +
                "&state=" + state +
                "&code_challenge=" + URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8) +
                "&code_challenge_method=S256";

        return authUrl;
    }

    /**
     * Exchange authorization code for access token
     */
    public TwitterCredential exchangeCodeForToken(String code, String letterboxdId, String state) throws IOException {
        // Retrieve stored credential to validate state and get codeVerifier
        Optional<TwitterCredential> maybe = twitterCredentialRepository.findByLetterboxdId(letterboxdId);
        if (maybe.isEmpty()) {
            throw new IOException("No pending OAuth state for letterboxdId: " + letterboxdId);
        }

        TwitterCredential pending = maybe.get();
        if (pending.getOauthState() == null || !pending.getOauthState().equals(state)) {
            throw new IOException("Invalid OAuth state");
        }

        String codeVerifier = pending.getCodeVerifier();
        if (codeVerifier == null) {
            throw new IOException("Missing code verifier for PKCE");
        }

        String basicAuth = Base64.getEncoder().encodeToString(
                (apiKey + ":" + apiSecret).getBytes(StandardCharsets.UTF_8)
        );

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", callbackUrl)
                .add("code_verifier", codeVerifier)
                .build();

        Request request = new Request.Builder()
                .url(TWITTER_OAUTH_TOKEN_URL)
                .addHeader("Authorization", "Basic " + basicAuth)
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to exchange code for token: " + response.body().string());
            }

            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            String accessToken = jsonNode.get("access_token").asText();
            String refreshToken = jsonNode.has("refresh_token") ? jsonNode.get("refresh_token").asText() : null;

            // Get Twitter user info
            TwitterUserInfo userInfo = getUserInfo(accessToken);

            // Populate and save credential
            pending.setTwitterId(userInfo.id);
            pending.setTwitterHandle(userInfo.username);
            pending.setAccessToken(accessToken);
            pending.setBearerToken(accessToken);
            pending.setUpdatedAt(ZonedDateTime.now());
            pending.setIsActive(true);
            // clear sensitive PKCE/state
            pending.setCodeVerifier(null);
            pending.setOauthState(null);

            return twitterCredentialRepository.save(pending);
        }
    }

    /**
     * Get Twitter user information using access token
     */
    private TwitterUserInfo getUserInfo(String accessToken) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/users/me")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get user info: " + response.body().string());
            }

            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode data = jsonNode.get("data");

            return new TwitterUserInfo(
                    data.get("id").asText(),
                    data.get("username").asText()
            );
        }
    }

    /**
     * Check if Letterboxd profile has Twitter credentials
     */
    public Optional<TwitterCredential> getCredentialsByLetterboxdId(String letterboxdId) {
        return twitterCredentialRepository.findByLetterboxdId(letterboxdId);
    }

    /**
     * Test if credentials are valid (can tweet)
     */
    public boolean testTweetCapability(String letterboxdId) {
        Optional<TwitterCredential> credential = getCredentialsByLetterboxdId(letterboxdId);
        if (credential.isEmpty()) {
            return false;
        }

        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets")
                .addHeader("Authorization", "Bearer " + credential.get().getBearerToken())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Tweet a message using stored credentials
     */
    public String tweetMessage(String letterboxdId, String message) throws IOException {
        Optional<TwitterCredential> credential = getCredentialsByLetterboxdId(letterboxdId);
        if (credential.isEmpty()) {
            throw new IllegalArgumentException("No Twitter credentials found for letterboxdId: " + letterboxdId);
        }

        RequestBody body = RequestBody.create(
                "{\"text\":\"" + message.replace("\"", "\\\"") + "\"}",
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets")
                .addHeader("Authorization", "Bearer " + credential.get().getBearerToken())
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to post tweet: " + response.body().string());
            }
            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("data").get("id").asText();
        }
    }

    private String generateCodeChallenge(String codeVerifier) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
        } catch (Exception e) {
            // fallback to plain
            return codeVerifier;
        }
    }

    private String generateCodeVerifier() {
        byte[] random = new byte[32];
        new java.security.SecureRandom().nextBytes(random);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(random);
    }

    public static class TwitterUserInfo {
        public String id;
        public String username;

        public TwitterUserInfo(String id, String username) {
            this.id = id;
            this.username = username;
        }
    }
}
