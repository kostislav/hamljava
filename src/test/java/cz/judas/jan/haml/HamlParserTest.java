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
}
