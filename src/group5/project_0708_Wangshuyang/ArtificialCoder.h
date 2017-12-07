#pragma once
#include "stdafx.h"

class ArtificialCoder
{
private:
	std::vector<int> listOfInput;    //程序试数列表
	std::vector<int> possibleAdress;    //需要直接调用的内存地址
	std::vector<int> ram;    //内存模拟
	std::vector<std::string> VMCode;
public:
	void getadrs(std::string ch);      //将VMCode中访问到的地址提出来, ch -> Typename+Space+Nunber
	void initialise();	//对输入的VMCoder进行初始化操作
	void search();   //搜索程序
	bool judge();     //判断是否等效
};