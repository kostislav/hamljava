package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.tree.mutable.MutableHtmlNode;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RubyGrammarTest {
    @Test
    public void hashTypesCannotBeCombined() throws Exception {
        assertThat(RubyGrammar.hash().tryEat(new InputString("{ a: 'gf', :tr => 'hg' }"), new MutableHtmlNode()), is(Optional.empty()));
    }
}
