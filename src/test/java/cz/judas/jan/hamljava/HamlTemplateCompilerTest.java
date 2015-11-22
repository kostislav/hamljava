package cz.judas.jan.hamljava;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.runtime.RubyConstants;
import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;
import cz.judas.jan.hamljava.runtime.methods.AdditionalFunctions;
import cz.judas.jan.hamljava.template.HamlTemplate;
import cz.judas.jan.hamljava.template.TemplateContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cz.judas.jan.hamljava.testutil.ShortCollections.list;
import static cz.judas.jan.hamljava.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HamlTemplateCompilerTest {
    private HamlTemplateCompiler templateCompiler;

    @Before
    public void setUp() throws Exception {
        templateCompiler = new HamlTemplateCompiler(AdditionalFunctions.EMPTY);
    }

    @Test
    public void processesHaml() throws Exception {
        assertParses(
                "!!! 5\n%html\n\t%head\n\t\t%title blah",
                "<!DOCTYPE html>\n<html><head><title>blah</title></head></html>"
        );
    }

    @Test
    public void textLinesAreNotEscaped() throws Exception {
        HamlTemplate template = templateCompiler.compile("%p\n\t<div id=\"blah\">Blah!</div>");

        assertThat(
                template.evaluate(false, Collections.emptyMap()),
                is("<p><div id=\"blah\">Blah!</div></p>")
        );
    }

    @Test
    public void usesVariables() throws Exception {
        assertParses(
                "%title\n\t= @title\n%p= @content",
                map("title", "MyPage", "content", "blah"),
                "<title>MyPage</title><p>blah</p>"
        );
    }

    @Test
    public void combinedIdClassAndOtherAttributes() throws Exception {
        assertParses(
                "%span#huhl.blah.bleh{ color: 'red' } text",
                "<span id=\"huhl\" class=\"blah bleh\" color=\"red\">text</span>"
        );
    }

    @Test
    public void findsBothBareAndGetterProperties() throws Exception {
        assertParses(
                "%span.name= @person.name\n%span= @person.age",
                map("person", new Person("karl", 654)),
                "<span class=\"name\">karl</span><span>655</span>"
        );
    }

    @Test
    public void chainedMethodCalls() throws Exception {
        assertParses(
                "%span.name= @people.iterator.next",
                map("people", ImmutableList.of("varl")),
                "<span class=\"name\">varl</span>"
        );
    }

    @Test
    public void methodCallWithParameter() throws Exception {
        assertParses(
                "%span= @person.getFakeAge(54)",
                map("person", new Person("karl", 654)),
                "<span>54</span>"
        );
    }

    @Test
    public void methodCallWithMultipleParameters() throws Exception {
        assertParses(
                "%span= @person.getFakeName('bob', \"dylan\")",
                map("person", new Person("karl", 654)),
                "<span>bob dylan</span>"
        );
    }

    @Test
    public void methodCallWithoutBrackets() throws Exception {
        assertParses(
                "%span= @person.getFakeName 'mike', \"oldfield\"",
                map("person", new Person("karl", 654)),
                "<span>mike oldfield</span>"
        );
    }

    @Test
    public void simpleForeach() throws Exception {
        assertParses(
                "%div\n\t- @values.each do\n\t\t%span blah",
                map("values", list("a", "b")),
                "<div><span>blah</span><span>blah</span></div>"
        );
    }

    @Test
    public void foreachWithParameter() throws Exception {
        assertParses(
                "%div\n\t- @values.each do |value|\n\t\t%span= value",
                map("values", list("a", "b"), "blbost", "e"),
                "<div><span>a</span><span>b</span></div>"
        );
    }

    @Test
    public void stringInterpolation() throws Exception {
        assertParses(
                "%title\n\tblah #{@abc} gheg #{@ghi}ef",
                map("abc", "def", "ghi", "jkl"),
                "<title>blah def gheg jklef</title>"
        );
    }

    @Test
    public void doesNotReadFieldIfMethodHasParameters() throws Exception {
        assertParses(
                "%span= @person.name('whatever')",
                map("person", new Person("karl", 654)),
                "<span>I am whatever</span>"
        );
    }

    @Test
    public void callsInnerTemplate() throws Exception {
        HamlTemplate layoutTemplate = compileAndLink("%html\n\t%body\n\t\t- yield");
        HamlTemplate innerTemplate = compileAndLink("%div blah bleh");

        String html = layoutTemplate.evaluate(Collections.emptyMap(), innerTemplate);

        assertThat(html, is("<html><body><div>blah bleh</div></body></html>"));
    }

    @Test
    public void handlesNonAsciiChars() throws Exception {
        assertParses(
                "%div řečiště",
                "<div>řečiště</div>"
        );
    }

    @Test
    public void canCallMethodsOnLocalVariables() throws Exception {
        assertParses(
                "%ul\n\t- @people.each do |person|\n\t\t%li #{person.name} is #{person.age}",
                map("people", list(new Person("abc", 36), new Person("def", 64))),
                "<ul><li>abc is 37</li><li>def is 65</li></ul>"
        );
    }

    @Test
    public void findsInterfaceMethods() throws Exception {
        assertParses(
                "%div= @people.size",
                map("people", list(new Person("abc", 36), new Person("def", 64))),
                "<div>2</div>"
        );
    }

    @Test
    public void explicitUnescaping() throws Exception {
        assertParses(
                "%div!= '<p>a</p>'",
                "<div><p>a</p></div>"
        );
    }

    @Test
    public void booleanHtmlAttributes() throws Exception {
        assertParses(
                "%input(name='what' autofocus)",
                "<input name=\"what\" autofocus=\"autofocus\" />"
        );
    }

    @Test
    public void interpolationInStrings() throws Exception {
        assertParses(
                "%span(class=\"widget_#{@person.name}\")",
                map(
                        "person", new Person("franco", 43)
                ),
                "<span class=\"widget_franco\"></span>"
        );
    }

    @Test
    public void customGlobalFunction() throws Exception {
        templateCompiler = new HamlTemplateCompiler(
                new AdditionalFunctions(map(
                        "func", new DoubleYieldFunction()
                ))
        );

        HamlTemplate template = templateCompiler.compile("%ul\n\t- func do |v|\n\t\t%li= v");

        assertThat(
                template.evaluate(
                        Collections.emptyMap()
                ),
                is("<ul><li>aa</li><li>bb</li></ul>")
        );
    }

    @Test
    public void ifCondition() throws Exception {
        assertParses(
                "#content\n\t- if @value\n\t\t%p Value is set.\n\tSome text",
                map(
                        "value", true
                ),
                "<div id=\"content\"><p>Value is set.</p>Some text</div>"
        );
    }

    private void assertParses(String input, String expected) {
        assertParses(input, Collections.emptyMap(), expected);
    }

    private void assertParses(String input, Map<String, ?> fieldValues, String expected) {
        assertThat(
                templateCompiler.compile(input).evaluate(fieldValues),
                is(expected)
        );
    }

    private HamlTemplate compileAndLink(String template) {
        return templateCompiler.compile(template);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class Person {
        public final String name;
        private final int age;

        private Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age + 1;
        }

        public int getFakeAge(int fakeValue) {
            return fakeValue;
        }

        public String getFakeName(String fakeFirstName, String fakeLastName) {
            return fakeFirstName + " " + fakeLastName;
        }

        public String name(String fakeName) {
            return "I am " + fakeName;
        }
    }

    private static class DoubleYieldFunction implements UnboundRubyMethod {
        @Override
        public Object invoke(List<?> arguments, UnboundRubyMethod block, HtmlOutput htmlOutput, TemplateContext templateContext) {
            block.invoke(list("aa"), EMPTY_BLOCK, htmlOutput, templateContext);
            block.invoke(list("bb"), EMPTY_BLOCK, htmlOutput, templateContext);
            return RubyConstants.NIL;
        }
    }
}
