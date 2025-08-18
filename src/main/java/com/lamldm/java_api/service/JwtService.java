package com.lamldm.java_api.service;

import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.exception.AppException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtService {
    @NonFinal
    @Value("${jwt.accessTokenKey}")
    String ACCESS_TOKEN_KEY;

    @NonFinal
    @Value("${jwt.refreshTokenKey}")
    String REFRESH_TOKEN_KEY;

    private String generateToken(User user, long expirySeconds, String signerKey, String scope) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder().subject(user.getEmail()).issuer("ml-dev.com").issueTime(new Date()).expirationTime(new Date(Instant.now().plus(expirySeconds, ChronoUnit.SECONDS).toEpochMilli())).jwtID(UUID.randomUUID().toString());

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

    public String generateAccessToken(User user, long expirySeconds) {
        return generateToken(user, expirySeconds, ACCESS_TOKEN_KEY, "ADMIN");
    }

    public String generateRefreshToken(User user, long expirySeconds) {
        return generateToken(user, expirySeconds, REFRESH_TOKEN_KEY, null);
    }
}
