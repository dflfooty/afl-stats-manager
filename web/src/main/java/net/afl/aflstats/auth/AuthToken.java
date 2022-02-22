package net.afl.aflstats.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.AuthorityUtils;

@Transient
public class AuthToken extends AbstractAuthenticationToken {
    private String apiKey;

    public AuthToken(String apiKey, boolean authenticated) {
      super(AuthorityUtils.NO_AUTHORITIES);
      this.apiKey = apiKey;
      setAuthenticated(authenticated);
    }
  
    public AuthToken(String apiKey) {
      super(AuthorityUtils.NO_AUTHORITIES);
      this.apiKey = apiKey;
      setAuthenticated(false);
    }
  
    public AuthToken() {
      super(AuthorityUtils.NO_AUTHORITIES);
      setAuthenticated(false);
    }
  
    @Override
    public Object getCredentials() {
      return null;
    }
 
    @Override
    public Object getPrincipal() {
      return apiKey;
    }
}
