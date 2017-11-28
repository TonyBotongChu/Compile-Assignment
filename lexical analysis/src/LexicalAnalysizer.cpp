#include "LexicalAnalysizer.h"

LexicalAnalysizer::LexicalAnalysizer()
{
    //ctor
}

void LexicalAnalysizer::lineAnalyse(std::string s)
{
    v.clear();
    for(int i = 0; i < s.length(); i++)
    {
        // 抛弃空格和换行符
        if(s[i] == ' ' || s[i] == '\n' || s[i] == '\t' || s[i] == 13)
        {
            continue;
        }
        if(isLetter(s[i]))
        {
            std::string tempString = "";
            tempString += s[i];
            while(i < s.length()-1 && (isLetter(s[i+1]) || isNum(s[i+1])))
            {
                tempString += s[++i];
            }
            LexicalItem word;
            word.setWordName(tempString);
            word.setValue(tempString);
            if(isReservedWord(tempString))
            {
                word.setWordType(KEYWORD);
            }
            else
            {
                word.setWordType(INDENTIFIER);
            }
            v.push_back(word);
            continue;
        }
        if(isNum(s[i]))
        {
            bool isDouble = false;
            bool isDoubleError = false;
            std::string tempString = "";
            tempString += s[i];
            LexicalItem word;
            while(i < s.length()-1 && (isNum(s[i+1]) || s[i+1] == '.'))
            {
                if(s[i+1] == '.')
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
                tempString += s[++i];
            }
            if(isDoubleError)
                break;
            word.setWordName(tempString);
            //char* strarr[70];
            if(isDouble)
            {
                word.setValue(tempString);
            }
            else
            {
                int val;
                val = StringToint(tempString);
                word.setValue(IntToString(val));
            }
            //word.setValue(tempString);
            word.setWordType(CONSTANT);
            v.push_back(word);
            continue;
        }
        if(isDelimiter(s[i]))
        {
            addDelimiter(s[i]);
            continue;
        }
        if(isSingleBoundary(s[i]))
        {
            addSingleBoundary(s[i]);
            continue;
        }
        if(isHeadofBinaryOperator(s[i]))
        {
            LexicalItem word;
            if(s[i] == '<' && i < s.length()-1 && (s[i+1] == '=' || s[i+1] == '>'))
            {
                std::string tempString = "";
                tempString += s[i];
                tempString += s[++i];
                word.setWordName(tempString);
                word.setValue(tempString);
                word.setWordType(BINARY_OPERATOR);
                v.push_back(word);
            }
            else if((s[i] == '>' || s[i] == ':') && i < s.length()-1 && (s[i+1] == '='))
            {
                std::string tempString = "";
                tempString += s[i];
                tempString += s[++i];
                word.setWordName(tempString);
                word.setValue(tempString);
                word.setWordType(BINARY_OPERATOR);
                v.push_back(word);
            }
            else
            {
                addSingleBoundary(s[i]);
            }
            continue;
        }
        if(s[i] == '/')
        {
            if(i >= s.length()-1 || s[i+1] != '/')
            {
                addSingleBoundary(s[i]);
                continue;
            }
            if(i < s.length()-1 && s[i+1] == '/')
            {
                i = s.length()-1;
                continue;
            }
        }
        else
        {
            addError(s[i]);
            break;
        }
    }
}

bool LexicalAnalysizer::isReservedWord(std::string s)
{
    if(s == "const")
        return true;
    if(s == "var")
        return true;
    if(s == "procedure")
        return true;
    if(s == "odd")
        return true;
    if(s == "if" || s == "then" || s == "else")
        return true;
    if(s == "while" || s == "do")
        return true;
    if(s == "call")
        return true;
    if(s == "begin" || s == "end")
        return true;
    if(s == "repeat" || s == "until")
        return true;
    if(s == "read")
        return true;
    if(s == "write")
        return true;
    return false;
}

bool LexicalAnalysizer::isLetter(char c)
{
    if(c >= 'a' && c <= 'z')
        return true;
    if(c >= 'A' && c <= 'Z')
        return true;
    return false;
}

bool LexicalAnalysizer::isNum(char c)
{
    if(c >= '0' && c <= '9')
        return true;
    else
        return false;
}

bool LexicalAnalysizer::isDelimiter(char c)
{
    char delimiter[] =
    {
        '(', ')', ',', ';', '.', '[', ']', '{', '}'
    };
    for (int i = 0; i < (int)(sizeof(delimiter)/sizeof(char)); i++)
    {
        if(c == delimiter[i])
        {
            return true;
        }
    }
    return false;
}

bool LexicalAnalysizer::isSingleBoundary(char c)
{
    // 如果为单分界符，返回true；否则返回false
    char singleBoundary[] =
    {
        '+', '-', '*', '/', '='
    };
    for (int i = 0; i < (int)(sizeof(singleBoundary)/sizeof(char)); i++)
    {
        if(c == singleBoundary[i])
        {
            return true;
        }
    }
    return false;
}

bool LexicalAnalysizer::isHeadofBinaryOperator(char c)
{
    char headofBinaryOperator[] =
    {
        '<', '>', ':'
    };
    for (int i = 0; i < (int)(sizeof(headofBinaryOperator)/sizeof(char)); i++)
    {
        if(c == headofBinaryOperator[i])
        {
            return true;
        }
    }
    return false;
}

//bool LexicalAnalysizer::isSecondofBinaryOperator(char c)
//{
//    char secondofBinaryOperator[] = {
//        '<', '>'
//    };
//    for (int i = 0; i < sizeof(secondofBinaryOperator)/sizeof(char); i++)
//    {
//        if(c == secondofBinaryOperator[i])
//        {
//            return true;
//        }
//    }
//    return false;
//}

void LexicalAnalysizer::addDelimiter(char c)
{
    LexicalItem word;
    word.setWordName(std::string(1, c));
    word.setValue(std::string(1, c));
    word.setWordType(DELIMITER);
    v.push_back(word);
}

void LexicalAnalysizer::addSingleBoundary(char c)
{
    LexicalItem word;
    word.setWordName(std::string(1, c));
    word.setValue(std::string(1, c));
    word.setWordType(SINGLE_OPERATOR);
    v.push_back(word);
}

void LexicalAnalysizer::addError()
{
    LexicalItem word;
    word.setWordName("Invalid Input");
    word.setValue("Error");
    word.setWordType(ERROR_WORD);
    v.push_back(word);
}

void LexicalAnalysizer::addError(char c)
{
    LexicalItem word;
    word.setWordName("Invalid Input");
    word.setValue(IntToString((int)c));
    word.setWordType(ERROR_WORD);
    v.push_back(word);
}

int LexicalAnalysizer::StringToint(std::string s)
{
    int num = 0;
    for(int i = s.length()-1; i >= 0; i--)
    {
        num *= 10;
        num += (int)s[i] - (int)'0';
    }
    return num;
}

std::string LexicalAnalysizer::IntToString(int num)
{
    if(num == 0)
        return "0";
    std::string s = "";
    char c;
    while(num != 0)
    {
        c = num%2 + '0';
        num /= 2;
        s = c+s;
    }
    return s;
}
