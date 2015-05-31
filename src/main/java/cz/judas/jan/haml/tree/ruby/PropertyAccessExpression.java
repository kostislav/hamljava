package cz.judas.jan.haml.tree.ruby;

import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.ruby.methods.IterableEach;
import cz.judas.jan.haml.ruby.reflect.PropertyAccessCreator;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.Collections;

public class PropertyAccessExpression implements PossibleMethodCall {
    private static final PropertyAccessCreator PROPERTY_ACCESS_CREATOR = new PropertyAccessCreator(ImmutableMultimap.of(
            Iterable.class, new IterableEach()
    ));

    private final RubyExpression target;
    private final String name;

    public PropertyAccessExpression(RubyExpression target, String name) {
        this.target = target;
        this.name = name;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        Object targetObject = target.evaluate(htmlOutput, templateContext).asJavaObject();
        return RubyObject.wrap(PROPERTY_ACCESS_CREATOR.createFor(name, targetObject.getClass()).get(targetObject, RubyBlock.EMPTY, htmlOutput, templateContext));
    }

    @Override
    public MethodCallExpression withBlock(RubyBlock block) {
        return new MethodCallExpression(target, name, Collections.emptyList(), block);
    }
}
