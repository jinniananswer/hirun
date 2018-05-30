package quartz;

import com.most.core.pub.backgroundtask.BackgroundTaskXml;
import com.most.core.pub.backgroundtask.CronBackgroundTask;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;


/**
 * Created by pc on 2018-05-19.
 */
public class QuartzTest {

    @Test
    public void test(){
        try{
            List<CronBackgroundTask> list = BackgroundTaskXml.getCronBackgroundTask();
            System.out.println("---------exit------");
        } catch(Exception e) {
            System.out.println("---------异常------"); e.printStackTrace();
        }
    }
}
