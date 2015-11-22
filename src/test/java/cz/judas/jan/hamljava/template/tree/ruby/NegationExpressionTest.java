package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import cz.judas.jan.hamljava.testutil.MockTemplateContext;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class NegationExpressionTest {
    @Test
    public void negatesOriginalValue() throws Exception {
        assertThat(negationOf(true), is(false));
        assertThat(negationOf(false), is(true));
        assertThat(negationOf(RubyConstants.NIL), is(true));
        assertThat(negationOf("abcd"), is(false));
    }

    private static Object negationOf(Object originalValue) {
        NegationExpression negationExpression = new NegationExpression(new ConstantRubyExpression(originalValue));

        return negationExpression.evaluate(MockHtmlOutput.create(), MockTemplateContext.EMPTY);
    }
}