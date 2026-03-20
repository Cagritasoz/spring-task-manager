package com.cagritasoz.taskmanager.infrastructure.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils { //A JWT Token consists of HEADER.PAYLOAD(CLAIMS).SIGNATURE

    private static final String SECRET_KEY = "Tn895rXrcw5OVZ/L42AHK64qVPC9OXZCzC7e3z2HXbE="; //In base64 format. Base64 is a format used to represent bytes with human-readable strings.
    //Hex format is another format that can be used to represent bytes with human-readable strings called "Hex strings".

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);

    }

    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) //Expires after 5 minutes!
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);

    }

    private Claims extractAllClaims(String token) {

        return Jwts
                .parserBuilder() //Create a builder for a JWT parser.
                .setSigningKey(getSignInKey()) //Supply the secret key.
                .build() //Build the parser object.
                .parseClaimsJws(token) //Split JWT to it's three parts, compute the expected signature and compare it with the actual one provided. Throw exception if they don't match, if they match parse the Claims.
                .getBody(); //Extract the Claims object form the Jws object.

    }

    private Key getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); //Decode the Base64 string into the byte array.

        return Keys.hmacShaKeyFor(keyBytes); //Convert the byte array into a SecretKey suitable for HMAC-SHA algorithms (e.g., HS256).

    }


}
