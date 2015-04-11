package cz.judas.jan.haml;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HamlParserTest {
    @Test
    public void processesHaml() throws Exception {
        HamlParser parser = new HamlParser();

        assertThat(parser.process("!!! 5\n%html\n\t%head\n\t\t%title blah"), is("<!DOCTYPE html>\n<html><head><title>blah</title></head></html>"));
    }
}
