package cz.judas.jan.hamljava;

import cz.judas.jan.hamljava.parsing.FunctionalNodeBuilder;
import cz.judas.jan.hamljava.parsing.HamlTreeBuilder;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.HamlTemplate;
import cz.judas.jan.hamljava.template.tree.RootNode;

import java.io.*;
import java.nio.charset.Charset;

public class HamlTemplateCompiler {
    private final HamlTreeBuilder treeBuilder;

    public HamlTemplateCompiler(AdditionalFunctions functions) {
        treeBuilder = new HamlTreeBuilder(new FunctionalNodeBuilder(functions));
    }

    public HamlTemplate compile(Reader input) throws IOException {
        RootNode rootNode = treeBuilder.buildTreeFrom(input);
        return new HamlTemplate(rootNode);
    }

    public HamlTemplate compile(String input) {
        try {
            return compile(new StringReader(input));
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException when reading from string", e);
        }
    }

    public HamlTemplate compile(InputStream input, Charset encoding) throws IOException {
        return compile(new InputStreamReader(input, encoding));
    }
}
