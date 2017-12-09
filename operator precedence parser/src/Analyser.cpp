#include "Analyser.h"

Analyser::Analyser()
{
    initPriorityMatrix();
    analyserStack.push_back('#');
}

Analyser::~Analyser()
{
    //dtor
}

void Analyser::initPriorityMatrix()
{
    /*
      vertical: in stack
      horizon: out stack

        # + * ( ) i
      # - < < < - <
      + > > < < > <
      * > > > < > <
      ( - < < < = <
      ) > > > > > -
      i > > < - > -
    */
    setPriorityMaztrix('#', '#', '-');
    setPriorityMaztrix('#', '+', '<');
    setPriorityMaztrix('#', '*', '<');
    setPriorityMaztrix('#', '(', '<');
    setPriorityMaztrix('#', ')', '-');
    setPriorityMaztrix('#', 'i', '<');

    setPriorityMaztrix('+', '#', '>');
    setPriorityMaztrix('+', '+', '>');
    setPriorityMaztrix('+', '*', '<');
    setPriorityMaztrix('+', '(', '<');
    setPriorityMaztrix('+', ')', '>');
    setPriorityMaztrix('+', 'i', '<');

    setPriorityMaztrix('*', '#', '>');
    setPriorityMaztrix('*', '+', '>');
    setPriorityMaztrix('*', '*', '>');
    setPriorityMaztrix('*', '(', '<');
    setPriorityMaztrix('*', ')', '>');
    setPriorityMaztrix('*', 'i', '<');

    setPriorityMaztrix('(', '#', '-');
    setPriorityMaztrix('(', '+', '<');
    setPriorityMaztrix('(', '*', '<');
    setPriorityMaztrix('(', '(', '<');
    setPriorityMaztrix('(', ')', '=');
    setPriorityMaztrix('(', 'i', '<');

    setPriorityMaztrix(')', '#', '>');
    setPriorityMaztrix(')', '+', '>');
    setPriorityMaztrix(')', '*', '>');
    setPriorityMaztrix(')', '(', '>');
    setPriorityMaztrix(')', ')', '>');
    setPriorityMaztrix(')', 'i', '-');

    setPriorityMaztrix('i', '#', '>');
    setPriorityMaztrix('i', '+', '>');
    setPriorityMaztrix('i', '*', '<');
    setPriorityMaztrix('i', '(', '-');
    setPriorityMaztrix('i', ')', '>');
    setPriorityMaztrix('i', 'i', '-');
}

void Analyser::setPriorityMaztrix(char index1, char index2, char val)
{
    /*
      -(null) is -1
      < is 0
      > is 1
      = is 2
    */
    short int temp;
    switch(val)
    {
    case '<':
        temp = 0;
        break;
    case '>':
        temp = 1;
        break;
    case '=':
        temp = 2;
        break;
    default:
        temp = -1;
    }
    priority[GetOperatorIndex(index1)][GetOperatorIndex(index2)] = temp;
}

bool Analyser::isOperator(char c)
{
    char operatorSet[] =
    {
        '#', '+', '*', '(', ')', 'i'
    };
    for(int i = 0; i < (int)(sizeof(operatorSet)/sizeof(char)); i++)
    {
        if(c == operatorSet[i])
            return true;
    }
    return false;
}

short int Analyser::getPriorityMaztrix(char index1, char index2)
{
    return priority[GetOperatorIndex(index1)][GetOperatorIndex(index2)];
}

char Analyser::getPriorityMaztrixChar(char index1, char index2)
{
    char temp;
    switch(getPriorityMaztrix(index1, index2))
    {
    case 0:
        temp = '<';
        break;
    case 1:
        temp = '>';
        break;
    case 2:
        temp = '=';
        break;
    default:
        temp = '-';
    }
    return temp;
}

int Analyser::GetOperatorIndex(char o)
{
    switch(o)
    {
    case '#':
        return 0;
    case '+':
        return 1;
    case '*':
        return 2;
    case '(':
        return 3;
    case ')':
        return 4;
    case 'i':
        return 5;
    default:
        throw "unknown op";
        return -1;
    }
}

void Analyser::initQueue(std::string s)
{
    analyserQueue.str = s+"#";
    analyserQueue.startnum = 0;
}

std::string Analyser::printStack()
{
    std::string s = "";
    for(int i = 0; i < (int)analyserStack.size(); i++)
    {
        s += analyserStack.at(i);
    }
    return s;
}

std::string Analyser::printQueue()
{
    return analyserQueue.str.substr(analyserQueue.startnum);
}

void Analyser::analyse()
{

    char cache;
    char rop;
    //char prioritychar;
    int index;
    int step = 0;
    AnalyserLine l;
    try
    {
        while(analyserQueue.str[analyserQueue.startnum] != '#' || analyserStack.size() != 2)
        {
            cache = analyserQueue.str[analyserQueue.startnum];
            rop = getRightOp(index);
            l.step = step++;
            l.stackstr = printStack();
            l.priority = getPriorityMaztrixChar(rop, cache);
            l.readin = cache;
            l.queuestr = printQueue();
            if(comparePriority(rop, cache))
            {
                l.status = "reduce";
                printLine(l);
                Reduce();
            }
            else
            {
                l.status = "readin";
                printLine(l);
                Readin();
            }
        }
        if(analyserStack.at(0) != 'E')
        {
            analyserStack.pop_back();
            analyserStack.push_back('E');
        }
        l.step++;
        l.status = "done";
        l.stackstr = printStack();
        l.priority = '-';
        l.queuestr = "#";
        printLine(l);
    }
    catch(const char* msg)
    {
        //std::cerr << msg << std::endl;
        l.stackstr = std::string(msg);
        l.priority = '-';
        l.queuestr = "";
        printLine(l);
    }
    catch(...)
    {
        //std::cerr << "unknown error" << std::endl;
        l.stackstr = "unknown error";
        l.priority = '-';
        l.queuestr = "";
        printLine(l);
    }
}

void Analyser::printLine(AnalyserLine &a)
{
    std::cout << std::left << std::setw(8) << a.step;
    std::cout << std::left << std::setw(16) << a.stackstr;
    std::cout << std::left << std::setw(10) << a.priority;
    //std::cout << std::left << std::setw(4) << a.readin;
    std::cout << std::left << std::setw(10) << a.status;
    std::cout << std::left << std::setw(16) << a.queuestr;
    std::cout << std::endl;
}

//char Analyser::LastOperatorInStack()
//{
//    int bias = analyserStack.size()-1;
//    while(bias >= 0 && !isOperator(analyserStack.at(bias)))
//    {
//        bias--;
//    }
//    return analyserStack[bias];
//}
//
//char Analyser::FirstOperatorInQueue()
//{
//    int bias = analyserQueue.startnum;
//    while(bias < (int)analyserQueue.str.size() && !isOperator(analyserQueue.str[bias]))
//    {
//        bias++;
//    }
//    return analyserQueue.str[bias];
//}

//std::string Analyser::getReduceSubstr(int &i)
//{
//    i = analyserStack.size()-1;
//    std::string s = "";
//    while(i >= 0)
//    {
//        if(analyserStack.at(i) == 'i')
//        {
//            return "i";
//        }
//        else if(analyserStack.at(i) == '#')
//        {
//            return "#";
//        }
//
//        i--;
//    }
//}

char Analyser::getRightOp(int &index)
{
    index = analyserStack.size()-1;
    while(index >= 0)
    {
        if(isOperator(analyserStack.at(index)))
            return analyserStack.at(index);
        index--;
    }
    return '#';
}

void Analyser::Reduce()
{
    int index;
    char rop = getRightOp(index);
    if(index < 0)
        throw "reduction error";
    switch(rop)
    {
    case 'i':
        analyserStack.pop_back();
        analyserStack.push_back('F');
        break;
    case ')':
        if(index < 2 || analyserStack.at(index-2) !='(')
            throw "reduction error";
        else if(!isE(analyserStack.at(index-1)))
        {
            //std::cerr << analyserStack.at(index-1)  << std::endl;
            throw "reduction error";
        }
        else
        {
            analyserStack.pop_back();
            analyserStack.pop_back();
            analyserStack.pop_back();
            analyserStack.push_back('F');
        }
        break;
    case '+':
        if(index == (int)analyserStack.size()-1 || index == 0)
            throw "reduction error";
        if(isE(analyserStack.at(index-1)) && isT(analyserStack.at(index+1)))
        {
            analyserStack.pop_back();
            analyserStack.pop_back();
            analyserStack.pop_back();
            analyserStack.push_back('E');
        }
        break;
    case '*':
        if(index == (int)analyserStack.size()-1 || index == 0)
            throw "reduction error";
        if(isT(analyserStack.at(index-1)) && isF(analyserStack.at(index+1)))
        {
            analyserStack.pop_back();
            analyserStack.pop_back();
            analyserStack.pop_back();
            analyserStack.push_back('T');
        }
        break;
    case '#':
        if(analyserStack.size() != 2)
            throw "reduction error";
        analyserStack.pop_back();
        analyserStack.push_back('E');
        break;
    default:
        throw "reduction error";
    }
}

void Analyser::Readin()
{
    analyserStack.push_back(analyserQueue.str[analyserQueue.startnum++]);
}

bool Analyser::comparePriority(char c1, char c2)
{
    /*
      -(null) is -1
      < is 0
      > is 1
      = is 2
    */
    switch(priority[GetOperatorIndex(c1)][GetOperatorIndex(c2)])
    {
    case 0:
    case 2:
        return false;
    case 1:
        return true;
    default:
        throw "compare failed";
    }
}

bool Analyser::isE(char c)
{
    return c == 'E' || isT(c);
}

bool Analyser::isT(char c)
{
    return c == 'T' || isF(c);
}

bool Analyser::isF(char c)
{
    return c == 'F';
}
