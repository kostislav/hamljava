package cz.judas.jan.haml.testutil;

import cz.judas.jan.haml.output.HtmlOutput;
import cz.judas.jan.haml.output.StreamHtmlOutput;

@SuppressWarnings("UtilityClass")
public class MockHtmlOutput {
    public static HtmlOutput create() {
        return new StreamHtmlOutput(true);
    }
}
