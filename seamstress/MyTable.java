package seamstress;

import java.util.ArrayList;
import java.util.List;

public class MyTable {
    private final Row[] rows;

    public static String process(String input) {
        int idx1 = input.indexOf("<p>|");
        if (idx1 < 0)
            return input;
        int idx2 = input.indexOf("|</p>", idx1);
        if (idx2 < 0)
            return input;
        MyTable table = new MyTable(input.substring(idx1 + 3, idx2 + 1));
        String prefix = input.substring(0, idx1 + 3);
        String postfix = process(input.substring(idx2 + 1));
        return String.format("%s%s%s",
                prefix, table.replace(), postfix);
    }

    public MyTable(String input) {
        String[] lines = input.split("\\R");
        rows = new Row[lines.length];
        for (int col = 0; col < lines.length; col++)
            rows[col] = new Row(lines[col]);
//        System.out.println(Arrays.toString(rows));
    }

    private String replace() {
        StringBuilder builder = new StringBuilder();
        builder.append("<table>");
        boolean withHeaders = rows.length > 1 && rows[1].isSep();
        for (int r = 0; r < rows.length; r++) {
            Row row = rows[r];
            builder.append("<tr>");
            if (withHeaders && r == 0)
                emitColumn(builder, row, "th");
            else if (!(withHeaders && r == 1))
                emitColumn(builder, row, "td");
            builder.append("</tr>");
        }
        builder.append("</table>");
        return builder.toString();
    }

    private void emitColumn(StringBuilder builder, Row row, String tag) {
        for (String col : row.columns)
            builder.append("<").append(tag).append(">")
                    .append(col)
                    .append("</").append(tag).append(">");
    }

    private static class Row {
        private final String line;
        private final List<String> columns = new ArrayList<>();

        public Row(String line) {
            this.line = line;
            int s = 0;
            try {
                int at = 0;
                while (line.charAt(at) != '|')
                    at++;
                at++;
                s = at;
                for (; ; ) {
                    while (line.charAt(at) != '|')
                        at++;
                    columns.add(line.substring(s, at));
                    at++;
                    s = at;
                }
            } catch (Exception ignored) {
            }
            if (line.length() > s)
                columns.add(line.substring(s));
        }

        @Override
        public String toString() {
            return line;
        }

        public boolean isSep() {
            for (String s : columns) {
                if (s.contains("---"))
                    return true;
            }
            return false;
        }
    }
}
