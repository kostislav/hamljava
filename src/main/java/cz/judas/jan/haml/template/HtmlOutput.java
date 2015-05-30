package cz.judas.jan.haml.template;

public class HtmlOutput {
    private final StringBuilder stringBuilder = new StringBuilder();

    public HtmlOutput addUnescaped(Object... values) {
        for (Object value : values) {
            stringBuilder.append(value);
        }
        return this;
    }

    public String build() {
        return stringBuilder.toString();
    }
}
