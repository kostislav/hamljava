package cz.judas.jan.haml.output;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StreamHtmlOutputTest {
    @Test
    public void addUnescapedDoesNotEscape() throws Exception {
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(false);

        htmlOutput.addUnescaped("<bleh>");

        assertThat(htmlOutput.build(), is("<bleh>"));
    }

    @Test
    public void addEscapedDoesEscape() throws Exception {
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(false);

        htmlOutput.addEscaped("<>&\"'");

        assertThat(htmlOutput.build(), is("&lt;&gt;&amp;&quot;&#39;"));
    }

    @Test
    public void addEscapesIfDefaultEscapingIsOn() throws Exception {
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(true);

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
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(true);

        htmlOutput.htmlTag(
                "a",
                (attributeBuilder) -> attributeBuilder.attribute("href", "http://someurl.com"),
                (bodyBuilder) -> bodyBuilder.add(">> Click here <<")
        );

        assertThat(htmlOutput.build(), is("<a href=\"http://someurl.com\">&gt;&gt; Click here &lt;&lt;</a>"));
    }
}
