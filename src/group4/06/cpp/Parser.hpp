#ifndef N2T_ASSEMBLER_PARSER
#define N2T_ASSEMBLER_PARSER
#include <fstream>
#include <string>
#include <iostream>
#include "SymbolTable.hpp"

using namespace std;

enum command_type { A_COMMAND, C_COMMAND, L_COMMAND, NULL_COMMAND};

class Parser
{
public:
	Parser(string filename);
	bool hasMoreCommands();
	void advance();
	command_type commandType();
	string symbol();
	string dest();
	string comp();
	string jump();
	SymbolTable pclabel;
private:
	int file_start = 1;
	int equal_pos, semi_pos;
	string cur_line;
	unsigned int psr_line = 0;
	ofstream psr_temp_file;
	ifstream psr_in_file;
	char getcharnotspace();
	char getcharnotcommented();
	int c_parsed = 0;
	void c_parse();
	string str_dest, str_comp, str_jump;
	command_type type = NULL_COMMAND;
};

Parser::Parser(string filename)
{
	char cur = 0;
	psr_temp_file.open("nocomment.txt", ios_base::trunc);
	psr_in_file.open(filename);
	while (1)
	{
		cur = getcharnotcommented();
		if (cur == EOF)break;
		psr_temp_file.put(cur);
	}
	psr_temp_file.close();
	psr_in_file.close();
	cout << "Initialize completed!" << endl;
	psr_temp_file.open("nolabel.txt", ios_base::trunc);
	psr_in_file.open("nocomment.txt");
	string line_string;
	while (!psr_in_file.eof())
	{
		getline(psr_in_file, line_string);
		if (line_string.empty())continue;
		if (line_string[0] == '(')
		{
			string symbolname = line_string.substr(1, line_string.length() - 2);
			pclabel.addEntry(symbolname, 0, psr_line+1);
			continue;
		}
		if (file_start == 1)
		{
			psr_temp_file << line_string;
			file_start = 0;
		}
		else
		{
			psr_temp_file << '\n' << line_string;
			psr_line++;
		}
	}
	psr_temp_file.close();
	psr_in_file.close();
	cout << "SymbolTablePC established!" << endl;
	psr_in_file.open("nolabel.txt");
	/*for (int i = 0; i < 100; i++)
	{
		getline(psr_in_file, cur_line);
		c_parse();
	}
	pclabel.TestST();
	system("pause");*/
}

bool Parser::hasMoreCommands()
{
	if (psr_in_file.get() == EOF)
	{
		return false;
	}
	psr_in_file.seekg(-1, ios_base::cur);
	return true;
}

void Parser::advance()
{
	getline(psr_in_file, cur_line);
	c_parsed = 0;
}

command_type Parser::commandType()
{
	if (cur_line[0] == '@')type = A_COMMAND;
	else type = C_COMMAND;
	return type;
}

string Parser::symbol()
{
	if (type != A_COMMAND)
	{
		cout << "Function 'symbol()' cannot be used when type is not A_COMMAND or undefined!" << endl;
		system("pause");
		exit(0);
	}
	return cur_line.substr(1, cur_line.length() - 1);
}

string Parser::dest()
{
	if (type != C_COMMAND)
	{
		cout << "Function 'dest()' cannot be used when type is not C_COMMAND or undefined!" << endl;
		system("pause");
		exit(0);
	}
	if (c_parsed == 0)c_parse();
	return str_dest;
}

string Parser::comp()
{
	if (type != C_COMMAND)
	{
		cout << "Function 'comp()' cannot be used when type is not C_COMMAND or undefined!" << endl;
		system("pause");
		exit(0);
	}
	if (c_parsed == 0)c_parse();
	return str_comp;
}

string Parser::jump()
{
	if (type != C_COMMAND)
	{
		cout << "Function 'jump()' cannot be used when type is not C_COMMAND or undefined!" << endl;
		system("pause");
		exit(0);
	}
	if (c_parsed == 0)c_parse();
	return str_jump;
}

char Parser::getcharnotspace()
{
	char cur = ' ';
	while (cur == ' ')
	{
		cur = psr_in_file.get();
	}
	return cur;
}

char Parser::getcharnotcommented()
{
	int num = 0;
	char cur;
	while (1)
	{
		cur = getcharnotspace();
		if (num == 1)
		{
			if (cur != '/')
			{
				psr_in_file.seekg(-1, ios_base::cur);
				return '/';
			}
			if (cur == '/')
			{
				while ((cur != '\n') & (cur != EOF))
				{
					cur = getcharnotspace();
				}
				return cur;
				//cur = getcharnotspace();
				//num = 0;
			}
		}
		if (num == 0 && cur == '/')
		{
			num = 1;
		}
		if (cur != '/')
		{
			return cur;
		}
	}
}

void Parser::c_parse()
{
	int leng = cur_line.length();
	for (equal_pos = 1; equal_pos < leng; equal_pos++)
	{
		if (cur_line[equal_pos] == '=')
		{
			str_dest = cur_line.substr(0, equal_pos);
			break;
		}
	}
	if (equal_pos == leng)
	{
		str_dest = "";
		equal_pos = -1;
	}
	for (semi_pos = equal_pos+2; semi_pos < leng; semi_pos++)
	{
		if (cur_line[semi_pos] == ';')
		{
			break;
		}
	}
	str_comp = cur_line.substr(equal_pos + 1, semi_pos - equal_pos - 1);
	if (semi_pos != leng)
	{
		str_jump = cur_line.substr(semi_pos + 1, leng - semi_pos - 1);
	}
	else str_jump = "";
	c_parsed = 1;
	//cout << "dest:" << str_dest << "   " << "comp:" << str_comp << "   " << "jump:" << str_jump << endl;
}

#endif