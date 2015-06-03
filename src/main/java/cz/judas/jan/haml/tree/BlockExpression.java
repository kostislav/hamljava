package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.runtime.RubyConstants;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.Iterator;
import java.util.List;

public class BlockExpression implements RubyBlock {
    private final List<HamlNode> children;
    private final List<String> argumentNames;

    public BlockExpression(List<HamlNode> children, Iterable<String> argumentNames) {
        this.children = ImmutableList.copyOf(children);
        this.argumentNames = ImmutableList.copyOf(argumentNames);
    }

    @Override
    public Object invoke(List<?> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        ImmutableMap<String, Object> localVariables = assignNamesTo(arguments);
        for (HamlNode child : children) {
            child.evaluate(htmlOutput, templateContext.withLocalVariables(localVariables));
        }
        return RubyConstants.NIL;
    }

    private ImmutableMap<String, Object> assignNamesTo(List<?> arguments) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

        Iterator<String> nameIterator = argumentNames.iterator();
        Iterator<?> values = arguments.iterator();
        while(nameIterator.hasNext()) {
            builder.put(nameIterator.next(), values.next());
        }

        return builder.build();
    }
}
