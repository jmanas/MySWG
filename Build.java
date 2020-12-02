import seamstress.MyProcessor;

import java.io.File;

public class Build {
    public static void main(String[] args) {
        String root = args[0];
        try {
            MyProcessor myProcessor = new MyProcessor(new File(root).getAbsoluteFile());
            myProcessor.go();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
