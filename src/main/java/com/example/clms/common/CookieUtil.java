package com.example.clms.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CookieUtil {

    private Cookie[] cookies;
    private Cookie cookie;

    public CookieUtil(HttpServletRequest request) {
        this.cookies = request.getCookies();
    }

    // 쿠키 이름으로 쿠키 값을 얻는 메소드
    public String getValue(String name) {

        for (Cookie value : this.cookies) {
            if (value.getName().equals(name)) {
                return value.getValue();
            }
        }
        return null;
    }

    // 쿠키 추가하는 메서드
    public CookieUtil addCookie(String key, String value) {
        this.cookie = new Cookie(key, value);
        return this;
    }

    // 쿠키 추가후에 쿠키 만료일을 체이닝하는 메서드
    public CookieUtil setExpire(int period) {
        this.cookie.setMaxAge(period);
        return this;
    }

    // 쿠키 추가후에 HttpOnly 옵션을 체이닝하는 메서드
    public CookieUtil setHttpOnly(boolean setHttpOnly) {
        this.cookie.setHttpOnly(setHttpOnly);
        return this;
    }

    // 쿠키 생성을 마칠 때, 쿠키를 리턴하는 메서드
    public Cookie build() {
        return this.cookie;
    }
}