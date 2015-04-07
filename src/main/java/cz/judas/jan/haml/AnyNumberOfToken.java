package cz.judas.jan.haml;

public class AnyNumberOfToken implements Token {
    private final Token inner;

    public AnyNumberOfToken(Token inner) {
        this.inner = inner;
    }

    @Override
    public int tryEat(String line, int position, ParsingResult parsingResult) throws ParseException {
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
}
