package cz.judas.jan.hamljava.template;

import cz.judas.jan.hamljava.runtime.Nil;
import cz.judas.jan.hamljava.runtime.RubyBlock;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TemplateContextTest {
    @Test
    public void returnsExistingField() throws Exception {
        TemplateContext templateContext = new TemplateContext(
                map("value1", "abcde"),
                RubyBlock.EMPTY
        );

        assertThat(templateContext.getField("value1"), is((Object)"abcde"));
    }

    @Test
    public void returnsNilForNonExistentField() throws Exception {
        TemplateContext templateContext = new TemplateContext(Collections.emptyMap(), RubyBlock.EMPTY);

        assertThat(templateContext.getField("value1"), is((Object)Nil.INSTANCE));
    }

    @Test
    public void returnsNilForNullField() throws Exception {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("value2", null);
        TemplateContext templateContext = new TemplateContext(fields, RubyBlock.EMPTY);

        assertThat(templateContext.getField("value2"), is((Object)Nil.INSTANCE));
    }
}
