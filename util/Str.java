package util;

import seamstress.Config;

public class Str {
    public static int indexOf(String line, char pattern) {
        int idx = line.indexOf(pattern);
        if (idx > 0 && line.charAt(idx - 1) == '\\')
            return -1;
        return idx;
    }

    public static int indexOf(String line, char pattern, int start) {
        int idx = line.indexOf(pattern, start);
        if (idx > 0 && line.charAt(idx - 1) == '\\')
            return -1;
        return idx;
    }

    public static int indexOf(String line, String pattern) {
        int idx = line.indexOf(pattern);
        if (idx > 0 && line.charAt(idx - 1) == '\\')
            return -1;
        return idx;
    }

    public static int indexOf(String line, String pattern, int start) {
        int idx = line.indexOf(pattern, start);
        if (idx > 0 && line.charAt(idx - 1) == '\\')
            return -1;
        return idx;
    }

    public static String replace(String str, char ch1, char ch2) {
        if (ch1 == ch2)
            return str;
        StringBuilder builder = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (ch == ch1)
                builder.append(ch2);
            else
                builder.append(ch);
        }
        return builder.toString();
    }

    public static String macros(String txt, Config config) {
        try {
            int l1 = indexOf(txt, "<<");
            int l2 = indexOf(txt, ">>", l1 + 2);
            String before = txt.substring(0, l1);
            String body = txt.substring(l1 + 2, l2).trim();
            String after = txt.substring(l2 + 2);
            String visible = config.get(body);
            if (visible.isEmpty())
                System.out.println("empty variable: " + body);
            return macros(String.format("%s%s%s", before, visible, after), config);
        } catch (Exception ignored) {
        }

        return txt;
    }
}