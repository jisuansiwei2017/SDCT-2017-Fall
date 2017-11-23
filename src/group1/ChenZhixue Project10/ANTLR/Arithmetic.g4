grammar Arithmetic;
/*
 * Parser Rules
 */
equation			: expression WHITESPACE* relop WHITESPACE* expression ;
expression			: term WHITESPACE* (op WHITESPACE* term)* ;
term 				: number
					| IDENTIFIER
					| LPAREN WHITESPACE* expression WHITESPACE* RPAREN
					;
relop 				: EQ|GT|LT ;
op					: PLUS|MINUS|MULTIPLY|DIVIDE ;
number 				: MINUS? NUMBER (POINT NUMBER)? ;
/*
 * Lexer Rules
 */

IDENTIFIER			: [a-zA-Z][a-zA-Z0-9]* ;
NUMBER				: [0-9]+	;
EQ 					: '=' ;
GT					: '>' ;
LT					: '<' ;
LPAREN				: '(' ;
RPAREN				: ')' ;
POINT				: '.' ;
PLUS				: '+' ;
MINUS				: '-' ;
MULTIPLY			: '*' ;
DIVIDE				: '/' ;
WHITESPACE          : [ \r\n\t] + -> channel (HIDDEN) ;
