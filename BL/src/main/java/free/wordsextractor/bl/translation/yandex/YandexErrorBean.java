package free.wordsextractor.bl.translation.yandex;

import com.google.gson.annotations.SerializedName;

/**
 * Error bean for JSON response from Yandex service
 */
class YandexErrorBean {
    @SerializedName("code")
    private Integer code;                                 /* the code of error */

    @SerializedName("message")
    private String msg;                                   /* the error's message */


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
