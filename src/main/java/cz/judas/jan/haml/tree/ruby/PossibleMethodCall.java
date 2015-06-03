package cz.judas.jan.haml.tree.ruby;

import cz.judas.jan.haml.runtime.RubyBlock;

public interface PossibleMethodCall extends RubyExpression {
    RubyExpression withBlock(RubyBlock block);
}
