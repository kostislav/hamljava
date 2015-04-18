package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RubyGrammarTest {
    @Test
    public void hashTypesCannotBeCombined() throws Exception {
        assertThat(RubyGrammar.HASH.tryEat(new InputString("{ a: 'gf', :tr => 'hg' }"), new MutableHtmlNode()), is(false));
    }
}
