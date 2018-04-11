package com.most.core.app.database.meta.data;

/**
 * @Author jinnian
 * @Date 2018/3/12 10:44
 * @Description:
 */
public class ColumnData {

    private String name;

    private DataFieldType type;

    private int length;

    private boolean nullable;

    private String comment;

    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataFieldType getType() {
        return type;
    }

    public void setType(DataFieldType type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("col: name=" + this.name + ";type=" + this.type + ";length=" + this.length + ";isnullable=" + this.nullable + ";comment=" + this.comment + ";\n");
        return sb.toString();
    }
}
