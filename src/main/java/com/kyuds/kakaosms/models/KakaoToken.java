package com.kyuds.kakaosms.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoToken {
    // https://docs.kakaoi.ai/kakao_i_connect_message/bizmessage/api/api_reference/oauth/

    private String code;
    private DetailedResult result;
    private String access_token;
    private String token_type;
    private Integer expires_in;
}
