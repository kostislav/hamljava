package cz.judas.jan.haml;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HamlParserTest {
    private HamlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new HamlParser();
    }

    @Test
    public void processesRealSimpleHaml() throws Exception {
        assertThat(parser.process("html"), is("<html></html>"));
    }

    @Test
    public void processesNestedTag() throws Exception {
        assertThat(parser.process("html\n\thead"), is("<html><head></head></html>"));
    }

    @Test
    public void doctype() throws Exception {
        assertThat(parser.process("!!! 5"), is("<!DOCTYPE html>\n"));
    }

    @Test
    public void tagsCanHaveTextContent() throws Exception {
        assertThat(parser.process("html\n\thead\n\t\ttitle something"), is("<html><head><title>something</title></head></html>"));
    }

    @Test
    public void implicitClosing() throws Exception {
        assertThat(parser.process("ul\n\tli blah\np bleh"), is("<ul><li>blah</li></ul><p>bleh</p>"));
    }

    @Test
    public void classAttribute() throws Exception {
        assertThat(parser.process("span.bluh bra bh"), is("<span class=\"bluh\">bra bh</span>"));
    }
}
