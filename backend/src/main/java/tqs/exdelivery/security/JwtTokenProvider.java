package tqs.exdelivery.security;

import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import tqs.exdelivery.entity.CustomUserDetails;

import java.util.Date;

@Component
public class JwtTokenProvider {
  private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);
  // @Value("${app.jwtSecret}")
  private String jwtSecret = "secret";

  // @Value("${app.jwtExpirationInMs}")
  private int jwtExpirationInMs = 604800000;

  public String generateToken(Authentication authentication) {

    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder()
        .setSubject(Long.toString(user.getUserId()))
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public Long getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException ex) {
      logger.info("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      logger.info("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      logger.info("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      logger.info("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      logger.info("JWT claims string is empty.");
    }
    return false;
  }
}
