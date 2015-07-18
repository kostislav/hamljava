package cz.judas.jan.hamljava.runtime.reflect;

import cz.judas.jan.hamljava.runtime.RubyBlock;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.template.TemplateContext;

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
