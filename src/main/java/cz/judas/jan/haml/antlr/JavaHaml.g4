grammar JavaHaml;

tagName : PERCENT WORD;

PERCENT : '%';
WORD : ('a'..'z'|'A'..'Z')+;