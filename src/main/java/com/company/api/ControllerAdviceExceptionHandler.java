package com.company.api;

import com.company.dto.error.ErrorDetailsDto;
import com.company.dto.error.ErrorMessageDto;
import com.company.exception.ServiceRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdviceExceptionHandler.class);

    @ExceptionHandler({ServiceRuntimeException.class})
    public ResponseEntity<ErrorMessageDto> handleServiceRuntimeException(ServiceRuntimeException ex) {
        logger.error("Handled ServiceRuntimeException", ex);
        return new ResponseEntity<>(new ErrorMessageDto(ex.getMessage()), ex.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.info("Handled MethodArgumentNotValidException", ex);
        List<ErrorDetailsDto> errorList = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> new ErrorDetailsDto(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());

        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

}
