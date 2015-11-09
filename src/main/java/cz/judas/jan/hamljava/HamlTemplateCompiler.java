package cz.judas.jan.hamljava;

import cz.judas.jan.hamljava.parsing.FunctionalNodeBuilder;
import cz.judas.jan.hamljava.parsing.HamlTreeBuilder;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.CompiledHamlTemplate;
import cz.judas.jan.hamljava.template.LinkedHamlTemplate;
import cz.judas.jan.hamljava.template.tree.RootNode;

import java.io.*;
import java.nio.charset.Charset;

public class HamlTemplateCompiler {
    private final HamlTreeBuilder treeBuilder;

    public HamlTemplateCompiler(AdditionalFunctions functions) {
        treeBuilder = new HamlTreeBuilder(new FunctionalNodeBuilder(functions));
    }

    public LinkedHamlTemplate compile(Reader input) throws IOException {
        RootNode rootNode = treeBuilder.buildTreeFrom(input);
        return new CompiledHamlTemplate(rootNode).link();
    }

    public LinkedHamlTemplate compile(String input) {
        try {
            return compile(new StringReader(input));
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException when reading from string", e);
        }
    }

    public LinkedHamlTemplate compile(InputStream input, Charset encoding) throws IOException {
        return compile(new InputStreamReader(input, encoding));
    }
}
