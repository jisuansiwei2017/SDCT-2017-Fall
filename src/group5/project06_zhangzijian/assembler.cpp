#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cmath>
#include <algorithm>
#include <iostream>
#include <map>

using namespace std;

char name[10000]="\0";
char Sent[10000]="\0";
int lenth=0;
int RAM[40000]={0};
char C[10000]="\0";
 
void prt10to2(int NUM_)
{
	for(int i=14;i>=0;i--)
		putchar('0'+((NUM_>>i)&1));
	return;
}

int Read10(char *ch)
{
	int ret=0;
	for(int i=0;ch[i]>='0' && ch[i]<='9';i++)
		ret=ret*10+ch[i]-'0';
	return ret;
}

int getAdress()
{
	int Adress=0;
	if(Sent[1]>='0' && Sent[1]<='9')
		Adress=Read10(Sent+1);
	else
	{
		if(strcmp(Sent+1,"SP")==0) return 0;
		if(strcmp(Sent+1,"LCL")==0) return 1;
		if(strcmp(Sent+1,"ARG")==0) return 2;
		if(strcmp(Sent+1,"THIS")==0) return 3;
		if(strcmp(Sent+1,"THAT")==0) return 5;
		if(strcmp(Sent+1,"SCREEN")==0) return 16384;
		if(strcmp(Sent+1,"KBD")==0) return 24576;
		Adress=Read10(Sent+2);
	}
	return Adress;
}

void A_instruction()
{
	int Adress=0;
	Adress=getAdress();		//将字符串转换成十进制数 
	printf("0");
	prt10to2(Adress);		//将十进制数输出位二进制数  
	puts("");
	return;
}

void judge_d_j(char *ch,int &dest,int &jump)
{
	for(int i=0;ch[i]!='\0';i++)
	{
		if(ch[i]=='=')
			dest=i;
		if(ch[i]==';')
			jump=i;
	}
	return;
}

void prt_c_C_instruction(int dest,int jump)
{
	int l=dest+1;
	int Len=(jump==-1?lenth:jump)-l;
	for(int i=0;i<Len;i++)
		C[i]=Sent[l+i];
	C[Len]='\0';
	//fprintf(stderr,"%s\n",C);
	if(strcmp(C,"0")==0) printf("0101010");
	if(strcmp(C,"1")==0) printf("0111111");
	if(strcmp(C,"-1")==0) printf("0111010");
	if(strcmp(C,"D")==0) printf("0001100");
	if(strcmp(C,"A")==0) printf("0110000");
	if(strcmp(C,"!D")==0) printf("0001101");
	if(strcmp(C,"!A")==0) printf("0110001");
	if(strcmp(C,"-D")==0) printf("0001111");
	if(strcmp(C,"-A")==0) printf("0110011");
	if(strcmp(C,"D+1")==0) printf("0011111");
	if(strcmp(C,"A+1")==0) printf("0110111");
	if(strcmp(C,"D-1")==0) printf("0001110");
	if(strcmp(C,"A-1")==0) printf("0110010");
	if(strcmp(C,"D+A")==0) printf("0000010");
	if(strcmp(C,"D-A")==0) printf("0010011");
	if(strcmp(C,"A-D")==0) printf("0000111");
	if(strcmp(C,"D&A")==0) printf("0000000");
	if(strcmp(C,"D|A")==0) printf("0010101");
	if(strcmp(C,"M")==0) printf("1110000");
	if(strcmp(C,"!M")==0) printf("1110001");
	if(strcmp(C,"-M")==0) printf("1110011");
	if(strcmp(C,"M+1")==0) printf("1110111");
	if(strcmp(C,"M-1")==0) printf("1110010");
	if(strcmp(C,"D+M")==0) printf("1000010");
	if(strcmp(C,"D-M")==0) printf("1010011");
	if(strcmp(C,"M-D")==0) printf("1000111");
	if(strcmp(C,"D&M")==0) printf("1000000");
	if(strcmp(C,"D|M")==0) printf("1010101");
	
	return;
}

void prt_d_C_instruction(int dest)
{
	if(dest==-1)
	{
		printf("000");
		return;
	}
	int Len=dest;
	for(int i=0;i<Len;i++)
		C[i]=Sent[i];
	C[Len]='\0';
	if(strcmp(C,"M")==0) printf("001");
	if(strcmp(C,"D")==0) printf("010");
	if(strcmp(C,"MD")==0) printf("011");
	if(strcmp(C,"A")==0) printf("100");
	if(strcmp(C,"AM")==0) printf("101");
	if(strcmp(C,"AD")==0) printf("110");
	if(strcmp(C,"AMD")==0) printf("111");
	return;
}

void prt_j_C_instruction(int jump)
{
	if(jump==-1)
	{
		printf("000");
		return;
	}
	int Len=lenth-jump-1;
	for(int i=0;i<Len;i++)
		C[i]=Sent[jump+1+i];
	C[Len]='\0';
	if(strcmp(C,"JGT")==0) printf("001");
	if(strcmp(C,"JEQ")==0) printf("010");
	if(strcmp(C,"JGE")==0) printf("011");
	if(strcmp(C,"JLT")==0) printf("100");
	if(strcmp(C,"JNE")==0) printf("101");
	if(strcmp(C,"JLE")==0) printf("110");
	if(strcmp(C,"JMP")==0) printf("111");
	return;
}

void C_instruction()
{
	printf("111");
	int dest=-1,jump=-1;
	judge_d_j(Sent,dest,jump);		//得到'='和';'的位置 
	//fprintf(stderr,"%d %d\n",dest,jump);
	prt_c_C_instruction(dest,jump);		 //输出c1~c6 
	prt_d_C_instruction(dest);		//输出d1~d3 
	prt_j_C_instruction(jump);		//输出j1~j3 
	puts("");
	return;
}

void Trans()
{
	if(Sent[0]=='@')
		A_instruction(); 
	else C_instruction();
	return;
}

int Done()
{
	int ch=0;
	int flag=0;
	for(;ch=getchar();)
	{
		if(ch==-1)
			return 0;
		if(ch==' ') continue;
		if(ch=='\n') break;
		if(ch=='(')
			flag=1;
		if(flag==0)
			Sent[lenth++]=ch;
		if(ch==')')
			flag=0;
	}	//除去空格 换行 (*) 
	Sent[lenth]='\0';
	for(int i=1;i<lenth;i++)
		if(Sent[i]=='/' && Sent[i-1]=='/')
		{
			Sent[i-1]='\0';
			lenth=i-1;
		}	//去掉注释 
	if(lenth!=0)
	{
		//fprintf(stderr,"%s\n",Sent);
		Trans();	//翻译 
	}
	memset(Sent,'\0',sizeof(char)*lenth);
	lenth=0;
	return 1;
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
	strcat(name,".hack");
	freopen(name,"w",stdout);
	for(;Done(););	//处理每条语句 
	return 0;
}
