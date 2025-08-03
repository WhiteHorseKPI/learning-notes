package com.learningnotes.service.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey


@Service
class JwtServiceImpl : JwtService {
    @Value("\${spring.jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${spring.jwt.expiration}")
    private var jwtExpirationDuration: Long = 0

    override fun extractUsername(token: String): String? = extractAllClaims(token).subject

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    override fun generateToken(claims: Map<String, Any>, userDetails: UserDetails): String {
        val issuedAtMillis = System.currentTimeMillis()
        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.username)
            .issuedAt(Date(issuedAtMillis))
            .expiration(Date(issuedAtMillis + jwtExpirationDuration))
            .signWith(this.signingKey)
            .compact()
    }

    private fun isTokenExpired(token: String?): Boolean = extractAllClaims(token)
        .expiration
        .before(Date())

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser()
            .verifyWith(this.signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private val signingKey: SecretKey by lazy {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        Keys.hmacShaKeyFor(keyBytes)
    }
}