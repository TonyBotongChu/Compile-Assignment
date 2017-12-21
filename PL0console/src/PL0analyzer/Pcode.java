package PL0analyzer;

public class Pcode
{
	public enum PAction
	{
		LIT,//0 , a 取常量a放入数据栈栈顶
		OPR,//0 , a 执行运算，a表示执行某种运算
		LOD,//L ，a 取变量（相对地址为a，层差为L）放到数据栈的栈顶
		STO,//L ，a 将数据栈栈顶的内容存入变量（相对地址为a，层次差为L）
		CAL,//L ，a 调用过程（转子指令）（入口地址为a，层次差为L）
		INT,//0 ，a 数据栈栈顶指针增加a
		JMP,//0 ，a无条件转移到地址为a的指令
		JPC,//0 ，a 条件转移指令，转移到地址为a的指令
		RED,//L ，a 读数据并存入变量（相对地址为a，层次差为L）
		WRT //0 ，0 将栈顶内容输出
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