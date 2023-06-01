/**
 * File Name - [Dirt.java]
 * Description - Dirt class
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

public class Dirt extends UnMoveable{

    /**
     * Dirt
     * constructor which just makes a Dirt object (has impossible values and ju st used in decision making, never made and applied to game )

     */
    public Dirt() {
        super(-1, -1, -1);
    }

    /**
     * Dirt
     * constructor
     * @param - x - the x index of the dirt
     * @param - y - the y index of the dirt
     * @param - maxAge - the max age of dirt (used for base nutrition when plants or trees spawn on it)
     */
    public Dirt(int y, int x, int dirtMaxAge) {
        super(x, y, dirtMaxAge);
    }


    /**
     * updateDirt
     * update specs of the dirt
     */
    public void updateDirt() {
        this.setAge(this.getAge() + 1);

        if (this.getAge() > this.getMaxAge()) { // to make sure that the dirts age wont get too high, as when i plant or tree spawns in on the dirt, its nutritional value is based off of this, (so the plant shouldnt have a initial nutritional value of 100000000)
            this.setAge(this.getMaxAge());
        }

    }





}
