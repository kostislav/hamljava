package cz.judas.jan.haml.parser;

public class SetOfCharacters {
    private final long containedAsciiLower;
    private final long containedAsciiUpper;

    private SetOfCharacters(long containedAsciiLower, long containedAsciiUpper) {
        this.containedAsciiLower = containedAsciiLower;
        this.containedAsciiUpper = containedAsciiUpper;
    }

    public boolean contains(char c) {
        if(c >= 0 && c < 128) {
            if(c < 64) {
                return (containedAsciiLower & (1 << c)) != 0;
            } else {
                return (containedAsciiUpper & (1 << (c - 64))) != 0;
            }
        } else {
            return false;
        }
    }

    public SetOfCharacters mergedWith(SetOfCharacters other) {
        return new SetOfCharacters(
                containedAsciiLower | other.containedAsciiLower,
                containedAsciiUpper | other.containedAsciiUpper
        );
    }

    public static SetOfCharacters range(char lower, char upper) {
        long lowerMask = 0;
        long upperMask = 0;
        for(int i = lower; i <= upper; i++) {
            lowerMask = addToLower(lowerMask, i);
            upperMask = addToUpper(upperMask, i);
        }
        return new SetOfCharacters(lowerMask, upperMask);
    }

    public static SetOfCharacters single(char c) {
        return new SetOfCharacters(
                addToLower(0, c),
                addToUpper(0, c)
        );
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
}
