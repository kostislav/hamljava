package cz.judas.jan.hamljava.output;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StreamHtmlOutputTest {
    private StreamHtmlOutput htmlOutput;
    private StringWriter writer;

    @Before
    public void setUp() throws Exception {
        writer = new StringWriter();
        htmlOutput = new StreamHtmlOutput(writer, true);
    }

    @Test
    public void addUnescapedDoesNotEscape() throws Exception {
        htmlOutput.addUnescaped("<bleh>");

        assertThat(writer.toString(), is("<bleh>"));
    }

    @Test
    public void addEscapedDoesEscape() throws Exception {
        htmlOutput = new StreamHtmlOutput(writer, false);

        htmlOutput.addEscaped("<>&\"'");

        assertThat(writer.toString(), is("&lt;&gt;&amp;&quot;&#39;"));
    }

    @Test
    public void addEscapesIfDefaultEscapingIsOn() throws Exception {
        htmlOutput.add("<>&\"'");

        assertThat(writer.toString(), is("&lt;&gt;&amp;&quot;&#39;"));
    }

    @Test
    public void addDoesNotEscapesIfDefaultEscapingIsOff() throws Exception {
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(writer, false);

        htmlOutput.add("<>&\"'");

        assertThat(writer.toString(), is("<>&\"'"));
    }

    @Test
    public void createsHtmlTag() throws Exception {
        htmlOutput.htmlTag(
                "a",
                (attributeBuilder) -> attributeBuilder.attribute("href", "http://someurl.com"),
                (bodyBuilder) -> bodyBuilder.add(">> Click here <<")
        );

        assertThat(writer.toString(), is("<a href=\"http://someurl.com\">&gt;&gt; Click here &lt;&lt;</a>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingContentFailsForVoidTag() throws Exception {
        htmlOutput.htmlTag(
                "input",
                (attributeBuilder) -> {},
                (bodyBuilder) -> bodyBuilder.add("something")
        );
    }

    @Test
    public void properlyClosesVoidTag() throws Exception {
        htmlOutput.htmlTag(
                "img",
                (attributeBuilder) -> attributeBuilder.attribute("src", "http://img"),
                (bodyBuilder) -> {}
        );

        assertThat(writer.toString(), is("<img src=\"http://img\" />"));
    }

    @Test
    public void setsBooleanAttributeIfTrue() throws Exception {
        htmlOutput.htmlTag(
                "input",
                (attributeBuilder) -> attributeBuilder.attribute("autofocus", true),
                (bodyBuilder) -> {}
        );

        assertThat(writer.toString(), is("<input autofocus=\"autofocus\" />"));
    }

    @Test
    public void skipsBooleanAttributeIfFalse() throws Exception {
        htmlOutput.htmlTag(
                "input",
                (attributeBuilder) -> attributeBuilder.attribute("autofocus", false),
                (bodyBuilder) -> {}
        );

        assertThat(writer.toString(), is("<input />"));
    }
}
