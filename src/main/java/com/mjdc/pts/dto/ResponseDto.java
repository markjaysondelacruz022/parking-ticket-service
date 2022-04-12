package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mjdc.pts.util.DateUtil;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {

    private LocalDateTime timestamp;
    private String message;
    private Object data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    public LocalDateTime getTimestamp() {
        return Optional.ofNullable(timestamp).isEmpty() ? DateUtil.getCurrentLocalDateTime() : this.timestamp;
    }
}
