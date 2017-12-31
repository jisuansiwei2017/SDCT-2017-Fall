%{
#include "stdio.h"
#include "mytokenizer.h"
%}
%%
[/][/].*              ;
[/][*][^/]*[*][/]  	  ;
"class"               printkeyword();
"{"					  printsymbol();
"static"			  printkeyword();
"field"				  printkeyword();
"char"   			  printkeyword();
"while"				  printkeyword();
"constructor"		  printkeyword();
"boolean" 			  printkeyword();
"this" 			      printkeyword();
"method"			  printkeyword();
";"					  printsymbol();
"function" 			  printkeyword();
"void"				  printkeyword();
"("					  printsymbol();
")"					  printsymbol();
"~"					  printsymbol();
[\n]                  ;
"<"                   printf("<symbol> &lt; </symbol>\n");
"&" 				  printf("<symbol> &amp; </symbol>\n");
">"					  printf("<symbol> &gt; </symbol>\n");
"let"				  printkeyword();
"[" 				  printsymbol();
"]"					  printsymbol();
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
"true"    	   		  printkeyword();
"false"				  printkeyword();
","					  printsymbol();
"null"				  printkeyword();
["].*["]		 	  {printf("<stringConstant> string constant </stringConstant>\n",yytext);}
[0-9]+		      	  {printf("<integerConstant> %s </integerConstant>\n",yytext);}
[a-zA-Z'_'][a-zA-Z0-9'_']*		  {printf("<identifier> %s </identifier>\n",yytext);}
.                     ;
%%
int main(void)
{
	printf("<tokens>\n");
	yylex();
	printf("</tokens>\n");
}