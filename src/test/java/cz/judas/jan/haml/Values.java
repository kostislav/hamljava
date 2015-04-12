package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.RubyValue;
import cz.judas.jan.haml.tree.StringRubyValue;

@SuppressWarnings("UtilityClass")
public class Values {
    public static RubyValue stringValue(String value) {
        return new StringRubyValue(value);
    }
}
