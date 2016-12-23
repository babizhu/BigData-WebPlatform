package org.bbz.bigdata.webplatform.service.oozie;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by liulaoye on 16-12-23.
 * test
 */
public class OozieClientTest{
    @Test
    public void run() throws Exception{

        String oozieUrl = "192.168.1.5:11000/oozie";
        OozieClient oc = new OozieClient( oozieUrl );
        final Properties conf = oc.createConfiguration();
        conf.setProperty("nameNode", "hdfs://quickstart.cloudera:8020");
        conf.setProperty("jobTracker", "localhost:8032");
        conf.setProperty("queueName", "default");

        conf.setProperty("oozie.use.system.libpath", "true");
        conf.setProperty("oozie.libpath", "/lib");

        conf.setProperty("oozie.wf.application.path", "hdfs://quickstart.cloudera:8020/user/cloudera/ly");

        conf.setProperty("user.name", "root");
        conf.setProperty("arg1", "test_arg1");//workflow 全局参数

        try {
            //
            String jobId = oc.run(conf);
            WorkflowJob job = oc.getJobInfo(jobId);
            System.out.println(jobId);
            System.out.println(job.getAppName());
            //System.out.println(job.getId());
            System.out.println(job.getStartTime());
            WorkflowJob.Status status = job.getStatus();
            if(status == WorkflowJob.Status.RUNNING)
                System.out.println("Workflow job running");
            else
                System.out.println("Problem starting Workflow job");
            String log=null;
            for(int i=0;i<10;i++){
                log=oc.getJobLog(jobId);
                System.out.println(log);
                Thread.sleep(1000);
            }

            // oc.kill(jobId);

        } catch (OozieClientException e) {
            e.printStackTrace();
        }


    }

}