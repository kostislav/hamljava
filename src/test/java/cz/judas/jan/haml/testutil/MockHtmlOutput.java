package cz.judas.jan.haml.testutil;

import cz.judas.jan.haml.output.HtmlOutput;

@SuppressWarnings("UtilityClass")
public class MockHtmlOutput {
    public static HtmlOutput create() {
        return new HtmlOutput(true);
    }
}
