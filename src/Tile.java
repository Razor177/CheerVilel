/**
 * File Name - [Tile.java]
 * Description -  abstract tile class
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

public abstract class Tile {
    private int x, y;
    private final int proximity;
    private final int maxAge;
    private int age;


    /**
     * Tile
     * constructor - will create a Tile object
     * @param x - the x index
     * @param y - the y index
     * @param proximity - the proximity distance/slight distance
     * @param maxAge - the max age
     */
    Tile(int x, int y, int proximity, int maxAge) {
        this.x = x;
        this.y = y;
        this.proximity = proximity;
        this.maxAge = maxAge;
        this.age = 0;

    }


    /**
     * setX
     * sets the x
     * @param x - the x index
     */

    public void setX(int x) {
        this.x = x;
    }

    /**
     * setY
     * sets the y
     * @param y - the y index
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * setAge
     * sets the age
     * @param age - the new age of thing
     */
    public void setAge(int age) {
        this.age = age;
    }


    /**
     * getX
     * gets the x index
     * @return x - the x index
     */
    public int getX() {
        return x;
    }

    /**
     * getY
     * gets the y index
     * @return - the y index
     */
    public int getY() {
        return y;
    }

    /**
     * getProximity
     * gets the proximity
     * @return - the proximity
     */
    public int getProximity() {
        return proximity;
    }

    /**
     * getAge
     * returns this age
     * @return - the age of this
     */

    public int getAge() {
        return age;
    }

    /**
     * getMaxAge
     * returns this maxAge
     * @return - the max age
     */
    public int getMaxAge() {
        return maxAge;
    }

}

