package weixin.cp.model;

public class WxCpException extends Exception {

    private Integer errcode;
    private String responseBody;

    public Integer getErrcode() {
        return errcode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public WxCpException(String responseBody) {
        this.responseBody = responseBody;
    }


    public WxCpException(Integer errcode, String responseBody) {
        this.errcode = errcode;
        this.responseBody = responseBody;
    }

    public WxCpException(String message, int errcode, String responseBody) {
        super(message);
        this.errcode = errcode;
        this.responseBody = responseBody;
    }

    public WxCpException(String message, Throwable cause, int errcode, String responseBody) {
        super(message, cause);
        this.errcode = errcode;
        this.responseBody = responseBody;
    }

    public WxCpException(Throwable cause, int errcode, String responseBody) {
        super(cause);
        this.errcode = errcode;
        this.responseBody = responseBody;
    }

    public WxCpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int errcode, String responseBody) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errcode = errcode;
        this.responseBody = responseBody;
    }
}
