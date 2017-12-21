package PL0analyzer;

public class GrammarAnalyzer
{
	public LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
	public SymbolTable table = new SymbolTable();

	private int num = 0;
	private int level = 0;
	private int address = 0;
	private int val;
	private String name = "this line";

	public void ProgramAnalyze(String s)
	{
		lexicalAnalyzer.lineAnalyse(s);
		SubProgram();
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
						status = 1;
					}
					else if (item.getValue() == "var")
					{
						status = 3;
					}
					else if (item.getValue() == "procedure")
					{
						status = 5;
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
			default:
				error("unknown error");
			}
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

	private void do_const()
	{
		readsym();
	}

	private void do_ident()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.IDENTIFIER))
		{
			error(name + "should be identifier");
		}
		name = item.getValue();
		readsym();
	}

	// 等号的处理
	private void do_equalsign()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (!item.isTypeEqual(Item.Type.SINGLE_OPERATOR))
		{
			error(name + "should be single operator");
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
			error(name + "should be ,");
		}
		readsym();
	}

	// 分号的处理
	private void do_semicolon()
	{
		Item item = lexicalAnalyzer.v.get(num);
		if (item.getValue() != ";")
		{
			error(name + "should be ;");
		}
		readsym();
	}
}