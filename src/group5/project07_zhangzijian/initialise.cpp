#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cmath>
#include <algorithm>
#include <iostream>
#include <vector>
#include <string>

using namespace std;

class ArtificialCoder
{

private:
	vector<int> listOfInput;
	vector<int> possibleAdress;
	vector<int> ram;
	vector<string> VMCode;

public:
	int Stringtoint(string ch)
	{
		int len=ch.length();
		int ret=0;
		for(int i=0;i<len;i++)
			ret=ret*10+ch[i]-'0';
		return 0;
	}
	
	void Getadrs(string ch)
	{
		int type=0,Place=0,len=ch.length();
		if(ch.substr(0,8)=="constant") type=-1,Place=9;
		if(ch.substr(0,5)=="local") type=1,Place=6;
		if(ch.substr(0,8)=="argument") type=2,Place=9;
		if(ch.substr(0,4)=="this") type=3,Place=5;
		if(ch.substr(0,4)=="that") type=4,Place=5;
		if(ch.substr(0,4)=="temp") type=5,Place=5;
		if(ch.substr(0,7)=="pointer") type=6,Place=8;
		if(ch.substr(0,6)=="static") type=7,Place=7;
		int rk=Stringtoint(ch.substr(Place,len));
		int val;
		
		if(type==-1) val=rk;
		else if(type==0) val=ram[0];
		else if(type==6) val=3+rk;
		else if(type==7) val=16+rk;
		else if(type==5) val=5+rk;
		else val=ram[type]+rk;
		listOfInput.push_back(val);
		if(type!=-1) possibleAdress.push_back(val);
	}
	
	void Initialise()
	{
		int Size=VMCode.size();
		for(int i=0;i<Size;i++)
		{
			string ch=VMCode[i];
			int len=ch.length();
			if(ch.substr(0,4)=="push" || ch.substr(0,3)=="pop")
			{
				if(ch[1]=='u')
					Getadrs(ch.substr(5,len));
				else Getadrs(ch.substr(4,len));
			}
		}
		return;
	}
};

int main()
{
	return 0;
}
