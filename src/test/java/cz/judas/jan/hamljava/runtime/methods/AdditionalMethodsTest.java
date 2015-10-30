package cz.judas.jan.hamljava.runtime.methods;

import org.junit.Before;
import org.junit.Test;

import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AdditionalMethodsTest {
    private AdditionalMethod<CharSequence> addXMethod;
    private AdditionalMethods additionalMethods;

    @Before
    public void setUp() throws Exception {
        addXMethod = (target, arguments, block, htmlOutput, templateContext) -> target + "x";
        additionalMethods = new AdditionalMethods(map(
                CharSequence.class, map("add_x", addXMethod)
        ));
    }

    @Test
    public void returnsMethodIfFoundForClass() throws Exception {
        AdditionalMethod<CharSequence> method = additionalMethods.find(CharSequence.class, "add_x");

        assertThat(method, is(addXMethod));
    }

    @Test(expected = RuntimeException.class)
    public void failsIfMethodNotFoundForClass() throws Exception {
        additionalMethods.find(Integer.class, "add_x");
    }

    @Test(expected = RuntimeException.class)
    public void failsIfMethodWithNameNotFoundForClass() throws Exception {
        additionalMethods.find(String.class, "add_y");
    }

    @Test
    public void returnsMethodIfFoundForSuperclass() throws Exception {
        AdditionalMethod<String> method = additionalMethods.find(String.class, "add_x");

        assertThat(method, is(addXMethod));
    }
}