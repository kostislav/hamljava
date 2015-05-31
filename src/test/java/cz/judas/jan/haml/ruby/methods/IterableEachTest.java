package cz.judas.jan.haml.ruby.methods;

import cz.judas.jan.haml.ruby.*;
import cz.judas.jan.haml.template.HtmlOutput;
import cz.judas.jan.haml.template.TemplateContext;
import cz.judas.jan.haml.testutil.MockTemplateContext;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cz.judas.jan.haml.testutil.ShortCollections.list;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IterableEachTest {

    private IterableEach iterableEach;

    @Before
    public void setUp() throws Exception {
        iterableEach = new IterableEach();
    }

    @Test
    public void callsBlockForEveryItem() throws Exception {
        CapturingBlock block = new CapturingBlock();

        iterableEach.invoke(list("abc", "def"), Collections.emptyList(), block, new HtmlOutput(), MockTemplateContext.EMPTY);

        block.assertArguments(is(list(
                list((RubyObject)new RubyObjectBase("abc")),
                list(new RubyObjectBase("def"))
        )));
    }

    @Test
    @Incompatibility
    public void returnsNil() throws Exception {
        RubyObject result = iterableEach.invoke(list("abc", "def"), Collections.emptyList(), new CapturingBlock(), new HtmlOutput(), MockTemplateContext.EMPTY);

        assertThat(result, is((RubyObject)Nil.INSTANCE));
    }

    private static class CapturingBlock implements RubyBlock {
        private final List<List<? extends RubyObject>> arguments = new ArrayList<>();

        @Override
        public RubyObject invoke(List<RubyObject> arguments, RubyBlock block, HtmlOutput htmlOutput, TemplateContext templateContext) {
            this.arguments.add(arguments);
            return Nil.INSTANCE;
        }

        public void assertArguments(Matcher<? super List<List<? extends RubyObject>>> matcher) {
            assertThat(arguments, matcher);
        }
    }
}
