package net.afl.aflstats.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class AuthProvider implements AuthenticationProvider {
    
    @Value("${API_KEY:apikey}")
    private String apikey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        String principal = (String) authentication.getPrincipal();

        if(ObjectUtils.isEmpty(principal)) {
            throw new InsufficientAuthenticationException("No API key");
        } else {
            if (principal.equals(apikey)) {
                return new AuthToken(principal, true);
            } else {
                throw new BadCredentialsException("Invalid API key");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
      return AuthToken.class.isAssignableFrom(authentication);
    }
}
