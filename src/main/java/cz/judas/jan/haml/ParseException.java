package cz.judas.jan.haml;

public class ParseException extends Exception {
    public ParseException(String document, int position) {
        super("Could not parse line " + enclosingLine(document, position));
    }

    private static String enclosingLine(String document, int position) {
        int lineEnd = adjust(document.indexOf('\n', position), document.length());
        int lineStart = adjust(document.lastIndexOf('\n', position), 0);
        return document.substring(lineStart, lineEnd);
    }

    private static int adjust(int index, int replacement) {
        if(index == -1) {
            return replacement;
        } else {
            return index;
        }
    }
}
