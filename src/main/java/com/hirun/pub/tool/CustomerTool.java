package com.hirun.pub.tool;

import com.hirun.pub.domain.entity.cust.CustomerEntity;

/**
 * Created by pc on 2018-05-01.
 */
public class CustomerTool {

    public static boolean isNewCust(CustomerEntity customerEntity) {
        if(customerEntity.getCustStatus().equals("9")) {
            return true;
        } else {
            return false;
        }
    }
}
