package cz.judas.jan.haml;

import cz.judas.jan.haml.parsing.HamlTreeBuilder;
import cz.judas.jan.haml.template.HamlTemplate;
import cz.judas.jan.haml.tree.RootNode;

import java.io.*;
import java.nio.charset.Charset;

public class HamlTemplateBuilder {
    private final HamlTreeBuilder treeBuilder = new HamlTreeBuilder();

    public HamlTemplate buildFrom(Reader input) {
        try {
            RootNode rootNode = treeBuilder.buildTreeFrom(input);
            return new HamlTemplate(rootNode);
        } catch (IOException e) {
            throw new RuntimeException("Could not read input", e);
        }
    }

    public HamlTemplate buildFrom(String input) {
        return buildFrom(new StringReader(input));
    }

    public HamlTemplate buildFrom(InputStream input, Charset encoding) {
        return buildFrom(new InputStreamReader(input, encoding));
    }
}
