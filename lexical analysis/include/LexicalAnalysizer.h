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
    protected:

    private:
};

#endif // LEXICALANALYSIZER_H
