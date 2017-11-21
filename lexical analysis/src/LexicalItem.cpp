#include "LexicalItem.h"

LexicalItem::LexicalItem()
{
    //ctor
}

LexicalItem::~LexicalItem()
{
    //dtor
}

std::string LexicalItem::wordTypeToString()
{
    switch (wordType)
    {
    case 1:
        return "keyword";
    case 2:
        return "identifier";
    case 3:
        return "constant";
    case 4:
        return "delimiter";
    case 5:
        return "operator";
    case 6:
        return "binaryoperator";
    default:
        return "error";
    }
}


