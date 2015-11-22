package cz.judas.jan.hamljava.template.tree.ruby;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.Nil;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.template.TemplateContext;
import cz.judas.jan.hamljava.template.tree.HamlNode;

import java.util.List;

public class ConditionalExpression implements RubyExpression {
    private final RubyExpression condition;
    private final List<HamlNode> trueBlock;

    public ConditionalExpression(RubyExpression condition, Iterable<? extends HamlNode> trueBlock) {
        this.condition = condition;
        this.trueBlock = ImmutableList.copyOf(trueBlock);
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        if (!isFalsey(condition.evaluate(htmlOutput, templateContext))) {
            for (HamlNode child : trueBlock) {
                child.evaluate(htmlOutput, templateContext);
            }
        }
        return RubyConstants.NIL;
    }

    public static boolean isFalsey(Object value) {
        return value == null || value instanceof Nil || Boolean.FALSE.equals(value);
    }
}
