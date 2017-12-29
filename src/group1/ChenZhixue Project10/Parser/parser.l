%{
#include "stdio.h"
#include "test1.h"
int a[50]={0};
int len=0;
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
[/][/].*              ;
[/][*][^/]*[*][/]  	  ;
"class"               {print(n);printf("<class>\n");n++;print(n);printkeyword();len=push(a,1,len);class=1;}
"{"					  n=leff(n,function,iese);
"static"			  {type(a,len,n);n++;}
"boolean" 			  {print(n);printkeyword();}
";"					  {n=seli(a,len,n,class,var_value,term,letst,dost,returnst);term=0;var_value=0;letst=0;dost=0;returnst=0;object=0;class=0;}
"function" 			  {len=push(a,2,len);n=func(n);function1=1;function=1;}
"constructor" 		  {len=push(a,2,len);n=func(n);function1=1;function=1;}
"field"				  {type(a,len,n);n++;}
"this" 				  {ths(n);}
"method" 			  {len=push(a,2,len);n=func(n);function1=1;function=1;}
"void"				  {print(n);printkeyword();}
"~"					  {print(n);printsymbol();}
"("					  {n=lefk(n,function1,object);smb=1;}
")"					  {n=rigk(n,function1,object);function1=0;object=0;smb=0;}
[\n]                  ;
"let"				  {n=let(n,statement);letst=1;statement=1;}
"<"                   {print(n);printf("<symbol> &lt; </symbol>\n");}
"&" 				  {print(n);printf("<symbol> &amp; </symbol>\n");}
">"					  {print(n);printf("<symbol> &gt; </symbol>\n");}
"[" 				  {n=leffa(n);smb=1;}
"]"					  {n=rigfa(n);smb=0;}
"if"				  {n=iff(n,statement);len=push(a,3,len);iese=1;}
"while" 	          {n=whl(n,statement);len=push(a,4,len);}
[}][\n][ ]*[e][l][s][e][ ][{]	  {n=elsef(n);statement=0;}
"do" 				  {n=doo(n,statement);dost=1;statement=1;object=1;}
"="					  {n=equal(n);term=1;smb=1;}
"var"				  {n=var(n);var_value=1;}
"return"			  {n=retn(n);returnst=1;}
"."					  {print(n);printsymbol();object=1;}
"*"					  {n--;print(n);printf("</term>\n");print(n);printsymbol();print(n);printf("<term>\n");n++;}
[+/\|]				  {n--;print(n);printf("</term>\n");print(n);printsymbol();print(n);printf("<term>\n");n++;}
"-"					  {n=mins(n,smb);mns=smb;smb=0;}	
"~"                 		  {n=mins(n,1);mns=1;}
"}"				  	  {n=rigf(a,len,n);statement=0;len=len-1;}
"field"				  {print(n);printkeyword();}
","					  {print(n);printsymbol();}
"int"				  {print(n);printkeyword();}
"true"				  {print(n);printkeyword();}
"false"				  {print(n);printkeyword();}
"null"				  {print(n);printkeyword();}
["].*["]		 	  {print(n);printf("<stringConstant> string constant </stringConstant>\n",yytext);}
[0-9]				  {print(n);printf("<integerConstant> %s </integerConstant>\n",yytext);smb=0;if(mns){n--;print(n);printf("</term>\n");}mns=0;}
[a-zA-Z'_'][a-zA-Z0-9'_']*		  {n=idf(n,mns);smb=0;mns=0;}
.                     ;
%%
