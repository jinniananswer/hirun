package com.hirun.other;

import com.hirun.pub.domain.enums.plan.PlanType;
import org.junit.Test;

/**
 * Created by pc on 2018-05-18.
 */
public class OtherTest {

    @Test
    public void enumTest() {
        System.out.println(PlanType.normalWork.getValue());
    }
}
