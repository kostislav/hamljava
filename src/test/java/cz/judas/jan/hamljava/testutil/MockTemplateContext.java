package cz.judas.jan.hamljava.testutil;

import cz.judas.jan.hamljava.runtime.RubyBlock;
import cz.judas.jan.hamljava.template.TemplateContext;

import java.util.Collections;

@SuppressWarnings("UtilityClass")
public class MockTemplateContext {
    public static final TemplateContext EMPTY = new TemplateContext(Collections.emptyMap(), RubyBlock.EMPTY);
}
