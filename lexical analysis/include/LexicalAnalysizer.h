#ifndef LEXICALANALYSIZER_H
#define LEXICALANALYSIZER_H

#include "LexicalItem.h"
#include <vector>

class LexicalAnalysizer
{
    public:
        LexicalAnalysizer();
        void lineAnalyse(std::string s, std::vector<LexicalItem> &v);
        bool isReservedWord(std::string s);
        bool isLetter(char c);
        bool isNum(char c);
        bool isSingleBoundary(char c);

    protected:

    private:
};

#endif // LEXICALANALYSIZER_H
