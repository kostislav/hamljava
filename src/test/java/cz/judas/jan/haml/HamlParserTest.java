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
    public void processesHaml() throws Exception {
        assertParses("!!! 5\n%html\n\t%head\n\t\t%title blah", "<!DOCTYPE html>\n<html><head><title>blah</title></head></html>");
    }

    @Test
    public void textLinesAreNotEscaped() throws Exception {
        assertParses("%p\n\t<div id=\"blah\">Blah!</div>", "<p><div id=\"blah\">Blah!</div></p>");
    }

    private void assertParses(String input, String output) throws ParseException {
        assertThat(parser.process(input), is(output));
    }
}
