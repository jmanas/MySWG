package seamstress;

import com.github.rjeschke.txtmark.Processor;
import util.IO;
import util.Str;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class MyProcessor {
    private static final char SEPARATOR_CHAR = File.separatorChar;
    private static final String SEPARATOR_STRING = File.separator;
    private static final char SEPARATOR = '/';
    private static final String NL = System.getProperty("line.separator");

    private final File root;
    private final File site;
    private final Config rootConfig;

    public MyProcessor(File configFile)
            throws Exception {
        rootConfig = new Config(configFile);
        root = rootConfig.getRoot();
        String dst_site = rootConfig.get("site");
        if (dst_site != null && dst_site.length() > 0)
            this.site = new File(root, dst_site);
        else
            this.site = new File(root, "_site");
    }

    public void go() {
        go(site, root);
    }

    private void go(File dst, File src) {
        if (!dst.exists())
            dst.mkdirs();
        File[] children = src.listFiles();
        if (children == null)
            return;
        for (File child : children) {
            String childname = child.getName();
            if (childname.charAt(0) == '_')
                continue;

            if (child.isDirectory()) {
                go(new File(dst, childname), child);
                continue;
            }

            long srcDate = child.lastModified();

            String ext = ".md";
            if (childname.endsWith(ext)) {
                String rootname = childname.substring(0, childname.length() - ext.length());
                String dstname = rootname + ".html";
                File dstChild = new File(dst, dstname);
                if (haveTo(srcDate, dstChild)) {
                    System.out.println("generate: " + getRelativePath(site, dstChild));
                    translate(dstChild, child);
                }
                continue;
            }

            {
                File dstChild = new File(dst, childname);
                if (!child.equals(dstChild) && haveTo(srcDate, dstChild)) {
                    System.out.println("copy: " + getRelativePath(site, dstChild));
                    IO.copy(dstChild, child);
                }
            }
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean haveTo(long srcDate, File dstChild) {
        if (!dstChild.exists())
            return true;
        if ("true".equalsIgnoreCase(rootConfig.get("force")))
            return true;
        if (dstChild.length() == 0)
            return true;
        return dstChild.lastModified() < srcDate;
    }

    private void translate(File dst, File src) {
        try (
                PrintWriter writer = IO.unicodePrintWriter(dst, StandardCharsets.UTF_8)
        ) {
            Config config = new Config(rootConfig);
            config.set("ptr", pathToRoot(src));
            insert(writer, config, src);
        } catch (IOException e) {
            try {
                Files.delete(dst.toPath());
            } catch (IOException ignored) {
            }
            e.printStackTrace();
        }
    }

    private void insert(PrintWriter writer, Config config, File src)
            throws IOException {
        if (src.getName().toLowerCase().endsWith(".md")) {
            processMD(writer, config, src);
            return;
        }
        if (src.getName().toLowerCase().endsWith(".mmd")) {
            processMMD(writer, config, src);
            return;
        }

        try (BufferedReader reader = IO.getReader(src)) {
            for (; ; ) {
                String line = reader.readLine();
                if (line == null)
                    break;
                writer.println(line);
            }
        }
    }

    private void processMD(PrintWriter writer, Config config0, File src)
            throws IOException {
        BufferedReader reader1 = IO.getReader(src);
        Config config1 = new Config(config0, reader1);

        StringBuilder builder = new StringBuilder();
        for (; ; ) {
            String line = reader1.readLine();
            if (line == null)
                break;
            if (line.startsWith("//"))
                continue;
            if (line.startsWith("<<<")) {
                dump(writer, builder);
                String s = Str.macros(line.substring(3).trim(), config1);
                writer.println(s);
                builder = new StringBuilder();
                continue;
            }
            if (line.startsWith("<<")) {
                dump(writer, builder);
                String filename = Str.macros(line.substring(2).trim(), config1);
                File src2 = getFile(src, filename);
                insert(writer, config1, src2);
                builder = new StringBuilder();
                continue;
            }
            line = Str.macros(line, config1);
            builder.append(line).append(NL);
        }
        reader1.close();
        dump(writer, builder);
    }

    private void dump(PrintWriter writer, StringBuilder builder)
            throws IOException {
        StringReader stringReader = new StringReader(builder.toString());
        String rev0 = Processor.process(stringReader);
        String rev1 = MyTable.process(rev0);
        writer.write(rev1);
    }

    private void processMMD(PrintWriter writer, Config config0, File src)
            throws IOException {
        BufferedReader reader1 = IO.getReader(src);

        for (; ; ) {
            String line = reader1.readLine();
            if (line == null)
                break;
            if (line.startsWith("//"))
                continue;
            if (line.startsWith("<<")) {
                String filename = Str.macros(line.substring(2).trim(), (config0));
                File src2 = getFile(src, filename);
                insert(writer, (config0), src2);
                continue;
            }
            line = Str.macros(line, (config0));
            writer.println(line);
        }
        reader1.close();
    }

    private File getFile(File src, String filename) {
        File file = new File(filename);
        if (file.isAbsolute())
            return file;
        if (filename.startsWith("/"))
            return new File(rootConfig.getRoot(), filename.substring(1));
        return new File(src.getParent(), filename);
    }

    private String pathToRoot(File src) {
        try {
            String rPath = root.getCanonicalPath();
            String cPath = src.getCanonicalPath();
            if (cPath.startsWith(rPath))
                cPath = cPath.substring(rPath.length());
            int depth = 0;
            for (int i = 0; i < cPath.length(); i++) {
                if (cPath.charAt(i) == SEPARATOR_CHAR)
                    depth++;
            }
            if (depth == 1)
                return ".";
            String ptr = "";
            for (int i = 1; i < depth; i++) {
                if (ptr.length() > 0)
                    ptr += SEPARATOR;
                ptr += "..";
            }
            return ptr;
        } catch (IOException e) {
            return ".";
        }
    }

    private String getRelativePath(File wrt, File dst) {
        try {
            String rPath = wrt.getCanonicalPath();
            String cPath = dst.getCanonicalPath();
            if (cPath.startsWith(rPath))
                cPath = cPath.substring(rPath.length());

            if (cPath.startsWith(SEPARATOR_STRING))
                cPath = cPath.substring(SEPARATOR_STRING.length());
            return Str.replace(cPath, SEPARATOR_CHAR, SEPARATOR);
        } catch (IOException e) {
            return dst.toString();
        }
    }
}