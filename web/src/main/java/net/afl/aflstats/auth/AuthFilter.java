package net.afl.aflstats.auth;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthFilter extends AbstractAuthenticationProcessingFilter {
    
    private static final String BEARER = "bearer";
    private static final Pattern authorizationPattern = Pattern.compile(
        "^Bearer (?<token>[\\d|a-f]{8}-([\\d|a-f]{4}-){3}[\\d|a-f]{12})$",
		Pattern.CASE_INSENSITIVE);

    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
        new AntPathRequestMatcher("/api/**"),
        new AntPathRequestMatcher("/jobs/**")
    );

    public AuthFilter(AuthenticationManager authenticationManager) {
        super(PROTECTED_URLS, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        
        Optional<String> apikey = Optional.ofNullable(resolveFromAuthorizationHeader(request));
        AuthToken token = apikey.map(AuthToken::new).orElse(new AuthToken());

        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authResult) throws IOException, ServletException {
  
      SecurityContextHolder.getContext().setAuthentication(authResult);
      chain.doFilter(request, response);
    }

    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        
        String token = null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if(StringUtils.startsWithIgnoreCase(authorization, BEARER)) {
            log.info("Auth Header={}", authorization);
            Matcher matcher = authorizationPattern.matcher(authorization);
            if(matcher.matches()) {
                token = matcher.group("token");
            }
        }
                  
        log.info("Token={}", token);
        return token;
    }
}
