package online.toosimple.smartlog.vo;

import lombok.*;
import online.toosimple.smartlog.constant.LogConstant;
import online.toosimple.smartlog.enums.ResultCode;
import org.slf4j.MDC;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private String msgId;

    private T data;

    R (int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.msgId = MDC.get(LogConstant.TRACE_ID);
        this.data = data;
    }

    public static <T> R<T> success(T data) {
        return data(ResultCode.SUCCESS, data);
    }

    public static <T> R<T> error(int code, String msg, T data) {
        return new R<>(code, msg, data);
    }

    public static <T> R<T> data(ResultCode resultCode, T data) {
        return new R<>(resultCode.getCode(), resultCode.getMsg(), data);
    }


}
