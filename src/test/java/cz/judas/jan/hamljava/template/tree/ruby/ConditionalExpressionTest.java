package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.output.StreamHtmlOutput;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.TemplateContext;
import cz.judas.jan.hamljava.testutil.Expressions;
import cz.judas.jan.hamljava.testutil.Nodes;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static cz.judas.jan.hamljava.testutil.ShortCollections.list;
import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ConditionalExpressionTest {
    private ConditionalExpression conditionalExpression;

    @Before
    public void setUp() throws Exception {
        conditionalExpression = new ConditionalExpression(
                Expressions.fieldReference("blah"),
                list(
                        Nodes.textNode(Expressions.string("abc"))
                )
        );
    }

    @Test
    public void evaluatesBlockIfConditionIsTrue() throws Exception {
        assertThat(evaluatedWith(true), is("abc"));
    }

    @Test
    public void doesNotEvaluateAnythingIfConditionIsFalse() throws Exception {
        assertThat(evaluatedWith(false), is(""));
    }

    @Test
    public void evaluatesBlockIfConditionIsARandomObject() throws Exception {
        assertThat(evaluatedWith(new Object()), is("abc"));
    }

    @Test
    public void doesNotEvaluateAnythingIfConditionIsNil() throws Exception {
        assertThat(evaluatedWith(RubyConstants.NIL), is(""));
    }

    private String evaluatedWith(Object fieldValue) {
        StringWriter writer = new StringWriter();

        conditionalExpression.evaluate(
                new StreamHtmlOutput(writer, true),
                new TemplateContext(
                        map("blah", fieldValue),
                        UnboundRubyMethod.EMPTY_BLOCK
                )
        );

        return writer.toString();
    }
}