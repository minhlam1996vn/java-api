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
public class JwtService {
    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    public String generateToken(User user, long expirySeconds, boolean scope) {
        try {
            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer("LamLDM")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(expirySeconds, ChronoUnit.SECONDS).toEpochMilli()))
                    .jwtID(UUID.randomUUID().toString());

            if (scope) {
                claimsBuilder.claim("scope", "ADMIN");
            }

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader(JWSAlgorithm.HS512),
                    new Payload(claimsBuilder.build().toJSONObject())
            );

            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
