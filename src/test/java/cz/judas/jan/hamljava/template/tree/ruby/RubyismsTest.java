package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.runtime.Nil;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class RubyismsTest {
    @Test
    public void falseyValuesAreFalsey() throws Exception {
        assertThat(Rubyisms.isFalsey(null), is(true));
        assertThat(Rubyisms.isFalsey(RubyConstants.NIL), is(true));
        assertThat(Rubyisms.isFalsey(new Nil()), is(true));
        assertThat(Rubyisms.isFalsey(false), is(true));
    }

    @Test
    public void otherValuesAreNotFalsey() throws Exception {
        assertThat(Rubyisms.isFalsey(true), is(false));
        assertThat(Rubyisms.isFalsey(""), is(false));
        assertThat(Rubyisms.isFalsey(0), is(false));
        assertThat(Rubyisms.isFalsey(new Object()), is(false));
    }
}