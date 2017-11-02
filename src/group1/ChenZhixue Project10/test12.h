#ifndef Header_h
#define Header_h


//basic print functions
void print(int n){
	for(int i=1;i<=n;i++)
		printf("\t");
}

void printkeyword(){
	printf("<keyword> %s </keyword>\n",yytext);
}
void printsymbol(){
	printf("<symbol> %s </symbol>\n",yytext);
}

int jg(int function,int iese){
	if(iese)
		return function;
	else 
		return 0;
}

//functions to implement stack
//1 for class, 2 for functiontype, 3 for if, 4 for while
int push(int * a, int d,int len){
	a[len]=d;
	len+=1;
	return len;
}
int pop(int *a, int len){
	int t=a[len-1];
	a[len-1]=0;
	return t;
}

//functions in rules
int leffa(int n){
	print(n);printsymbol();
	print(n);printf("<expression>\n");n++;
	print(n);printf("<term>\n");n++;
	return n;
}

int rigfa(int n){
	n--;print(n);printf("</term>\n");
	n--;print(n);printf("</expression>\n");
	print(n);printsymbol();
	return n;
}

int rigf(int * a,int len,int n){
	int t=pop(a,len);
	if(t==3){
		n--;print(n);printf("</statements>\n");
		print(n);printsymbol();
		print(n);printf("</ifStatement>\n");
		return n;
	}
	if(t==2){
		n--;
		print(n);printf("</statements>\n");
		print(n);printsymbol();n--;
		print(n);printf("</subroutineBody>\n");n--;
		print(n);printf("</subroutineDec>\n");
	}
	if(t==1){
		print(n);printsymbol();
		n--;
		printf("</class>\n");
	}
	return n;
}
int mins(int n, int smb){
	if(smb){
		print(n);printsymbol();
		print(n);printf("<term>\n");
		n++;return n;
	}
	n--;print(n);printf("</term>\n");print(n);printsymbol();print(n);printf("<term>\n");n++;return n;
}

int idf(int n,int mns){
	print(n);printf("<identifier> %s </identifier>\n",yytext);
	if(mns){
		n--;print(n);printf("</term>\n");return n;
	}
	return n;
}

int rigk(int n,int function1,int object){
	if(function1){
		print(n);printf("</parameterList>\n");
	}
	if(object){
		print(n);printf("</expressionList>\n");
	}
	if(1-function1-object){
		n--;print(n);printf("</term>\n");n--;
		print(n);printf("</expression>\n");
	}
	print(n);printsymbol();
	return n;
}

int lefk(int n,int function1,int object){
	print(n);printsymbol();
	if(function1){
		print(n);printf("<parameterList>\n");
	}
	if(object){
		print(n);printf("<expressionList>\n");
	}
	if(1-function1-object){
		print(n);printf("<expression>\n");n++;
		print(n);printf("<term>\n");n++;
	}
	return n;
}

int let(int n,int statement){
	if(1-statement){
		print(n);printf("<statements>\n");
		n++;
	}
	print(n);printf("<letStatement>\n");n++;
	print(n);printkeyword();
	return n;
}
int iff(int n,int statement){
	if(1-statement){
		print(n);printf("<statements>\n");
		n++;
	}
	print(n);printf("<ifStatement>\n");n++;
	print(n);printkeyword();
	return n;
}

int whl(int n,int statement){
	if(1-statement){
		print(n);printf("<statements>\n");
		n++;
	}
	print(n);printf("<whileStatement>\n");n++;
	print(n);printkeyword();
	return n;
}

int elsef(int n){
	n--;print(n);printf("</statements>\n");
	print(n);printf("<symbol> } </symbol>");
	print(n);printf("<keyword> else </keyword>\n");
    print(n);printf("<symbol> { </symbol>");
	return n;
}
int doo(int n,int statement){
	if(1-statement){
		print(n);printf("<statements>\n");
		n++;
	}
	print(n);printf("<doStatement>\n");n++;
	print(n);printkeyword();
	return n;
}
int retn(int n){
	print(n);printf("<returnStatement>\n");
	n++;
	print(n);printkeyword();
	return n;
}

int equal(int n){
	print(n);printsymbol();
	print(n);printf("<expression>\n");n++;
	print(n);printf("<term>\n");n++;
	return n;
}

int stat(int n){
	print(n);printf("<classVarDec>\n");n++;
	print(n);printkeyword();
	return n;
}

int seli(int n,int class,int var_value,int term,int letst,int dost,int returnst){
	if(class){
		print(n);printsymbol();
		n--;print(n);printf("</classVarDec>\n");
	}
	if(var_value){
		print(n);printsymbol();
		n--;print(n);printf("</varDec>\n");
	}
	if(term){
		n--;print(n);printf("</term>\n");
		n--;print(n);printf("</expression>\n");
		print(n);printsymbol();
	}
	if(letst){
		n--;print(n);printf("</letStatement>\n");
	}
	if(dost){
		print(n);printsymbol();
		n--;print(n);printf("</doStatement>\n");
	}
	if(returnst){
		print(n);printsymbol();
		n--;print(n);printf("</returnStatement>\n");
	}
	return n;
}
int func(int n){
	print(n);printf("<subroutineDec>\n");
	n++;print(n);printkeyword();
	return n;
}

int leff(int n,int function,int iese){
	if(iese){
		int a;
		print(n);printsymbol();
		return n;
	}
	if(function-iese){
		print(n);
		printf("<subroutineBody>\n");
		n++;print(n);printsymbol();
		return n;
	}
	print(n);printsymbol();
	return n;
}

int var(int n){
	print(n);printf("<varDec>\n");
	n++;
	print(n);printkeyword();
	return n;
}

#endif /* Header_h */