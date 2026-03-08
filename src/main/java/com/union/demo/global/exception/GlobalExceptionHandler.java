package com.union.demo.global.exception;

import com.union.demo.global.common.ApiErrorResponse;
import com.union.demo.global.common.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.rmi.AccessException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //잘못된 요청(DTO 검증 실패): client가 dto에 맞게 요청을 보내지 않은 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException e){
        String message=e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error->error.getField()+ " : "+error.getDefaultMessage())
                .findFirst()
                .orElse("잘못된 요청입니다.");

        ApiErrorResponse body= ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("BAD_REQ_400")
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    //잘못된 파라미터
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException e){
        ApiErrorResponse body=ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("WRONG_TYPE_400")
                .message("요청 파라미터 타입이 올바르지 않습니다.")
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    //401: 로그인 안됨 상태 ex 토큰 없음/만료/위조/아이디비번 오류
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException e){
        ApiErrorResponse body= ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("AUTH_401")
                .message("로그인 필요합니다.")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);

    }

    //403: 권한 없음: 로그인은 되어있지만 이 리소스를 쓸 권한이 없음 ex. 본인 글이 아닌데 삭제 시도
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException e){
        ApiErrorResponse body=ApiErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .code("AUTH_403")
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    //404: resources not found
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElementException(NoSuchElementException e){

        ApiErrorResponse body=ApiErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .code("NOT_FOUND_404")
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    //409: 중복된 데이터(username 중복, loginId 중복)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(IllegalStateException e){
        ApiErrorResponse body=ApiErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .code("CONFLICT_409")
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //409: 중복체크를 통과했는데 db에서 터지는 경우
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException e){
        ApiErrorResponse body=ApiErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .code("DB_409")
                .message("이미 존재하는 데이터이거나 제약조건을 위반했습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //서버 오류: 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e){
        ApiErrorResponse body=ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("SERVER_500")
                .message("서버 오류가 발생했습니다."+e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
