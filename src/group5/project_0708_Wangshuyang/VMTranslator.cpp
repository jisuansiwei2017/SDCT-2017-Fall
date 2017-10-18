// VMtranslater.cpp: 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include "VMtranslator.h"
using namespace std;

void getFiles(string path, string exd, vector<string>& files)    //http://blog.csdn.net/shaoxiaohu1/article/details/37499803
{
	//文件句柄
	long   hFile = 0;
	//文件信息
	struct _finddata_t fileinfo;
	string pathName, exdName;

	if (0 != strcmp(exd.c_str(), ""))
	{
		exdName = "\\*." + exd;
	}
	else
	{
		exdName = "\\*";
	}

	if ((hFile = _findfirst(pathName.assign(path).append(exdName).c_str(), &fileinfo)) != -1)
	{
		do
		{
			//如果是文件夹中仍有文件夹,迭代之
			//如果不是,加入列表
			if ((fileinfo.attrib &  _A_SUBDIR))
			{
				if (strcmp(fileinfo.name, ".") != 0 && strcmp(fileinfo.name, "..") != 0)
					getFiles(pathName.assign(path).append("\\").append(fileinfo.name), exd, files);
			}
			else
			{
				if (strcmp(fileinfo.name, ".") != 0 && strcmp(fileinfo.name, "..") != 0)
					files.push_back(pathName.assign(path).append("\\").append(fileinfo.name));
			}
		} while (_findnext(hFile, &fileinfo) == 0);
		_findclose(hFile);
	}
}

int main(int argc, char* argv[])
{
	string vmfile;
	string asmfile;
	if (argc == 1)
	{
		cerr << "Lack of Parameters:Please Spcify your file" << endl;
		return 0;
	}
	else if (argc == 2)
	{
		vmfile = argv[1];
		ifstream tryinput(vmfile);
		if (!tryinput)
		{
			cerr << "No such a file or directory:The file name should not be with space" << endl;
			return 0;
		}
		auto pos = vmfile.rfind('.');
		if (pos == string::npos)    //没找到
		{
			cerr << "The file is not .vm file. If it's a directory please specify the output file with .asm" << endl;
			return 0;
		}
		else
		{
			asmfile = vmfile.substr(0, pos);
			asmfile.append(".asm");
			VMtranslator vm(vmfile, asmfile);
			vm.translate();
		}
	}
	else if (argc == 3)
	{
		vmfile = argv[1];
		asmfile = argv[2];
		auto pos = vmfile.rfind('.');
		if (pos != string::npos)    //找到了拓展名
		{
			VMtranslator vm(vmfile, asmfile);
			vm.translate();
		}
		else
		{
			vector<string> AllInputFiles;
			getFiles(vmfile,"vm",AllInputFiles);
			for (auto beg = AllInputFiles.begin(); beg != AllInputFiles.end(); beg++)
			{
				if (*beg == "Sys.vm")
				{
					AllInputFiles.erase(beg);
					break;
				}
			}
			VMtranslator vm(asmfile);
			vm.translate(AllInputFiles);
		}
	}
	else
	{
		cerr << "Unexpected Parameters.The file name should not be with space " << endl;
		return 0;
	}
	return 0;
}

void VMtranslator::translate() {
	EXP expression;
	while (!((expression = advance()).empty()))
	{
		parser(expression);
	}
}

void VMtranslator::translate(vector<string>& AllinputFile) {
	writeBootstrapper();
	vmfile.open("Sys.vm");
	vmfile.close();
	translate();
	for (auto pos = AllinputFile.begin(); pos != AllinputFile.end(); pos++)
	{
		vmfile.open(*pos);
		translate();
		vmfile.close();
	}
}

EXP VMtranslator::advance()
{
	EXP expression;
	do {
		getline(vmfile, expression);
		vmcursor++;
	} while ((!precompile(expression)) && (!vmfile.eof()));    //判断是否有效语句，最后一行，若不是有效语句，则会返回空字符串；
	return expression;
}

bool VMtranslator::precompile(EXP& expression) {
	auto posofNote = expression.find("//");                  //去除注释
	if (posofNote != expression.npos)
		expression.erase(posofNote);
	for (int pos = 0, end = expression.size(); pos != end; pos++)            //去除多余空格
	{
		if (expression[pos] == ' ')
		{
			if (pos == 0)                  //去除起始空格
			{
				int i = 0;
				for (; i != expression.size() && expression[i] == ' '; i++);
				expression.erase(0, i);
			}
			else if (expression[pos - 1] != ' ')                   //去除中间多余空格及尾后空格
			{
				int i = pos;
				for (; i != expression.size() && expression[i] == ' '; i++);
				if (i == expression.size() && expression[i - 1] == ' ')    //去除尾后空格
				{
					expression.erase(pos);
					pos--;
				}
				else
					expression.erase(pos + 1, i - pos - 1);
			}
			else                                   //向前去除空格
			{
				int i = pos;
				for (; i != 0 && expression[i] == ' '; i--);
				expression.erase(i, pos + 1);
				pos = i;
			}
		}
		end = expression.size();              //更新字符串尾
	}
	if (expression.empty())
		return false;
	return true;
}

void VMtranslator::parser(EXP exp)
{
	istringstream expression(exp);
	EXP command;
	EXP arg1;
	//STATIC staticArg2;
	int numberArg2;
	expression >> command;
	if (command == PUSH)
	{
		expression >> arg1;
		expression >> numberArg2;
		if (!expression.fail())
			writePush(arg1, numberArg2);
		//else
		//writePush(staticArg2);
	}
	else if (command == POP)
	{
		expression >> arg1;
		expression >> numberArg2;
		if (!expression.fail())
			writePop(arg1, numberArg2);
		//else
		//writePop(staticArg2);
	}
	else if (command == NEG || command == NOT)
	{
		writeSingleArithmetic(command);
	}
	else if (command == ADD || command == SUB || command == EQUAL ||
		command == GREATER_THAN || command == LOWER_THAN || command == AND || command == OR)
	{
		writeDoubleArithmetic(command);
	}
	else if (command == LABEL)
	{
		expression >> arg1;
		if (!expression.fail())
			writeLabel(arg1);
	}
	else if (command == GOTO)
	{
		expression >> arg1;
		if (!expression.fail())
			writeGoto(0,arg1);
	}
	else if (command == IFGOTO)
	{
		expression >> arg1;
		if (!expression.fail())
			writeGoto(1, arg1);
	}
	else if (command == FUNCTION)
	{
		expression >> arg1;
		expression >> numberArg2;
		if (!expression.fail())
			writeFunction(arg1, numberArg2);
	}
	else if (command == CALL)
	{
		expression >> arg1;
		expression >> numberArg2;
		if (!expression.fail())
			writeCall(arg1,numberArg2);
	}
	else if (command == RETURN)
	{
			writeReturn();
	}
	else
	{
		cerr << "line " << vmcursor << " errorC0001: syntax error:undefined symbol" << endl;
	}
	return;
}

void VMtranslator::writePush(EXP block, int number)
{
	if (block == CONSTANT)
	{
		SETA(number);
		ASSIGN_D(REGA);
	}
	else
	{
		if (block == LOCAL) {
			SETA(LCL);
			ASSIGN_D(REGM);
		}
		else if (block == ARGUMENT)
		{
			SETA(ARG);
			ASSIGN_D(REGM);
		}
		else if (block == THIS)
		{
			SETA(THI);
			ASSIGN_D(REGM);
		}
		else if (block == THAT)
		{
			SETA(THA);
			ASSIGN_D(REGM);
		}
		else if (block == TEMP)
		{
			SETA(TMP);
			ASSIGN_D(REGA);
		}
		else if (block == STATIC)
		{
			SETA(STA);
			ASSIGN_D(REGA);
		}
		else if (block == POINTER)
		{
			SETA(R3);
			ASSIGN_D(REGA);
		}
		else
		{
			SETA(block);
			ASSIGN_D(REGA);
		}
		SETA(number);
		TOADD(REGA, REGD, REGA);
		ASSIGN_D(REGM);
	}
	SETA(SP);
	ASSIGN_A(REGM);
	ASSIGN_M(REGD);
	TOADD(REGD, REGA, ONE);
	SETA(SP);
	ASSIGN_M(REGD);
}



void VMtranslator::writePop(EXP block, int number)
{
	SETA(number);
	ASSIGN_D(REGA);
	if (block == LOCAL) {
		SETA(LCL);
		TOADD(REGA, REGD, REGM);
	}
	else if (block == ARGUMENT)
	{
		SETA(ARG);
		TOADD(REGA, REGD, REGM);
	}
	else if (block == THIS)
	{
		SETA(THI);
		TOADD(REGA, REGD, REGM);
	}
	else if (block == THAT)
	{
		SETA(THA);
		TOADD(REGA, REGD, REGM);
	}
	else if (block == TEMP)
	{
		SETA(TMP);
		TOADD(REGA, REGD, REGA);
	}
	else if (block == STATIC)
	{
		SETA(STA);
		TOADD(REGA, REGD, REGA);
	}
	else if (block == POINTER)
	{
		SETA(R3);
		TOADD(REGA, REGD, REGA);
	}
	else
	{
		SETA(block);
		TOADD(REGA, REGD, REGA);
	}
	ASSIGN_D(REGA);
	SETA(R13);
	ASSIGN_M(REGD);
	SETA(SP);
	TOSUB(REGAM, REGM, ONE);
	ASSIGN_D(REGM);
	SETA(R13);
	ASSIGN_A(REGM);
	ASSIGN_M(REGD);
}
void VMtranslator::writeSingleArithmetic(EXP instruction)
{
	SETA(SP);
	TOSUB(REGA, REGM, ONE);
	ASSIGN_D(REGM);
	if (instruction == NOT)
	{
		TONOT(REGM, REGM);   //M=M+D
	}
	else if (instruction == NEG)
	{
		TONEG(REGM, REGM);
	}
}

void VMtranslator::writeDoubleArithmetic(EXP instruction)
{
	SETA(SP);                     //@SP
	TOSUB(REGA, REGM, ONE);      //A=M-1
	ASSIGN_D(REGM);               //D=M
	TOSUB(REGA, REGA, ONE);       //A=A-1
	if (instruction == ADD)
	{
		TOADD(REGM, REGM, REGD);   //M=M+D
	}
	else if (instruction == SUB)
	{
		TOSUB(REGM, REGM, REGD);
	}
	else if (instruction == AND)
	{
		TOAND(REGM, REGM, REGD);
	}
	else if (instruction == OR)
	{
		TOOR(REGM, REGM, REGD);
	}
	else if (instruction == EQUAL)
	{
		TOSUB(REGD, REGM, REGD);      //D=M-D
		SETA(Casmcursor + 6);         //如果M=D，D为0则跳至@-1
		JUMPWHEN(REGD, JEQ);         //D:JEQ
		SETA(0);                     //@0
		ASSIGN_D(REGA);              //D=A
		SETA(Casmcursor + 4);                      //跳过为真时情况
		JUMPWHEN(0, JMP);            //0;JMP
		SETA("1");                     //@1
		TONEG(REGD, REGA);              //D=-A
		SETA(SP);                    //@SP
		TOSUB(REGA, REGM, ONE);      //A=M-1
		TOSUB(REGA, REGA, ONE);      //A=A-1
		ASSIGN_M(REGD);           //M=!D
	}
	else if (instruction == GREATER_THAN)
	{
		TOSUB(REGD, REGM, REGD);      //D=M-D
		SETA(Casmcursor + 6);         //如果M>D，D为0则跳至@-1
		JUMPWHEN(REGD, JGT);         //D:JGT
		SETA(0);                     //@0
		ASSIGN_D(REGA);              //D=A
		SETA(Casmcursor + 4);                      //跳过为真时情况
		JUMPWHEN(0, JMP);            //0;JMP
		SETA("1");                     //@1
		TONEG(REGD, REGA);              //D=-A
		SETA(SP);                    //@SP
		TOSUB(REGA, REGM, ONE);      //A=M-1
		TOSUB(REGA, REGA, ONE);      //A=A-1
		ASSIGN_M(REGD);           //M=D
	}
	else if (instruction == LOWER_THAN)
	{
		TOSUB(REGD, REGM, REGD);      //D=M-D
		SETA(Casmcursor + 6);         //如果M<D，D为0则跳至@-1
		JUMPWHEN(REGD, JLT);         //D:JLT
		SETA(0);                     //@0
		ASSIGN_D(REGA);              //D=A
		SETA(Casmcursor + 4);                      //跳过为真时情况
		JUMPWHEN(0, JMP);            //0;JMP
		SETA("1");                     //@1
		TONEG(REGD, REGA);              //D=-A
		SETA(SP);                    //@SP
		TOSUB(REGA, REGM, ONE);      //A=M-1
		TOSUB(REGA, REGA, ONE);      //A=A-1
		ASSIGN_M(REGD);           //M=!D
	}
	TOADD(REGD, REGA, ONE);
	SETA(SP);
	ASSIGN_M(REGD);
}

void VMtranslator::writeLabel(EXP labelname)                   //写Label
{
	EXP asmlabelname= "LABLE_" + labelname;
	vmLableTable.insert(make_pair(labelname,asmlabelname));    //记录LABEL
	TOLABEL(asmlabelname);
}
void VMtranslator::writeGoto(bool conditional, EXP labelname)                  //写goto和if-goto
{
	EXP asmlabelname = "LABLE_" + labelname;
	if (conditional)
	{
		SETA(SP);
		TOSUB(REGAM, REGM, ONE);
		ASSIGN_D(REGM);
		SETA(asmlabelname);
		JUMPWHEN(REGD, JNE);
	}
	else
	{
		SETA(asmlabelname);
		JUMPWHEN(0, JMP);
	}
}

void VMtranslator::writeFunction(EXP Functioname, int nVars)
{
	EXP asmlabelname = "FUNCTION_" + Functioname;
	TOLABEL(asmlabelname);
	SETA(nVars);
	ASSIGN_D(REGA);
	SETA(Casmcursor + 19);
	JUMPWHEN(REGD, JEQ);
	SETA(SP);
	TOADD(REGM, REGM, REGD);
	SETA(R14);
	ASSIGN_M(REGD);
	SETA(LCL);
	ASSIGN_D(REGM);
	SETA(R15);
	ASSIGN_M(REGD);
	SETA(R15);                 //LOOP START;
	ASSIGN_A(REGM);
	ASSIGN_M('0');
	SETA(R15);
	TOADD(REGM, REGM, ONE);
	SETA(R14);
	TOSUB(REGMD, REGM, ONE);
	SETA(Casmcursor-7);
	JUMPWHEN(REGD, JNE);       //LOOP END

}
void VMtranslator::writeCall(EXP Functioname, int nArgs)
{
	EXP asmlabelname = "FUNCTION_" + Functioname;
	writePush(CONSTANT,Casmcursor+98);     //8
	writePush(LCL, 0);               //11
	writePush(ARG, 0);
	writePush(THI, 0);
	writePush(THA, 0);
	writePush(SP, 0);
	writePush(CONSTANT, nArgs+5);
	writeDoubleArithmetic(SUB);     //8
	writePop(ARG, 0);              //13
	SETA(SP);        //4
	ASSIGN_D(REGM);
	SETA(LCL);
	ASSIGN_M(REGD);
	SETA(asmlabelname);
	JUMPWHEN(0, JMP);    //2
}
void VMtranslator::writeReturn(void)
{
	SETA(LCL);
	ASSIGN_D(REGM);
	SETA(R14);
	ASSIGN_M(REGD);
	writePop(ARGUMENT, 0);
	SETA(ARG);
	TOADD(REGD, REGM, ONE);
	SETA(SP);
	ASSIGN_M(REGD);
	SETA(R14);
	TOSUB(REGAM, REGM, ONE);
	ASSIGN_D(REGM);
	SETA(THA);
	ASSIGN_M(REGD);
	SETA(R14);
	TOSUB(REGAM, REGM, ONE);
	ASSIGN_D(REGM);
	SETA(THI);
	ASSIGN_M(REGD);
	SETA(R14);
	TOSUB(REGAM, REGM, ONE);
	ASSIGN_D(REGM);
	SETA(ARG);
	ASSIGN_M(REGD);
	SETA(R14);
	TOSUB(REGAM, REGM, ONE);
	ASSIGN_D(REGM);
	SETA(LCL);
	ASSIGN_M(REGD);
	SETA(R14);
	TOSUB(REGA, REGM, ONE);
	ASSIGN_A(REGM);
	JUMPWHEN(0, JMP);
}

void VMtranslator::writeBootstrapper()
{
	SETA(256);
	ASSIGN_D(REGA);
	SETA(SP);
	ASSIGN_M(REGD);
	SETA(1);
	TONEG(REGD,REGA);
	SETA(LCL);
	ASSIGN_M(REGD);
	SETA(2);
	TONEG(REGD, REGA);
	SETA(ARG);
	ASSIGN_M(REGD);
	SETA(3);
	TONEG(REGD, REGA);
	SETA(THI);
	ASSIGN_M(REGD);
	SETA(4);
	TONEG(REGD, REGA);
	SETA(THA);
	ASSIGN_M(REGD);
	writeCall("Sys.init",0);
}
