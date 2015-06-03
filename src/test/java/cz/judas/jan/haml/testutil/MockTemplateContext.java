package cz.judas.jan.haml.testutil;

import cz.judas.jan.haml.runtime.RubyBlock;
import cz.judas.jan.haml.template.TemplateContext;

import java.util.Collections;

@SuppressWarnings("UtilityClass")
public class MockTemplateContext {
    public static final TemplateContext EMPTY = new TemplateContext(Collections.emptyMap(), RubyBlock.EMPTY);
}
