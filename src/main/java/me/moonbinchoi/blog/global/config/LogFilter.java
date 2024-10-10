package me.moonbinchoi.blog.global.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(final jakarta.servlet.FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String requestURI = httpRequest.getRequestURI();
        final String uuid = UUID.randomUUID().toString();

        try {
            log.info("Request [{}][{}]", uuid, requestURI);
            filterChain.doFilter(request, response);
        } catch (final Exception e) {
            throw e;
        } finally {
            log.info("Response [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
