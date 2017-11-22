#ifndef LEXICALITEM_H
#define LEXICALITEM_H

#include <string>

#define KEYWORD 1
#define INDENTIFIER 2
#define CONSTANT 3
#define DELIMITER 4
#define SINGLE_OPERATOR 5
#define BINARY_OPERATOR 6
#define ERROR_WORD -1

class LexicalItem
{
    public:
        LexicalItem();
        virtual ~LexicalItem();

        void setWordName(std::string s)
        {
            wordName = s;
        }
        std::string getWordName()
        {
            return wordName;
        }

        void setWordType(short int i)
        {
            wordType = i;
        }
        short int getWordType()
        {
            return wordType;
        }

        std::string wordTypeToString();

        void setValue(std::string s)
        {
            wordValue = s;
        }
        std::string getValue()
        {
            return wordValue;
        }

    protected:

    private:
        std::string wordName;
        short int wordType;
        std::string wordValue;
};

#endif // LEXICALITEM_H
