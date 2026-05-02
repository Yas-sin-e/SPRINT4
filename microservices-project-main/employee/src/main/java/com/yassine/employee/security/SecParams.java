package com.yassine.employee.security;

public interface SecParams {
    long EXP_TIME = 10L * 24 * 60 * 60 * 1000; // 10 jours en ms
    String SECRET = "yassine@gmail.com";
    String PREFIX = "Bearer ";
}
