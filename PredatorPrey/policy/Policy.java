package policy;

import statespace.*;

/**
 * <p>Title: opdr 2 - Mc, Q, Sarsa</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author duy chuan
 * @version 1.0
 */
public interface Policy {

    public String getAction(State s);
    
}
