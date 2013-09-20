package policy;

import statespace.*;

/*
 * Interface for all policies; a policy must contain a getAction method.
 */
public interface Policy {

    public String getAction(State s);
    
}
