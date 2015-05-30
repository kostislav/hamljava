package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static cz.judas.jan.haml.testutil.ShortCollections.list;
import static cz.judas.jan.haml.testutil.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HamlTemplateBuilderTest {
    private HamlTemplateBuilder templateBuilder;

    @Before
    public void setUp() throws Exception {
        templateBuilder = new HamlTemplateBuilder();
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
        assertParses(
                "%p\n\t<div id=\"blah\">Blah!</div>",
                "<p><div id=\"blah\">Blah!</div></p>"
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

    private void assertParses(String input, String expected) {
        assertParses(input, Collections.emptyMap(), expected);
    }

    private void assertParses(String input, Map<String, ?> fieldValues, String expected) {
        assertThat(
                templateBuilder.buildFrom(input).evaluateFor(fieldValues),
                is(expected)
        );
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
}
