package com.ratelimiting.rulshortener.service;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = CHARACTERS.length();

    public String encode(long id) {
        if (id == 0) return String.valueOf(CHARACTERS.charAt(0));
        StringBuilder encoded = new StringBuilder();
        while (id > 0) {
            encoded.append(CHARACTERS.charAt((int) (id % BASE)));
            id /= BASE;
        }
        return encoded.reverse().toString();
    }

    public long decode(String shortUrl) {
        long id = 0;
        for (char c : shortUrl.toCharArray()) {
            id = id * BASE + CHARACTERS.indexOf(c);
        }
        return id;
    }
}