package com.samajh.samajh_backend.service;
//testing the service directly, without Spring’s web layer, to avoid Boot 4 test-slice package differences

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

class SaathiChatServiceTest {

    private static final String URL = "https://example.test/chat";

    private MockRestServiceServer server;

    private SaathiChatService serviceWith(String apiKey) {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        return new SaathiChatService(builder, apiKey, URL, "test-model");
    }

    @Test
    void returnsTrimmedReplyOnSuccess() {
        SaathiChatService service = serviceWith("test-key");
        server.expect(requestTo(URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer test-key"))
                .andRespond(withSuccess(
                        "{\"choices\":[{\"message\":{\"content\":\"  Hello there  \"}}]}",
                        MediaType.APPLICATION_JSON));

        assertThat(service.getReply("hi")).isEqualTo("Hello there");
        server.verify();
    }

    @Test
    void missingApiKeyReturns503WithoutCallingProvider() {
        SaathiChatService service = serviceWith("");

        assertThatThrownBy(() -> service.getReply("hi"))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE));
        server.verify(); // no requests expected
    }

    @Test
    void providerAuthErrorReturns502() {
        SaathiChatService service = serviceWith("test-key");
        server.expect(requestTo(URL)).andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThatThrownBy(() -> service.getReply("hi"))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY));
    }

    @Test
    void providerRateLimitReturns502() {
        SaathiChatService service = serviceWith("test-key");
        server.expect(requestTo(URL)).andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertThatThrownBy(() -> service.getReply("hi"))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY));
    }

    @Test
    void providerServerErrorReturns502() {
        SaathiChatService service = serviceWith("test-key");
        server.expect(requestTo(URL)).andRespond(withServerError());

        assertThatThrownBy(() -> service.getReply("hi"))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY));
    }

    @Test
    void emptyChoicesReturns502() {
        SaathiChatService service = serviceWith("test-key");
        server.expect(requestTo(URL))
                .andRespond(withSuccess("{\"choices\":[]}", MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> service.getReply("hi"))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY));
    }

    @Test
    void blankContentReturns502() {
        SaathiChatService service = serviceWith("test-key");
        server.expect(requestTo(URL))
                .andRespond(withSuccess(
                        "{\"choices\":[{\"message\":{\"content\":\"   \"}}]}",
                        MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> service.getReply("hi"))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY));
    }

    @Test
    void networkFailureReturns504() {
        SaathiChatService service = serviceWith("test-key");
        server.expect(requestTo(URL))
                .andRespond(request -> { throw new IOException("simulated network failure"); });

        assertThatThrownBy(() -> service.getReply("hi"))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.GATEWAY_TIMEOUT));
    }
}