package cz.judas.jan.hamljava;

import com.google.common.collect.ImmutableMap;
import cz.judas.jan.hamljava.parsing.HamlTreeBuilder;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.template.CompiledHamlTemplate;
import cz.judas.jan.hamljava.template.LinkedHamlTemplate;
import cz.judas.jan.hamljava.template.tree.RootNode;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

public class HamlTemplateCompiler {
    private final HamlTreeBuilder treeBuilder = new HamlTreeBuilder();
    private final Map<String, ? extends UnboundRubyMethod> functions;

    public HamlTemplateCompiler(Map<String, ? extends UnboundRubyMethod> functions) {
        this.functions = ImmutableMap.copyOf(functions);
    }

    public LinkedHamlTemplate compile(Reader input) throws IOException {
        RootNode rootNode = treeBuilder.buildTreeFrom(input);
        return new CompiledHamlTemplate(rootNode).link(functions);
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
