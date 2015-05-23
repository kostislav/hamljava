grammar JavaHaml;

tokens { INDENT, DEDENT }

@lexer::header {
  import com.yuvalshavit.antlr4.DenterHelper;
}

@lexer::members {
  private final DenterHelper denter = DenterHelper.builder()
    .nl(NL)
    .indent(JavaHamlParser.INDENT)
    .dedent(JavaHamlParser.DEDENT)
    .pullToken(JavaHamlLexer.super::nextToken);

  @Override
  public Token nextToken() {
    return denter.nextToken();
  }
}

htmlTag: tagName (NL | childTag);

tagName : PERCENT WORD;

childTag: INDENT htmlTag DEDENT;

PERCENT : '%';

WORD : ('a'..'z'|'A'..'Z')+;

NL: ('\r'? '\n' (' ' | '\t')*);