package com.most.core.app.service.config.loader;

import com.most.core.app.service.config.ServiceConfig;

import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/4/16 21:43
 * @Description:
 */
public interface IServiceConfigLoader {

    public List<ServiceConfig> load();
}
