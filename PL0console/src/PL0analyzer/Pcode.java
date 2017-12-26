package PL0analyzer;

public class Pcode
{
	public enum PAction
	{
		/*
		LDA(0),
		LOD(1),
		LDI(2),
		DIS(3),
		FCT(8),
		INT(9),//将栈顶元素加上y
		JMP(10),//无条件转移到y
		JPC(11),
		CAL(19),//调用用户过程或函数
		RED(27),//读（y表示类型，1：整型，2：实型，3：字符型）
		HLT(31),//停止
		MUS(36),//求负
		ORR(51),//逻辑或
		ADD(52),//整型加
		SUB(53),//整型减
		ADR(54),//实型加
		SUR(55),//实型减
		AND(56),//逻辑与
		MUL(57),//整型乘
		DIV(58),//实型除
		MUR(60),//实型乘
		DIR(61),//实型除
		RDL(62),//readln（读完一行）
		WRL(63);//writeln（换行写）
		*/

		LIT,//0 , a 取常量a放入数据栈栈顶
		OPR,//0 , a 执行运算，a表示执行某种运算
		// 1+, 2-, 3*, 4/, 6odd, 8=, 9<>, 10<, 11<=, 12>, 13>=
		LOD,//L ，a 取变量（相对地址为a，层差为L）放到数据栈的栈顶
		STO,//L ，a 将数据栈栈顶的内容存入变量（相对地址为a，层次差为L）
		CAL,//L ，a 调用过程（转子指令）（入口地址为a，层次差为L）
		INT,//0 ，a 数据栈栈顶指针增加a
		JMP,//0 ，a无条件转移到地址为a的指令
		JPC,//0 ，a 条件转移指令，转移到地址为a的指令
		RED,//L ，a 读数据并存入变量（相对地址为a，层次差为L）
		WRT //0 ，0 将栈顶内容输出

		/*
		private int f;

		private PAction(int F)
		{
			f = F;
		}

		public int getF()
		{
			return f;
		}
		*/
		}

		public Pcode(){}
		public Pcode(PAction pAction, int arg1, int arg2)
		{
			this();
			this.setAction(pAction);
			this.setArg1(arg1);
			this.setArg2(arg2);
		}

	public void setAction(PAction pAction)
	{
		action = pAction;
	}

	public PAction getAction()
	{
		return action;
	}

	public void setArg1(int arg1)
	{
		this.arg1 = arg1;
	}

	public int getArg1()
	{
		return arg1;
	}

	public void setArg2(int arg2)
	{
		this.arg2 = arg2;
	}

	public int getArg2()
	{
		return arg2;
	}

	// P-Code由一个指令和两个参数组成
	private PAction action;
	private int arg1;
	private int arg2;
} 