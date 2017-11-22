#include <iostream>
#include "LexicalAnalysizer.h"

using namespace std;

int main()
{
    string s;
    LexicalAnalysizer analysizer;
    getline(cin, s);
    analysizer.lineAnalyse(s);
    for(int i = 0; i < analysizer.v.size(); i++)
    {
        cout << analysizer.v.at(i).getWordName();
        cout << '\t';
        cout << analysizer.v.at(i).wordTypeToString();
        cout << endl;
    }
    return 0;
}
