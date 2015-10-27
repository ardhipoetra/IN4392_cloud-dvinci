package nl.tudelft.vmlocal;

/**
 * Created by ardhipoetra on 10/26/15.
 */
public class LocalMain {
    public static void main(String[] args) {

        Operation cn = new Operation();
        boolean test = cn.GetInfo(args[0],args[1]);
        System.out.println(test);
        int i = 3;

        //tmp
        cn.rotate(args[i]);

//        if ("-convert") :
//        try {
//            cn.ModifyExtension(args[i]);
//            i += 2;
//        } catch(ArrayIndexOutOfBoundsException ex){
//            System.out.println("Wrong indexing or provided data!");
//        }

//        for (String arg : args) {
//            switch(arg) {
//                case "-convert" :
//                    try {
//                        cn.ModifyExtension(args[i]);
//                        i += 2;
//                    } catch(ArrayIndexOutOfBoundsException ex){
//                        System.out.println("Wrong indexing or provided data!");
//                    }
//                    break;
//                case "-scaletox" :
//                    try {
//                        cn.scaletox(args[i]);
//                        i += 2;
//                    } catch(ArrayIndexOutOfBoundsException ex){
//                        System.out.println("Wrong indexing or provided data!");
//                    }
//                    break;
//                case "-scaletoy" :
//                    try {
//                        cn.scaletoy(args[i]);
//                        i += 2;
//                    } catch(ArrayIndexOutOfBoundsException ex){
//                        System.out.println("Wrong indexing or provided data!");
//                    }
//                    break;
//                case "-resize" :
//                    try {
//                        cn.resize(args[i], args[i+1]);
//                        i += 3;
//                    } catch(ArrayIndexOutOfBoundsException ex){
//                        System.out.println("Wrong indexing or provided data!");
//                    }
//                    break;
//                case "-name" :
//                    try {
//                        cn.ModifyName(args[i]);
//                        i += 2;
//                    } catch(ArrayIndexOutOfBoundsException ex){
//                        System.out.println("Wrong indexing or provided data!");
//                    }
//                    break;
//                case "-rotate" :
//                    try {
//                        cn.rotate(args[i]);
//                        i += 2;
//                    } catch(ArrayIndexOutOfBoundsException ex){
//                        System.out.println("Wrong indexing or provided data!");
//                    }
//                    break;
//                case "-colorspace" :
//                    try {
//                        cn.colorspace(args[i]);
//                        i += 2;
//                    } catch(ArrayIndexOutOfBoundsException ex){
//                        System.out.println("Wrong indexing or provided data!");
//                    }
//                    break;
//
//            }
//        }

        cn.GetItDone();

        String path = cn.ReturnPath();

        if (path != null)
            System.out.println("The new file can be found here :"+path);
        else
            System.out.println("An error occured! No new file is available!");
    }

}
