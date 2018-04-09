package free.wordsextractor.bl.translation.yandex;

import com.google.gson.annotations.SerializedName;

public class YandexErrorBean {
    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String msg;


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
