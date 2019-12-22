package tacos.rest.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestControllerUtils {
    public static <T> ResponseEntity<T> createResponseEntity(T body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }
}
