package ml;

import java.io.*;
import java.util.*;

public class ml {
    public static void main(String[] args) {

        //creating File working directory bc python file requires relative path
        
        File wd = new File("C:\\Users\\aayus\\Documents\\GitHub\\CocoLabs-TrashBot\\ML Model\\CocoLabs TensorFlow\\example");

        //creating the command to run on the command line

        String[] cmd = {
            "python", //python
            "tf_example.py", //relative to working directory
            "\"C:\\Users\\aayus\\Documents\\GitHub\\CocoLabs-TrashBot\\ML Model\\TestForMLModel.jpg\"" //the argument (the image) (needed as an absolute path not relative)

        };

        //keeping this here because not relative to wd "C:\\Users\\aayus\\Documents\\GitHub\\CocoLabs-TrashBot\\ML Model\\CocoLabs TensorFlow\\example\\tf_example.py"

        //actually trying the command
        //try/catch incase of an io exception (cus if I screwed up above)

        try {
            Runtime.getRuntime().exec(cmd, null, wd);
        } catch (IOException e) {
            System.out.println("" + e);
            e.printStackTrace();
        }
    
    }
}
