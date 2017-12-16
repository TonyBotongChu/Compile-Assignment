package PL0analyzer;

import java.util.*;

public class Analyzer
{
	public Vector<Item> v = new Vector<Item>();

	public void lineAnalyse(String s)
	{
		// 词法分析程序-主程序
		v.clear();
		for(int i = 0; i < s.length(); i++)
		{
			// 抛弃空格和换行符
			if(s.charAt(i) == ' ' || s.charAt(i) == '\n' || s.charAt(i) == '\t' || s.charAt(i) == 13)
			{
				continue;
			}
			if(Character.isLetter(s.charAt(i)))
			{
				String tempString = "";
				tempString += s.charAt(i);
				while(i < s.length()-1 && (Character.isLetter(s.charAt(i+1)) || Character.isDigit(s.charAt(i+1))))
				{
					tempString += s.charAt(++i);
				}
				Item word = new Item();
				word.setWordName(tempString);
				word.setWordValue(tempString);
				if(isReservedWord(tempString))
				{
					word.setWordType(Item.Type.KEYWORD);
				}
				else
				{
					word.setWordType(Item.Type.INDENTIFIER);
				}
				v.add(word);
				continue;
			}
			if(Character.isDigit(s.charAt(i)))
			{
				boolean isDouble = false;
				boolean isDoubleError = false;
				String tempString = "";
				tempString += s.charAt(i);
				Item word = new Item();
				while(i < s.length()-1 && (Character.isDigit(s.charAt(i+1)) || s.charAt(i+1) == '.'))
				{
					if(s.charAt(i+1) == '.')
					{
						if(isDouble)
						{
							addError();
							isDoubleError = true;
							break;
						}
						else
						{
							isDouble = true;
						}
					}
					tempString += s.charAt(++i);
				}
				if(isDoubleError)
					break;
				word.setWordName(tempString);
				if(isDouble)
				{
					word.setWordValue(tempString);
				}
				else
				{
					int val;
					val = Integer.parseInt(tempString);
					word.setWordValue(String.valueOf(val));
				}
				word.setWordType(Item.Type.CONSTANT);
				v.add(word);
				continue;
			}
			if(isDelimiter(s.charAt(i)))
			{
				addDelimiter(s.charAt(i));
				continue;
			}
			if(isSingleBoundary(s.charAt(i)))
			{
				addSingleBoundary(s.charAt(i));
				continue;
			}
			if(isHeadofBinaryOperator(s.charAt(i)))
			{
				Item word = new Item();
				if(s.charAt(i) == '<' && i < s.length()-1 && (s.charAt(i+1) == '=' || s.charAt(i+1) == '>'))
				{
					String tempString = "";
					tempString += s.charAt(i);
					tempString += s.charAt(++i);
					word.setWordName(tempString);
					word.setWordValue(tempString);
					word.setWordType(Item.Type.BINARY_OPERATOR);
					v.add(word);
				}
				else if((s.charAt(i) == '>' || s.charAt(i) == ':') && i < s.length()-1 && (s.charAt(i+1) == '='))
				{
					String tempString = "";
					tempString += s.charAt(i);
					tempString += s.charAt(++i);
					word.setWordName(tempString);
					word.setWordValue(tempString);
					word.setWordType(Item.Type.BINARY_OPERATOR);
					v.add(word);
				}
				else
				{
					addSingleBoundary(s.charAt(i));
				}
				continue;
			}
			if(s.charAt(i) == '/')
			{
				if(i >= s.length()-1 || s.charAt(i+1) != '/')
				{
					addSingleBoundary(s.charAt(i));
					continue;
				}
				if(i < s.length()-1 && s.charAt(i+1) == '/')
				{
					i = s.length()-1;
					continue;
				}
			}
			else
			{
				addError(s.charAt(i));
				break;
			}
		}

		return;
	}

	private boolean isReservedWord(String s)
	{
		// 判断这个词是否为保留字
		if (s.equals("const"))
			return true;
		if (s.equals("var"))
			return true;
		if (s.equals("procedure"))
			return true;
		if (s.equals("odd"))
			return true;
		if (s.equals("if") || s.equals("then") || s.equals("else"))
			return true;
		if (s.equals("while") || s.equals("do"))
			return true;
		if (s.equals("call"))
			return true;
		if (s.equals("begin") || s.equals("end"))
			return true;
		if (s.equals("repeat") || s.equals("until"))
			return true;
		if (s.equals("read"))
			return true;
		if (s.equals("write"))
			return true;
		return false;
	}

	private boolean isDelimiter(char c)
	{
		// 判断一个字符是否为分隔符
		char delimiter[] = { '(', ')', ',', ';', '.', '[', ']', '{', '}' };
		for (char aDelimiter : delimiter)
		{
			if (c == aDelimiter)
			{
				return true;
			}
		}
		return false;
	}

	private boolean isSingleBoundary(char c)
	{
		// 如果为单分界符，返回true；否则返回false
		char singleBoundary[] = { '+', '-', '*', '/', '=' };
		for (char aSingleBoundary : singleBoundary)
		{
			if (c == aSingleBoundary)
			{
				return true;
			}
		}
		return false;
	}

	private boolean isHeadofBinaryOperator(char c)
	{
		// 判断一个字符是否为双目运算符的第一个字符
		char headofBinaryOperator[] = { '<', '>', ':' };
		for (char aHeadofBinaryOperator : headofBinaryOperator)
		{
			if (c == aHeadofBinaryOperator)
			{
				return true;
			}
		}
		return false;
	}

	private void addDelimiter(char c)
	{
		Item item = new Item();
		item.setWordName(c);
		item.setWordValue(c);
		item.setWordType(Item.Type.DELIMITER);
		v.add(item);
	}

	private void addSingleBoundary(char c)
	{
		Item item = new Item();
		item.setWordName(c);
		item.setWordValue(c);
		item.setWordType(Item.Type.SINGLE_OPERATOR);
		v.add(item);
	}

	private void addError()
	{
		Item item = new Item();
		item.setWordName("Invalid Input");
		item.setWordValue("Error");
		item.setWordType(Item.Type.ERROR_WORD);
	}

	private void addError(char c)
	{
		Item item = new Item();
		item.setWordName("Invalid Input");
		item.setWordValue(c);
		item.setWordType(Item.Type.ERROR_WORD);
	}
}