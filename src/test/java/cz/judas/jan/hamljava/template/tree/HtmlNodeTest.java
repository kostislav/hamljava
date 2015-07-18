package cz.judas.jan.hamljava.template.tree;

import cz.judas.jan.hamljava.output.StreamHtmlOutput;
import cz.judas.jan.hamljava.template.tree.ruby.RubyHashExpression;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Test;

import java.util.Collections;

import static cz.judas.jan.hamljava.testutil.Expressions.string;
import static cz.judas.jan.hamljava.testutil.Expressions.symbol;
import static cz.judas.jan.hamljava.testutil.Nodes.textNode;
import static cz.judas.jan.hamljava.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HtmlNodeTest {
    @Test
    public void mergesClassAttributes() throws Exception {
        HtmlNode htmlNode = new HtmlNode(
                "div",
                list(
                        RubyHashExpression.singleEntryHash(symbol("class"), string("abc")),
                        RubyHashExpression.singleEntryHash(symbol("class"), string("def"))
                ),
                textNode(string("content")),
                Collections.emptyList()
        );
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(false);

        htmlNode.evaluate(htmlOutput, MockTemplateContext.EMPTY);

        assertThat(htmlOutput.build(), is("<div class=\"abc def\">content</div>"));
    }

    @Test
    public void mergesIdAttributes() throws Exception {
        HtmlNode htmlNode = new HtmlNode(
                "div",
                list(
                        RubyHashExpression.singleEntryHash(symbol("id"), string("abc")),
                        RubyHashExpression.singleEntryHash(symbol("id"), string("def"))
                ),
                textNode(string("content")),
                Collections.emptyList()
        );
        StreamHtmlOutput htmlOutput = new StreamHtmlOutput(false);

        htmlNode.evaluate(htmlOutput, MockTemplateContext.EMPTY);

        assertThat(htmlOutput.build(), is("<div id=\"abc_def\">content</div>"));
    }
}
