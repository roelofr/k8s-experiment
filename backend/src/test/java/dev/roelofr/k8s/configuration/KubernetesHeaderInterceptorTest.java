package dev.roelofr.k8s.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KubernetesHeaderInterceptorTest {
    @Test
    void ensureHostnameIsAdded() throws Exception {
        // Prep mocks
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var handler = mock(Object.class);

        // Call the method
        new KubernetesHeaderInterceptor().afterCompletion(
            request,
            response,
            handler,
            null
        );

        // Et Voila
        then(response)
            .should(times(1))
            .setHeader("X-Hostname", System.getenv("HOSTNAME"));
    }
}
