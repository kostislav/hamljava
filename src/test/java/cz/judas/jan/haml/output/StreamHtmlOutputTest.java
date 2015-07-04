package cz.judas.jan.haml.output;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StreamHtmlOutputTest {
    private StreamHtmlOutput htmlOutput;

    @Before
    public void setUp() throws Exception {
        htmlOutput = new StreamHtmlOutput(true);
    }

    @Test
    public void addUnescapedDoesNotEscape() throws Exception {
        htmlOutput.addUnescaped("<bleh>");

        assertThat(htmlOutput.build(), is("<bleh>"));
    }

    @Test
    public void addEscapedDoesEscape() throws Exception {
        htmlOutput = new StreamHtmlOutput(false);

        htmlOutput.addEscaped("<>&\"'");

        assertThat(htmlOutput.build(), is("&lt;&gt;&amp;&quot;&#39;"));
    }

    @Test
    public void addEscapesIfDefaultEscapingIsOn() throws Exception {
        htmlOutput.add("<>&\"'");

        assertThat(htmlOutput.build(), is("&lt;&gt;&amp;&quot;&#39;"));
    }

    @Test
    public void addDoesNotEscapesIfDefaultEscapingIsOff() throws Exception {
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(false);

        htmlOutput.add("<>&\"'");

        assertThat(htmlOutput.build(), is("<>&\"'"));
    }

    @Test
    public void createsHtmlTag() throws Exception {
        htmlOutput.htmlTag(
                "a",
                (attributeBuilder) -> attributeBuilder.attribute("href", "http://someurl.com"),
                (bodyBuilder) -> bodyBuilder.add(">> Click here <<")
        );

        assertThat(htmlOutput.build(), is("<a href=\"http://someurl.com\">&gt;&gt; Click here &lt;&lt;</a>"));
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

        assertThat(htmlOutput.build(), is("<img src=\"http://img\" />"));
    }

    @Test
    public void setsBooleanAttributeIfTrue() throws Exception {
        htmlOutput.htmlTag(
                "input",
                (attributeBuilder) -> attributeBuilder.attribute("autofocus", true),
                (bodyBuilder) -> {}
        );

        assertThat(htmlOutput.build(), is("<input autofocus=\"autofocus\" />"));
    }

    @Test
    public void skipsBooleanAttributeIfFalse() throws Exception {
        htmlOutput.htmlTag(
                "input",
                (attributeBuilder) -> attributeBuilder.attribute("autofocus", false),
                (bodyBuilder) -> {}
        );

        assertThat(htmlOutput.build(), is("<input />"));
    }
}
