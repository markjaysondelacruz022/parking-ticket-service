package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mjdc.pts.util.DateUtil;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {

    private Date timestamp;
    private String message;
    private Object data;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    public Date getTimestamp() {
        return Optional.ofNullable(timestamp).isEmpty() ? DateUtil.getCurrentDateTime() : this.timestamp;
    }
}
