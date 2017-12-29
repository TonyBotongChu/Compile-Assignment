package PL0analyzer;

public class Item
{
	// 用于表示已经被词法分析程序分析出来的词
	public void setWordName(String s)
	{
		wordName = s;
	}

	public void setWordName(char c)
	{
		wordName = String.valueOf(c);
	}

	public String getWordName()
	{
		return wordName;
	}

	public enum Type
	{
		KEYWORD, IDENTIFIER, CONSTANT, DELIMITER, SINGLE_OPERATOR, BINARY_OPERATOR, ERROR_WORD
	}

	public void setWordType(Type type)
	{
		wordType = type;
	}

	public Type getWordType()
	{
		return wordType;
	}

	public boolean isTypeEqual(Type type)
	{
		return wordType == type;
	}

	public String wordTypeToString()
	{
		switch (wordType)
		{
		case KEYWORD:
			return "keyword";
		case IDENTIFIER:
			return "identifier";
		case CONSTANT:
			return "constant";
		case DELIMITER:
			return "delimiter";
		case SINGLE_OPERATOR:
			return "single operator";
		case BINARY_OPERATOR:
			return "binary operator";
		default:
			return "error";
		}
	}

	public void setWordValue(String s)
	{
		wordValue = s;
	}

	public void setWordValue(char c)
	{
		wordValue = String.valueOf(c);
	}

	public String getValue()
	{
		return wordValue;
	}

	private String wordName = "unknown";
	private Type wordType;
	private String wordValue = "unknown";
} 