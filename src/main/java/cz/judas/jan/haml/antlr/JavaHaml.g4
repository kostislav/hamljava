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

document: doctype? htmlTag+;

doctype: doctypeStart SPACE WORD NL;

doctypeStart: EXCLAMATION EXCLAMATION EXCLAMATION;

htmlTag: tagName classAttribute* (SPACE text)? (NL | childTags);

tagName: PERCENT WORD;

classAttribute: DOT WORD;

text: (~NL)+;

childTags: INDENT htmlTag+ DEDENT;


EXCLAMATION: '!';

DOT: '.';

PERCENT : '%';

WORD : ('a'..'z' | 'A'..'Z' | '0'..'9')+;

SPACE: ' ';

NL: ('\r'? '\n' (' ' | '\t')*);