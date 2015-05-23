package cz.judas.jan.haml.parser;

public class SetOfCharacters implements CharPredicate {
    public static final SetOfCharacters WHITESPACE = whitespace();

    private final long containedAsciiLower;
    private final long containedAsciiUpper;
    private final boolean defaultMatch;

    private SetOfCharacters(long containedAsciiLower, long containedAsciiUpper, boolean defaultMatch) {
        this.containedAsciiLower = containedAsciiLower;
        this.containedAsciiUpper = containedAsciiUpper;
        this.defaultMatch = defaultMatch;
    }

    public boolean contains(char c) {
        if(c >= 0 && c < 128) {
            if(c < 64) {
                return (containedAsciiLower & (1 << c)) != 0;
            } else {
                return (containedAsciiUpper & (1 << (c - 64))) != 0;
            }
        } else {
            return defaultMatch;
        }
    }

    public SetOfCharacters union(SetOfCharacters other) {
        return new SetOfCharacters(
                containedAsciiLower | other.containedAsciiLower,
                containedAsciiUpper | other.containedAsciiUpper,
                defaultMatch || other.defaultMatch
        );
    }

    public SetOfCharacters intersection(SetOfCharacters other) {
        return new SetOfCharacters(
                containedAsciiLower & other.containedAsciiLower,
                containedAsciiUpper & other.containedAsciiUpper,
                defaultMatch && other.defaultMatch
        );
    }

    @Override
    public boolean test(char c) {
        return contains(c);
    }

    public SetOfCharacters negate() {
        return new SetOfCharacters(
                ~containedAsciiLower,
                ~containedAsciiUpper,
                !defaultMatch
        );
    }

    public static SetOfCharacters range(char lower, char upper) {
        long lowerMask = 0;
        long upperMask = 0;
        for(int i = lower; i <= upper; i++) {
            lowerMask = addToLower(lowerMask, i);
            upperMask = addToUpper(upperMask, i);
        }
        return new SetOfCharacters(lowerMask, upperMask, false);
    }

    public static SetOfCharacters single(char c) {
        return new SetOfCharacters(
                addToLower(0, c),
                addToUpper(0, c),
                false
        );
    }

    public static SetOfCharacters explicit(char... chars) {
        long lowerMask = 0;
        long upperMask = 0;
        for (char c : chars) {
            lowerMask = addToLower(lowerMask, c);
            upperMask = addToUpper(upperMask, c);
        }
        return new SetOfCharacters(lowerMask, upperMask, false);
    }

    private static long addToLower(long lower, int index) {
        if(index >= 0 && index < 64) {
            return lower | (1 << index);
        } else {
            return lower;
        }
    }

    private static long addToUpper(long upper, int index) {
        if(index >= 64 && index < 128) {
            return upper | (1 << (index - 64));
        } else {
            return upper;
        }
    }

    private static SetOfCharacters whitespace() {
        long lowerMask = 0;
        long upperMask = 0;
        for (int i = 0; i < 128; i++) {
            if(Character.isWhitespace(i)) {
                lowerMask = addToLower(lowerMask, i);
                upperMask = addToUpper(upperMask, i);
            }
        }
        return new SetOfCharacters(lowerMask, upperMask, false);
    }
}
