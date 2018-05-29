package com.hirun.app.core;

import com.hirun.app.dao.plan.PlanDAO;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.app.service.register.ServiceRegister;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

/**
 * Created by pc on 2018-05-18.
 */
public class DAOTest {

    @Before
    public void bootStart() throws Exception {
        ConnectionFactory.init();
    }

    @Test
    public void createDAOByAnnotationTest() {
        try{
            PlanDAO dao = createDAO(PlanDAO.class);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private <T extends GenericDAO> T createDAO(Class<T> clazz) throws Exception{
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
