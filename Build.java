import seamstress.MyProcessor;

import java.io.File;

public class Build {
    public static void main(String[] args) {
        String root;
        if (args.length > 0)
            root = args[0];
        else
            root = ".";
        try {
            MyProcessor myProcessor = new MyProcessor(new File(root));
            myProcessor.go();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
