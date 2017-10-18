#pragma once

#include"stdafx.h"
typedef std::string EXP;
typedef std::string STATIC;
typedef std::string ASMLABLE;
//typedef int POINTER;
#define PUSH "push"                                          //VM命令
#define POP "pop"
#define ADD "add"
#define SUB "sub"
#define NEG "neg"
#define EQUAL "eq"
#define GREATER_THAN "gt"
#define LOWER_THAN "lt"
#define AND "and"
#define OR "or"
#define NOT "not"
#define LABEL "label"
#define GOTO "goto"
#define IFGOTO "if-goto"
#define FUNCTION "function"
#define CALL "call"
#define RETURN "return"
#define SP "SP"                               //内存地址代码
#define LCL "LCL"
#define ARG "ARG"
#define THI "THIS"
#define THA "THAT"
#define TMP "5"
#define STA "16"
#define R0 "R0"
#define R1 "R1"
#define R2 "R2"
#define R3 "R3"
#define R4 "R4"
#define R5 "R5"
#define R6 "R6"
#define R7 "R7"
#define R8 "R8"
#define R9 "R9"
#define R10 "R10"
#define R11 "R11"
#define R12 "R12"
#define R13 "R13"
#define R14 "R14"
#define R15 "R15"
#define SETA(a) (asmfile<<'@'<<(a)<<endl,asmcursor++)                                                        //汇编语句书写
#define ASSIGN_A(a) (asmfile<<"A="<<(a)<<endl,asmcursor++)
#define ASSIGN_M(a) (asmfile<<"M="<<(a)<<endl,asmcursor++)
#define ASSIGN_D(a) (asmfile<<"D="<<(a)<<endl,asmcursor++)
#define TONOT(a,b) (asmfile<<(a)<<'='<<'!'<<(b)<<endl,asmcursor++)
#define TONEG(a,b) (asmfile<<(a)<<'='<<'-'<<(b)<<endl,asmcursor++)
#define TOAND(a,b,c) (asmfile<<(a)<<'='<<(b)<<'&'<<(c)<<endl,asmcursor++)
#define TOOR(a,b,c) (asmfile<<(a)<<'='<<(b)<<'|'<<(c)<<endl,asmcursor++)
#define TOADD(a,b,c) (asmfile<<(a)<<'='<<(b)<<'+'<<(c)<<endl,asmcursor++)
#define TOSUB(a,b,c) (asmfile<<(a)<<'='<<(b)<<'-'<<(c)<<endl,asmcursor++)
#define JUMPWHEN(a,b) (asmfile<<(a)<<';'<<(b)<<endl,asmcursor++)
#define TOLABEL(a) (asmfile<<'('<<(a)<<')'<<endl)
#define JGT ";JGT"                                               //跳转关键字
#define JEQ ";JEQ"
#define JLT ";JLT"
#define JGE ";JGE"
#define JNE ";JNE"
#define JLE ";JLE"
#define JMP ";JMP"
#define REGA 'A'                                     //寄存器代号
#define REGM 'M'
#define REGD 'D'
#define REGAM "AM"
#define REGAD "AD"
#define REGMD "MD"
#define REGAMD "AMD"
#define CONSTANT "constant"                       //内存块代号
#define LOCAL "local"
#define ARGUMENT "argument"
#define THIS "this"
#define THAT "that"
#define STATIC "static"
#define TEMP "temp"
#define POINTER "pointer"
#define ONE "1"

// VMtranslator类的声明

class VMtranslator
{
protected:
	std::ifstream vmfile;     //输入文件
	std::ofstream asmfile;     //输出文件
	int vmcursor = 0;           //指出VM文件正在编译的行数
	int asmcursor = 0;
	const int& Casmcursor = asmcursor;
	std::map<std::string,ASMLABLE> vmLableTable;      //VM语言跳转lable表,(label,地址）
	std::map<std::string,ASMLABLE> vmFunctionTable;    //VM语言函数表（label，地址）
	std::vector<ASMLABLE> asmLableTable;          //asmLabel表
	EXP advance();    //前进并返回下一行经预处理的代码,文件到达末尾时返回空字符串。
	bool precompile(EXP& expression);    //代码预处理，去除多余空格以及注释，有代码则返回真
	void parser(EXP exp);       //语句解释器，调用相关的写代码程式,并输出语法错误在屏幕上
	void writePush(EXP block, int number);              //写push代码
	void writePop(EXP block, int number);              //写pop代码
	void writeSingleArithmetic(EXP instruction);      //写单因子计算代码
	void writeDoubleArithmetic(EXP instruction);      //写双因子计算代码
	void writeLabel(EXP Labelname);                   //写Label
	void writeGoto(bool conditional,EXP labelname);                   //写goto和if-goto
	void writeFunction(EXP Functioname, int nVars);
	void writeCall(EXP Functioname, int nArgs);
	void writeReturn(void);
	void writeBootstrapper();
public:
	VMtranslator(std::string vmdir, std::string asmdir) {             //构造函数，制定输入及输出文件
		vmfile.open(vmdir);
		asmfile.open(asmdir);
	}
	VMtranslator(std::string asmdir) {             //构造函数，制定输入及输出文件
		asmfile.open(asmdir);
	}
	void translate();		//翻译器主体 翻译当前文件
	void VMtranslator::translate(std::vector<std::string>& vmfiles);    //翻译多个文件
};
