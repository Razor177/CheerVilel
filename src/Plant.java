/**
 * File Name - [Plant.java]
 * Description -  Plant class
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

import java.util.Random;

public class Plant extends UnMoveable{
    private int nutrition;

    /**
     * Plant
     * constructor - creates an impossible plant which is just used for deciison making, never actually implemented in game
     */
    public Plant() {
        super(-1, -1, -1);
    }

    /**
     * Plant
     * constructor - creates a plant whichll actually be used in game
     * @param x - the x index
     * @param y - the y index
     * @param nutrition - the initial nutrition of the Plant
     * @param plantMaxAge - the max age of plant
     */
    public Plant(int x, int y, int nutrition, int plantMaxAge) {
        super(x, y, plantMaxAge);
        this.nutrition = nutrition;
    }

    /**
     * updatePlant
     * will update plant specs
     * @param map - the map
     */
    public void updatePlant(Tile[][] map) {
        Random rand = new Random();

        this.setAge(this.getAge() + 1);

        if (this.getAge() > getMaxAge()) {
            if (rand.nextBoolean()) { // randomizes plant death a little bit so that if a plot grew in all at once, it would die in a small spread
                map[this.getY()][this.getX()] = new Dirt(this.getY(), this.getX(), this.getMaxAge());
            }
        }

        int porabolaNutritionValue = ((((-3) * (this.getAge() - 5)^2 )/5) + 20); // parabolic nutritional value which will give plants the most nutritional value at their prime, and have less value early and late in life

        if (porabolaNutritionValue > 0) {
           this.setNutrition(porabolaNutritionValue);
        }
    }


    /**
     * getNutrition
     * retuns the nutrition
     * @return the nutrition
     */
    public int getNutrition() {
        return nutrition;
    }


    /**
     * setNutrition
     * sets the nutrition o given value
     * @param nutrition - the new nutrition
     */
    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }
}
