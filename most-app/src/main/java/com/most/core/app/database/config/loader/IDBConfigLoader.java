package com.most.core.app.database.config.loader;

import com.most.core.app.database.config.DatabaseConfig;

import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/2/14 2:30
 * @Description:
 */
public interface IDBConfigLoader {

    /**
     * 装载数据库配置信息
     * @return
     */
    public List<DatabaseConfig> load();
}
