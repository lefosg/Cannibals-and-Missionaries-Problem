import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CMMain
{   
    public static void main(String[] arguments)
    {        
        //input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter N (no. of cannibals and missionaries): ");
        int N = scanner.nextInt();
        System.out.print("Enter M (boat capacity): ");
        int M = scanner.nextInt();
        System.out.print("Enter K (max number of crosses): ");
        int K = scanner.nextInt();
        scanner.close();

        if(N < 0 || M <= 1 || K <= 0)
        {
            System.out.println("Invalid parameters given!");
            return;
        }

        long start = System.currentTimeMillis();
        //run algorithm
        State initialState = new State(N,M,true);
        aStar_ClosedSet algo = new aStar_ClosedSet();

        State finalState = algo.run(initialState, K);  
        
        if(finalState == null)
        {
            //if no solution was found, print a message, then exit
            System.out.println("Could not find a solution");
            return;
        }
        else
        {
            //find the path of the solution
            ArrayList<State> path = new ArrayList<State>();  
            State temp = finalState;
            while(temp != null)
            {
                path.add(temp);
                temp = temp.getFather();
            }
            //reverse the path, then print it
            Collections.reverse(path);  
            System.out.println("-------------SOLUTION PATH-------------");
            for(State state:path)
            {
                state.printState();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Duration: " + (end-start) + " milliseconds");
    }
}