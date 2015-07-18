package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.runtime.RubyBlock;

public interface PossibleMethodCall extends RubyExpression {
    RubyExpression withBlock(RubyBlock block);
}
