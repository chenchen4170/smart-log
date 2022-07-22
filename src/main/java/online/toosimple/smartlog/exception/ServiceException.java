package online.toosimple.smartlog.exception;

import online.toosimple.smartlog.enums.IResultCode;
import online.toosimple.smartlog.enums.ResultCode;

//继承IResultCode，使全局异常捕获可以获取自定义的code和msg
public class ServiceException extends RuntimeException implements IResultCode {

    private IResultCode resultCode;

    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    @Override
    public int getCode() {
        return this.resultCode.getCode();
    }

    @Override
    public String getMsg() {
        return this.resultCode.getMsg();
    }

}
