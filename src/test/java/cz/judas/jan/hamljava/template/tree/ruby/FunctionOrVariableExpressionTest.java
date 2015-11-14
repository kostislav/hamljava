package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.runtime.methods.ConstantFunction;
import cz.judas.jan.hamljava.template.TemplateContext;
import cz.judas.jan.hamljava.testutil.MockHtmlOutput;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FunctionOrVariableExpressionTest {
    @Test
    public void returnsVariableIfOnlyVariableExists() throws Exception {
        FunctionOrVariableExpression expression = new FunctionOrVariableExpression("value", AdditionalFunctions.EMPTY);

        Object value = evaluate(expression, map(
                "value", 45
        ));

        assertThat(value, is(45));
    }

    @Test
    public void returnsVariableIfBothVariableAndFunctionExists() throws Exception {
        FunctionOrVariableExpression expression = new FunctionOrVariableExpression("value", new AdditionalFunctions(map(
                "value", new ConstantFunction("def")
        )));

        Object value = evaluate(expression, map(
                "value", 45
        ));

        assertThat(value, is(45));
    }

    @Test
    public void callsFunctionIfOnlyFunctionExists() throws Exception {
        FunctionOrVariableExpression expression = new FunctionOrVariableExpression("value", new AdditionalFunctions(map(
                "value", new ConstantFunction("def")
        )));

        Object value = evaluate(expression, Collections.emptyMap());

        assertThat(value, is("def"));
    }

    private Object evaluate(FunctionOrVariableExpression expression, Map<String, Integer> locals) {
        return expression.evaluate(
                MockHtmlOutput.create(),
                new TemplateContext(
                        Collections.emptyMap(),
                        UnboundRubyMethod.EMPTY_BLOCK
                ).withLocalVariables(locals)
        );
    }
}