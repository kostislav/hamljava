package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static cz.judas.jan.haml.ShortCollections.list;
import static cz.judas.jan.haml.ShortCollections.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HamlParserTest {
    private static final VariableMap EMPTY_VARIABLE_MAP = new VariableMap(Collections.emptyMap());

    private HamlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new HamlParser();
    }

    @Test
    public void processesHaml() throws Exception {
        assertThat(
                parser.process("!!! 5\n%html\n\t%head\n\t\t%title blah", EMPTY_VARIABLE_MAP),
                is("<!DOCTYPE html>\n<html><head><title>blah</title></head></html>")
        );
    }

    @Test
    public void textLinesAreNotEscaped() throws Exception {
        assertThat(
                parser.process("%p\n\t<div id=\"blah\">Blah!</div>", EMPTY_VARIABLE_MAP),
                is("<p><div id=\"blah\">Blah!</div></p>")
        );
    }

    @Test
    public void usesVariables() throws Exception {
        assertThat(
                parser.process("%title\n\t= @title\n%p= @content", new VariableMap(map("title", "MyPage", "content", "blah"))),
                is("<title>MyPage</title><p>blah</p>")
        );
    }

    @Test
    public void combinedIdClassAndOtherAttributes() throws Exception {
        assertThat(
                parser.process("%span#huhl.blah.bleh{ color: 'red' } text", EMPTY_VARIABLE_MAP),
                is("<span id=\"huhl\" class=\"blah bleh\" color=\"red\">text</span>")
        );
    }

    @Test
    public void findsBothBareAndGetterProperties() throws Exception {
        assertThat(
                parser.process("%span.name= @person.name\n%span= @person.age", new VariableMap(map("person", new Person("karl", 654)))),
                is("<span class=\"name\">karl</span><span>655</span>")
        );
    }

    @Test
    public void chainedMethodCalls() throws Exception {
        assertThat(
                parser.process("%span.name= @people.iterator.next", new VariableMap(map("people", ImmutableList.of("varl")))),
                is("<span class=\"name\">varl</span>")
        );
    }

    @Test
    public void methodCallWithParameter() throws Exception {
        assertThat(
                parser.process("%span= @person.getFakeAge(54)", new VariableMap(map("person", new Person("karl", 654)))),
                is("<span>54</span>")
        );
    }

    @Test
    public void methodCallWithMultipleParameters() throws Exception {
        assertThat(
                parser.process("%span= @person.getFakeName('bob', \"dylan\")", new VariableMap(map("person", new Person("karl", 654)))),
                is("<span>bob dylan</span>")
        );
    }

    @Test
    public void methodCallWithoutBrackets() throws Exception {
        assertThat(
                parser.process("%span= @person.getFakeName 'mike', \"oldfield\"", new VariableMap(map("person", new Person("karl", 654)))),
                is("<span>mike oldfield</span>")
        );
    }

    @Test
    public void simpleForeach() throws Exception {
        assertThat(
                parser.process("%div\n\t- @values.each do\n\t\t%span blah", new VariableMap(map("values", list("a", "b")))),
                is("<div><span>blah</span><span>blah</span></div>")
        );
    }

    private static class Person {
        public final String name;
        private final int age;

        private Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @SuppressWarnings("UnusedDeclaration")
        public int getAge() {
            return age + 1;
        }

        @SuppressWarnings("UnusedDeclaration")
        public int getFakeAge(int fakeValue) {
            return fakeValue;
        }

        @SuppressWarnings("UnusedDeclaration")
        public String getFakeName(String fakeFirstName, String fakeLastName) {
            return fakeFirstName + " " + fakeLastName;
        }
    }
}
