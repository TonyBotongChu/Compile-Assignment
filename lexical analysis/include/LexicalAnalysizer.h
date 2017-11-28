#ifndef LEXICALANALYSIZER_H
#define LEXICALANALYSIZER_H

#include "LexicalItem.h"
#include <vector>

class LexicalAnalysizer
{
public:
    LexicalAnalysizer();
    void lineAnalyse(std::string s);
    bool isReservedWord(std::string s);
    bool isLetter(char c);
    bool isNum(char c);
    bool isSingleBoundary(char c);
    bool isHeadofBinaryOperator(char c);

    void addSingleBoundary(char c);
    void addError();
//        bool isSecondofBinaryOperator(char c);

    std::vector<LexicalItem> v;

    void FloatToString(float fNum,char *pStr)
    {
        unsigned int nData = ((unsigned int *)&fNum)[0];

        for (int i = 0; i < 32; i ++)
        {
            pStr[31 - i] = (char)(nData & 1) + '0';
            nData >>= 1;
        }
        pStr[32] = '\0';
    }

    std::string IntToString(int num);
    //std::string IntToString(std::string num);
    std::string DoubleToString(double num);
protected:

private:
};

#endif // LEXICALANALYSIZER_H
