#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cmath>
#include <algorithm>
#include <iostream>

using namespace std;

char name[100]="\0";
int top=-1;
int Count=0;

bool Read(char ch[])
{
	char cc='\0';
	int flag=0;
	int len=0;
	for(;scanf("%c",&cc)!=EOF;)
	{
		flag=max(flag,1);
		if(cc=='/')
			flag=2;
		if(cc=='\n') break;
		if(flag==2) continue;
		if(cc==' ')
			break;
		ch[++len]=cc;
	}
	ch[len+1]='\0';
	return flag>0;
}

int Read_int()
{
	int ret=0;
	int cc=0;
	
	for(;cc=getchar();)
	{
		if(cc>='0' && cc<='9')
			ret=ret*10+cc-'0';
		else break;
	}
	return ret;
}

int Gettype(char ch[])
{
	if(strcmp(ch+1,"constant")==0) return -1;
	if(strcmp(ch+1,"local")==0) return 1;
	if(strcmp(ch+1,"argument")==0) return 2;
	if(strcmp(ch+1,"this")==0) return 3;
	if(strcmp(ch+1,"that")==0) return 4;
	if(strcmp(ch+1,"temp")==0) return 5;
	if(strcmp(ch+1,"pointer")==0) return 6;
	if(strcmp(ch+1,"static")==0) return 7;
	return 0;
}

void Topreduce()
{
	printf("@0\n");
	printf("M=M-1\n");
	return;
}

void Topincrease()
{
	printf("@0\n");
	printf("M=M+1\n");
	return;
}

void Getadrs(int type,int rk,int adrs)
{
	if(type==0)
	{
		printf("@0\n");
		printf("D=M\n");
	}
	else if(type==6)
	{
		printf("@%d\n",3+rk);
		printf("D=A\n");
	}
	else if(type==7)
	{
		printf("@%d\n",16+rk);
		printf("D=A\n");
	}
	else
	{
		if(type==5)
		{
			printf("@5\n");
			printf("D=A\n");
		}
		else
		{
			printf("@%d\n",type);
			printf("D=M\n");
		}
		printf("@%d\n",rk);
		printf("D=D+A\n");
	}
	printf("@%d\n",adrs);
	printf("M=D\n");
	return;
}

void Getval(int type,int rk,int adrs)
{
	return;
}

void Moveto2(int tmpadrs1,int tmpadrs2)
{
	printf("@%d\n",tmpadrs1);
	printf("A=M\n");
	printf("D=M\n");
	printf("@%d\n",tmpadrs2);
	printf("A=M\n");
	printf("M=D\n");
	return;
}

void Moveto(int type1,int rk1,int type2,int rk2)
{
	Getadrs(type2,rk2,10001);
	if(type1==-1)
	{
		printf("@%d\n",rk1);
		printf("D=A\n");
		printf("@10001\n");
		printf("A=M\n");
		printf("M=D\n");
	}
	else
	{
		Getadrs(type1,rk1,10000);
		Moveto2(10000,10001);
	}
	return;
}

void Workpush()
{
	char ch[100]="\0";
	Read(ch);
	int p=0,rk=0;
	p=Gettype(ch);
	rk=Read_int();
	Moveto(p,rk,0,++top);
	Topincrease();
	return;
}

void Workpop()
{
	char ch[100]="\0";
	Read(ch);
	int p=0,rk=0;
	p=Gettype(ch);
	rk=Read_int();
	Topreduce();
	Moveto(0,top--,p,rk);
	return;
}

char Prtoperation(int operation)
{
	if(operation==0) return '+';
	if(operation==1) return '-';
	if(operation==6) return '&';
	if(operation==7) return '|';
}

void Stackcal(int operation)
{
	
	if(operation!=2 && operation!=8)
	{
		Topreduce();
		Getadrs(0,top,10001);
		Topreduce();
		Getadrs(0,top-1,10000);
		Topincrease();
		printf("@10001\n");
		printf("A=M\n");
		printf("D=M\n");
		printf("@10000\n");
		printf("A=M\n");
		if(operation==1 || (operation>=3 && operation<=5)) printf("M=M-D\n");
		else printf("M=D%cM\n",Prtoperation(operation));
		top--;
	}
	if(operation>=3 && operation<=5)
	{
		Topreduce();
		Getadrs(0,top,10000);
		printf("@10000\n");
		printf("A=M\n");
		printf("D=M\n");
		Count++;
		printf("@TRUE%d\n",Count);
		
		if(operation==3)printf("D;JEQ\n");
		if(operation==4)printf("D;JGT\n");
		if(operation==5)printf("D;JLT\n");
		
		printf("@10000\n");
		printf("A=M\n");
		printf("M=0\n");
		printf("@FALSE%d\n",Count);
		printf("0;JMP\n");
		
		printf("(TRUE%d)\n",Count);
		printf("@10000\n");
		printf("A=M\n");
		printf("M=-1\n");
		
		printf("(FALSE%d)\n",Count);
		Topincrease();
	}
	else if(operation==2 || operation==8)
	{
		Topreduce();
		Getadrs(0,top,10000);
		printf("@10000\n");
		printf("A=M\n");
		if(operation==2)
			printf("M=-M\n");
		else printf("M=!M\n");
		Topincrease();
	}
	return;
}

void Work()
{
	char ch[100]="\0";
	int len=0;
	for(;Read(ch);)
	{
		len=strlen(ch+1);
		if(strcmp(ch+1,"push")==0) Workpush();
		if(strcmp(ch+1,"pop")==0) Workpop();
		if(strcmp(ch+1,"add")==0) Stackcal(0);
		if(strcmp(ch+1,"sub")==0) Stackcal(1);
		if(strcmp(ch+1,"neg")==0) Stackcal(2);
		if(strcmp(ch+1,"eq")==0) Stackcal(3);
		if(strcmp(ch+1,"gt")==0) Stackcal(4);
		if(strcmp(ch+1,"lt")==0) Stackcal(5);
		if(strcmp(ch+1,"and")==0) Stackcal(6);
		if(strcmp(ch+1,"or")==0) Stackcal(7);
		if(strcmp(ch+1,"not")==0) Stackcal(8);
		fprintf(stderr,"%s\n",ch+1);
	}
	return;
}

int main(int argc,char *argv[])
{
	freopen(argv[1],"r",stdin);
	strcpy(name,argv[1]);
	for(int i=0;;i++)
		if(name[i]=='.')
		{
			name[i]='\0';
			break;
		}
	strcat(name,".asm");
	freopen(name,"w",stdout);
	Work();
	return 0;
}
