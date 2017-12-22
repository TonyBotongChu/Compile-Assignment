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

	private void error(String s) throws GrammarException
	{
		throw new GrammarException(s);
	}

	/*
	 *登录常量进符号表
     * 参数：
     * name：常量名
     * level：所在层次
     * value：值
     * address：相对于所在层次基地址的地址
     */
	public void addCONST(String name, int level, double value, int address)
	{
		if(existIDENTIFIER_thislevel(name, level))
		{
			error("identifier already exists");
		}
		Symbol symbol = new Symbol();
		symbol.setName(name);
		symbol.setLevel(level);
		symbol.setValue(value);
		symbol.setAddress(address);
		symbol.setType(Symbol.SymbolType.CONST);
		symbol.setSize(4);
		table.add(symbol);
		String str = symbol.getName() + " " + symbol.SymbolTypeString() + " " + symbol.getValue();
		System.out.println(str);
	}

	/*
     *  登录变量进符号表
     *  参数同上
     *  说明：由于登录符号表操作都是在变量声明或常量声明或过程声明中调用，
     *  而PL/0不支持变量声明时赋值，所以不传入参数value
     */
	public void addVAR(String name, int level, int address)
	{
		if(existIDENTIFIER_thislevel(name, level))
		{
			error("identifier already exists");
		}
		Symbol symbol = new Symbol();
		symbol.setName(name);
		symbol.setLevel(level);
		symbol.setAddress(address);
		symbol.setType(Symbol.SymbolType.VAR);
		symbol.setSize(0);
		table.add(symbol);
		String str = symbol.getName() + " " + symbol.SymbolTypeString() + " " + symbol.getValue();
		System.out.println(str);
	}

/*
	public void setVAR(String name, double value)
	{
		int level;
		for (int i = table.size()-1; i >= 0; i--)
		{
			Symbol symbol = table.get(i);
			// 只关注同level或更靠前level的变量
			if (i == table.size()-1 || level > symbol.getLevel()))
				level = symbol.getLevel();
			if (level >= symbol.getLevel())
			{
				if (symbol.getType() == Symbol.SymbolType.VAR && symbol.getName() == name)
				{
					symbol.setValue(value);
					table.set(i, symbol);
				}
			}
		}
	}
	*/

	/*
	 * 登录过程进符号表，参数同上
	 */
	public void addPROCEDURE(String name, int level, int address)
	{
		if(existIDENTIFIER_thislevel(name, level))
		{
			error("identifier already exists");
		}
		Symbol symbol = new Symbol();
		symbol.setName(name);
		symbol.setLevel(level);
		symbol.setAddress(address);
		symbol.setType(Symbol.SymbolType.PROCEDURE);
		symbol.setSize(0);
		table.add(symbol);
		String str = symbol.getName() + " " + symbol.SymbolTypeString() + " " + symbol.getValue();
		System.out.println(str);
	}

	public boolean existIDENTIFIER_thislevel(String name, int level)
	{
		for (int i = table.size() - 1; i >= 0; i--)
		{
			Symbol symbol = table.get(i);
			if(symbol.getLevel() != level)
				break;
			if(symbol.getName() == name)
			{
				return true;
			}
		}
		return false;
	}

	public ArrayList<String> toStringArrayList()
	{
		ArrayList<String> stringArrayList = new ArrayList<>();
		for(Symbol symbol : table)
		{
			String str = symbol.getName() + " " + symbol.SymbolTypeString() + " " + symbol.getValue();
			stringArrayList.add(str);
		}
		return stringArrayList;
	}
}
