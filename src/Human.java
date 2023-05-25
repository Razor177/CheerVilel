/**
 * File Name - [Human.java]
 * Description - the human class
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

import java.util.ArrayList;
import java.util.Random;

public class Human extends Moveable{

    private final boolean gender;
    private int hunger;
    private final int tickAge = 1;
    private final int tickHunger;
    private final int tickHealth;
    private final int legalRatio = 8; // this is the value at which division of the maxAge will result in the legal age, E.G. if max age is 80, then 80/8 = 10, thus the legal age would be 10.
    private final double elderlyAge = 0.90d;
    private final int starvingHunger;
    private final int humanInitialHealth;


    /**
     * Human
     * constructor - creates a human with impossible values - wont be used in the game itself, just in pathfinding for humans and zombies
     * @param - x - the x index
     * @param - y - the y index
     * @param - proximity - the proximity distance
     * @param - health  - the health of the human
     * @param - maxAge - the max age a human can like to
     */
    public Human() {
        super(-1, -1, -1, -1, -1);
        this.tickHunger = -1;
        this.starvingHunger = -1;
        this.tickHealth = -1;
        this.humanInitialHealth = -1;
        this.gender = false;
    }

    /**
     * Human
     * creates a human
     * @param x - x index
     * @param y - y index
     * @param proximity - the proximity distance
     * @param health - the health of the human
     * @param maxAge - the mas age
     * @param gender - the gender
     * @param hunger - the hunger
     * @param tickHunger - the hunger a human will gain per tick
     * @param tickHealth - the health loss a human will experience per tick
     * @param starvingHunger - the hunger at which a human will take damage
     */

    public Human(int x, int y, int proximity, int health, int maxAge, boolean gender, int hunger, int tickHunger, int tickHealth, int starvingHunger) {
        super(x, y, proximity, health, maxAge);
        this.gender = gender;
        this.hunger = hunger;
        this.tickHunger = tickHunger;
        this.tickHealth = tickHealth;
        this.starvingHunger = starvingHunger;
        this.humanInitialHealth = health;
    }


    /**
     * move
     * it will, based on the objects hunger and other stats, find a wanted class. IT will then either initiate a interact method with that tyle, or it will move the object diretcly onto the tile.
     * @param proximity - the nearby vision objects
     * @param map - the map
     */
    public void move(Tile[][] proximity, Tile[][] map) {

        int numberWanted;
        int choice;
        Tile decision;
        ArrayList<Tile> wantedList = new ArrayList<>();
        Tile wantedTile;

        if (this.getAge() > this.getMaxAge()/legalRatio) { // if our human is of legal age
            wantedTile = new Plant();

            if (this.getHunger() < this.getStarvingHunger()/2) { // if the human isnt too hungry
                wantedTile = new Human();
            }

        } else if (this.getHunger() >= this.getStarvingHunger()/2) { // if the human is hungry
            wantedTile = new Plant();
        } else {
            wantedTile = new Dirt();
        }

        Random rand = new Random();

        if (wantedTile instanceof Human) {

            numberWanted = findNumberWanted(proximity, wantedList, wantedTile, this); // finds the number of wanted tiles in its proximity

            if (numberWanted > 0) { // if there are tiles that the human wanted
                choice = rand.nextInt(numberWanted); // random int from 0 to the length of the wantedList (numberWanted)
                decision = wantedList.get(choice); // the random index in the watnedList is the decision of the human

                this.interact(proximity, decision, map, numberWanted); // interact is called with the chosen object
            } else {
                wantedTile = new Plant();  // is there are no wanted human tiles in the proximity, the next logical want for the human is more food
            }
        }

        if (wantedTile instanceof Plant) {

            numberWanted = findNumberWanted(proximity, wantedList, wantedTile, this); // finds the number of wanted tiles in its proximity

            if (numberWanted > 0) { // if there are tiles that the human wanted
                choice = rand.nextInt(numberWanted);// random int from 0 to the length of the wantedList (numberWanted)
                decision = wantedList.get(choice);// the random index in the watnedList is the decision of the human

                this.interact(proximity, decision, map, numberWanted); // interaction with plant is run
            } else {
                wantedTile = new FruitTree(); // if there is no main food source of human, it wants the next best thing
            }
        }

        if (wantedTile instanceof FruitTree) {

            numberWanted = findNumberWanted(proximity, wantedList, wantedTile, this);// finds the number of wanted tiles in its proximity

            if (numberWanted > 0) {// if there are tiles that the human wanted
                choice = rand.nextInt(numberWanted); // random int from 0 to the length of the wantedList (numberWanted)
                decision = wantedList.get(choice); // the random index in the watnedList is the decision of the human

                this.interact(proximity, decision, map, numberWanted); // interaction between human and fruitTree is run

            } else {
                wantedTile = new Dirt(); // if there is no humans, or any food source in its vicinity, the human would want to move away from area to a hopefully better one
            }
        }

        if (wantedTile instanceof Dirt) {

            numberWanted = findNumberWanted(proximity, wantedList, wantedTile, this);// finds the number of wanted tiles in its proximity

            if (numberWanted > 0) { // if there are some wanted tiles in proximity
                choice = rand.nextInt(numberWanted);// random int from 0 to the length of the wantedList (numberWanted)
                decision = wantedList.get(choice);// the random index in the watnedList is the decision of the human

                this.interact(proximity, decision, map, numberWanted); // the human interacts with dirt

            } else {
                // stay
            }
        }


    }


    /**
     * interact
     * this method will, based on the otherObject, will run some interaction between the human and the other object, resulting in different outcomes
     * @param proximity - the nearby vision/proximity
     * @param otherObject - the other object which is being interacted wiht
     * @param map - the map
     * @param numberWanted - the number of foudn wanted tiles
     */

    public void interact(Tile[][] proximity, Tile otherObject, Tile[][] map, int numberWanted) {

        Random rand = new Random();

        if (otherObject instanceof Plant) {
            Plant otherObPlant = (Plant) otherObject; // casting the other object as a plant if its a plant

            this.setHunger(this.getHunger() - otherObPlant.getNutrition()); // human gains hunger based on plants nutrition (human eats it )

            map[otherObPlant.getY()][otherObPlant.getX()] = this; // human moves to the plant on the map
            setDirt(map, this.getX(), this.getY(), (this.getMaxAge()/10)); // dirt is left behind the human

        } else if (otherObject instanceof FruitTree) {
            FruitTree otherObFruitTree = (FruitTree) otherObject; // casting the other object as a fruittree if its a fruittree

            // we dont need to check if the plant is ripe or mature, because we do it in the findNumberWanted
            this.setHunger(this.getHunger() - otherObFruitTree.getNutrition()); // human gains hunger based on trees nutrition (human eats the fruit  )
            otherObFruitTree.setFruitTimer(); // reset the fruit timer - (like u ate the fruit and the tree needs some time to grow them back)

        } else if (otherObject instanceof Human) {

            Human otherHuman = (Human) otherObject;

            if ((this.getGender()) != (otherHuman.getGender()))  { // different genders needed to make babies

                ArrayList<Tile> suitableBabyStartingList = new ArrayList<>();
                int babyChoice;
                Tile babyDecision;
                boolean twins;

                // this will find all of the dirt and grass tiles - suitable starting locations
                for (Tile[] row : proximity) {
                    for (Tile currentTile : row ) {
                        if ((currentTile instanceof Dirt) || (currentTile instanceof Plant))  {
                            suitableBabyStartingList.add(currentTile);
                        }
                    }
                }


                if (suitableBabyStartingList.size() > 0) { // if there are suitable locations

                    if (suitableBabyStartingList.size() >= 2) {

                        twins = rand.nextBoolean(); // 50% chance for the humans to have twins (if there is 2 or more suitable locations)

                        if (twins) {
                            for (int i = 0; i < 2; i++) {
                                babyChoice = rand.nextInt(suitableBabyStartingList.size()); // random int from 0 to the number of suitable locations
                                babyDecision = suitableBabyStartingList.get(babyChoice); // babyDecision is the tile at index chosen above in the arrayLIst of possible tiles
                                suitableBabyStartingList.remove(babyChoice); // removes the suitable location from the arraylist so thta the twin wont spawn there

                                this.setHunger(this.getHunger() + tickHunger); // adult gains some hunger for having the kid
                                createBaby(map, babyDecision);
                            }

                        } else { // if no twins
                            babyChoice = rand.nextInt(suitableBabyStartingList.size()); // random int from 0 to the number of suitable locations
                            babyDecision = suitableBabyStartingList.get(babyChoice);// babyDecision is the tile at index chosen above in the arrayLIst of possible tiles

                            this.setHunger(this.getHunger() + tickHunger);// adult gains some hunger for having the kid

                            createBaby(map, babyDecision);
                        }

                    } else {
                        babyDecision = suitableBabyStartingList.get(0); // babyDecision is the tile at index chosen above in the arrayLIst of possible tiles

                        this.setHunger(this.getHunger() + tickHunger); // parent gains hunger
                        createBaby(map, babyDecision);
                    }

                } else { // if there is no suitable location, the baby takes the place of the human
                    createBaby(map, this);
                }

                otherHuman.setHunger(otherHuman.getHunger() + tickHunger);// othre parent also gains hunger
            }

        } else if (otherObject instanceof Dirt) {
            Dirt otherObDirt = (Dirt) otherObject;

            map[otherObDirt.getY()][otherObDirt.getX()] = this; // human moves to the dirt object
            setDirt(map, this.getX(), this.getY(), (this.getMaxAge()/10)); // leave dirt behind

        }
    }


    /**
     * createBaby
     * will create a somewhat random human
     * @param map
     * @param babyDecision
     */
    public void createBaby(Tile[][] map, Tile babyDecision) {

        Random rand = new Random();

        int vision, health, maxAge, hunger;
        boolean gender;

        vision = 1;
        health = rand.nextInt(3) + humanInitialHealth; // slightly random health
        maxAge = rand.nextInt(3) + getMaxAge(); // slightly random maxAge
        gender = rand.nextBoolean(); // random gender
        hunger = 0;

        map[babyDecision.getY()][babyDecision.getX()] = new Human(babyDecision.getX(), babyDecision.getY(), vision, health, maxAge, gender, hunger, this.tickHunger, this.tickHealth, this.starvingHunger);
    }


    /**
     * updateHuman
     * will update human specs
     * @param map - the game map
     */
    public void updateHuman(Tile[][] map) {

        this.setAge(this.getAge() + tickAge); // adds to age

        this.setHunger(this.getHunger() + tickHunger); // hungrier over time

        if ((this.getHunger() > (this.getStarvingHunger()/6)) || (this.getAge() > this.getMaxAge() * elderlyAge)) { // if humans hunger is getting close to the starvingHunger, or if its age is in the elderly age range
            this.setHealth(this.getHealth() - tickHealth);
        }

        if (this.getHunger() < 0) { // if it has eaten so much food its hunger is negative, gain back health and set hunger back to 0

            int extraNutrition = Math.abs(this.getHunger());
            this.setHunger(0);
            this.setHealth(this.getHealth() + extraNutrition);

        }


        if ((this.getHunger() > this.getStarvingHunger()) || (this.getHealth() < 0) || (this.getAge() > this.getMaxAge())){ // if its hunger is over starving hunger or its health lower than 0 or age is larger than max age, human hsould die
            setDirt(map, this.getX(), this.getY(), this.getMaxAge()/10);
        }




        }


    /**
     * setHunger
     * sets the hunger
     * @param hunger - new hunger
     */
    public void setHunger(int hunger) {

        this.hunger = hunger;
    }

    /**
     * getGender
     * returns the gender
     * @return - the gender
     */
    public boolean getGender() {

        return gender;
    }

    /**
     * getHunger
     * returns the hunger
     * @return - the hunger
     */
    public int getHunger() {

        return hunger;
    }

    /**
     * getStarvingHunger
     * returns the starving hunger
     * @return - the starving hunger
     */
    public int getStarvingHunger() {
        return starvingHunger;
    }

    /**
     * getLegalRatio
     * returns the denominator needed to find the legal age
     * @return - the legalRatio
     */
    public int getLegalRatio() {
        return this.legalRatio;
    }

}
