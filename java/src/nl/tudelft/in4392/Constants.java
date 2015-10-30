package nl.tudelft.in4392;

/**
 * Created by ardhipoetra on 10/8/15.
 */
public class Constants {
    public static final String CREDENTIALS = "cld1593:w96dxdOJ";

    public static final int INTERVAL_JOB = 300; // in ms

    public static final String TEST_USER_1 = "user1md5hash";
    public static final String TEST_USER_2 = "user2md5hash";
    public static final String TEST_ADMIN = "adminmd5hash";
    public static final String TEST_TARGET_SSH = "node312";

    public static final String START_WORKSPACE_VM = "/home/cld1593/cloud-dvinci/java/out";
    public static final String LOCAL_ENGINE_JAR = "/home/cld1593/Local_Engine/dist/local-engine.jar";

    public static final String SSH_USER = "cld1593";
    public static final String SSH_PASS = "6uvfi5EY";
    public static final String SSH_ID = "/home/"+SSH_USER+"/.ssh/id_dsa";
    public static final String SSH_KNOWN_HOSTS =     "/home/"+SSH_USER+"/.ssh/known_hosts";

    public static final String VM_TEMPLATE_DEFAULT = "/home/"+SSH_USER+"/OpenNebula/centos-smallnet-qcow2.one";

    public static final int VM_MONITOR_CHECK_INTERVAL = 500; // in ms

    public static final int VM_KILL_TIMEOUT = 50; // in s
}
