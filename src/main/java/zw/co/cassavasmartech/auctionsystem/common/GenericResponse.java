package zw.co.cassavasmartech.auctionsystem.common;

import lombok.Builder;
import lombok.Data;
import zw.co.cassavasmartech.auctionsystem.common.enums.ResponseCode;

@Data
@Builder
public class GenericResponse<T> {
    private ResponseCode responseCode;
    private String narrative;
    private T entity;

    public GenericResponse(ResponseCode responseCode, String narrative, T entity) {
        this.responseCode = responseCode;
        this.narrative = narrative;
        this.entity = entity;
    }
}
