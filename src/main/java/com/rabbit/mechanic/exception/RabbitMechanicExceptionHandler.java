package com.rabbit.mechanic.exception;

import com.rabbit.mechanic.error.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Rabbit Mechanic exception handler
 */
@ControllerAdvice
public class RabbitMechanicExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle "not found" exceptions
     * @param ex exception
     * @param request http Servlet Request
     * @return {@link Error}
     */
    @ExceptionHandler(value = {
            CustomerNotFoundException.class,
            CarNotFoundException.class,
            RepairNotFoundException.class,
            EmployeeNotFoundException.class
    })
    public ResponseEntity<Error> handlerNotFoundException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle "already exists" exceptions
     * @param ex exception
     * @param request http Servlet Request
     * @return {@link Error}
     */
    @ExceptionHandler(value = {
            CustomerAlreadyExistsException.class,
            CarAlreadyExistsException.class,
            RepairAlreadyExistsException.class,
            EmployeeAlreadyExistsException.class
            })
    public ResponseEntity<Error> handlerConflictException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT);
    }

    /**
     * Handle exception
     * @param ex exception
     * @param request rhttp Servlet Request
     * @return {@link Error}
     */
    @ExceptionHandler(value = {
            DataBaseCommunicationException.class
            })
    public ResponseEntity<Error> handlerBadRequestException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle method argument not valid
     * @param ex exception
     * @param request http Servlet Request
     * @return {@link Error}
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Error> handlerAnyOtherException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle method argument not valid
     * @param ex Method Argument Not Valid exception
     * @param headers httpHeaders
     * @param httpStatus httpStatus
     * @param request Web Request
     * @return {@link Error}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus httpStatus, WebRequest request) {
        Error error = Error.builder()
                .timestamp(new Date())
                .message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Build the error response
     * @param ex exception
     * @param request http Servlet Request
     * @param httpStatus httpStatus
     * @return {@link Error}
     */
    public ResponseEntity<Error> buildErrorResponse(Exception ex, HttpServletRequest request, HttpStatus httpStatus) {
        Error error = Error.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, httpStatus);
    }
}
