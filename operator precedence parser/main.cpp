#include <iostream>
#include "Analyser.h"

using namespace std;

int main()
{
    Analyser a;
    string s;
    cout << "Input:" << endl;
    cin >> s;
    cout << left << setw(8) << "step";
    cout << left << setw(16) << "stack";
    cout << left << setw(10) << "priority";
    //cout << left << setw(4) << "readin";
    cout << left << setw(10) << "status";
    cout << left << setw(16) << "input str";
    cout << endl;
    a.initQueue(s);
    a.analyse();
    return 0;
}
