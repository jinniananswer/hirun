package com.most.core.app.database.meta;

import com.most.core.app.database.meta.data.TableMetaData;

import java.sql.SQLException;

/**
 * @Author jinnian
 * @Date 2018/3/12 10:39
 * @Description:
 */
public interface IMetaDataLoader {

    public TableMetaData loadMetaData(String databaseName, String tableName) throws SQLException;
}
