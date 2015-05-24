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

doctype: doctypeStart SPACE (WORD | NUMBER) NL;

doctypeStart: EXCLAMATION EXCLAMATION EXCLAMATION;

htmlTag: (tagName? attribute* (plainText | rubyContent)? (NL | childTags)) | escapedText NL | rubyContent NL | text NL;

tagName: PERCENT WORD;

attribute: idAttribute | classAttribute | attributeHash | htmlAttributes;

classAttribute: DOT WORD;

idAttribute: HASH WORD;

htmlAttributes: LEFT_BRACKET whitespace? (htmlAttributeEntry whitespace)* htmlAttributeEntry whitespace? RIGHT_BRACKET ;

htmlAttributeEntry: htmlAttributeKey EQUALS_SIGN expression;

htmlAttributeKey: WORD | (WORD COLON WORD);

escapedText: BACKSLASH text;

plainText: SPACE text;

text: (~NL)+;

rubyContent: EQUALS_SIGN whitespace? expression;

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

expression: symbol | singleQuotedString | doubleQuotedString | methodCall | fieldReference | intValue;

symbol: COLON (WORD | singleQuotedString);

singleQuotedString: SINGLE_QUOTE singleQuotedStringContent SINGLE_QUOTE;

singleQuotedStringContent: (~SINGLE_QUOTE)*;

doubleQuotedString: DOUBLE_QUOTE doubleQuotedStringContent DOUBLE_QUOTE;

doubleQuotedStringContent: (~DOUBLE_QUOTE)*;

intValue: NUMBER;

fieldReference: AT_SIGN WORD;

methodCall: fieldReference (DOT methodName)+ methodParameters?;

methodParameters: LEFT_BRACKET whitespace? expression whitespace? RIGHT_BRACKET;

methodName: WORD;




// Lexer rules

EXCLAMATION: '!';
DOT: '.';
COMMA: ',';
HASH: '#';
PERCENT : '%';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
LEFT_BRACKET: '(';
RIGHT_BRACKET: ')';
COLON: ':';
AT_SIGN: '@';
EQUALS_SIGN: '=';
SINGLE_QUOTE: '\'';
DOUBLE_QUOTE: '"';
BACKSLASH: '\\';
FAT_ARROW: '=>';
NUMBER: ('0'..'9')+;
WORD : ('a'..'z' | 'A'..'Z' | '0'..'9' | '-')+;
SPACE: ' ';
WHITESPACE: (' ' | '\t')+;

NL: ('\r'? '\n' (' ' | '\t')*);

UNKNOWN: .;