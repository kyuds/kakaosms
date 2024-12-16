package com.kyuds.kakaosms;

import com.kyuds.kakaosms.models.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class KakaoController {

    @Value("${kakaoex.sample.phone}")
    private String phone;

    @Autowired
    private KakaoService service;

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> send() {
        MessageResponse response = service.sendMessage(phone);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
