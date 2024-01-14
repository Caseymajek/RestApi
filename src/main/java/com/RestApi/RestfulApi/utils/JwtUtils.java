package com.RestApi.RestfulApi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class JwtUtils {

    //First Step for creating Jwt(getKey, expirationDate)

                    //(2)
    private Supplier<SecretKeySpec> getKey = () ->{
        Key key = Keys.hmacShaKeyFor("dbbefc5da224a3a26811c6812ab2e41af693f6399240947053e8907a3dfcf0d03c840e2e699d4e2d237f83eb838d5f6863626cea0bc25a5a87694b7d4f8bf7ea"
                .getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
    };
                    //(4)
    private Supplier<Date> expirationTime = () ->
            Date.from(LocalDateTime.now()
                            .plusMinutes(10)
                            .atZone(ZoneId.systemDefault())
                            .toInstant());
                   // (1)
    public Function<UserDetails, String> createJwt = (userDetails)->{
                    //(3)
        Map<String, Object> claims = new HashMap<>();
                    //(1)
        return Jwts.builder()
                .signWith(getKey.get())
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))//THE TIME AT WHICH JWT TOKEN IS CREATED
                .expiration(expirationTime.get())//TIME AT WHICH JWT TOKEN IS SET TO EXPIRE
                .compact();
    };

    //SECOND STEP IS TO EXTRACT USERNAME AND EXPIRATION DATE FROM TOKEN -//BEFORE THAT, EXTRACT CLAIMS
    //It takes in token that has already been created and returns the claims
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = Jwts.parser().verifyWith(getKey.get())//parser help us to pass through this token after verifying it, it will be able to get the payload .i.e claims from the token for us
                .build()
                .parseEncryptedClaims(token).getPayload();
        return claimResolver.apply(claims);// we are returning this claims back into the extractClaim
    }
    //EXTRACTING THE USERNAME FROM THE TOKEN
    public Function<String, String> extractUsername = (token) -> extractClaim(token, Claims::getSubject);//It is taken in token and returning the username- and we get the username from the extractClaim in the above method

    //EXTRACTING THE EXPIRATION DATE FROM TOKEN
    public Function<String, Date> extractExpirationTime = (token) -> extractClaim(token, Claims::getExpiration);

    //THIRD STEP IS TO CREATE FUNCTION TO CHECK IF TOKEN isValid ans isExpired
    public Function<String, Boolean> isTokenExpired = (token) -> extractExpirationTime.apply(token).after(new Date(System.currentTimeMillis()));
    //IT TAKES IN TOKEN AND RETURN BOOLEAN, IT WILL CHECK THE TIME AS OF NOW AND COMPARED IT WITH THE TIME FOUND IN THE ABOVE METHOD (extractExpirationTime) TO KNOW IF THE TIME HAS REACH THE EXPIRATION TIME OR NOT

    //CREATE FUNCTION THAT WILL HOLD isTokenValid
    public BiFunction<String, String, Boolean>  isTokenValid = (token, username)-> isTokenExpired.apply(token) &&
            Objects.equals(extractUsername.apply(token), username);
    // IT FIRSTLY CHECK IF THE TOKEN HAS EXPIRED, THEN CHECK THE USERNAME THAT WAS EXTRACTED FROM TOKEN, IF THE USERNAME IS EQUAL TO THE USERNAME THAT WAS GIVEN AT THE USER CLASS
}
