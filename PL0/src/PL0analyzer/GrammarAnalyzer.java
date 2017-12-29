package PL0analyzer;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

public class GrammarAnalyzer
{
	public LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
	public SymbolTable table = new SymbolTable();
	public Vector<Pcode> codelist = new Vector<>();

	private int num = 0;
	private int level = 0;
	private Stack<Integer> address = new Stack<>();
	private double val;
	private String name = "this line";

	public GrammarAnalyzer()
	{
		address.push(3);
	}

	public void ProgramAnalyze(ArrayList<String> stringArrayList)
	{
		for (String s : stringArrayList)
			lexicalAnalyzer.lineAnalyse(s);
		//System.out.println("Program analyze");
		SubProgram();
		do_dot();
		if (num < lexicalAnalyzer.v.size() - 1)
		{
			error("illegal input");
		}
	}

	// 分程序的分析
	private void SubProgram()
	{
		int status = 0;// 状态机的状态编号，详见文档
		while (num < lexicalAnalyzer.v.size())
		{
			switch (status)
			{
			case 0:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.isTypeEqual(Item.Type.KEYWORD))
				{
					if (item.getValue().equals("const"))
					{
						do_const();
						status = 1;
					}
					else if (item.getValue().equals("var"))
					{
						do_var();
						status = 3;
					}
					else if (item.getValue().equals("procedure"))
					{
						do_procedure();
						status = 5;
					}
					else
					{
						ProgramSentense();
						return;
					}
				}
				else
				{
					ProgramSentense();
					return;
				}
			}
			break;
			case 1:
				do_ident();
				do_equalsign();
				do_number();
				addCONST(name, val);
				status = 2;
				break;
			case 2:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals(","))
				{
					do_comma();
					status = 1;
				}
				else if (item.getValue().equals(";"))
				{
					do_semicolon();
					status = 0;
				}
			}
			break;
			case 3:
			{
				do_ident();
				addVAR(name, level);
				status = 4;
			}
			break;
			case 4:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals(","))
				{
					do_comma();
					status = 3;
				}
				else if (item.getValue().equals(";"))
				{
					do_semicolon();
					status = 0;
				}
			}
			break;
			case 5:
				do_ident();
				do_semicolon();
				addPROCEDURE(name);
				SubProgram();
				do_semicolon();
				status = 0;
				break;
			default:
				error("unknown error");
			}
		}
	}

	// 语句的分析
	private void ProgramSentense()
	{
		int status = 0;
		int addr1 = -1, addr2 = -1;
		while (num < lexicalAnalyzer.v.size())
		{
			switch (status)
			{
			case 0:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.isTypeEqual(Item.Type.IDENTIFIER))
				{
					Symbol symbol = find_ident();
					do_assignmentsymbol();
					ProgramExpression();
					codelist.add(new Pcode(Pcode.PAction.STO, level - symbol.getLevel(), symbol.getAddress()));
					return;
				}
				else if (item.isTypeEqual(Item.Type.KEYWORD))
				{
					if (item.getValue().equals("call"))
					{
						do_call();
						Symbol symbol = find_ident();
						codelist.add(new Pcode(Pcode.PAction.CAL, level - symbol.getLevel(), symbol.getAddress()));
						return;
					}
					else if (item.getValue().equals("begin"))
					{
						do_begin();
						ProgramSentense();
						status = 1;
					}
					else if (item.getValue().equals("if"))
					{
						do_if();
						ProgramCondition();
						do_then();
						int addr = codelist.size();
						Pcode jpc = new Pcode(Pcode.PAction.JPC, 0, 0);
						codelist.add(jpc);
						ProgramSentense();
						jpc.setArg2(codelist.size());
						codelist.set(addr, jpc);
						if (lexicalAnalyzer.v.get(num).getValue().equals("else"))
						{
							addr = codelist.size();
							Pcode jmp = new Pcode(Pcode.PAction.JMP, 0, 0);
							codelist.add(jmp);
							do_else();
							ProgramSentense();
							jmp.setArg2(codelist.size());
							codelist.set(addr, jmp);
						}
						return;
					}
					else if (item.getValue().equals("while"))
					{
						addr1 = codelist.size();
						do_while();
						ProgramCondition();
						addr2 = codelist.size();
						Pcode jpc = new Pcode(Pcode.PAction.JPC, 0, 0);
						codelist.add(jpc);
						do_do();
						ProgramSentense();
						codelist.add(new Pcode(Pcode.PAction.JMP, 0, addr1));
						jpc.setArg2(codelist.size());
						codelist.set(addr2, jpc);
						return;
					}
					else if (item.getValue().equals("read"))
					{
						do_read();
						do_leftparenthese();
						status = 3;
					}
					else if (item.getValue().equals("write"))
					{
						do_write();
						do_leftparenthese();
						status = 5;
					}
					else if (item.getValue().equals("repeat"))
					{
						do_repeat();
						addr1 = codelist.size();
						ProgramSentense();
						status = 8;
					}
					else
						return;
				}
				else
					return;
			}
			break;
			case 1:
			case 2:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals("end"))
				{
					do_end();
					return;
				}
				else if (item.getValue().equals(";"))
				{
					do_semicolon();
					ProgramSentense();
					status = 1;
				}
			}
			break;
			case 3:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.isTypeEqual(Item.Type.IDENTIFIER))
				{
					Symbol symbol = find_ident();
					codelist.add(new Pcode(Pcode.PAction.RED, level-symbol.getLevel(), symbol.getAddress()));
				}
				else
					error("should be number or identifier");
				status = 4;
			}
			break;
			case 4:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals(")"))
				{
					do_rightparenthese();
					return;
				}
				else if (item.getValue().equals(","))
				{
					do_comma();
					status = 3;
				}
			}
			break;
			case 5:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.isTypeEqual(Item.Type.CONSTANT))
				{
					int val = do_number();
					codelist.add(new Pcode(Pcode.PAction.LIT, 0, val));
					codelist.add(new Pcode(Pcode.PAction.WRT, 0, 0));
				}
				else if (item.isTypeEqual(Item.Type.IDENTIFIER))
				{
					Symbol symbol = find_ident();
					codelist.add(new Pcode(Pcode.PAction.LOD, level-symbol.getLevel(), symbol.getAddress()));
					codelist.add(new Pcode(Pcode.PAction.WRT, 0, 0));
				}
				else
					error("should be number or identifier");
				status = 6;
			}
			break;
			case 6:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals(")"))
				{
					do_rightparenthese();
					return;
				}
				else if (item.getValue().equals(","))
				{
					do_comma();
					status = 5;
				}
			}
			break;
			case 8:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals("until"))
				{
					do_until();
					addr2 = codelist.size();
					ProgramCondition();
					codelist.add(new Pcode(Pcode.PAction.JPC, 0, addr1));
				}
				else if (item.getValue().equals(";"))
				{
					do_semicolon();
					ProgramSentense();
					status = 8;
				}
			}
			break;
			default:
				return;
			}
		}
	}

	// 条件的分析
	private void ProgramCondition()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.getValue().equals("odd"))
		{
			do_odd();
			ProgramExpression();
			codelist.add(new Pcode(Pcode.PAction.OPR, 0, 6));
		}
		else
		{
			ProgramExpression();
			int op_num = do_relationOp();
			ProgramExpression();
			codelist.add(new Pcode(Pcode.PAction.OPR, 0, op_num));
		}
	}

	// 表达式的分析
	private void ProgramExpression()
	{
		int status = 0;
		while (num < lexicalAnalyzer.v.size())
		{
			switch (status)
			{
			case 0:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals("+"))
				{
					do_plussign();
					codelist.add(new Pcode(Pcode.PAction.LIT, 0, 0));
					ProgramItem();
					codelist.add(new Pcode(Pcode.PAction.OPR, 0, 1));
				}
				else if (item.getValue().equals("-"))
				{
					do_subsign();
					codelist.add(new Pcode(Pcode.PAction.LIT, 0, 0));
					ProgramItem();
					codelist.add(new Pcode(Pcode.PAction.OPR, 0, 2));
				}
				else
					ProgramItem();
				status = 1;
			}
			break;
			case 1:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals("+"))
				{
					do_plussign();
					ProgramItem();
					codelist.add(new Pcode(Pcode.PAction.OPR, 0, 1));
				}
				else if (item.getValue().equals("-"))
				{
					do_subsign();
					ProgramItem();
					codelist.add(new Pcode(Pcode.PAction.OPR, 0, 2));
				}
				else
					return;
			}
			break;
			default:
				return;
			}
		}
	}

	// 项的分析
	private void ProgramItem()
	{
		int status = 0;
		while (num < lexicalAnalyzer.v.size())
		{
			switch (status)
			{
			case 0:
				ProgramFactor();
				status = 1;
				break;
			case 1:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue().equals("*"))
				{
					do_multsign();
					ProgramFactor();
					codelist.add(new Pcode(Pcode.PAction.OPR, 0, 3));
				}
				else if (item.getValue().equals("/"))
				{
					do_divsign();
					ProgramFactor();
					codelist.add(new Pcode(Pcode.PAction.OPR, 0, 4));
				}
				else
					return;
			}
			}
		}
	}

	// 因子的分析
	private void ProgramFactor()
	{
		Item item = lexicalAnalyzer.v.get(num);
		// 向codelist添加变量（const和var）
		if (item.isTypeEqual(Item.Type.IDENTIFIER))
		{
			Symbol symbol = find_ident();
			codelist.add(new Pcode(Pcode.PAction.LOD, level - symbol.getLevel(), symbol.getAddress()));
		}
		// 向codelist添加常量（数字）
		else if (item.isTypeEqual(Item.Type.CONSTANT))
		{
			do_number();
			codelist.add(new Pcode(Pcode.PAction.LIT, 0, Integer.valueOf(item.getValue())));
		}
		else
		{
			do_leftparenthese();
			ProgramExpression();
			do_rightparenthese();
		}
	}

	private void error(String s) throws GrammarException
	{
		throw new GrammarException(s);
	}

	private void readsym()
	{
		num++;
	}

	// 检查要处理的字符是否符合语法规则
	private void checksym(String s)
	{
		if (num >= lexicalAnalyzer.v.size())
		{
			error("missing " + s);
		}
		if (!lexicalAnalyzer.v.get(num).getValue().equals(s))
		{
			error("should be " + s);
		}
	}

	private void do_const()
	{
		checksym("const");
		readsym();
	}

	private void do_var()
	{
		checksym("var");
		readsym();
	}

	private void do_procedure()
	{
		checksym("procedure");
		readsym();
	}

	private void do_call()
	{
		checksym("call");
		readsym();
	}

	private void do_begin()
	{
		checksym("begin");
		level++;
		address.push(3);
		readsym();
	}

	private void do_end()
	{
		checksym("end");
		level--;
		address.pop();
		readsym();
	}

	private void do_if()
	{
		checksym("if");
		readsym();
	}

	private void do_then()
	{
		checksym("then");
		readsym();
	}

	private void do_else()
	{
		checksym("else");
		readsym();
	}

	private void do_while()
	{
		checksym("while");
		readsym();
	}

	private void do_do()
	{
		checksym("do");
		readsym();
	}

	private void do_read()
	{
		checksym("read");
		readsym();
	}

	private void do_write()
	{
		checksym("write");
		readsym();
	}

	private void do_odd()
	{
		checksym("odd");
		readsym();
	}

	// 新的标识符
	private void do_ident()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.IDENTIFIER))
		{
			error("should be identifier");
		}
		name = item.getValue();
		readsym();
	}

	// 已知的标识符
	private Symbol find_ident()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.IDENTIFIER))
		{
			error("should be identifier");
		}
		name = item.getValue();
		if (table.table.isEmpty())
			error(name + " is undefined identifier");
		int level = table.table.lastElement().getLevel();
		for (int i = table.table.size() - 1; i >= 0; i--)
		{
			Symbol symbol = table.table.get(i);
			// 只关注同level或更靠前level的变量
			if (level > symbol.getLevel())
				level = symbol.getLevel();
			if (level >= symbol.getLevel() && symbol.getName().equals(name))
			{
				readsym();
				return symbol;
			}
		}
		error(name + " is undefined identifier");
		return null;
	}

	// 等号的处理
	private void do_equalsign()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.SINGLE_OPERATOR))
		{
			error("should be single operator");
		}
		checksym("=");
		readsym();
	}

	// 数字的处理
	private int do_number()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.CONSTANT))
		{
			error(name + " should be constant");
		}
		readsym();
		return (int)Integer.parseInt(item.getValue());
	}

	// 点的处理
	private void do_dot()
	{
		checksym(".");
		readsym();
	}

	// 逗号的处理
	private void do_comma()
	{
		checksym(",");
		readsym();
	}

	// 分号的处理
	private void do_semicolon()
	{
		checksym(";");
		readsym();
	}

	// 赋值符号的处理
	private void do_assignmentsymbol()
	{
		checksym(":=");
		readsym();
	}

	// 左括号的处理
	private void do_leftparenthese()
	{
		checksym("(");
		readsym();
	}

	// 右括号的处理
	private void do_rightparenthese()
	{
		checksym(")");
		readsym();
	}

	// 关系运算符的处理
	private int do_relationOp()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.isTypeEqual(Item.Type.SINGLE_OPERATOR))
		{
			if (item.getValue().equals("=") || item.getValue().equals("<") || item.getValue().equals(">"))
			{
				readsym();
			}
			if (item.getValue().equals("="))
				return 8;
			if (item.getValue().equals("<"))
				return 10;
			if (item.getValue().equals(">"))
				return 12;
		}
		else if (item.isTypeEqual(Item.Type.BINARY_OPERATOR))
		{
			if (item.getValue().equals("<>") || item.getValue().equals("<=") || item.getValue().equals(">="))
			{
				readsym();
			}
			if (item.getValue().equals("<>"))
				return 9;
			if (item.getValue().equals("<="))
				return 11;
			if (item.getValue().equals(">="))
				return 13;
		}
		else
			error("should be relation operator");
		return -1;
	}

	// 加号的处理
	private void do_plussign()
	{
		checksym("+");
		readsym();
	}

	// 减号的处理
	private void do_subsign()
	{
		checksym("-");
		readsym();
	}

	// 乘号的处理
	private void do_multsign()
	{
		checksym("*");
		readsym();
	}

	// 除号的处理
	private void do_divsign()
	{
		checksym("/");
		readsym();
	}

	private void do_repeat()
	{
		checksym("repeat");
		readsym();
	}

	private void do_until()
	{
		checksym("until");
		readsym();
	}

	private void addCONST(String name, double value)
	{
		int addr = address.pop() + 1;
		table.addCONST(name, level, value, addr);
		address.push(addr);
	}

	private void addVAR(String name, double value)
	{
		int addr = address.pop() + 1;
		table.addVAR(name, level, addr);
		address.push(addr);
	}

	private void addPROCEDURE(String name)
	{
		int addr = address.pop() + 1;
		table.addPROCEDURE(name, level, addr);
		address.push(addr);
	}
}