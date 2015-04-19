package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import cz.judas.jan.haml.tree.*;

@SuppressWarnings("UtilityClass")
public class Expressions {
    public static RubyExpression string(String value) {
        return new RubyString(value);
    }

    public static RubyExpression symbol(String value) {
        return new RubySymbol(value);
    }

    public static RubyHash hash(RubyExpression key, RubyExpression value) {
        return new RubyHash(ImmutableList.of(new HashEntry(key, value)));
    }
}
