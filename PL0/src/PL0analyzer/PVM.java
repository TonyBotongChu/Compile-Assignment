package PL0analyzer;

import java.util.*;

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