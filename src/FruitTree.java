/**
 * File Name - [FruitTree.java]
 * Description -  FruitTree class
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

import java.util.Random;

public class FruitTree extends UnMoveable{
    private int nutrition;
    private int fruitTimer; // timer for how often trees can actually give fruit
    private int saplingTime; // time when tree is too young to make fruit
    private final int fullSaplingTime;

    /**
     * FruitTree
     * Constructor - makes an impossible FruitTree (just used for pathfinding, never actually used in game )
     */
    public FruitTree() {
        super(-1, -1, -1);
        this.fullSaplingTime = -1;

    }

    /**
     * FruitTree
     * Constructor - creates a FruitTree object which will be used in game
     * @param x - the x cordinate
     * @param y - the y cordinate
     * @param maxAge - the max age a tree can live
     * @param nutrition - nutrition
     */
    public FruitTree(int x, int y, int maxAge, int nutrition) {
        super(x, y, maxAge);
        this.fruitTimer = this.getMaxAge() / 10; // fruit should fruit 8 times before dying of age (since they give so much)
        this.saplingTime = this.getMaxAge() / 5; // tree growing time before it can fruit
        this.nutrition = nutrition;
        this.fullSaplingTime = this.getMaxAge() / 5; // full timer constant to reset fruitTimer after a human has eaten the Fruit Tree
    }

    /**
     * updateFruitTree
     * will update the specs of the FruitTree
     * @param map - the map
     */
    public void updateFruitTree(Tile[][] map) {
        Random rand = new Random();

        this.setAge(this.getAge() + 1);

        if (fruitTimer > 0) {
            fruitTimer--;
        }

        if (this.getAge() > getMaxAge()) {
            if (rand.nextBoolean()) { // spreads out when FruitTrees die
                map[this.getY()][this.getX()] = new Dirt(this.getY(), this.getX(), this.getMaxAge()/10);
            }
        }

        if (this.getAge() > saplingTime) { // if its grown for a bit then it can bear fruits
            int porabolaNutritionValue = (int) Math.round(((-(2 * Math.sqrt(30) + 11)/500) * Math.pow((this.getAge() - 600 + 100 * Math.sqrt(30)), 2) + 120)); // porabolic nutritional equation to have the fruit tree bear more valueable fruits in the middle of its life

            if (porabolaNutritionValue > 0) {
                this.setNutrition(porabolaNutritionValue);
            }

        }
    }


    /**
     * getNutriton
     * returns the nutritional value
     * @return - the nutrition value
     */
    public int getNutrition() {
        return nutrition;
    }

    /**
     * getFruitTimer
     * will return the fruit timer
     * @return the time the fruit timer is at
     */
    public int getFruitTimer() {
        return fruitTimer;
    }

    /**
     * getSaplingTime
     * will return the saplingTime
     * @return the sapling time
     */
    public int getSaplingTime() {
        return saplingTime;
    }

    /**
     * setFruitTimer
     * will set the fruitTimer to the given value
     */
    public void setFruitTimer() {
        this.fruitTimer = this.fullSaplingTime;
    }

    /**
     * setNutrition
     * will set the nutrition to the given value
     * @param nutrition - the new nutrition
     */
    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

}
