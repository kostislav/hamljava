package cz.judas.jan.haml.parser;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SetOfCharactersTest {
    @Test
    public void rangeContainsAllFromRange() throws Exception {
        SetOfCharacters range = SetOfCharacters.range('A', 'C');

        assertThat(range.contains('A'), is(true));
        assertThat(range.contains('B'), is(true));
        assertThat(range.contains('C'), is(true));
    }

    @Test
    public void rangeDoesNotContainOtherChars() throws Exception {
        SetOfCharacters range = SetOfCharacters.range('b', 'd');

        assertThat(range.contains('a'), is(false));
        assertThat(range.contains('e'), is(false));
    }

    @Test
    public void singleCharContainsThatChar() throws Exception {
        SetOfCharacters single = SetOfCharacters.single('x');

        assertThat(single.contains('x'), is(true));
    }

    @Test
    public void singleCharDoesNotContainOtherChars() throws Exception {
        SetOfCharacters single = SetOfCharacters.single('x');

        assertThat(single.contains('y'), is(false));
    }

    @Test
    public void mergedSetContainsBoth() throws Exception {
        SetOfCharacters set1 = SetOfCharacters.range('a', 'c');
        SetOfCharacters set2 = SetOfCharacters.range('6', '8');
        SetOfCharacters merged = set1.union(set2);

        assertThat(merged.contains('a'), is(true));
        assertThat(merged.contains('b'), is(true));
        assertThat(merged.contains('c'), is(true));
        assertThat(merged.contains('6'), is(true));
        assertThat(merged.contains('7'), is(true));
        assertThat(merged.contains('8'), is(true));
    }

    @Test
    public void mergedSetDoesNotContainOtherRandomChars() throws Exception {
        SetOfCharacters set1 = SetOfCharacters.range('a', 'c');
        SetOfCharacters set2 = SetOfCharacters.range('6', '8');
        SetOfCharacters merged = set1.union(set2);

        assertThat(merged.contains(' '), is(false));
    }

    @Test
    public void doesNotContainEsotericCharacters() throws Exception {
        SetOfCharacters single = SetOfCharacters.single('x');

        assertThat(single.contains('\u1234'), is(false));
    }
}
