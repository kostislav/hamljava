package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyObject;
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
    public RubyObject invoke(List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        ImmutableMap<String, RubyObject> localVariables = assignNamesTo(arguments);
        for (HamlNode child : children) {
            child.evaluate(htmlOutput, templateContext.withLocalVariables(localVariables));
        }
        return RubyObject.NIL;
    }

    private ImmutableMap<String, RubyObject> assignNamesTo(List<RubyObject> arguments) {
        ImmutableMap.Builder<String, RubyObject> builder = ImmutableMap.builder();

        Iterator<String> nameIterator = argumentNames.iterator();
        Iterator<RubyObject> values = arguments.iterator();
        while(nameIterator.hasNext()) {
            builder.put(nameIterator.next(), values.next());
        }

        return builder.build();
    }
}
