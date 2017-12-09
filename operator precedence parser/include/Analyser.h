#ifndef ANALYSER_H
#define ANALYSER_H

#include <vector>
#include <string>
#include <iostream>
#include <iomanip>

#define OPERATOR_NUM 5

class Analyser
{
    public:
        Analyser();
        ~Analyser();
        static int GetOperatorIndex(char o);
        short int getPriorityMaztrix(char index1, char index2);
        char getPriorityMaztrixChar(char index1, char index2);
        static bool isOperator(char c);
        std::vector<char> analyserStack;
        //std::vector<char> analyserQueue;
        std::string printStack();
        std::string printQueue();
        void initQueue(std::string s);
        void analyse();
    protected:

    private:
        short int priority[OPERATOR_NUM+1][OPERATOR_NUM+1];
        void initPriorityMatrix();
        void setPriorityMaztrix(char index1, char index2, char val);
        struct AnalyserQueue
        {
            // will push until the whole string to analyze get into this queue
            // and the pop until this queue is empty
            std::string str;
            int startnum;
        }analyserQueue;
        struct AnalyserLine
        {
            int step;
            std::string stackstr;
            char priority;
            std::string status;
            char readin;
            std::string queuestr;
        };
        void printLine(AnalyserLine &a);
        //char LastOperatorInStack();
        //char FirstOperatorInQueue();
        void RightmostReduction();
        //std::string getReduceSubstr(int &i);
        char getRightOp(int &index);
        void Reduce();
        void Readin();
        bool comparePriority(char c1, char c2);
        bool isE(char c);
        bool isT(char c);
        bool isF(char c);
};

#endif // ANALYSER_H
