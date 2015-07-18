package cz.judas.jan.hamljava.output;

import org.junit.Test;

public class VoidTagHtmlOutputTest {
    @Test(expected = IllegalStateException.class)
    public void addCharThrowsException() throws Exception {
        VoidTagHtmlOutput voidOutput = new VoidTagHtmlOutput();

        voidOutput.addChar('a');
    }

    @Test(expected = IllegalStateException.class)
    public void addUnescapedThrowsException() throws Exception {
        VoidTagHtmlOutput voidOutput = new VoidTagHtmlOutput();

        voidOutput.addUnescaped("a");
    }

    @Test(expected = IllegalStateException.class)
    public void addEscapedThrowsException() throws Exception {
        VoidTagHtmlOutput voidOutput = new VoidTagHtmlOutput();

        voidOutput.addEscaped("a");
    }

    @Test(expected = IllegalStateException.class)
    public void addThrowsException() throws Exception {
        VoidTagHtmlOutput voidOutput = new VoidTagHtmlOutput();

        voidOutput.add("a");
    }

    @Test(expected = IllegalStateException.class)
    public void htmlTagException() throws Exception {
        VoidTagHtmlOutput voidOutput = new VoidTagHtmlOutput();

        voidOutput.htmlTag(
                "a",
                (builder) -> {
                },
                (builder) -> {
                }
        );
    }
}
