package nl.tudelft.in4392.manager;

import nl.tudelft.in4392.Constants;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

public class VMmanager {

    public static Client c;

	public static VirtualMachine createVM() throws Exception{

        FileInputStream fileInputStream = new FileInputStream(Constants.VM_TEMPLATE_DEFAULT);
        byte[] buffer = new byte[fileInputStream.available()];
        int length = fileInputStream.read(buffer);
        fileInputStream.close();

        String vmtemplate = new String(buffer, 0, length, Charset.forName("UTF-8"));

        try {
            if (c == null)
                c = new Client(Constants.CREDENTIALS, null);
        } catch (ClientConfigurationException e) {
            e.printStackTrace();
        }

        OneResponse rc = VirtualMachine.allocate(c, vmtemplate);

        if(rc.isError()) {
            System.out.println( "failed!");
            throw new Exception( rc.getErrorMessage() );
        }

        int newVMID = Integer.parseInt(rc.getMessage());
        System.out.println("ok, ID " + newVMID + ".");

        VirtualMachine vm = new VirtualMachine(newVMID, c);

        rc = vm.info();

        if(rc.isError())
            throw new Exception( rc.getErrorMessage() );

        System.out.println("The new VM " + vm.getName() + " has status: " + vm.status());

        return vm;
    }

    public static OneResponse deleteVM(VirtualMachine vm) {
        return null;
    }

    public static String showVMInfo(VirtualMachine vm) {
        return "";
    }

    public static String showAllVms() {
        try {
            if (c == null)
                c = new Client(Constants.CREDENTIALS, null);
        } catch (ClientConfigurationException e) {
            e.printStackTrace();
        }

        VirtualMachinePool vpool = new VirtualMachinePool(c);

        return vpool.infoAll().getMessage();
    }

    public static VirtualMachine getAvailableVM() {
        return new VirtualMachine(40885, c);
    }
}
