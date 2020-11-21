package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Encoding {
    private final int bomCount;
    private final Charset charset;

    private final byte[] starter;
    private final int n;

    private static final List<Charset> charsetList;
    private static final Map<Charset, byte[]> BOM_MAP;

    static {
        charsetList = new ArrayList<>();
        charsetList.add(StandardCharsets.UTF_8);
        charsetList.add(StandardCharsets.UTF_16BE);
        charsetList.add(StandardCharsets.UTF_16LE);
        charsetList.add(StandardCharsets.ISO_8859_1);

        BOM_MAP = new HashMap<>();
        BOM_MAP.put(StandardCharsets.UTF_8, new byte[]{(byte) 0xef, (byte) 0xbb, (byte) 0xbf});
        BOM_MAP.put(StandardCharsets.UTF_16BE, new byte[]{(byte) 0xfe, (byte) 0xff});
        BOM_MAP.put(StandardCharsets.UTF_16LE, new byte[]{(byte) 0xff, (byte) 0xfe});
    }

    public Encoding(InputStream is)
            throws IOException {
        starter = new byte[256];
        n = is.read(starter);

        for (Charset set : BOM_MAP.keySet()) {
            if (matchBOM(BOM_MAP.get(set))) {
                bomCount = BOM_MAP.get(set).length;
                charset = set;
                return;
            }
        }
        for (Charset set : charsetList) {
            if (match("áéíóú".getBytes(set))) {
                bomCount = 0;
                charset = set;
                return;
            }
        }

        bomCount = 0;
        charset = Charset.defaultCharset();
    }

    public static byte[] getBOM(Charset encoding) {
        return BOM_MAP.get(encoding);
    }

    public int getBomCount() {
        return bomCount;
    }

    public Charset getCharset() {
        return charset;
    }

    private boolean matchBOM(byte[] pattern) {
//        System.out.println(bytesToHex(pattern));
        for (int at = 0; at < pattern.length; at++) {
            if (starter[at] != pattern[at])
                return false;
        }
        return true;
    }

    private boolean match(byte[] pattern) {
//        System.out.println(bytesToHex(pattern));
        for (int start = 0; start + pattern.length < n; start++) {
            boolean match = true;
            for (int at = 0; at < pattern.length; at++) {
                if (starter[start + at] != pattern[at]) {
                    match = false;
                    break;
                }
            }
            if (match)
                return true;
        }
        return false;
    }

    private static final String HEX_ARRAY = "0123456789ABCDEF";

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            builder.append(HEX_ARRAY.charAt(v >>> 4));
            builder.append(HEX_ARRAY.charAt(v & 0x0F));
            builder.append(' ');
        }
        return builder.toString();
    }

    public static void main(String[] args)
            throws IOException {
        File file = new File("test/test1.md");
        InputStream is = new FileInputStream(file);
        Encoding encoding = new Encoding(is);
        System.out.println(encoding.getCharset());
    }

}
