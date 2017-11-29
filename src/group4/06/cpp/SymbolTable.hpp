#ifndef N2T_ASSEMBLER_SYMBOLTABLE
#define N2T_ASSEMBLER_SYMBOLTABLE
#include <string>
//#include <iostream>

using namespace std;

class SymbolTable
{
public:
	SymbolTable();
	void addEntry(string symbol, int mod, int address = 0);
	bool contains(string symbol);
	int GetAddress(string symbol);
	//void TestST();
private:
	struct st_stat_line
	{
		string symbol;
		int address;
	};
	struct p_st_linked_list;
	struct p_st_node;
	typedef p_st_linked_list* st_linked_list;
	typedef p_st_node* st_node;
	struct p_st_linked_list
	{
		unsigned int st_size;
		st_node head, tail;
	};
	struct p_st_node
	{
		st_stat_line data;
		st_node next;
	};
	st_linked_list p;
};

SymbolTable::SymbolTable()
{
	p = new p_st_linked_list;
	p->st_size = 0;
	p->head = NULL;
	p->tail = NULL;
}

void SymbolTable::addEntry(string symbol, int mod, int address )
{
	st_node t = new p_st_node;
	t->data.symbol = symbol;
	t->next = NULL;
	if (mod == 0)  //解决带括号的代码位置标记
	{
		t->data.address = address;
	}
	else  //用户自定义地址名
	{
		t->data.address = 16 + p->st_size;
	}
	if (!(p->head))
	{
		p->head = t;
		p->tail = t;
	}
	else
	{
		p->tail->next = t;
		p->tail = t;
	}
	p->st_size++;
}

bool SymbolTable::contains(string symbol)
{
	st_node t = p->head;
	while (t)
	{
		if (symbol == t->data.symbol)return true;
		t = t->next;
	}
	return false;
}

int SymbolTable::GetAddress(string symbol)
{
	st_node t = p->head;
	while (t)
	{
		if (symbol == t->data.symbol)return t->data.address;
		t = t->next;
	}
}

//void SymbolTable::TestST()
//{
//	st_node t = p->head;
//	while (t)
//	{
//		cout << "Symbol:" << t->data.symbol << endl << "Address:" << t->data.address << endl;
//		t = t->next;
//	}
//}

#endif