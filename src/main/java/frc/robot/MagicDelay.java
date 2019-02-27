package frc.robot;
import java.util.TreeSet;
import java.util.TreeMap;
/**This class, inspired by a utility class delayBoolean by team 254, contains methods
 *  to time functions that require a delay. */

 public class MagicDelay {
     
      private static MagicDelay minstance = new MagicDelay();
   private MagicDelay() {}
 
      public static MagicDelay getInstance() {return minstance;}




    TreeSet<String> IDs = new TreeSet<String>();
 TreeMap<String, DelayBoolean> IDsToBooleans = new TreeMap<String, DelayBoolean>();

 /**If this is the first time this operation has been called with the arbitrary ID string, creates a new DelayBoolean 
  * and returns it. If, however, this method has been called before with this ID, it returns the DelayBoolean object that was created before.
  The purpose of this is so that code which is looped can have a delay be created and not be reset with each new loop. */
public  DelayBoolean startDelay(int delay, String ID) {
if (IDs.add(ID))
{DelayBoolean d = new DelayBoolean(delay);
    IDsToBooleans.put(ID,d);
     return d;}
     else
     return IDsToBooleans.get(ID);
    }

/**Clears the memory used in startDelay so that you can use the same ID again.*/
public void clearMemory() {
   IDsToBooleans.clear();
   IDs.clear();}


 }