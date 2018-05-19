package com.most.core.app.database.dao.factory;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.StrongObjectDAO;

import java.lang.reflect.Constructor;

/**
 * Created by pc on 2018-05-18.
 */
public class DAOFactory {

    public static final <T extends GenericDAO> T createDAO(Class<T> clazz) throws Exception{
        DatabaseName databaseName = clazz.getAnnotation(DatabaseName.class);
        if(databaseName == null) {
            throw new Exception("类【" + clazz.getSimpleName() + "】没有定义DatabaseName注解");
        }

        String value = databaseName.value();
        Constructor constructor = clazz.getConstructor(String.class);
        Object dao = constructor.newInstance(value);

        return (T)dao;
    }
}
