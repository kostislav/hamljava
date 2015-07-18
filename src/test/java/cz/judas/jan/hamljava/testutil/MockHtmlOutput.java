package cz.judas.jan.hamljava.testutil;

import cz.judas.jan.hamljava.output.HtmlOutput;
import cz.judas.jan.hamljava.output.StreamHtmlOutput;

@SuppressWarnings("UtilityClass")
public class MockHtmlOutput {
    public static HtmlOutput create() {
        return new StreamHtmlOutput(true);
    }
}
