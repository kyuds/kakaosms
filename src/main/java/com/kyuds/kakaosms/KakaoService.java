package com.kyuds.kakaosms;

import com.kyuds.kakaosms.models.KakaoToken;
import com.kyuds.kakaosms.models.MessageRequest;
import com.kyuds.kakaosms.models.MessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.time.Instant;
import java.util.UUID;

@Service
public class KakaoService {
    private static final String BASE_URL = "";
    private static final String TOKEN_URL = "";
    private static final String SEND_URL = "";

    @Value("${kakao.biz.client.id}")
    private String clientId;

    @Value("${kakao.biz.client.secret}")
    private String clientSecret;

    @Value("${kakao.biz.sender.key}")
    private String senderKey;

    @Value("${kakaoex.sample.templatecode}")
    private String templateCode;

    private String token;
    private Instant expiry;

    private synchronized String getKakaoToken() {
        if (token == null || Instant.now().isAfter(expiry)) {
            try {
                ResponseEntity<KakaoToken> response = new RestTemplate().exchange(
                        BASE_URL + TOKEN_URL,
                        HttpMethod.POST,
                        getAuthRequestEntity(),
                        KakaoToken.class);
                KakaoToken kakaoToken = response.getBody();
                if (kakaoToken != null) {
                    switch (kakaoToken.getCode()) {
                        case "401":
                        case "403":
                        case "404":
                            System.out.println(kakaoToken.getResult().getDetail_message());
                            return null;
                    }
                    token = kakaoToken.getAccess_token();
                    expiry = Instant.now().plusSeconds(kakaoToken.getExpires_in() - 60);
                } else {
                    return null;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return token;
    }

    public MessageRequest sampleMessageRequest(String phone) {
        return new MessageRequest(
                senderKey,
                UUID.randomUUID().toString(),
                templateCode,
                phone,
                "메세지");
    }

    public MessageResponse sendMessage(String phone) {
        String token = getKakaoToken();
        if (token != null) {
            try {
                ResponseEntity<MessageResponse> response = new RestTemplate().exchange(
                        BASE_URL + SEND_URL,
                        HttpMethod.POST,
                        getSendRequestEntity(phone),
                        MessageResponse.class);
                return response.getBody();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return null;
    }

    private String tokenAuthHeader() {
        return String.format("Basic %s %s", clientId, clientSecret);
    }

    private String sendAuthHeader() {
        return String.format("Bearer %s", getKakaoToken());
    }

    private HttpEntity<MultiValueMap<String, String>> getAuthRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "*/*");
        headers.set(HttpHeaders.AUTHORIZATION, tokenAuthHeader());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");

        return new HttpEntity<>(formData, headers);
    }

    private HttpEntity<MessageRequest> getSendRequestEntity(String phone) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "*/*");
        headers.set(HttpHeaders.AUTHORIZATION, sendAuthHeader());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(sampleMessageRequest(phone), headers);
    }
}
