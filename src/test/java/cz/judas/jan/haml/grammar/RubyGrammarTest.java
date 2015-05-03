package cz.judas.jan.haml.grammar;

import cz.judas.jan.haml.parser.InputString;
import cz.judas.jan.haml.tree.ruby.RubyHash;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RubyGrammarTest {
    @Test
    public void hashTypesCannotBeCombined() throws Exception {
        RubyGrammar rubyGrammar = new RubyGrammar();

        assertThat(rubyGrammar.hash().tryEat(new InputString("{ a: 'gf', :tr => 'hg' }")), is(Optional.<RubyHash>empty()));
    }
}
