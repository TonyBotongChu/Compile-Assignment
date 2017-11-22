#include <iostream>
#include <iomanip>
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
        cout << left << setw(15) << analysizer.v.at(i).getWordName();
        //cout << '\t';
        cout << left << setw(20) << analysizer.v.at(i).wordTypeToString();
        //cout << '\t';
        cout << left << setw(10) << analysizer.v.at(i).getValue();
        cout << endl;
    }
    return 0;
}
