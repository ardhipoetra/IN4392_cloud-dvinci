package nl.tudelft.in4392;

/**
 * Created by ardhipoetra on 10/8/15.
 */
public class Constants {
    public static final String CREDENTIALS = "cld1593:w96dxdOJ";

    public static final int INTERVAL_JOB = 100; // in ms

    public static final String TEST_USER_1 = "user1md5hash";
    public static final String TEST_USER_2 = "user2md5hash";
    public static final String TEST_ADMIN = "adminmd5hash";
    public static final String TEST_TARGET_SSH = "node312";

    public static final String SSH_USER = "cld1593";
    public static final String SSH_PASS = "6uvfi5EY";
    public static final String SSH_ID = "/home/"+SSH_USER+"/.ssh/id_dsa";
    public static final String SSH_KNOWN_HOSTS =     "/home/"+SSH_USER+"/.ssh/known_hosts";

    public static final String VM_TEMPLATE_DEFAULT = "/home/"+SSH_USER+"/OpenNebula/centos-smallnet-disk.one";

}
