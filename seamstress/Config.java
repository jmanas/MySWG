package seamstress;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jose on 07-Jan-16.
 */
public class Config {
    private File root;
    private final Properties properties;

    Config( File configFile)
            throws Exception {
        root = configFile.getParentFile();

        properties = new Properties();
        try (BufferedReader reader = util.IO.getReader(configFile)
        ) {
            for (; ; ) {
                String line = reader.readLine();
                if (line == null)
                    break;
                if (line.startsWith("//"))
                    continue;
                try {
                    int dotdot = line.indexOf(':');
                    String key = line.substring(0, dotdot).trim();
                    String val = line.substring(dotdot + 1).trim();
                    properties.put(key, val);
                } catch (Exception ignored) {
                }
            }
        }

        if (properties.contains("root"))
            root = new File(properties.getProperty("root"));
        properties.setProperty("root", root.getCanonicalPath());
    }

    public Config(Config context) {
        this.root = context.root;
        properties = new Properties(context.properties);
    }

    public Config(Config context, BufferedReader reader)
            throws IOException {
        this.root = context.root;
        properties = new Properties(context.properties);
        for (; ; ) {
            String line = reader.readLine();
            if (line == null)
                break;
            if (line.startsWith("---"))
                break;
            try {
                int dotdot = line.indexOf(':');
                String key = line.substring(0, dotdot).trim();
                String val = line.substring(dotdot + 1).trim();
                set(key, val);
            } catch (Exception ignored) {
            }
        }
    }

    public File getRoot() {
        return root;
    }

    public void set(String key, String val) {
        properties.put(key, val);
    }

    public String get(String key) {
        return properties.getProperty(key, "");
    }

    public String get(String key, String def) {
        return properties.getProperty(key, def);
    }
}
