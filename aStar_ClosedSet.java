import java.util.HashSet;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

public class aStar_ClosedSet
{
    private ArrayList<State> frontier;
    private HashSet<State> closedSet;

    aStar_ClosedSet()
    {
        this.frontier = new ArrayList<State>();
        this.closedSet = new HashSet<State>();
    }
    State run(State initialState, int maxCrosees)
    {
        frontier.add(initialState);
        while(!frontier.isEmpty())
        {
            Collections.sort(frontier);
            State currentState = frontier.remove(0);
            closedSet.add(currentState);

            if(currentState.isFinal())
            {
                return currentState;
            }

            if(currentState.getG() > maxCrosees)
            {
                continue;
            }

            ArrayList<State> children = currentState.getChildren();
            Iterator<State> iter = children.iterator();
            while(iter.hasNext())
            {
                State child = iter.next();
                if(closedSet.contains(child) || frontier.contains(child))
                {
                   iter.remove();
                }
            }            
            frontier.addAll(children);
        }
        return null;
    }
}
