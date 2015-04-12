package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RubyGrammarTest {
    @Test
    public void hashTypesCannotBeCombinedIn() throws Exception {
        assertThat(RubyGrammar.HASH.tryEat("{ a: 'gf', :tr => 'hg' }", 0, new MutableHtmlNode()), is(-1));
    }
}
