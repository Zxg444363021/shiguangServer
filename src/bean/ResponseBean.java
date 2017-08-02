package bean;

import entity.User;

/**
 * Created by ZXG on 2017/4/28.
 */

public class ResponseBean {
    public static final String
            GET_IDENTIFY_CODE_SUCC="00",
            GET_IDENTIFY_CODE_FAIL="01",
            REGIST_SUCC="11",
            REGIST_FAIL="10",
            LOGIN_SUCC="21",
            LOGIN_FAIL="20";


    private String code, message, data;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                ", user=" + user +
                '}';
    }
}
