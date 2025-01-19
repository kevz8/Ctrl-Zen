package thunderbirdsonly.nwhackbackend.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String  encodeKey = "L970Mz5+p+IBaMVirOkeLMZBpQw1/CFPJpI9AP6cF3w=ThunderbirdsOnly";
    private static final long expireTime = 21600000 * 4 * 7;


    public static String generateToken(Map<String, Object> claims) {
        String jwt = Jwts.builder().
                signWith(SignatureAlgorithm.HS256, encodeKey)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();

        return jwt;
    }



    public static Claims decodeToken(String jwt) {
        Claims claim =  Jwts.parser()
                .setSigningKey(encodeKey)
                .parseClaimsJws(jwt)
                .getBody();
        return  claim;
    }


}// 6 hours

