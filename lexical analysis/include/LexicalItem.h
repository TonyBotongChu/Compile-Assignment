#ifndef LEXICALITEM_H
#define LEXICALITEM_H

#include <string>

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
