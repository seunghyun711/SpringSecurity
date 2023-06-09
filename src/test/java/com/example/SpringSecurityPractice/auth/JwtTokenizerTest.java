package com.example.SpringSecurityPractice.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenizerTest {
    private static JwtTokenizer jwtTokenizer;
    private String secretKey;
    private String base64EncodedSecretKey;

    // 테스트에 사용할 Secret Key를 Base64 형식으로 인코딩하여 인코딩된 Secret Key를 각 테스트 케이스에서 사용한다.
    @BeforeAll
    public void init(){
        jwtTokenizer = new JwtTokenizer();
        secretKey = "hong1239123192312321312312319241";

        base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);
    }

    /*
     plain text인 Secret Key가 Base64 형식으로 인코딩이 정상적으로 수행 되는지 테스트
     디코딩한 값이 원본 plain text Secret Key와 일치하는지 테스트하면 된다.
     */
    @Test
    public void encodeBase64SecretKeyTest(){
        System.out.println(base64EncodedSecretKey);

        assertThat(secretKey, Matchers.is(new String(Decoders.BASE64.decode(base64EncodedSecretKey))));
    }

    // JwtTokenizer가 AccessToken을 정상적으로 생성하는지 테스트
    // JWT는 생성할 때마다 값이 바뀌기 때문에 우선 생성된 AccessTokne이 null이 아닌지 여부만 테스트한다.
    @Test
    public void generateAccessTokenTest(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        System.out.println(accessToken);

        assertThat(accessToken, notNullValue());
    }

    // refreshToken을 정상적으로 생성하는지 테스트
    @Test
    public void generateRefreshTokenTest(){
        String subject = "test refresh token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiration = calendar.getTime();

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        System.out.println(refreshToken);

        assertThat(refreshToken, notNullValue());
    }

    // Signature를 잘 검증하는지 테스트
    // 생성된 JWT를 verifySignature()로 전달해서 Exception이 발생하지 않으면 검증이 잘 된것.
    @DisplayName("dose not throw any Exception when jws verify")
    @Test
    public void verifySignatureTest() throws InterruptedException {
        String accessToken = getAccessToken(Calendar.MINUTE, 10);
        assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
    }

    // JWT 생성시 지정한 만료일시가 지나면 JWT가 만료되는지 테스트
    @DisplayName("throw ExpiredJwtException when jws verify")
    @Test
    public void verifyExpirationTest() throws InterruptedException {
        String accessToken = getAccessToken(Calendar.SECOND, 1);
        assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));

        TimeUnit.MILLISECONDS.sleep(1500); // 만료일시 보다 긴 시간을 sleep

        assertThrows(ExpiredJwtException.class, () -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
    }

    private String getAccessToken(int timeUnit, int timeAmount) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, timeAmount);
        Date expiration = calendar.getTime();
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;

    }

}
