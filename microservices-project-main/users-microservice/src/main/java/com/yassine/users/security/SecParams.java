package com.yassine.users.security;

public interface SecParams {
    long EXP_TIME = 10L * 24 * 60 * 60 * 1000; // 10 jours en ms
    String SECRET = "yassine@gmail.com";
    String HEADER_NAME = "Authorization";
    String TOKEN_PREFIX = "Bearer ";
}
