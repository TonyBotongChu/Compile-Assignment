#include <iostream>
#include <iomanip>
#include <fstream>
#include "LexicalAnalysizer.h"

using namespace std;

void debug_test()
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
}

void file_test(string path)
{
    ifstream input;
    string s;
    LexicalAnalysizer analysizer;
    input.open(path.c_str());
    if(input)
    {
        cout << left << setw(15) << "Name";
        cout << left << setw(20) << "Type";
        cout << left << setw(10) << "Value";
        cout << endl;
        for(int i = 0; i < 50; i++)
        {
            cout << "-";
        }
        cout << endl;
        while(getline(input, s))
        {
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
        }
    }
    else
    {
        cout << "file not found" << endl;
    }
}

int main()
{
    //debug_test();
    //file_test("/home/zbt/Documents/lexicalanalysis.input");
    cout << "test file:";
    string s;
    cin >> s;
    file_test(s);
    //file_test("lexicalanalysis.input");
    //file_test("test.txt");
    return 0;
}
