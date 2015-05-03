package cz.judas.jan.haml;

import cz.judas.jan.haml.tree.ruby.RubyExpression;
import cz.judas.jan.haml.tree.ruby.RubyHash;
import cz.judas.jan.haml.tree.ruby.RubyString;
import cz.judas.jan.haml.tree.ruby.RubySymbol;

@SuppressWarnings("UtilityClass")
public class Expressions {
    public static RubyExpression string(String value) {
        return new RubyString(value);
    }

    public static RubyExpression symbol(String value) {
        return new RubySymbol(value);
    }

    public static RubyHash hash(RubyExpression key, RubyExpression value) {
        return RubyHash.singleEntryHash(key, value);
    }
}
