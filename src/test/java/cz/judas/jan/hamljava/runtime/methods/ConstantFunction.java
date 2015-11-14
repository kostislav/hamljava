package cz.judas.jan.hamljava.runtime.methods;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.util.List;

public class ConstantFunction implements UnboundRubyMethod {
    private final String value;

    public ConstantFunction(String value) {
        this.value = value;
    }

    @Override
    public Object invoke(List<?> arguments, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        return value;
    }
}
