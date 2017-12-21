package PL0analyzer;

import java.util.Vector;

public class GrammarAnalyzer
{
	public LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
	public SymbolTable table = new SymbolTable();
	public Vector<Pcode> pcodeList = new Vector<>();

	private int num = 0;
	private int level = 0;
	private int address = 0;
	private double val;
	private String name = "this line";

	public void ProgramAnalyze(String s)
	{
		lexicalAnalyzer.lineAnalyse(s);
		SubProgram();
		if (num < lexicalAnalyzer.v.size())
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
					if (item.getValue() == "const")
					{
						do_const();
						status = 1;
					}
					else if (item.getValue() == "var")
					{
						do_var();
						status = 3;
					}
					else if (item.getValue() == "procedure")
					{
						do_procedure();
						status = 5;
					}
					else
					{
						ProgramSentense();
					}
				}
			}
			break;
			case 1:
				do_ident();
				do_equalsign();
				do_number();
				table.addCONST(name, level, val, address);
				status = 2;
				break;
			case 2:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue() == ",")
				{
					do_comma();
					status = 1;
				}
				else if (item.getValue() == ";")
				{
					do_semicolon();
					status = 0;
				}
			}
			break;
			case 3:
			{
				do_ident();
				table.addVAR(name, level, address);
				status = 4;
			}
			break;
			case 4:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue() == ",")
				{
					do_comma();
					status = 3;
				}
				else if (item.getValue() == ";")
				{
					do_semicolon();
					status = 0;
				}
			}
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
		while (num < lexicalAnalyzer.v.size())
		{
			switch (status)
			{
			case 0:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.isTypeEqual(Item.Type.IDENTIFIER))
				{
					find_ident();
					do_assignmentsymbol();
					ProgramExpression();
				}
				else if (item.isTypeEqual(Item.Type.KEYWORD))
				{
					if (item.getValue() == "call")
					{
						do_call();
						find_ident();
					}
					else if (item.getValue() == "begin")
					{
						do_begin();
						ProgramSentense();
						status = 1;
					}
					else if (item.getValue() == "if")
					{
						do_if();
						ProgramCondition();
						do_then();
						ProgramSentense();
					}
					else if (item.getValue() == "while")
					{
						do_while();
						ProgramCondition();
						do_do();
						ProgramSentense();
					}
					else if (item.getValue() == "read")
					{
						do_read();
						do_leftparenthese();
						status = 3;
					}
					else if (item.getValue() == "write")
					{
						do_write();
						do_leftparenthese();
						status = 5;
					}
				}
			}
			break;
			case 1:
			case 2:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue() == "end")
				{
					do_end();
				}
				else if (item.getValue() == ";")
				{
					do_semicolon();
					ProgramSentense();
					status = 1;
				}
			}
			break;
			case 3:
			{
				find_ident();
				status = 4;
			}
			break;
			case 4:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue() == ")")
				{
					do_rightparenthese();
				}
				else if (item.getValue() == ",")
				{
					do_comma();
					status = 3;
				}
			}
			break;
			case 5:
			{
				find_ident();
				status = 4;
			}
			break;
			case 6:
			{
				Item item = lexicalAnalyzer.v.get(num);
				if (item.getValue() == ")")
				{
					do_rightparenthese();
				}
				else if (item.getValue() == ",")
				{
					do_comma();
					status = 5;
				}
			}
			break;
			default:
				return;
			}
		}
	}

	// 表达式的分析
	private void ProgramExpression()
	{
	}

	// 条件的分析
	private void ProgramCondition()
	{
	}

	private void error(String s) throws GrammarException
	{
		throw new GrammarException(s);
	}

	private void readsym()
	{
		num++;
	}

	private void do_const()
	{
		readsym();
	}

	private void do_var()
	{
		readsym();
	}

	private void do_procedure()
	{
		readsym();
	}

	private void do_call()
	{
		readsym();
	}

	private void do_begin()
	{
		level++;
		readsym();
	}

	private void do_end()
	{
		level--;
		readsym();
	}

	private void do_if()
	{
		readsym();
	}

	private void do_then()
	{
		readsym();
	}

	private void do_while()
	{
		readsym();
	}

	private void do_do()
	{
		readsym();
	}

	private void do_read()
	{
		readsym();
	}

	private void do_write()
	{
		readsym();
	}

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

	private boolean find_ident()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.IDENTIFIER))
		{
			error("should be identifier");
		}
		name = item.getValue();
		int level = table.table.lastElement().getLevel();
		for (int i = table.table.size() - 1; i >= 0; i--)
		{
			Symbol symbol = table.table.get(i);
			// 只关注同level或更靠前level的变量
			if (level > symbol.getLevel())
				level = symbol.getLevel();
			if (level >= symbol.getLevel() && symbol.getName() == name)
			{
				return true;
			}
		}
		error("undefined identifier");
		return false;
	}

	// 等号的处理
	private void do_equalsign()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.SINGLE_OPERATOR))
		{
			error("should be single operator");
		}
		if (item.getValue() != "=")
		{
			error("should be =");
		}
		readsym();
	}

	// 数字的处理
	private void do_number()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.CONSTANT))
		{
			error(name + " should be constant");
		}
		val = Integer.parseInt(item.getValue());
		readsym();
	}

	// 逗号的处理
	private void do_comma()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.getValue() != ",")
		{
			error("should be ,");
		}
		readsym();
	}

	// 分号的处理
	private void do_semicolon()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.getValue() != ";")
		{
			error("should be ;");
		}
		readsym();
	}

	// 赋值符号的处理
	private void do_assignmentsymbol()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.getValue() != ":=")
		{
			error("should be :=");
		}
		readsym();
	}

	// 左括号的处理
	private void do_leftparenthese()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.getValue() != "(")
		{
			error("should be (");
		}
		readsym();
	}

	// 右括号的处理
	private void do_rightparenthese()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.getValue() != ")")
		{
			error("should be )");
		}
		readsym();
	}

	// 关系运算符的处理
	private void do_relationOp()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.isTypeEqual(Item.Type.SINGLE_OPERATOR))
		{
			if (item.getValue() == "=" || item.getValue() == "<" || item.getValue() == ">")
			{
				readsym();
			}
		}
		else if (item.isTypeEqual(Item.Type.BINARY_OPERATOR))
		{
			if (item.getValue() == "<>" || item.getValue() == "<=" || item.getValue() == ">=")
			{
				readsym();
			}
		}
		else
			error("should be relation operator");
	}
}