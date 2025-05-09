package com.portalSite.common.exception.core;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler {
    private final ErrorResponseHandler errorResponseHandler;

    // 비즈니스 에러
    @ExceptionHandler(BaseException.class)
    public void handleBaseException(
            HttpServletResponse response,
            BaseException e
    ) throws IOException {
        errorResponseHandler.send(response, e.getStatus(), e.getMessage());
    }

    // 잘못된 Http Method 처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleMethodNotSupported(
            HttpServletResponse response
    ) throws IOException {
        String errorMessage = "요청한 HTTP 메서드는 지원되지 않습니다.";
        errorResponseHandler.send(response, HttpStatus.METHOD_NOT_ALLOWED, errorMessage);
    }

    // 잘못된 엔드포인트 요청 처리 (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNoHandlerFoundException(
            HttpServletResponse response,
            HttpServletRequest request
    ) throws IOException {
        String errorMessage = "요청한 리소스를 찾을 수 없습니다: " + request.getRequestURI();
        errorResponseHandler.send(response, HttpStatus.NOT_FOUND, errorMessage);
    }

    // 파라미터 존재하지 않을 때 발생
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void handleMissingServletRequestParameterException(
            HttpServletResponse response,
            MissingServletRequestParameterException e
    ) throws IOException {
        String errorMessage = e.getParameterName() + " 값이 누락되었습니다.";
        errorResponseHandler.send(response, HttpStatus.BAD_REQUEST, errorMessage);
    }

    // 잘못된 인자 값이 전달될 때 발생
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(
            HttpServletResponse response,
            IllegalArgumentException e
    ) throws IOException {
        String errorMessage = "잘못된 입력 값: " + e.getMessage();
        errorResponseHandler.send(response, HttpStatus.BAD_REQUEST, errorMessage);
    }

    // 파라미터 타입과 일치하지 않을 때 발생
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleMethodArgumentTypeMismatch(
            HttpServletResponse response,
            MethodArgumentTypeMismatchException e
    ) throws IOException {
        String errorMessage = String.format("파라미터 타입 불일치: %s (기대된 타입: %s, 실제 값: %s)", e.getName(),
                Objects.requireNonNull(e.getRequiredType()).getSimpleName(), e.getValue());
        errorResponseHandler.send(response, HttpStatus.BAD_REQUEST, errorMessage);
    }

    // HTTP 요청의 본문을 읽을 수 없을 때 발생
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleHttpMessageNotReadable(
            HttpServletResponse response,
            HttpMessageNotReadableException e
    ) throws IOException {
        errorResponseHandler.send(response, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // @Valid 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValid(
            HttpServletResponse response,
            MethodArgumentNotValidException e
    ) throws IOException {
        // 글로벌 에러 메시지들
        String globalErrorMessage = e.getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));

        // 필드 에러 메시지들
        String fieldErrorMessage = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));

        String errorMessage = globalErrorMessage + fieldErrorMessage;
        errorResponseHandler.send(response, HttpStatus.BAD_REQUEST, errorMessage);
    }

    // 컨트롤러 파라미터의 제약 애너테이션 에러
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleValidationException(HandlerMethodValidationException ex) {
        List<String> messages = ex.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()) // 검증 실패 애너테이션 목록
                .map(MessageSourceResolvable::getDefaultMessage) // 애너테이션 메세지 추출
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", messages
        ));
    }

    // 500 서버에러
    @ExceptionHandler(Exception.class)
    public void handleException(
            HttpServletResponse response,
            HttpServletRequest request,
            Exception e
    ) throws IOException {
        log.error("예상하지 못한 예외가 발생했습니다. URI:{}, 내용:{}", request.getRequestURI(), e.getMessage(), e);
        errorResponseHandler.send(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
