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
        assertParses("%html", "<html></html>");
    }

    @Test
    public void processesNestedTag() throws Exception {
        assertParses("%html\n\t%head", "<html><head></head></html>");
    }

    @Test
    public void html5Ddoctype() throws Exception {
        assertParses("!!! 5", "<!DOCTYPE html>\n");
    }

    @Test(expected = ParseException.class)
    public void otherDoctypesAreUnsupported() throws Exception {
        parser.process("!!! Strict");
    }

    @Test
    public void tagsCanHaveTextContent() throws Exception {
        assertParses("%html\n\t%head\n\t\t%title something", "<html><head><title>something</title></head></html>");
    }

    @Test
    public void implicitClosing() throws Exception {
        assertParses("%ul\n\t%li blah\n%p bleh", "<ul><li>blah</li></ul><p>bleh</p>");
    }

    @Test
    public void classAttribute() throws Exception {
        assertParses("%span.bluh bra bh", "<span class=\"bluh\">bra bh</span>");
    }

    @Test
    public void classAttributeWithoutContent() throws Exception {
        assertParses("%h1.bluh\n\t%span bra bh", "<h1 class=\"bluh\"><span>bra bh</span></h1>");
    }

    @Test
    public void multipleClassAttribute() throws Exception {
        assertParses("%span.bluh.lkj bra bh", "<span class=\"bluh lkj\">bra bh</span>");
    }

    @Test
    public void idAttribute() throws Exception {
        assertParses("%h2#hehe njhg", "<h2 id=\"hehe\">njhg</h2>");
    }

    @Test
    public void idAndClassAttribute() throws Exception {
        assertParses("%h2#hehe.dre njhg", "<h2 id=\"hehe\" class=\"dre\">njhg</h2>");
    }

    private void assertParses(String input, String output) throws Exception {
        assertThat(parser.process(input), is(output));
    }
}
