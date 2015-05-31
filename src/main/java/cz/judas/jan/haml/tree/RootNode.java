package cz.judas.jan.haml.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.judas.jan.haml.ruby.RubyObject;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode
@ToString
public class RootNode implements HamlNode {
    private static final Map<String, String> DOCTYPES = ImmutableMap.of(
            "5", "<!DOCTYPE html>"
    );

    private final Optional<String> doctype;
    private final List<HamlNode> children;

    public RootNode(Optional<String> doctype, Iterable<? extends HamlNode> children) {
        this.children = ImmutableList.copyOf(children);
        this.doctype = doctype;
    }

    @Override
    public RubyObject evaluate(HtmlOutput htmlOutput, TemplateContext templateContext) {
        doctype.ifPresent(doctype -> htmlOutput.addUnescaped(DOCTYPES.get(doctype)).addUnescaped('\n'));

        for (HamlNode child : children) {
            child.evaluate(htmlOutput, templateContext);
        }

        return RubyObject.NIL;
    }

    public String toHtmlString(TemplateContext templateContext) {
        HtmlOutput htmlOutput = new HtmlOutput();
        evaluate(htmlOutput, templateContext);
        return htmlOutput.build();
    }
}
