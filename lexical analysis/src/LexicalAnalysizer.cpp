#include "LexicalAnalysizer.h"

LexicalAnalysizer::LexicalAnalysizer()
{
    //ctor
}

void LexicalAnalysizer::lineAnalyse(std::string s, std::vector<LexicalItem> &v)
{
    v.clear();
    for(int i = 0; i < s.length(); i++)
    {
        // 抛弃空格和换行符
        if(s[i] == ' ' || s[i] == '\n')
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
            if(isReservedWord(tempString))
                cout << "(keyword," << tempString << ")" << endl;
            else
                cout << "(letters," << tempString << ")" << endl;
            continue;
        }
        if(isNum(s[i]))
        {
            string tempString = "";
            tempString += s[i];
            while(i < s.length()-1 && isNum(s[i+1]))
            {
                tempString += s[++i];
            }
            cout << "(number," << tempString << ")" << endl;
            continue;
        }
        if(isSingleBoundary(s[i]))
        {
            cout << "(singleBoundary," << s[i] << ")" << endl;
            continue;
        }
        if(s[i] == ':')
        {
            if(i < s.length()-1 && s[i+1] == '=')
            {
                cout << "(assign,)" << endl;
                i++;
            }
            else
            {
                cout << "(singleBoundary," << s[i] << ")" << endl;
            }
            continue;
        }
        if(s[i] == '/')
        {
            if(i >= s.length()-1 || s[i+1] != '/')
            {
                cout << "(singleBoundary," << s[i] << ")" << endl;
                continue;
            }
            if(s[i+1] == '/')
            {
                i = s.length()-1;
                continue;
            }
        }
        else
        {
            cout << "error" << endl;
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

bool LexicalAnalysizer::isSingleBoundary(char c)
{
    // 如果为单分界符，返回true；否则返回false
    char singleBoundary[] = {
        '+', '-', '*', '/', '(', ')', ',', ';', '=', '[', ']', '{', '}', '>', '<'
    };
    for (int i = 0; i < sizeof(singleBoundary)/sizeof(char); i++)
    {
        if(c == singleBoundary[i])
        {
            return true;
        }
    }
    return false;
}
