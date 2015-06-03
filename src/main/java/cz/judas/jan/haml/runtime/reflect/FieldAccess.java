package cz.judas.jan.haml.runtime.reflect;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.lang.reflect.Field;

public class FieldAccess implements PropertyAccess {
    private final Field field;

    public FieldAccess(Field field) {
        this.field = field;
    }

    @Override
    public Object get(Object target, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not get field " + field.getName() + " of " + target);
        }
    }
}
