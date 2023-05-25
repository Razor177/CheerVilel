/**
 * File Name - [Rock.java]
 * Description - Rock Class
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

public class Rock extends UnMoveable{

    /**
     * Rock
     * constructor - will create a rock object with given x and y
     * @param x - the x index
     * @param y - the y index
     */
    public Rock(int x, int y) {
        super(x, y, -1);
    }

}
