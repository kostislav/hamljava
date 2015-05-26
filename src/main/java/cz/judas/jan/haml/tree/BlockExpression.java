package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.HtmlOutput;
import cz.judas.jan.haml.TemplateContext;
import cz.judas.jan.haml.ruby.Nil;
import cz.judas.jan.haml.ruby.RubyBlock;
import cz.judas.jan.haml.ruby.RubyObject;

import java.util.List;

public class BlockExpression implements RubyBlock {
    private final List<HamlNode> children;

    public BlockExpression(List<HamlNode> children) {
        this.children = ImmutableList.copyOf(children);
    }

    @Override
    public RubyObject invoke(List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
        for (HamlNode child : children) {
            child.evaluate(htmlOutput, templateContext);
        }
        return Nil.INSTANCE;
    }
}