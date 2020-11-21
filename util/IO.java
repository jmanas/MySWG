package util;

import java.io.*;
import java.nio.charset.Charset;

public class IO {
    private static final byte[] BUFFER = new byte[4 * 1024];

    public static BufferedReader getReader(File file)
            throws IOException {
        InputStream is = new FileInputStream(file);
        Encoding encoding = new Encoding(is);
        is.close();

        is = new FileInputStream(file);
        if (encoding.getBomCount() > 0)
            is.read(new byte[encoding.getBomCount()]);
        return new BufferedReader(
                new InputStreamReader(is, encoding.getCharset()));
    }

    public static PrintWriter unicodePrintWriter(File file, Charset encoding)
            throws IOException {
        OutputStream os = new FileOutputStream(file);
        os.write(Encoding.getBOM(encoding));
        return new PrintWriter(
                new OutputStreamWriter(os, encoding));
    }

    public static void copy(File dst, File src) {
        try (
                InputStream is = new FileInputStream(src);
                OutputStream os = new FileOutputStream(dst)
        ) {
            for (; ; ) {
                int n = is.read(BUFFER);
                if (n < 0)
                    break;
                os.write(BUFFER, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(PrintWriter out, BufferedReader in)
            throws IOException {
        for (; ; ) {
            String line = in.readLine();
            if (line == null)
                break;
            out.println(line);
        }
    }

    public static void main(String[] args) {
        try (
                InputStream is = new FileInputStream(args[0])
        ) {
            Encoding encoding = new Encoding(is);
            System.out.println("by BOM:    " + encoding.getBomCount());
            System.out.println("character: " + encoding.getCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
