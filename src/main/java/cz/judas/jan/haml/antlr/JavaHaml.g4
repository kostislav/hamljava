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

htmlTag: (tagName? attribute* (SPACE text)? (NL | childTags)) | text NL;

tagName: PERCENT WORD;

attribute: idAttribute | classAttribute | attributeHash;

classAttribute: DOT WORD;

idAttribute: HASH WORD;

attributeHash: LEFT_BRACE whitespace? (hashEntry whitespace? COMMA whitespace?)* hashEntry whitespace? RIGHT_BRACE;

hashEntry: attributeKey whitespace? singleQuotedString;

attributeKey: WORD COLON;

singleQuotedString: SINGLE_QUOTE singleQuotedStringContent SINGLE_QUOTE;

singleQuotedStringContent: (~SINGLE_QUOTE)*;

text: (~NL)+;

childTags: INDENT htmlTag+ DEDENT;

whitespace: SPACE | WHITESPACE;


EXCLAMATION: '!';
DOT: '.';
COMMA: ',';
HASH: '#';
PERCENT : '%';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
COLON: ':';
SINGLE_QUOTE: '\'';
WORD : ('a'..'z' | 'A'..'Z' | '0'..'9' | '-')+;
SPACE: ' ';
WHITESPACE: (' ' | '\t')+;

NL: ('\r'? '\n' (' ' | '\t')*);