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
    .pullToken(super::nextToken);

  @Override
  public Token nextToken() {
    return denter.nextToken();
  }
}

// HAML

document: doctype? line+;

doctype: EXCLAMATION EXCLAMATION EXCLAMATION SPACE actualDoctype NL;

actualDoctype: WORD | NUMBER;

line: htmlElement | code | escapedText NL | rubyContent | plainText NL;

htmlElement: elementDefinition elementContent;

elementContent: (textContent (NL | childTags)) | rubyContent | NL | childTags;

elementDefinition: (elementName | idAttribute | classAttribute) shortAttribute* longAttribute*;

elementName: PERCENT (WORD | HTML_ELEMENT);

shortAttribute: idAttribute | classAttribute;

longAttribute: attributeHash | htmlAttributes;

classAttribute: DOT WORD;

idAttribute: HASH WORD;

htmlAttributes: LEFT_BRACKET whitespace? (htmlAttributeEntry whitespace?)* htmlAttributeEntry whitespace? RIGHT_BRACKET ;

htmlAttributeEntry: keyValueHtmlAttributeEntry | booleanHtmlAttributeEntry;

keyValueHtmlAttributeEntry: htmlAttributeKey EQUALS_SIGN expression;

booleanHtmlAttributeEntry: htmlAttributeKey;

htmlAttributeKey: WORD | (WORD COLON WORD);

escapedText: BACKSLASH hamlSpecialChar text;

hamlSpecialChar: BACKSLASH | EQUALS_SIGN | DASH | PERCENT | DOT | HASH;

textContent: SPACE text;

plainText: text;

text: textEntry+;

textEntry: interpolatedExpression | (BACKSLASH HASH LEFT_BRACE) | (HASH ~LEFT_BRACE) | ~(HASH | NL | DEDENT);

rubyContent: unescapedRubyContent | regularRubyContent;

unescapedRubyContent: EXCLAMATION regularRubyContent;

regularRubyContent: EQUALS_SIGN whitespace? statement;

code: DASH whitespace? statement;

childTags: INDENT line+ DEDENT;

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

statement: methodWithBlock | functionWithBlock | ifStatement | (expression NL);

methodWithBlock: methodCall whitespace block;

functionWithBlock: functionCall whitespace block;

expression: negatedExpression | localVariable | symbol | singleQuotedString | doubleQuotedString | methodCall | functionCall | fieldReference | intValue;

negatedExpression: EXCLAMATION whitespace? expression;

ifStatement: IF whitespace expression whitespace? childTags;

symbol: COLON (WORD | singleQuotedString);

singleQuotedString: SINGLE_QUOTE singleQuotedStringContent SINGLE_QUOTE;

singleQuotedStringContent: (~SINGLE_QUOTE)*;

doubleQuotedString: emptyDoubleQuotedString | nonEmptyDoubleQuotedString;

emptyDoubleQuotedString: DOUBLE_QUOTE DOUBLE_QUOTE;

nonEmptyDoubleQuotedString: DOUBLE_QUOTE doubleQuotedStringContent DOUBLE_QUOTE;

doubleQuotedStringContent: doubleQuotedStringElement+;

doubleQuotedStringElement: interpolatedExpression | (BACKSLASH HASH LEFT_BRACE) | (HASH ~LEFT_BRACE) | (BACKSLASH DOUBLE_QUOTE) | ~(HASH | NL | DEDENT | DOUBLE_QUOTE);

interpolatedExpression: HASH LEFT_BRACE expression RIGHT_BRACE;

intValue: NUMBER;

fieldReference: AT_SIGN WORD;

methodCall: methodTarget DOT singleMethodCall chainedMethodCalls;

methodTarget: fieldReference | localVariable;

functionCall: singleMethodCall chainedMethodCalls;

chainedMethodCalls: (DOT singleMethodCall)*;

localVariable: WORD;

singleMethodCall: methodName (whitespace? methodParameters)?;

methodParameters: methodParametersWithBrackets | emptyMethodParameters | methodParametersWithoutBrackets;

emptyMethodParameters: LEFT_BRACKET whitespace? RIGHT_BRACKET;

methodParametersWithBrackets: LEFT_BRACKET whitespace? methodParametersWithoutBrackets whitespace? RIGHT_BRACKET;

methodParametersWithoutBrackets: (expression whitespace? COMMA whitespace?)* expression;

methodName: WORD;

block: DO (whitespace? PIPE whitespace? blockArguments whitespace? PIPE)? childTags;

blockArguments: (localVariable whitespace? COMMA whitespace?)* localVariable;


// Lexer rules

FAT_ARROW: '=>';

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
NUMBER: (DIGIT)+;
DO: 'do';
IF: 'if';
WORD : (ASCII_LETTER | DIGIT | '-')+;
HTML_ELEMENT: (ASCII_LETTER | DIGIT ':' | '_' | '-')+;
SPACE: ' ';
WHITESPACE: WHITESPACE_CHAR+;

NL: ('\r'? '\n' WHITESPACE_CHAR*);

UNKNOWN: .;

fragment DIGIT: '0'..'9';
fragment ASCII_LETTER: 'a'..'z' | 'A'..'Z';
fragment WHITESPACE_CHAR: ' ' | '\t';
