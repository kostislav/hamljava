package cz.judas.jan.haml;

public class AnyNumberOfToken<T> implements Token<T> {
    private final Token<T> inner;

    public AnyNumberOfToken(Token<T> inner) {
        this.inner = inner;
    }

    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        int globalNewPosition = -1;
        int currentPosition = position;
        while(currentPosition < line.length()) {
            int newPosition = inner.tryEat(line, currentPosition, parsingResult);
            if(newPosition == -1) {
                return globalNewPosition;
            } else {
                globalNewPosition = newPosition;
                currentPosition = newPosition;
            }
        }
        return globalNewPosition;
    }

    public static <T> AnyNumberOfToken<T> anyNumberOf(Token<T> inner) {
        return new AnyNumberOfToken<>(inner);
    }
}
