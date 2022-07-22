package online.toosimple.smartlog.enums;


public enum ResultCode implements IResultCode{

    SUCCESS(200, "操作成功"),
    FAILURE(400, "业务异常")

    ;

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode(){
        return code;
    }

    @Override
    public String getMsg(){
        return msg;
    }
}
