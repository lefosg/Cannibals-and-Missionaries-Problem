import java.util.ArrayList;

class State implements Comparable<State>
{
    
    private int cansLeft, misLeft, cansRight, misRight;
    private boolean boatIsLeft; //true -> left, false -> right
    
    static public int N, M; 
    static private char initialSide;

    private int g;  //depth of the node
    private double h, total;  //h->heuristic value, total->h+g

    private State father;

    //constructors
    State(int n, int m, boolean boatSide) //constructor for the root
    {
        State.N = n;
        State.M = m;
        boatIsLeft = boatSide;
        State.initialSide = boatIsLeft ? 'L':'R';
        if(boatIsLeft)
        {
            cansLeft = n;
            misLeft = n;
            cansRight = 0;
            misRight = 0;
        }
        else
        {
            cansLeft = 0;
            misLeft = 0;
            cansRight = n;
            misRight = n;
        }
        father = null;
        this.g = 0;
        this.evaluate();
    }

    State(int cl, int ml, int cr, int mr, boolean bs, int g) //constructor for all the other nodes
    {
        this.cansLeft = cl;
        this.misLeft = ml;
        this.cansRight = cr;
        this.misRight = mr;
        this.boatIsLeft = bs;
        this.g = g;
    }

    //getters & setters
    public State getFather() {return father;}
    public void setFather(State f) {this.father = f;}

    public int getG() {return this.g;}
    public void setG(int g) {this.g = g;}

    public double getH() {return this.h;}

    public double getTotal() {return this.total;}
    public void setTotal(double total) {this.total = total;}

    public boolean getBoatIsLeft() {return boatIsLeft;}
    public void setBoatIsLeft(boolean bs) {this.boatIsLeft = bs;}

    public char getInitSide() {return State.initialSide;}

    public int getN() {return State.N;}
    public int getM() {return State.M;}

    //useful methods
    private double heuristic()
    {
        if(initialSide == 'L')
            return (double)(cansLeft+misLeft)/State.M;
        else
            return (double)(cansRight+misRight)/State.M;
    }

    public void evaluate()
    {
        this.h = heuristic();
        this.total = (double)(this.g + this.h);
    }

    public boolean isFinal()
    {
        if(initialSide == 'L')
        {    
            if(this.cansLeft == 0 && this.misLeft == 0 && this.cansRight == State.N && this.misRight == State.N)
            {
                return true;
            }
        }
        else
        {
            if(this.cansLeft == State.N && this.misLeft == State.N && this.cansRight == 0 && this.misRight == 0)
            {
                return true;
            }
        }
        return false;
    }

    public void printState()
    {   
        if(this.boatIsLeft)
        {     
            System.out.println(this.cansLeft + " C |\\------/                    |" + this.cansRight + "C");
            System.out.println( this.misLeft + " M |~\\____/~~~~~~~~~~~~~~~~~~~~~|" + this.misRight + "M");
        }
        else
        {
            System.out.println(this.cansLeft + " C |                    \\------/|" + this.cansRight + "C");
            System.out.println( this.misLeft + " M |~~~~~~~~~~~~~~~~~~~~~\\____/~|" + this.misRight + "M");
        }
        System.out.println("Depth: " + this.g + " Score: " + this.total);
    }

    //transition operators
    public boolean crossToRight(int cN, int mN)
    {
        if (!boatIsLeft)return false;
        if (State.M > 2 && cN > mN) return false;
        if (cN + mN > State.M)return false;
        if (cN + mN < 1)return false;
        if (this.cansLeft - cN < 0 || this.misLeft - mN < 0)return false;
        if (this.cansRight + cN > this.misRight + mN && this.misRight + mN != 0) return false;
        if (this.cansLeft - cN > this.misLeft - mN && this.misLeft - mN != 0) return false;
        return true;
    }
 
    public boolean crossToLeft(int cN, int mN)
    {   
        if (boatIsLeft) return false;
        if (State.M > 2 && cN > mN) return false;
        if (cN + mN > State.M)return false;
        if (cN + mN < 1)return false;
        if (this.cansRight - cN < 0 || this.misRight - mN < 0)return false;
        if (this.cansLeft + cN > this.misLeft + mN && this.misLeft + mN != 0) return false;
        if (this.cansRight - cN > this.misRight - mN && this.misRight - mN != 0) return false;
        return true;
    }
    //children-node creation
    public ArrayList<State> getChildren()
    {
        ArrayList<State> children = new ArrayList<State>();
        if(boatIsLeft)
        {
            for(int i = 0; i <= State.M; i++)
            {
                for(int j = 0; j <= State.M; j++)
                {
                    if(crossToRight(i,j))
                    {
                        State child = new State(this.cansLeft-i, this.misLeft-j, this.cansRight+i, this.misRight+j, false, this.g+1); //g
                        child.setFather(this);  
                        child.evaluate();   //h, total
                        children.add(child);
                    }
                }    
            }
        }
        else
        {
            for(int i = 0; i <= State.M; i++)
            {
                for(int j = 0; j <= State.M; j++)
                {
                    if(crossToLeft(i,j))
                    {
                        State child = new State(this.cansLeft+i, this.misLeft+j, this.cansRight-i, this.misRight-j, true, this.g+1); //g
                        child.setFather(this);  
                        child.evaluate();   //h, total
                        children.add(child);
                    }
                }    
            }
        }
        return children;
    }

    //override methods
    @Override
    public boolean equals(Object o)
    {
        State other = (State)o;
        if(this.cansLeft == other.cansLeft && this.misLeft == other.misLeft
        && this.cansRight == other.cansRight && this.misRight == other.misRight && this.boatIsLeft == other.boatIsLeft)
        {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(State other)
    {
        return Double.compare(this.total, other.total);
    }
}