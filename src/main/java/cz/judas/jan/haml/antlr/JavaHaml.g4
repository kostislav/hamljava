grammar JavaHaml;

// denter boilerplate

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

// HAML

document: doctype? htmlTag+;

doctype: doctypeStart SPACE WORD NL;

doctypeStart: EXCLAMATION EXCLAMATION EXCLAMATION;

htmlTag: (tagName? attribute* (SPACE text)? (NL | childTags)) | escapedText NL | text NL;

tagName: PERCENT WORD;

attribute: idAttribute | classAttribute | attributeHash;

classAttribute: DOT WORD;

idAttribute: HASH WORD;

escapedText: BACKSLASH text;

text: (~NL)+;

childTags: INDENT htmlTag+ DEDENT;

whitespace: SPACE | WHITESPACE;





// Ruby

attributeHash: LEFT_BRACE whitespace? (newStyleHashEntries | oldStyleHashEntries) whitespace? RIGHT_BRACE;

newStyleHashEntries: (newStyleHashEntry whitespace? COMMA whitespace?)* newStyleHashEntry;

newStyleHashEntry: attributeKey whitespace? expression;

oldStyleHashEntries: (oldStyleHashEntry whitespace? COMMA whitespace?)* oldStyleHashEntry;

oldStyleHashEntry: keyExpression whitespace? FAT_ARROW whitespace? valueExpression;

keyExpression: expression;

valueExpression: expression;

attributeKey: WORD COLON;

expression: symbol | singleQuotedString;

symbol: COLON WORD;

singleQuotedString: SINGLE_QUOTE singleQuotedStringContent SINGLE_QUOTE;

singleQuotedStringContent: (~SINGLE_QUOTE)*;



// Lexer rules

EXCLAMATION: '!';
DOT: '.';
COMMA: ',';
HASH: '#';
PERCENT : '%';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
COLON: ':';
SINGLE_QUOTE: '\'';
BACKSLASH: '\\';
FAT_ARROW: '=>';
WORD : ('a'..'z' | 'A'..'Z' | '0'..'9' | '-')+;
SPACE: ' ';
WHITESPACE: (' ' | '\t')+;

NL: ('\r'? '\n' (' ' | '\t')*);

UNKNOWN: .;