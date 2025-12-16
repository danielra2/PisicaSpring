package mycode.pisicaspring.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Logs every HTTP request and propagates a correlation id via MDC/headers.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class RequestLoggingFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String MDC_KEY = "requestId";
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = resolveRequestId(request);
        MDC.put(MDC_KEY, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        long start = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String fullPath = query == null || query.isBlank() ? uri : uri + "?" + query;

        LOGGER.info("Incoming {} {} from {}", method, fullPath, request.getRemoteAddr());
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            LOGGER.error("Request {} {} failed: {}", method, fullPath, ex.getMessage(), ex);
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;
            LOGGER.info("Completed {} {} with status {} in {} ms", method, fullPath, response.getStatus(), duration);
            MDC.remove(MDC_KEY);
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        String provided = request.getHeader(REQUEST_ID_HEADER);
        if (provided != null && !provided.isBlank()) {
            return provided.trim();
        }
        return UUID.randomUUID().toString();
    }
}
