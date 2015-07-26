package cz.judas.jan.hamljava;

import cz.judas.jan.hamljava.parsing.HamlTreeBuilder;
import cz.judas.jan.hamljava.template.HamlTemplate;
import cz.judas.jan.hamljava.template.tree.RootNode;

import java.io.*;
import java.nio.charset.Charset;

public class HamlTemplateBuilder {
    private final HamlTreeBuilder treeBuilder = new HamlTreeBuilder();

    public HamlTemplate buildFrom(Reader input) throws IOException {
        RootNode rootNode = treeBuilder.buildTreeFrom(input);
        return new HamlTemplate(rootNode);
    }

    public HamlTemplate buildFrom(String input) {
        try {
            return buildFrom(new StringReader(input));
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException when reading from string", e);
        }
    }

    public HamlTemplate buildFrom(InputStream input, Charset encoding) throws IOException {
        return buildFrom(new InputStreamReader(input, encoding));
    }
}
