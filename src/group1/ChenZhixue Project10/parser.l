%{
#include "stdio.h"
#include "test1.h"
int n=0;
int smb=0;
int else_value=0;
int class = 0;
int mns=0;
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
"class"               {printf("<class>\n");n++;printf("\t<keyword> class </keyword>\n");class=1;}
"{"					  n=leff(n,function,iese);
"static"			  n=stat(n);
"boolean" 			  {for(int i=1;i<=n;i++) printf("\t"); printf("<keyword> %s </keyword>\n",yytext);}
";"					  {n=seli(n,class,var_value,term,letst,dost,returnst);term=0;var_value=0;letst=0;dost=0;returnst=0;object=0;}
"function" 			  {if(class) {class=0;}function=1;function1=1;for(int i=1;i<=n;i++) printf("\t");printf("<subroutineDec>\n");n++;for(int i=1;i<=n;i++) printf("\t");printf("<keyword> %s </keyword>\n",yytext);}
"void"				  {for(int i=1;i<=n;i++) printf("\t"); printf("<keyword> %s </keyword>\n",yytext);}
"("					  {n=lefk(n,function1,object);smb=1;}
")"					  {n=rigk(n,function1,object);function1=0;object=0;smb=0;}
[\n]                  ;
"let"				  {n=let(n,statement);letst=1;statement=1;}
"[" 				  {n=leffa(n);smb=1;}
"]"					  {n=rigfa(n);smb=0;}
"if"				  {n=iff(n,statement);iese=1;}
"else"	  		      {n=elsef(n);statement=0;iese=1;else_value=1;}
"do" 				  {n=doo(n,statement);dost=1;statement=1;}
"="					  {n=equal(n);term=1;smb=1;}
"var"				  {n=var(n);var_value=1;}
"return"			  {n=retn(n);returnst=1;}
"."					  {print(n);printsymbol();object=1;}
"*"					  {n--;print(n);printf("</term>\n");print(n);printsymbol();print(n);printf("<term>\n");n++;}
[+/\|]				  {n--;print(n);printf("</term>\n");print(n);printsymbol();print(n);printf("<term>\n");n++;}
"-"					  {n=mins(n,smb);smb=0;mns=1;}	
"}"				  	  {n=rigf(n,function,iese,else_value);statement=0;function=jg(function,iese);iese=0;}
"int"				  {print(n);printkeyword();}
"false"				  {print(n);printkeyword();}
","					  {print(n);printsymbol();}
"null"				  {print(n);printkeyword();}
["].*["]		 	  {print(n);printf("<stringConstant> string constant </stringConstant>\n",yytext);}
[0-9]				  {print(n);printf("<integerConstant> %s </integerConstant>\n",yytext);smb=0;if(mns){n--;print(n);printf("</term>\n");}mns=0;}
[a-zA-Z'_'][a-zA-Z0-9'_']*		  {n=idf(n,mns);smb=0;mns=0;}
.                     ;
%%
