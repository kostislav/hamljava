package cz.judas.jan.hamljava.template.tree.ruby;

import cz.judas.jan.hamljava.runtime.UnboundRubyMethod;

public interface PossibleFunctionCall extends RubyExpression {
    RubyExpression withBlock(UnboundRubyMethod block);
}
