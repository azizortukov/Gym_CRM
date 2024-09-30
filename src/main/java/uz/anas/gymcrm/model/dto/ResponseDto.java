package uz.anas.gymcrm.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 For returning response in controller. If request has been responded successfully without any
 exception, data will be sent and errorMessage will be null. However, if any exception happened
 then, errorMessage will be sent and data would be null. Both of them can be null in case of APIs
 that don't need to return value, i.e. DELETE, PUT in some cases
 */
@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class ResponseDto<T> {

    private T data;
    private String errorMessage;
    private final LocalDateTime time = LocalDateTime.now();

    public ResponseDto(T data) {
        this.data = data;
    }

    public ResponseDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
