package com.example.SpringSecurityPractice.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtTokenizer {
    /*
     plain text인 Secret Key의 byte[]를 base65형식의 문자열로 인코딩한다.
     jjwt가 버전업 되면서 plain text 자체를 Secret Key로 사용하는 것은 암호학적인 작업에서 사용되는 Key가 항상 바이너리라는 사실과 밎지 않는 것을
     감안해 plain text 자체를 Secret Key로 생성하는 것은 권장하지 않는다.
     */

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 인증된 사용자에게 JWT를 최초로 발급해주기 위한 JWT 생성 메서드
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey); // Base64 형식 Secret Key 문자열을 이용해 Key 객체를 얻는다.

        return Jwts.builder()
                .setClaims(claims) // JWT에 포함시킬 Custom Claims를 추가한다. 여기에는 주로 인증된 사용자와 관련된 정보를 추가한다.
                .setSubject(subject) // JWT에 대한 제목 추가
                .setIssuedAt(Calendar.getInstance().getTime()) // JWT 발행 일자 설정
                .setExpiration(expiration) // JWT의 만료일시 지정
                .signWith(key) // 서명을 위한 Key 객체 설정
                .compact(); // JWT를 생성하고 직렬화한다.
     }

    // Access Token이 만료된 경우 AccessToken을 새로 생성할 수 있게 해주는 RefreshToekn 을 생성하는 메서드
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        // Refresh Token은 AccessToken을 새로 발급해주는 역할을 하는 토큰으로 별도의 Custom Claimsms는 추가할 필요가 없다.
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    // JWT의 서명에 사용할 Secret Key를 생성한다.
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] bytes = Decoders.BASE64.decode(base64EncodedSecretKey); // Base64 형식으로 인코딩된 SecretKey를 디코딩하여 byte array를 반환
        Key key = Keys.hmacShaKeyFor(bytes); // key byte array를 기반으로 적절한 HMAC 알고리즘을 적용한 Key 객체를 생성한다.

        return key;
    }
}
