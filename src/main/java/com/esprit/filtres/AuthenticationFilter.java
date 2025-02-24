package com.esprit.filtres;


import java.io.IOException;
import java.security.Key;
import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import io.jsonwebtoken.Jwts;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("Request filter invoked...");

        // Récupérer le header Authorization
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        System.out.println("Authorization Header: " + authorizationHeader);

        // Vérifier si le header Authorization est valide
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extraire le token après "Bearer "
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            // Valider le token
            validateToken(token);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
                .build());
    }

    private void validateToken(String token) {
        try {
            // Clé secrète utilisée pour signer et valider le JWT
            String keyString = "simplekey";
            Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");

            System.out.println("The key is : " + key);
            System.out.println("Test: " + Jwts.parser().setSigningKey(key).parseClaimsJws(token));
            System.out.println("#### Valid token : " + token);
        } catch (Exception e) {
            System.out.println("#### Invalid token : " + token);
            throw new SecurityException("Invalid token");
        }
    }
}
