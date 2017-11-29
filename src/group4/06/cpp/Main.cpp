#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include "Parser.hpp"
#include "SymbolTable.hpp"
#include "Code.hpp"

void create_predefined_table();
SymbolTable predefined;
SymbolTable memlabel;
string add2bin(int address);

void main()
{
	string infilename, outfilename;
	cout << "Assembler for Nand2Tetris Project 6" << endl;
	cout << "Please enter the full input file name:";
	cin >> infilename;
	cout << "Please enter the full output file name:";
	cin >> outfilename;
	create_predefined_table();
	Parser asblr(infilename);
	ofstream output(outfilename, ios_base::trunc);
	while (asblr.hasMoreCommands() == true)
	{
		asblr.advance();
		switch (asblr.commandType())
		{
		case A_COMMAND:
		{
			string a_symbol = asblr.symbol();
			int add;
			char a_symbol0 = a_symbol[0];
			if (a_symbol0 == '0' | a_symbol0 == '1' | a_symbol0 == '2' | a_symbol0 == '3' | a_symbol0 == '4' |
				a_symbol0 == '5' | a_symbol0 == '6' | a_symbol0 == '7' | a_symbol0 == '8' | a_symbol0 == '9')
			{
				istringstream is(a_symbol);
				is >> add;  //这里没有对混合情况的判定
			}
			else
			{
				if (asblr.pclabel.contains(a_symbol)) { add = asblr.pclabel.GetAddress(a_symbol); }
				else
				{
					if (predefined.contains(a_symbol)) { add = predefined.GetAddress(a_symbol); }
					else
					{
						if (memlabel.contains(a_symbol)) { add = memlabel.GetAddress(a_symbol); }
						else
						{
							memlabel.addEntry(a_symbol, 1);
							add = memlabel.GetAddress(a_symbol);
						}
					}
				}
			}
			string add15 = add2bin(add);
			output << '0' << add15 << '\n';
			break;
		}
		case C_COMMAND:
		{
			Code codecal;
			string dest = codecal.dest(asblr.dest());
			string comp = codecal.comp(asblr.comp());
			string jump = codecal.jump(asblr.jump());
			output<<"111"<<comp<<dest<<jump<<'\n';
			break;
		}
		}
	}
	output.close();
	cout << "Convertion completed!" << endl;
	system("pause");
}

void create_predefined_table()
{
	predefined.addEntry("SP", 0, 0);
	predefined.addEntry("LCL", 0, 1);
	predefined.addEntry("ARG", 0, 2);
	predefined.addEntry("THIS", 0, 3);
	predefined.addEntry("THAT", 0, 4);
	predefined.addEntry("SCREEN", 0, 16384);
	predefined.addEntry("KBD", 0, 24576);
	predefined.addEntry("R0", 0, 0);
	predefined.addEntry("R1", 0, 1);
	predefined.addEntry("R2", 0, 2);
	predefined.addEntry("R3", 0, 3);
	predefined.addEntry("R4", 0, 4);
	predefined.addEntry("R5", 0, 5);
	predefined.addEntry("R6", 0, 6);
	predefined.addEntry("R7", 0, 7);
	predefined.addEntry("R8", 0, 8);
	predefined.addEntry("R9", 0, 9);
	predefined.addEntry("R10", 0, 10);
	predefined.addEntry("R11", 0, 11);
	predefined.addEntry("R12", 0, 12);
	predefined.addEntry("R13", 0, 13);
	predefined.addEntry("R14", 0, 14);
	predefined.addEntry("R15", 0, 15);
	cout << "Predefined table established!" << endl;
}

string add2bin(int address)
{
	string bin = "000000000000000";
	int temp = address;
	int i = 0;
	while (temp != 0)
	{
		if (temp % 2 == 0)bin[14 - i] = '0';
		else bin[14 - i] = '1';
		temp = (int)(temp / 2);
		i++;
	}
	return bin;
}