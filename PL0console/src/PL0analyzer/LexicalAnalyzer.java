package PL0analyzer;

import java.util.*;

public class LexicalAnalyzer
{
	public Vector<Item> v = new Vector<Item>();

	// 词法分析程序-主程序
	// 每执行一次，分析源代码的一行
	public void lineAnalyse(String s)
	{
		//v.clear();
		for(int i = 0; i < s.length(); i++)
		{
			// 抛弃空格和换行符
			if(s.charAt(i) == ' ' || s.charAt(i) == '\n' || s.charAt(i) == '\t' || s.charAt(i) == 13)
			{
				continue;
			}
			// 如果是以字母开头，有可能是保留字，有可能是变量名
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
					word.setWordType(Item.Type.IDENTIFIER);
				}
				v.add(word);
				continue;
			}
			// 如果以数字开头，那么是整数或者浮点数
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
							addError("two dots in float");
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
				// 如果出现了两个及以上的小数点，则报错
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
			// 对于确定是分隔符的字符，简单处理即可
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
			// 仅凭第一个字符无法判断是单目运算符还是双目运算符时，再多读入一个字符
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
			// 如果是斜杠，那么需要判断是除号还是注释
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
				addError("unknown character", s.charAt(i));
				break;
			}
		}

		return;
	}

	// 判断这个词是否为保留字
	private boolean isReservedWord(String s)
	{
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

	// 判断一个字符是否为分隔符
	private boolean isDelimiter(char c)
	{
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

	// 如果为单分界符，返回true；否则返回false
	private boolean isSingleBoundary(char c)
	{
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

	// 判断一个字符是否为双目运算符的第一个字符
	private boolean isHeadofBinaryOperator(char c)
	{
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

	private void addError(String s) throws LexicalException
	{
		Item item = new Item();
		item.setWordName(s);
		item.setWordValue("Error");
		item.setWordType(Item.Type.ERROR_WORD);
		throw new LexicalException(s);
	}

	private void addError(String s, char c)
	{
		Item item = new Item();
		item.setWordName(s);
		item.setWordValue(c);
		item.setWordType(Item.Type.ERROR_WORD);
		throw new LexicalException(s);
	}
}