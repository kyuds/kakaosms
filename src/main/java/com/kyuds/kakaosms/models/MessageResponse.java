package com.kyuds.kakaosms.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private String code;
    private String uid;
    private String cid;
    private DetailedResult result;
}
