package com.hirun.web.biz.sso;

/**
 * @author Steven
 * @date 2019-12-02
 */
public class RestResult {

    /**
     * 数据行数
     */
    private Integer total;

    /**
     * 数据
     */
    private User rows;

    /**
     * 返回编码 0: 正常, 非0: 异常
     */
    private Integer code;

    /**
     * 异常时返回的错误信息
     */
    private String message;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public User getRows() {
        return rows;
    }

    public void setRows(User rows) {
        this.rows = rows;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("code: ").append(this.code).append(",");
        sb.append("total: ").append(this.total).append(",");
        sb.append(this.rows.toString()).append(",");
        sb.append("}");
        return sb.toString();
    }

}
