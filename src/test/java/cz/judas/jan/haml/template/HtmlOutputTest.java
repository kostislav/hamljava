package cz.judas.jan.haml.template;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HtmlOutputTest {
    @Test
    public void addUnescapedDoesNotEscape() throws Exception {
        HtmlOutput htmlOutput = new HtmlOutput(false);

        htmlOutput.addUnescaped("<bleh>");

        assertThat(htmlOutput.build(), is("<bleh>"));
    }

    @Test
    public void addEscapedDoesEscape() throws Exception {
        HtmlOutput htmlOutput = new HtmlOutput(false);

        htmlOutput.addEscaped("<>&\"'");

        assertThat(htmlOutput.build(), is("&lt;&gt;&amp;&quot;&#39;"));
    }

    @Test
    public void addEscapesIfDefaultEscapingIsOn() throws Exception {
        HtmlOutput htmlOutput = new HtmlOutput(true);

        htmlOutput.add("<>&\"'");

        assertThat(htmlOutput.build(), is("&lt;&gt;&amp;&quot;&#39;"));
    }

    @Test
    public void addDoesNotEscapesIfDefaultEscapingIsOff() throws Exception {
        HtmlOutput htmlOutput = new HtmlOutput(false);

        htmlOutput.add("<>&\"'");

        assertThat(htmlOutput.build(), is("<>&\"'"));
    }
}
