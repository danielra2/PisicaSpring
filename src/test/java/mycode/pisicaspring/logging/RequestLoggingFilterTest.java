package mycode.pisicaspring.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestLoggingFilterTest {

    private final RequestLoggingFilter filter = new RequestLoggingFilter();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void assignsRequestIdWhenHeaderMissing() throws ServletException, IOException {
        HttpServletRequest request = mockRequest(null);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        verify(response).setHeader(eq(RequestLoggingFilter.REQUEST_ID_HEADER), any());
        verify(chain).doFilter(request, response);
        assertThat(MDC.get("requestId")).isNull();
    }

    @Test
    void reusesProvidedRequestIdEvenOnError() throws Exception {
        HttpServletRequest request = mockRequest("  custom-id  ");
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        doThrow(new IllegalStateException("boom")).when(chain).doFilter(request, response);

        assertThatThrownBy(() -> filter.doFilterInternal(request, response, chain))
                .isInstanceOf(IllegalStateException.class);

        verify(response).setHeader(RequestLoggingFilter.REQUEST_ID_HEADER, "custom-id");
        assertThat(MDC.get("requestId")).isNull();
    }

    private HttpServletRequest mockRequest(String requestId) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(RequestLoggingFilter.REQUEST_ID_HEADER)).thenReturn(requestId);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/pisici");
        when(request.getQueryString()).thenReturn("page=0");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        return request;
    }
}
