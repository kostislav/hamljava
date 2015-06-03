package cz.judas.jan.haml.template.tree;

import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.testutil.MockTemplateContext;
import cz.judas.jan.haml.template.tree.ruby.RubyHashExpression;
import org.junit.Test;

import java.util.Collections;

import static cz.judas.jan.haml.testutil.Expressions.string;
import static cz.judas.jan.haml.testutil.Expressions.symbol;
import static cz.judas.jan.haml.testutil.Nodes.textNode;
import static cz.judas.jan.haml.testutil.ShortCollections.list;
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
        HtmlOutput htmlOutput = new HtmlOutput(false);

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
        HtmlOutput htmlOutput = new HtmlOutput(false);

        htmlNode.evaluate(htmlOutput, MockTemplateContext.EMPTY);

        assertThat(htmlOutput.build(), is("<div id=\"abc_def\">content</div>"));
    }
}
