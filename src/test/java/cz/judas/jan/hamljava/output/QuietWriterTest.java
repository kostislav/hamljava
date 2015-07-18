package cz.judas.jan.hamljava.output;

import org.junit.Test;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class QuietWriterTest {
    @Test
    public void writesByte() throws Exception {
        StringWriter stringWriter = new StringWriter();
        QuietWriter quietWriter = new QuietWriter(stringWriter);

        quietWriter.append('v');

        assertThat(stringWriter.toString(), is("v"));
    }

    @Test
    public void writesString() throws Exception {
        StringWriter stringWriter = new StringWriter();
        QuietWriter quietWriter = new QuietWriter(stringWriter);

        quietWriter.append("abc");

        assertThat(stringWriter.toString(), is("abc"));
    }

    @Test
    public void canBeChained() throws Exception {
        StringWriter stringWriter = new StringWriter();
        QuietWriter quietWriter = new QuietWriter(stringWriter);

        quietWriter.append('a').append("bc").append('d');

        assertThat(stringWriter.toString(), is("abcd"));
    }

    @Test(expected = RuntimeException.class)
    public void wrapsIOExceptionOnByteWrite() throws Exception {
        QuietWriter quietWriter = new QuietWriter(new BrokenWriter());

        quietWriter.append('a');
    }

    @Test(expected = RuntimeException.class)
    public void wrapsIOExceptionOnStringWrite() throws Exception {
        QuietWriter quietWriter = new QuietWriter(new BrokenWriter());

        quietWriter.append("aa");
    }

    private static class BrokenWriter extends FilterWriter {
        protected BrokenWriter() {
            super(new StringWriter());
        }

        @Override
        public StringWriter append(CharSequence csq) throws IOException {
            throw new IOException("Is broken");
        }

        @Override
        public StringWriter append(char c) throws IOException {
            throw new IOException("Is broken even more");
        }
    }
}
