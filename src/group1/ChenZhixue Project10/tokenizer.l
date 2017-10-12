%{
#include "stdio.h"
#include "test1.h"
int n=0;
int else_value=0;
int class = 0;
int function = 0;
int function1 = 0;
int arith = 0;
int letst = 0;
int iese = 0;
int term=0;
int object = 0;
int returnst = 0;
int dost = 0;
int statement = 0;
int var_value = 0;
%}
%%
"class"               {printf("<keyword> class </keyword>\n");class=1;}
"{"					  printsymbol();
"static"			  printkeyword();
"boolean" 			  printkeyword();
";"					  printsymbol();
"function" 			  printkeyword();
"void"				  printkeyword();
"("					  printsymbol();
")"					  printsymbol();
[\n]                  ;
"let"				  printkeyword();
"[" 				  printkeyword();
"]"					  printkeyword();
"if"				  printkeyword();
"else"	  		      printkeyword();
"do" 				  printkeyword();
"="					  printsymbol();
"var"				  printkeyword();
"return"			  printkeyword();
"."					  printsymbol();
"*"					  printsymbol();
[+/\|]				  printsymbol();
"-"					  printsymbol();	
"}"				  	  printsymbol();
"int"				  printkeyword();
"false"				  printkeyword();
","					  printsymbol();
"null"				  printkeyword();
["].*["]		 	  {printf("<stringConstant> string constant </stringConstant>\n",yytext);}
[0-9]				  {printf("<integerConstant> %s </integerConstant>\n",yytext);}
[a-zA-Z'_'][a-zA-Z0-9'_']*		  {printf("<identifier> %s </identifier>\n",yytext);}
.                     ;
%%
