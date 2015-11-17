package cz.judas.jan.hamljava.template.tree.ruby;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMultimap;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.reflect.PropertyAccess;
import cz.judas.jan.hamljava.runtime.reflect.PropertyAccessCreator;
import cz.judas.jan.hamljava.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class PropertyAccessExpression implements RubyExpression {
    private static final PropertyAccessCreator PROPERTY_ACCESS_CREATOR = new PropertyAccessCreator(ImmutableMultimap.of());

    private final RubyExpression target;
    private final String name;
    private final LoadingCache<Class<?>, PropertyAccess> cache;

    public PropertyAccessExpression(RubyExpression target, String name) {
        this.target = target;
        this.name = name;
        cache = CacheBuilder.newBuilder()
                .build(CacheLoader.from(key -> PROPERTY_ACCESS_CREATOR.createFor(key, name)));
    }

    @Override
    public Object evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        Object targetObject = target.evaluate(htmlOutput, templateContext);
        return cache.getUnchecked(targetObject.getClass()).get(targetObject, UnboundRubyMethod.EMPTY_BLOCK, htmlOutput, templateContext);
    }
}
