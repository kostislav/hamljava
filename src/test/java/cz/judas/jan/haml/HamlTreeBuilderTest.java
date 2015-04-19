package cz.judas.jan.haml;

import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.tree.RootNode;
import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.haml.Nodes.node;
import static cz.judas.jan.haml.Nodes.root;
import static cz.judas.jan.haml.Nodes.textNode;
import static cz.judas.jan.haml.Values.stringValue;
import static cz.judas.jan.haml.Values.symbol;
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
        assertParses("%html", root(node("html")));
    }

    @Test
    public void processesNestedTag() throws Exception {
        assertParses("%html\n\t%head", root(node("html", node("head"))));
    }

    @Test
    public void html5Ddoctype() throws Exception {
        assertParses("!!! 5", root("5"));
    }

    @Test
    public void tagsCanHaveTextContent() throws Exception {
        assertParses("%html\n\t%head\n\t\t%title something", root(node("html", node("head", node("title", stringValue("something"))))));
    }

    @Test
    public void implicitClosing() throws Exception {
        assertParses("%ul\n\t%li blah\n%p bleh", root(node("ul", node("li", stringValue("blah"))), node("p", stringValue("bleh"))));
    }

    @Test
    public void classAttribute() throws Exception {
        assertParses("%span.bluh bra bh", root(node("span", ImmutableMap.of(symbol("class"), stringValue("bluh")), stringValue("bra bh"))));
    }

    @Test
    public void classAttributeWithoutContent() throws Exception {
        assertParses("%h1.bluh\n\t%span bra bh", root(node("h1", ImmutableMap.of(symbol("class"), stringValue("bluh")), node("span", stringValue("bra bh")))));
    }

    @Test
    public void multipleClassAttributes() throws Exception {
        assertParses("%span.bluh.lkj bra bh", root(node("span", ImmutableMap.of(symbol("class"), stringValue("bluh lkj")), stringValue("bra bh"))));
    }

    @Test
    public void idAttribute() throws Exception {
        assertParses("%h2#hehe njhg", root(node("h2", ImmutableMap.of(symbol("id"), stringValue("hehe")), stringValue("njhg"))));
    }

    @Test
    public void idAndClassAttribute() throws Exception {
        assertParses("%h2#hehe.dre njhg", root(node("h2", ImmutableMap.of(symbol("id"), stringValue("hehe"), symbol("class"), stringValue("dre")), stringValue("njhg"))));
    }

    @Test
    public void defaultTagIsDiv() throws Exception {
        assertParses("#going-to-hell blah", root(node("div", ImmutableMap.of(symbol("id"), stringValue("going-to-hell")), stringValue("blah"))));
    }

    @Test
    public void partsCanBeSeparatedByWhitespace() throws Exception {
        assertParses("%span .bluh .lkj bra bh", root(node("span", ImmutableMap.of(symbol("class"), stringValue("bluh lkj")), stringValue("bra bh"))));
    }

    @Test
    public void oneGenericAttribute() throws Exception {
        assertParses("%input{ name: 'blah' }", root(node("input", ImmutableMap.of(symbol("name"), stringValue("blah")))));
    }

    @Test
    public void multipleGenericAttributes() throws Exception {
        assertParses("%input{ name: 'blah', value: 'bleh'}", root(node("input", ImmutableMap.of(symbol("name"), stringValue("blah"), symbol("value"), stringValue("bleh")))));
    }

    @Test
    public void textLines() throws Exception {
        assertParses("%gee\n\t%whiz\n\t\tWow this is cool!", root(node("gee", node("whiz", textNode(stringValue("Wow this is cool!"))))));
    }

    @Test
    public void tagsCanBeEscaped() throws Exception {
        assertParses("%title\n\t\\= @title", root(node("title", textNode(stringValue("= @title")))));
    }

    @Test
    public void oldStyleAttributeHash() throws Exception {
        assertParses("%input{ :name => 'blah', :value => 'bleh'}", root(node("input", ImmutableMap.of(symbol("name"), stringValue("blah"), symbol("value"), stringValue("bleh")))));
    }

    private void assertParses(String input, RootNode tree) throws Exception {
        assertThat(treeBuilder.buildTreeFrom(input), is(tree));
    }
}
