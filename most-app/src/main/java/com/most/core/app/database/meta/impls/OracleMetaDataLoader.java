package com.most.core.app.database.meta.impls;

import com.most.core.app.database.meta.IMetaDataLoader;
import com.most.core.app.database.meta.data.TableMetaData;

import java.sql.SQLException;

/**
 * @Author jinnian
 * @Date 2018/3/12 12:46
 * @Description:
 */
public class OracleMetaDataLoader implements IMetaDataLoader {

    public TableMetaData loadMetaData(String databaseName, String tableName) throws SQLException {
        return null;
    }
}
