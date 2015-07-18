package cz.judas.jan.hamljava.output;

import java.io.IOException;
import java.io.Writer;

public class QuietWriter {
    private final Writer writer;

    public QuietWriter(Writer writer) {
        this.writer = writer;
    }

    public QuietWriter append(char c) {
        try {
            writer.append(c);
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Could not write", e);
        }
    }

    public QuietWriter append(String s) {
        try {
            writer.append(s);
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Could not write", e);
        }
    }
}
