package PL0analyzer;

import java.util.*;

/*
 * 本模块部分参考了某位不知姓名的学长的作业
 * 原地址 http://www.cnblogs.com/hf-z/p/5542070.html
 * 谨在此向原作者表示感谢
 */

public class SymbolTable
{
	public Vector<Symbol> table = new Vector<>();

	/*
	 *登录常量进符号表
     * 参数：
     * name：常量名
     * level：所在层次
     * value：值
     * address：相对于所在层次基地址的地址
     */
	public void addCONST(String name, int level, int value, int address)
	{
		Symbol symbol = new Symbol();
		symbol.setName(name);
		symbol.setLevel(level);
		symbol.setValue(value);
		symbol.setAddress(address);
		symbol.setType(Symbol.SymbolType.CONST);
		symbol.setSize(4);
		table.add(symbol);
	}

	/*
     *  登录变量进符号表
     *  参数同上
     *  说明：由于登录符号表操作都是在变量声明或常量声明或过程声明中调用，
     *  而PL/0不支持变量声明时赋值，所以不传入参数value
     */
	public void addVAR(String name, int level, int address)
	{
		Symbol symbol = new Symbol();
		symbol.setName(name);
		symbol.setLevel(level);
		symbol.setAddress(address);
		symbol.setType(Symbol.SymbolType.VAR);
		symbol.setSize(0);
		table.add(symbol);
	}

	/*
	 * 登录过程进符号表，参数同上
	 */
	public void addPROCEDURE(String name, int level, int address)
	{
		Symbol symbol = new Symbol();
		symbol.setName(name);
		symbol.setLevel(level);
		symbol.setAddress(address);
		symbol.setType(Symbol.SymbolType.PROCEDURE);
		symbol.setSize(0);
		table.add(symbol);
	}
}
