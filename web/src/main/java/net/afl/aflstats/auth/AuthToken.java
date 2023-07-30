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

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (!super.equals(obj))
        return false;
      if (getClass() != obj.getClass())
        return false;
      AuthToken other = (AuthToken) obj;
      if (apiKey == null) {
        if (other.apiKey != null)
          return false;
      } else if (!apiKey.equals(other.apiKey))
        return false;
      return true;
    }
}
