/**
 * File Name - [Moveable.java]
 * Description - abstract moveable class
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

import java.util.ArrayList;
import java.util.Random;

public abstract class Moveable extends Tile{
    private int health;
    private boolean moved;

    /**
     * Moveable
     * constructor which creates a moveable object
     * @param x - the j index on map
     * @param y - the i index on map
     * @param proximity - the sight distance/proximity
     * @param health -  the health of object
     * @param maxAge -  the maxage of object
     */

    public Moveable(int x, int y, int proximity, int health, int maxAge) {
        super(x, y, proximity, maxAge);
        this.health = health;
        this.moved = false;
    }

    /**
     * findNumberWanted
     * finds the number of wanted tile object types in the proximity 2d array
     * @param proximity - the 2d array of its immediate vision - surroundings
     * @param wantedList - an arraylsit to add all of the found wantedobjects too
     * @param wantedTile - the type of tyle that the object is looking for
     * @param interactingTile - the object that has called findNumberWanted
     * @return - the number of found wantedTiles (length of wantedList)
     */
    public int findNumberWanted(Tile[][] proximity, ArrayList<Tile> wantedList, Tile wantedTile, Tile interactingTile) {

        for (Tile[] row : proximity) {
            for (Tile currentTile : row) {
                if (wantedTile.getClass().isInstance(currentTile)) { // if the tile we are at is the same class as the wanted tile

                    if (interactingTile instanceof Human) {
                        Human interactingHuman = (Human) interactingTile; // casting into human

                        if (wantedTile instanceof Human)  {
                            Human someOtherHuman = (Human) currentTile; // casting into a human
                            if (someOtherHuman.getAge() > (someOtherHuman.getMaxAge()/someOtherHuman.getLegalRatio())) { // if the human is of legal age
                                if (interactingHuman.getGender() != someOtherHuman.getGender() ) { // if the two humans are different genders
                                    wantedList.add(currentTile); // adds to arrayList
                                }
                            }

                        } else if (wantedTile instanceof Plant) {
                            wantedList.add(currentTile);

                        } else if (wantedTile instanceof FruitTree) {
                            FruitTree currentFruitTree = (FruitTree) currentTile;
                            if (currentFruitTree.getFruitTimer() == 0) { // if the fruitTimer is 0 (if there is fruit to eat)
                                if (currentFruitTree.getAge() > currentFruitTree.getSaplingTime()) {
                                    wantedList.add(currentTile);
                                }
                            }

                        } else if (wantedTile instanceof Dirt) {
                            wantedList.add(currentTile);

                        } else {
                            // it doesnt get here
                        }

                    }  else if ((wantedTile instanceof Human) && (interactingTile instanceof Zombie))  { // if zombie lookinf for human
                        wantedList.add(currentTile);

                    } else {
                        // doesnt get here
                    }
                }
            }
        }

        return wantedList.size();
    }

    /**
     * moveRandom
     * will randomly choose a direction to do
     * @param map - the map
     */
    public void moveRandom(Tile[][] map) {
        Random rand = new Random();

        int randDirection = rand.nextInt(4); // random number from 0-3

        if (randDirection == 0) {
            if (!(map[this.getY() + 1][this.getX()] instanceof Rock ) && (!(map[this.getY() + 1][this.getX()] instanceof Zombie)))  { // if above this object is not a rock or zombi
                map[this.getY() + 1][this.getX()] = this;
            }
            setDirt(map, this.getX(), this.getY(), (this.getProximity()*10)); // leaves dirt behind

        } else if (randDirection == 1) {
            if (!(map[this.getY() - 1][this.getX()] instanceof Rock) && (!(map[this.getY() - 1][this.getX()] instanceof Zombie)))  { // if below this object isnt a rock of zombie
                map[this.getY() - 1][this.getX()] = this;
            }
            setDirt(map, this.getX(), this.getY(), (this.getProximity()*10)); //leavse dirt behind

        } else if (randDirection == 2) {
            if (!(map[this.getY()][this.getX() + 1] instanceof Rock) && (!(map[this.getY()][this.getX() + 1] instanceof Zombie)))  { // if to the right isnt a rock or zombie
                map[this.getY()][this.getX() + 1] = this;
            }
            setDirt(map, this.getX(), this.getY(), (this.getProximity()*10)); // leaves dirt behind

        } else if (randDirection == 3) {
            if (!(map[this.getY()][this.getX() - 1] instanceof Rock) && (!(map[this.getY()][this.getX() - 1] instanceof Zombie)))  { // if to the left isnt a rock or zombie
                map[this.getY()][this.getX() - 1] = this;
            }
            setDirt(map, this.getX(), this.getY(), (this.getProximity()*10)); // leaves dirt behind
        }

    }

    /**
     * setDirt
     * sets the tile at i, j to dirt on the map
     * @param map - the map
     * @param j - the j index
     * @param i - the i index
     */
    public void setDirt(Tile[][] map, int j, int i, int dirtMaxAge) {
        map[i][j] = new Dirt(i, j, dirtMaxAge);
    }

    /**
     * move
     * abstract - will move the object or initiate interactions
     * @param proximity - the nearby vision objects
     * @param map - the map
     */
    public abstract void move(Tile[][] proximity, Tile[][] map);

    /**
     * interact
     * abstract - will have differeny outcomes for different types of interactions
     * @param proximity - the nearby vision/proximity
     * @param otherObject - the other object which is being interacted wiht
     * @param map - the map
     * @param numberWanted - the number of foudn wanted tiles
     */
    public abstract void interact(Tile[][] proximity, Tile otherObject, Tile[][] map, int numberWanted);


    /**
     * getHealth
     * retursn the health
     * @return- the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * getMoved
     * will return if the object has moved
     * @return - moved
     */
    public boolean getMoved() {
        return moved;
    }

    /**
     * setHealth
     * will set the health of an object
     * @param - health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * setMoved
     * will set the moved variable to true or false
     * @param moved
     */

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}

