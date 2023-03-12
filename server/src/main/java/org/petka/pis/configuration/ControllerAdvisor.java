package org.petka.pis.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                  @SuppressWarnings("unused") final HttpHeaders headers,
                                                                  @SuppressWarnings("unused")
                                                                      final HttpStatusCode status,
                                                                  @SuppressWarnings("unused") final WebRequest req) {
        return new ResponseEntity<>(ErrorResponse.builder().messages(ex.getCause().getMessage()).build(), headers,
                                    status);
    }


    /**
     * Handle IllegalArgumentException and returns BAD_REQUEST.
     *
     * @param ex      exception
     * @param request request
     * @return response entity
     */
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(
            final RuntimeException ex, @SuppressWarnings("unused") final WebRequest request) {

        return new ResponseEntity<>(ErrorResponse.builder().messages(ex.getMessage()).build(),
                                    HttpStatus.BAD_REQUEST);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {

        private int code;
        private String messages;
    }
}

