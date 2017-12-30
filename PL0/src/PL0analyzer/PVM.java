package PL0analyzer;

import java.util.*;

/*
* Pcode虚拟机，内含Pcode和符号表
* 如果有时间，将补充Pcode的运行
* */
public class PVM
{
	public Vector<Pcode> codelist;
	private SymbolTable symbolTable;

	public PVM(Vector<Pcode> list, SymbolTable s)
	{
		codelist = list;
		symbolTable = s;
	}
} 