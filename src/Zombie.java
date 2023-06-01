/**
 * File Name - [Zombie.java]
 * Description -  zombie class
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

import java.util.ArrayList;
import java.util.Random;

public class Zombie extends Moveable {

    private final int tickAge = 1;
    private final int tickHealth;
    private final int zombieInitialHealth;


    /**
     * Zombie
     * constructor for Zombie class
     * @param x - x index
     * @param y - y index
     * @param proximity - how far the object can see
     * @param health - the health
     * @param maxAge - the max Age
     * @param zombieTickHealth - the damage per tick
     */
    public Zombie(int x, int y, int proximity, int health, int maxAge, int zombieTickHealth) {
        super(x, y, proximity, health, maxAge);
        this.tickHealth = zombieTickHealth;
        this.zombieInitialHealth = health;
    }


    /**
     * move
     * pathfinding/decision making for the Zombie
     * @param proximity - the nearby vision objects
     * @param map - the map
     */
    public void move(Tile[][] proximity, Tile[][] map) {

        ArrayList<Tile> wantedList = new ArrayList<>();
        Tile decision;
        int numberWanted, choice;

        Human wantedTile = new Human();

        Random rand = new Random();

        numberWanted = findNumberWanted(proximity, wantedList, wantedTile, this); // finds all of the humans in proximity and adds the to array

        if (numberWanted > 0) { // if there is humans inside the proximity
            choice = rand.nextInt(numberWanted);  // random int from 0 to the sizee of the arraylist of the humans
            decision = wantedList.get(choice); // chooses a index (random human ) from the arrayList of humans

            this.interact(proximity, decision, map, numberWanted);
        } else {
            this.moveRandom(map); // if there is no humans in its proximity, the zombie moves randomly
        }

    }


    /**
     * interact
     * this method will have different outcomes and affects depending on the params
     * @param proximity - the nearby vision/proximity
     * @param otherObject - the other object which is being interacted wiht
     * @param map - the map
     * @param numberWanted - the number of foudn wanted tiles
     */

    public void interact(Tile[][] proximity, Tile otherObject, Tile[][] map, int numberWanted) {

        Human targetHuman = (Human) otherObject;

        if (this.getHealth() > targetHuman.getHealth()) { // if the zombies health is greate r than the humans health
            this.setHealth(this.getHealth() + targetHuman.getHealth()); // add the humans health to the xombies health
            map[targetHuman.getY()][targetHuman.getX()] = this; // moves the zombie onto the map where the human was
            setDirt(map, this.getX(), this.getY(), this.getMaxAge()/10); // leave dirt where we were

        } else if (this.getHealth() <= targetHuman.getHealth()) { // if the zombies health is lower than or equal to the humans health
            createZombie(map, targetHuman); // infect the human
        }
    }

    /**
     * createZombie
     * infects the human and will turn it into a zombie
     * @param map - the map
     * @param zombieTarget - the human which the xombie is about to infect
     */
    public void createZombie(Tile[][] map, Human zombieTarget) {

        int x, y, proximity, health, maxAge;

        proximity = zombieTarget.getProximity(); // gets the humans proximity - sight distance and sets it to the ozmbies health
        health = (zombieTarget.getHealth()) + (zombieTarget.getHealth()/5); // gets the humans health (and adds a fifth) and sets the zombies health to it
        x = zombieTarget.getX(); // sets zombies x to humans x
        y = zombieTarget.getY();// sets zombies y to humans y

        maxAge = (zombieTarget.getMaxAge()/3) * 2; // turns the zombies max health to two thirds of the humans

        map[zombieTarget.getY()][zombieTarget.getX()] = new Zombie(x, y, proximity, health, maxAge, this.tickHealth); // creates zombies
    }

    /**
     * updateZombie
     * will update specs of teh zombie
     * @param map - the map
     */
    public void updateZombie(Tile[][] map) {

        this.setAge(this.getAge() + tickAge);
        this.setHealth(this.getHealth() - tickHealth);

        if (this.getHealth() > (zombieInitialHealth) * 1.3) { // if the zombies health is greater than a third more than its initial health
            this.setHealth((zombieInitialHealth/4) * 3); // sets the health down to three fourths
        }


        if ((this.getAge() > this.getMaxAge()) || (this.getHealth()) < 0) { // if its age is greater than the max or if the health is less than 0
            this.setDirt(map, this.getX(), this.getY(), (this.getMaxAge()/10));
        }

    }

}
