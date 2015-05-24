package cz.judas.jan.haml.ruby;

import java.util.List;

public interface RubyBlock {
    RubyBlock EMPTY = (arguments, block) -> {
        throw new IllegalStateException("Block not present");
    };

    RubyObject invoke(List<RubyObject> arguments, RubyBlock block);
}
