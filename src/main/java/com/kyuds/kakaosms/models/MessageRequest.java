package com.kyuds.kakaosms.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MessageRequest {
    // https://docs.kakaoi.ai/kakao_i_connect_message/bizmessage/api/api_reference/at/

    private String message_type = "AT";
    private String sender_key;
    private String cid = UUID.randomUUID().toString();
    private String template_code;
    private String phone_number;
    private String sender_no = "-";
    private String message;
    private boolean fall_back_yn = true;
    private String fall_back_message_type = "SM";

    public MessageRequest(String sender_key, String cid, String template_code, String phone_number, String message) {
        this.sender_key = sender_key;
        this.cid = cid;
        this.template_code = template_code;
        this.phone_number = phone_number;
        this.message = message;
    }
}

