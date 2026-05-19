package com.ratelimiting.rulshortener.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ClickEvent {

    private String shortHash;
    private String ipAddress;
    private String userAgent;
}