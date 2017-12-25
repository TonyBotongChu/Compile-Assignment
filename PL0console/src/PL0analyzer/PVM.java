package PL0analyzer;

import java.util.*;

public class PVM
{
	public Vector<Pcode> codelist;
	private SymbolTable symbolTable;
	private Stack<Double> stack = new Stack<>();

	public PVM(Vector<Pcode> list, SymbolTable s)
	{
		codelist = list;
		symbolTable = s;
	}
} 