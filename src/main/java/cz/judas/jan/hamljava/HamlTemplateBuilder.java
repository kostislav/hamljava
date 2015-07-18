package cz.judas.jan.hamljava;

import cz.judas.jan.hamljava.parsing.HamlTreeBuilder;
import cz.judas.jan.hamljava.template.HamlTemplate;
import cz.judas.jan.hamljava.template.tree.RootNode;

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
