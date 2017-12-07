#pragma once
#include "stdafx.h"

class CPUEmulator {
private:
	char name[100] = "\0";
	int N;
	std::vector <std::string> Centence;
	int A = 0;
	int D = 0;
	std::string LOOP;
	int RAM[100000] = { 0 };
	int Cur = 0;
	std::map <std::string, int> G;
public:
	int Read10(std::string cnt);
	int M()
	{
		return RAM[A];
	}
	void A_instruction(std::string cnt);
	int judgetype(std::string cnt);
	int getval(std::string cnt);
	void C_instruction0(std::string cnt);
	void C_instruction(std::string cnt);
	void C_instruction1(string cnt);
	void Jump()
	{
		Cur = G[LOOP];
		return;
	}
	void Work();
};

