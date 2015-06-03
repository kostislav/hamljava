package cz.judas.jan.haml.parsing;

import cz.judas.jan.haml.template.tree.EmptyNode;
import cz.judas.jan.haml.template.tree.RootNode;
import cz.judas.jan.haml.template.tree.ruby.FieldReferenceExpression;
import cz.judas.jan.haml.template.tree.ruby.HashEntry;
import cz.judas.jan.haml.template.tree.ruby.RubyHashExpression;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static cz.judas.jan.haml.testutil.Expressions.*;
import static cz.judas.jan.haml.testutil.Nodes.*;
import static cz.judas.jan.haml.testutil.ShortCollections.list;
import static cz.judas.jan.haml.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HamlTreeBuilderTest {
    private HamlTreeBuilder treeBuilder;

    @Before
    public void setUp() throws Exception {
        treeBuilder = new HamlTreeBuilder();
    }

    @Test
    public void processesRealSimpleHaml() throws Exception {
        assertParses("%html", root(
                node("html")
        ));
    }

    @Test
    public void processesNestedTag() throws Exception {
        assertParses("%html\n\t%head", root(
                node("html", list(
                        node("head")
                ))
        ));
    }

    @Test
    public void recursiveNesting() throws Exception {
        assertParses("%html\n\t%head\n\t\t%script\n\t\t%title\n\t%body", root(
                node("html", list(
                        node("head", list(
                                node("script"),
                                node("title")
                        )),
                        node("body")
                ))
        ));
    }

    @Test
    public void html5Doctype() throws Exception {
        assertParses("!!! 5\n%html", root("5",
                node("html")
        ));
    }

    @Test
    public void tagsCanHaveTextContent() throws Exception {
        assertParses("%html\n\t%head\n\t\t%title something", root(
                node("html", list(
                        node("head", list(
                                node("title", string("something"))
                        ))
                ))
        ));
    }

    @Test
    public void implicitClosing() throws Exception {
        assertParses("%ul\n\t%li blah\n%p bleh", root(
                node("ul", list(
                        node("li", string("blah"))
                )),
                node("p", string("bleh"))
        ));
    }

    @Test
    public void classAttribute() throws Exception {
        assertParses("%span.bluh bra bh", root(
                node("span", map(symbol("class"), string("bluh")), string("bra bh"))
        ));
    }

    @Test
    public void classAttributeWithoutContent() throws Exception {
        assertParses("%h1.bluh\n\t%span bra bh", root(
                node("h1", map(symbol("class"), string("bluh")),
                        node("span", string("bra bh"))
                )
        ));
    }

    @Test
    public void multipleClassAttributes() throws Exception {
        assertParses("%span.bluh.lkj bra bh", root(
                node("span", list(hash(symbol("class"), string("bluh")), hash(symbol("class"), string("lkj"))), string("bra bh"))
        ));
    }

    @Test
    public void idAttribute() throws Exception {
        assertParses("%h2#hehe njhg", root(
                node("h2", map(symbol("id"), string("hehe")), string("njhg"))
        ));
    }

    @Test
    public void idAndClassAttribute() throws Exception {
        assertParses("%h2#hehe.dre njhg", root(
                node("h2", list(hash(symbol("id"), string("hehe")), hash(symbol("class"), string("dre"))), string("njhg"))
        ));
    }

    @Test
    public void defaultTagIsDiv() throws Exception {
        assertParses("#going-to-hell blah", root(
                node("div", map(symbol("id"), string("going-to-hell")), string("blah"))
        ));
    }

    @Test
    public void oneGenericAttribute() throws Exception {
        assertParses("%input{ name: 'blah' }", root(
                node("input", map(symbol("name"), string("blah")))
        ));
    }

    @Test
    public void multipleGenericAttributes() throws Exception {
        assertParses("%input{ name: 'blah', value: 'bleh'}", root(
                node("input", map(symbol("name"), string("blah"), symbol("value"), string("bleh")))
        ));
    }

    @Test
    public void textLines() throws Exception {
        assertParses("%gee\n\t%whiz\n\t\tWow this is cool!", root(
                node("gee", list(
                        node("whiz", list(
                                textNode(string("Wow this is cool!"))
                        ))
                ))
        ));
    }

    @Test
    public void tagsCanBeEscaped() throws Exception {
        assertParses("%title\n\t\\= @title", root(
                node("title", list(
                        textNode(string("= @title"))
                ))
        ));
    }

    @Test
    public void oldStyleAttributeHash() throws Exception {
        assertParses("%input{ :name => 'blah', :value => 'bleh'}", root(node("input", map(symbol("name"), string("blah"), symbol("value"), string("bleh")))));
    }

    @Test
    public void htmlStyleAttributes() throws Exception {
        assertParses("%html(xmlns='http://www.w3.org/1999/xhtml' lang='en')", root(
                node("html", map(symbol("xmlns"), string("http://www.w3.org/1999/xhtml"), symbol("lang"), string("en")))
        ));
    }

    @Test
    public void doubleQuoteValues() throws Exception {
        assertParses("%input{ name: \"bleh\" }", root(
                node("input", map(symbol("name"), string("bleh")))
        ));
    }

    @Test
    public void attributesWithColon() throws Exception {
        assertParses("%html(xml:lang='en'){ :'xml:fang' => 'ren', \"xml:tang\" => \"e\" }", root(
                node(
                        "html",
                        list(
                                hash(symbol("xml:lang"), string("en")),
                                new RubyHashExpression(list(
                                        new HashEntry(symbol("xml:fang"), string("ren")),
                                        new HashEntry(string("xml:tang"), string("e"))
                                ))
                        ),
                        EmptyNode.INSTANCE
                )
        ));
    }

    @Test
    public void uselessCodeNode() throws Exception {
        assertParses("- @something", root(
                codeNode(new FieldReferenceExpression("something"))
        ));
    }

    private void assertParses(String input, RootNode tree) throws Exception {
        assertThat(treeBuilder.buildTreeFrom(new StringReader(input)), is(tree));
    }
}
