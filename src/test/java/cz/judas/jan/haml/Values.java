package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.RubyExpression;
import cz.judas.jan.haml.tree.RubyString;

@SuppressWarnings("UtilityClass")
public class Values {
    public static RubyExpression stringValue(String value) {
        return new RubyString(value);
    }
}
