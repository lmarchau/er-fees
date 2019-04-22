package fr.lmarchau.proto.fees.web;

import fr.lmarchau.proto.fees.dto.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Error> handle(Throwable e) {
        return ResponseEntity.badRequest().body(Error.builder().message("Invalid Request").build());
    }

}
