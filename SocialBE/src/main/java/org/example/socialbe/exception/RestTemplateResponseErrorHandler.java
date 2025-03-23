package org.example.socialbe.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String body = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"))) {
            body = reader.lines().collect(Collectors.joining(""));
        } catch (Exception e) {
            log.error("Cannot read response body");
            log.error(e.getMessage(), e);
        }

        log.error("Error response {}: {}", response.getStatusCode().value(), body);

        RestErrorException restErrorException = new RestErrorException();
        restErrorException.setHttpStatus(response.getStatusCode().value());
        restErrorException.setBody(body);
        throw restErrorException;
    }
}
