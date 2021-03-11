package com.kimi.myshop.plus.business;

/**
 * 全局业务异常
 * @author 郭富城
 * @date 2021/3/11 21:46
 * @param
 * @return
 */
public class BusinessException extends RuntimeException {


    private static final long serialVersionUID = 4121349963876720770L;

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException() {

    }

    public BusinessException(BusinessStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }
}
