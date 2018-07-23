package com.most.core.pub.tools;

/**
 * Created by awx on 2018/7/22/022.
 */
public class Utility {

    public static final Throwable getBottomException(Throwable exception) {
        if (exception == null)
            return null;
        if (exception.getCause() != null)
            return getBottomException(exception.getCause());
        else
            return exception;
    }
}
