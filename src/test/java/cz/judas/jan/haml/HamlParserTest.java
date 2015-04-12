package cz.judas.jan.haml;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

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
        assertThat(
                parser.process("!!! 5\n%html\n\t%head\n\t\t%title blah", new VariableMap(Collections.emptyMap())),
                is("<!DOCTYPE html>\n<html><head><title>blah</title></head></html>")
        );
    }

    @Test
    public void textLinesAreNotEscaped() throws Exception {
        assertThat(
                parser.process("%p\n\t<div id=\"blah\">Blah!</div>", new VariableMap(Collections.emptyMap())),
                is("<p><div id=\"blah\">Blah!</div></p>")
        );
    }
}
