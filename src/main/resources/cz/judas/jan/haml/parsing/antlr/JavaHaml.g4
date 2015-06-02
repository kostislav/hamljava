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

doctype: EXCLAMATION EXCLAMATION EXCLAMATION SPACE actualDoctype NL;

actualDoctype: WORD | NUMBER;

htmlTag: realHtmlTag | code | escapedText NL | rubyContent NL | plainText NL;

realHtmlTag: tagName? attribute* tagContent? (NL | childTags);

tagContent: textContent | rubyContent;

tagName: PERCENT WORD;

attribute: idAttribute | classAttribute | attributeHash | htmlAttributes;

classAttribute: DOT WORD;

idAttribute: HASH WORD;

htmlAttributes: LEFT_BRACKET whitespace? (htmlAttributeEntry whitespace?)* htmlAttributeEntry whitespace? RIGHT_BRACKET ;

htmlAttributeEntry: htmlAttributeKey EQUALS_SIGN expression;

htmlAttributeKey: WORD | (WORD COLON WORD);

escapedText: BACKSLASH hamlSpecialChar text;

hamlSpecialChar: BACKSLASH | EQUALS_SIGN | DASH | PERCENT | DOT | HASH;

textContent: SPACE text;

plainText: text;

text: textEntry+;

textEntry: interpolatedExpression | (BACKSLASH HASH LEFT_BRACE) | (HASH ~LEFT_BRACE) | ~(HASH | NL);

rubyContent: EQUALS_SIGN whitespace? expression;

code: DASH whitespace expression ((whitespace block) | NL);

childTags: INDENT htmlTag+ DEDENT;

whitespace: SPACE+ | WHITESPACE;





// Ruby

attributeHash: LEFT_BRACE whitespace? (newStyleHashEntries | oldStyleHashEntries) whitespace? RIGHT_BRACE;

newStyleHashEntries: (newStyleHashEntry whitespace? COMMA whitespace?)* newStyleHashEntry;

newStyleHashEntry: attributeKey whitespace? expression;

oldStyleHashEntries: (oldStyleHashEntry whitespace? COMMA whitespace?)* oldStyleHashEntry;

oldStyleHashEntry: keyExpression whitespace? FAT_ARROW whitespace? valueExpression;

keyExpression: expression;

valueExpression: expression;

attributeKey: WORD COLON;

expression: localVariable | symbol | singleQuotedString | doubleQuotedString | methodCall | fieldReference | intValue;

symbol: COLON (WORD | singleQuotedString);

singleQuotedString: SINGLE_QUOTE singleQuotedStringContent SINGLE_QUOTE;

singleQuotedStringContent: (~SINGLE_QUOTE)*;

doubleQuotedString: emptyDoubleQuotedString | nonEmptyDoubleQuotedString;

emptyDoubleQuotedString: DOUBLE_QUOTE DOUBLE_QUOTE;

nonEmptyDoubleQuotedString: DOUBLE_QUOTE doubleQuotedStringContent DOUBLE_QUOTE;

doubleQuotedStringContent: (~DOUBLE_QUOTE)+;

interpolatedExpression: HASH LEFT_BRACE expression RIGHT_BRACE;

intValue: NUMBER;

fieldReference: AT_SIGN WORD;

methodCall: methodTarget singleMethodCall+;

methodTarget: fieldReference | localVariable;

localVariable: WORD;

singleMethodCall: DOT methodName (whitespace? methodParameters)?;

methodParameters: methodParametersWithBrackets | emptyMethodParameters | methodParametersWithoutBrackets;

emptyMethodParameters: LEFT_BRACKET whitespace? RIGHT_BRACKET;

methodParametersWithBrackets: LEFT_BRACKET whitespace? methodParametersWithoutBrackets whitespace? RIGHT_BRACKET;

methodParametersWithoutBrackets: (expression whitespace? COMMA whitespace?)* expression;

methodName: WORD;

block: DO (whitespace? PIPE whitespace? blockArguments whitespace? PIPE)? childTags;

blockArguments: (localVariable whitespace? COMMA whitespace?)* localVariable;


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
DASH: '-';
PIPE: '|';
BACKSLASH: '\\';
FAT_ARROW: '=>';
NUMBER: ('0'..'9')+;
DO: 'do';
WORD : ('a'..'z' | 'A'..'Z' | '0'..'9' | '-')+;
SPACE: ' ';
WHITESPACE: (' ' | '\t')+;

NL: ('\r'? '\n' (' ' | '\t')*);

UNKNOWN: .;