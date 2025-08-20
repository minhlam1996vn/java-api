package com.lamldm.java_api.service;

import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.exception.AppException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtService {
    @NonFinal
    @Value("${jwt.accessTokenKey}")
    String ACCESS_TOKEN_KEY;

    @NonFinal
    @Value("${jwt.accessTokenExpirationTime}")
    Long ACCESS_TOKEN_EXPIRATION_TIME;

    @NonFinal
    @Value("${jwt.refreshTokenKey}")
    String REFRESH_TOKEN_KEY;

    @NonFinal
    @Value("${jwt.refreshTokenExpirationTime}")
    Long REFRESH_TOKEN_EXPIRATION_TIME;

    public String generateAccessToken(User user, String jwtId) {
        return buildToken(user, jwtId, ACCESS_TOKEN_EXPIRATION_TIME, ACCESS_TOKEN_KEY, "ADMIN");
    }

    public String generateRefreshToken(User user, String jwtId) {
        return buildToken(user, jwtId, REFRESH_TOKEN_EXPIRATION_TIME, REFRESH_TOKEN_KEY, null);
    }

    public SignedJWT verifyAccessToken(String token) throws JOSEException, ParseException {
        return verifyToken(token, ACCESS_TOKEN_KEY);
    }

    public SignedJWT verifyRefreshToken(String token) throws JOSEException, ParseException {
        return verifyToken(token, REFRESH_TOKEN_KEY);
    }

    private String buildToken(User user, String jwtId, Long expirySeconds, String signerKey, String scope) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("ml-dev.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(expirySeconds, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(jwtId);

        if (scope != null && !scope.isEmpty()) {
            claimsBuilder.claim("scope", scope);
        }

        Payload payload = new Payload(claimsBuilder.build().toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private SignedJWT verifyToken(String token, String secretKey) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (!signedJWT.verify(verifier)) {
            throw new AppException("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (expiryTime == null || expiryTime.before(new Date())) {
            throw new AppException("Token expired", HttpStatus.UNAUTHORIZED);
        }

        return signedJWT;
    }
}
