package cz.judas.jan.haml.ruby;

import cz.judas.jan.haml.VariableMap;

import java.util.List;

public interface RubyBlock {
    RubyBlock EMPTY = (variableMap, arguments, block) -> {
        throw new IllegalStateException("Block not present");
    };

    RubyObject invoke(VariableMap variableMap, List<RubyObject> arguments, RubyBlock block);
}
