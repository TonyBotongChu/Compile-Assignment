package PL0analyzer;

public class Item
{
	// 用于表示已经被词法分析程序分析出来的词
	public void setWordName(String s)
	{
		wordName = s;
	}

	public String getWordName()
	{
		return wordName;
	}

	void setWordType(short i)
	{
		wordType = i;
	}

	public short getWordType()
	{
		return wordType;
	}

	public String wordTypeToString()
	{
		switch (wordType)
		{
		case 1:
			return "keyword";
		case 2:
			return "identifier";
		case 3:
			return "constant";
		case 4:
			return "delimiter";
		case 5:
			return "single operator";
		case 6:
			return "binary operator";
		default:
			return "error";
		}
	}

	public void setValue(String s)
	{
		wordValue = s;
	}

	public String getValue()
	{
		return wordValue;
	}

	private String wordName;
	private short wordType;
	private String wordValue;
} 