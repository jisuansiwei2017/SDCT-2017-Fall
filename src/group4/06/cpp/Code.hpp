#ifndef N2T_ASSEMBLER_CODE
#define N2T_ASSEMBLER_CODE
#include <string>
#include <iostream>

using namespace std;

class Code
{
public:
	string dest(string mnemonic);
	string comp(string mnemonic);
	string jump(string mnemonic);
private:
	string dest_table[8][2] = { "","000","M","001","D","010","MD","011","A","100","AM","101","AD","110","AMD","111" };
	string comp_table[28][2] = {"0","0101010", "1", "0111111", "-1", "0111010", "D", "0001100", "A", "0110000", "!D", "0001101", "!A", "0110001", 
		"-D", "0001111", "-A", "0110011", "D+1", "0011111", "A+1", "0110111", "D-1", "0001110", "A-1", "0110010", "D+A", "0000010", 
		"D-A", "0010011", "A-D", "0000111", "D&A", "0000000", "D|A", "0010101", "M", "1110000", "!M", "1110001", "-M", "1110011", 
		"M+1", "1110111", "M-1", "1110010", "D+M", "1000010", "D-M", "1010011", "M-D", "1000111", "D&M", "1000000", "D|M", "1010101", };
	string jump_table[8][2] = { "","000","JGT","001","JEQ","010","JGE","011","JLT","100","JNE","101","JLE","110","JMP","111" };
};

string Code::dest(string mnemonic)
{
	for (int i = 0; i < 8; i++)
	{
		if (mnemonic == dest_table[i][0])return dest_table[i][1];
	}
	cout << "Code error:dest value not right!" << endl;
	system("pause");
	exit(0);
}

string Code::comp(string mnemonic)
{
	for (int i = 0; i < 28; i++)
	{
		if (mnemonic == comp_table[i][0])return comp_table[i][1];
	}
	cout << "Code error:comp value not right!" << endl;
	system("pause");
	exit(0);
}

string Code::jump(string mnemonic)
{
	for (int i = 0; i < 8; i++)
	{
		if (mnemonic == jump_table[i][0])return jump_table[i][1];
	}
	cout << "Code error:jump value not right!" << endl;
	system("pause");
	exit(0);
}

#endif