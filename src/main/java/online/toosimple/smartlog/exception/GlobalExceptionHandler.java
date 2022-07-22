package online.toosimple.smartlog.exception;

import lombok.extern.slf4j.Slf4j;
import online.toosimple.smartlog.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 全局异常捕获
    @ExceptionHandler(value = ServiceException.class)
    public R handleServiceException(ServiceException e) {
        log.error("ServiceException: ", e);
        //使用R对象返回统一的格式
        return R.error(e.getCode(), e.getMsg(), null);
    }
}
