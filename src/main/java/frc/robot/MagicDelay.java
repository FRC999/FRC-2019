package frc.robot;
import java.util.TreeSet;
import java.util.TreeMap;
/**This class, inspired by a utility class delayBoolean by team 254, contains methods
 *  to time functions that require a delay. */

 public class MagicDelay {
    TreeSet<String> IDs = new TreeSet<String>();
 TreeMap<String, DelayBoolean> IDsToBooleans = new TreeMap<String, DelayBoolean>();
    DelayBoolean startDelay(int delay, String ID) {
if (IDs.add(ID))
{DelayBoolean d = new DelayBoolean(delay);
    IDsToBooleans.put(ID,d);
     return d;}
     else
     return IDsToBooleans.get(ID);
    }

    


 }