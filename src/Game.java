/**
 * File Name - [Game.java]
 * Description - This is the game object where the whole game will take place
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

 import java.util.Random;

 public class Game {

     private final int width, length;
     private final double grassThreshHold;
     private final int initialHumans;
     private final int initialZombies;
     private Tile[][] map;
     private final int fruitTreeMaxAge;
     private final int plantMaxAge;
     private final int zombieTickHealth;
     private final int humanTickHunger;
     private final int humanTickHealth;
     private final int humanStarvingHunger;
     private final int humanHealth;
     private final int zombieHealth;
     private final int humanMaxAge;
     private final int zombieMaxAge;

     /**
      * Game
      * constructor initializes a game with the specified parameters:
      * @param - width - The width of the game grid
      * @param - length - The length of the game grid
      * @param - grassThreshHold - The threshold value for grass growth
      * @param - initialHumans - The number of initial human
      * @param - initialZombies - The number of initial zombie
      * @param  - fruitTreeMaxAge - The maximum age of a fruit tree
      * @param - plantMaxAge - The maximum age of a plant entity
      * @param - zombieTickHealth - The health deduction for zombies per tick
      * @param - humanTickHunger - The hunger increment for humans per tick
      * @param - humanTickHealth - The health increment for humans per tick
      * @param - humanStarvingHunger - The hunger value at which humans start starving
      * @param - humanHealth - The initial health value for humans
      * @param - zombieHealth - The initial health value for zombies
      * @param - humanMaxAge - The maximum age of a human
      * @param - zombieMaxAge - The maximum age of a zombie
      */

     Game(int width, int length, double grassThreshHold, int initialHumans, int initialZombies, int fruitTreeMaxAge, int plantMaxAge, int zombieTickHealth, int humanTickHunger, int humanTickHealth, int humanStarvingHunger, int humanHealth, int zombieHealth, int humanMaxAge, int zombieMaxAge) {

         this.width = width;
         this.length = length;
         this.grassThreshHold = grassThreshHold;
         this.initialHumans = initialHumans;
         this.initialZombies = initialZombies;
         this.map = new Tile[width + 2][length + 2];
         this.fruitTreeMaxAge = fruitTreeMaxAge;
         this.plantMaxAge = plantMaxAge;
         this.zombieTickHealth = zombieTickHealth;
         this.humanTickHunger = humanTickHunger;
         this.humanTickHealth = humanTickHealth;
         this.humanStarvingHunger = humanStarvingHunger;
         this.humanHealth = humanHealth;
         this.zombieHealth = zombieHealth;
         this.humanMaxAge = humanMaxAge;
         this.zombieMaxAge = zombieMaxAge;

         initiateGame(); // will just spawn the initial humans, grass, zombies and boundaries
     }


     /*
     * runGame
     * will run the game loop
      */
     public void runGame() {

         MatrixDisplayWithMouse grid = new MatrixDisplayWithMouse("CheerVille", this); // will start off the panal for the games visuals

         while(true) {
             //Display the grid on a Panel
             grid.refresh();

             //Small delay
             try{Thread.sleep(50); } catch(Exception e) {}

             updateTileIndexs(); // will update all of the x and y of the tiles
             moveItemsOnGrid(); // moves all moveables

             updateTileIndexs();// will update all of the x and y of the tiles
             spawnGrass(); // spawns in some grass

             updateTileIndexs();// will update all of the x and y of the tiles
             spawnFruitTrees(); // spawns in some trees

             updateTileGeneral(); // will update general stats about objects
             resetAllMoved(); // resets a value within all Moveable objects back to false so they can be moved again

             //Display the grid on a panel
             grid.refresh();
         }

     }

     /*
     * moveItemsOnGrid
     * will find all moveable items, and if they havent been moved, it will move em
      */
     public void moveItemsOnGrid() {

         Tile[][] proximity; // the 2d array which will the the vision for the moveable object

         for (Tile[] row : this.map) {
             for (Tile currentTile : row) {

                 if (currentTile instanceof Moveable) {
                     Moveable currentMoveable = (Moveable) currentTile; // casting it to a moveable

                     if (!((currentMoveable).getMoved())) { // if a boolean value in it is false - if it hasnt been moved
                         proximity = generateProximity(currentMoveable); // calls up method to actually create the 2d array of vision around object
                         currentMoveable.move(proximity, this.map); // calls the overridden move method in the moveable object
                     }

                     currentMoveable.setMoved(true);
                 }

             }
         }

     }

     /**
      * resetAllMoved
      * will reset all of the moved boolean values in the map to false
      */
     public void resetAllMoved() {
         for (Tile[] row : this.map) {
             for (Tile currentTile : row) {

                 if (currentTile instanceof Moveable) {
                     ((Moveable) currentTile).setMoved(false);
                 }

             }
         }
     }

     /**
      * initiateGame
      * will spawn in all the initial things, like the initial humans, zombies, trees, walls
      */

     public void initiateGame() {
         spawnWalls();
         spawnDirt();
         spawnHumans();
         spawnZombies();
     }

     /**
      * spawnDirt
      * this will spawn all of the dirt on the empty board
      */

     public void spawnDirt() {

         for(int i = 0; i < this.map[0].length;i++) {
             for(int j = 0; j < this.map.length;j++) {
                 if (this.map[i][j] == null) {
                     this.map[i][j] = new Dirt(i, j, plantMaxAge);
                 }
             }
         }

     }

     /**
      * spawnWalls
      * This method will outline the game in a wall
      */

     public void spawnWalls() {

         for(int i = 0; i < this.map[0].length;i++) {
             for(int j = 0; j < this.map.length;j++) {
                 if ((i == 0) || (i == this.map.length - 1) || (j == 0) || (j == this.map[i].length - 1)) { // if the tile is at the very top, very bottom, very left or very right
                     this.map[i][j] = new Rock(i, j);
                 }
             }
         }

     }

     /**
      * spawnGrass
      * will take many things into account, and if they are all positive, it will spawn in a grass Tile
      */

     public void spawnGrass() {

         double numGrass;
         int totalGrass = totalGrass();

         if (totalGrass < (width * length) / 3) {
             for(int i = 1; i < this.map[1].length - 1; i++) {
                 for(int j = 1; j < this.map.length - 1; j++) {

                     if (this.map[i][j] instanceof Dirt) {

                         Dirt currentDirt = (Dirt) this.map[i][j]; // casting into dirt
                         numGrass = checkForGrass(currentDirt); // checks surroundings for the number of adjacent grass

                         if (Math.random() > (this.grassThreshHold - numGrass)) { // if the random number generated is larger than the threshhold - the number of numGrass in its vicinity (makes grass spawn around itself more often than just randomly and spread like a tumor)
                             map[i][j] = new Plant(i, j, currentDirt.getAge(), plantMaxAge); // spawns in a new grass at the dirts, x and y, with its initial nutritional value at the dirts age
                         }
                     }

                 }
             }
         }
     }

     /**
      * checkForGrass
      * checks the surroundings for the number of grass
      * return@ - the number of surrounding grass
      */

     public double checkForGrass(Dirt tile) {
         double countGrass = 0;

         Tile[][] proximity = generateProximity(tile); // creates the proximity or vision around the Tile

         for (Tile[] row : proximity) {
             for (Tile currentTile : row) {
                 if (currentTile instanceof Plant) {
                     countGrass += 0.05; // adds to the value of countGrass if the currentTile is grass
                 }
             }
         }

         return countGrass;

     }

     /**
      * totalGrass
      * will just find the number of grass on the map
      * param - tile - the tile around which the method is to hceck for grass
      * return@ - the number of grass found
      */

     public int totalGrass() {

         int totalGrass = 0;

         for (Tile[] row: this.map) {
             for (Tile currentTile : row) {
                 if (currentTile instanceof Plant) { // if its a plant object then add to total grass
                     totalGrass++;
                 }
             }
         }

         return totalGrass;
     }

     /**
      * spawnFruitTrees
      * will spawn in furit trees
      */

     public void spawnFruitTrees() {

         Random rand = new Random();

         int area, x, y;
         int initialNutrition;

         area = (this.getLength() * this.getWidth())/10000; // the number of fruitTrees there should be on a map,

         for (int i = 0; i < area; i++) {

             x = rand.nextInt(length - 2); // random x
             y = rand.nextInt(width - 2); // random y

             x++;
             y++;



             if (map[y][x] instanceof Dirt) {
                 Dirt replacedDirt = (Dirt) map[y][x];
                 initialNutrition = replacedDirt.getAge(); // gets the age of dirt as the initial nutrition value of the tree - (to immitate like how fertilized a TIle is )
             } else if (map[y][x] instanceof Plant) {
                 Plant replacedPlant = (Plant) map[y][x];
                 initialNutrition = replacedPlant.getAge();// gets the age of Plant  as the initial nutrition value of the tree - (to immitate like how fertilized a TIle is )
             } else {
                 initialNutrition = 0;
             }

             if ((y > (width/4)*3) || (y < width/4)) { // only spawns in trees in the northern quarter and southern quarter of the map
                 this.map[y][x] = new FruitTree(x, y, fruitTreeMaxAge, initialNutrition);
             } 
         }
     }

     /**
      * generateProximity
      * generates/copies a Tiles surroundings
      * return@ - the generated proximity
      */

     public Tile[][] generateProximity(Tile currentThing) {

         int collumX = 0;
         int rowY = 0;
         int sightDistance = currentThing.getProximity();

         int diameter = (sightDistance * 2) + 1; // finds th diameter of proxmiity based on the proximity/distance an object can see

         Tile[][] proximity = new Tile[diameter][diameter]; // initiates the proximity 2d array

         for (int i = (currentThing.getY() - sightDistance); i <= (currentThing.getY() + sightDistance); i++) { // cuts out the y of the map that is the y of the object - its sight abd + its sight
             for (int j = (currentThing.getX() - sightDistance); j <= (currentThing.getX() + sightDistance); j++) {// cuts out the x of the map that is the x of the object - its sight abd + its sight

                 proximity[rowY][collumX] = this.map[i][j]; // copies out every single tile that is in every direction of sight from the object

                 collumX++;
             }
             collumX = 0;
             rowY++;
         }

         return proximity; // the 2d array
     }

     /**
      * spawnHumans
      * will spawn in the initial number of numans with given stats (randomized a little bit )
      * param@ - currentThing - the current tile that we are generating proximity around
      * return@ - returns the generated proximity
      */

     public void spawnHumans() {
         int maxRow = (this.map.length - 2); // max row index for human
         int maxCol = (this.map[0].length - 2); // max column index for human

         int row, column;

         Random rand = new Random();

         for (int i = 0; i < this.initialHumans; i++) {
             row = rand.nextInt(maxRow); // random y
             column = rand.nextInt(maxCol); // random x
             row++;
             column++;

             if (this.map[row][column] instanceof Dirt) {
                 createRandomHuman(row, column); // sends the xreateRandomHuman the random x and y
             } else { // if its not a dirt - then do it again
                 i--;
             }
         }
     }

     /**
      * spawnZombies
      * spawns in the initial zombies
      */

     public void spawnZombies() {
         int maxRow = (map.length - 2); // max row index for zombie
         int maxCol = (map[0].length - 2); // max column

         int row, column;

         Random rand = new Random();

         for (int i = 0; i < this.initialZombies; i++) {
             row = rand.nextInt(maxRow); // random y
             column = rand.nextInt(maxCol); // random x
             row++;
             column++;

             if (this.map[row][column] instanceof Dirt) {
                 createRandomZombie(row, column); // create slightly randomized zombie
             } else {
                 i--; // if we are trying to spawn in a zombie where there is already something, dont and run the random cords generation again
             }
         }
     }

     /**
      * createRandomZombie
      * creates a slightly random zombie
      *  param@ - row - the y index of where to create the zombi
      *  param@ - column - the x index of where to make the xombie
      */

     public void createRandomZombie(int row, int column) {

         Random rand = new Random();
         int proximity, health, maxAge;

         proximity = 1;

         health = rand.nextInt(3) + this.zombieHealth; // slightly random health
         maxAge = rand.nextInt(5) + this.zombieMaxAge; // slightly random max age

         this.map[row][column] = new Zombie(column, row, proximity, health, maxAge, zombieTickHealth); // creates the new zombie
     }

     /**
      * createRandomHuman
      * will create a slightly random human
      *  param@ - row - the y index of where to create the human
      *  param@ - column - the x index of where to make the human
      */

     public void createRandomHuman(int row, int column) {

         Random rand = new Random();

         int proximity, health, maxAge, hunger;
         boolean gender;

         proximity = 1;
         health = rand.nextInt(3) + humanHealth; // slightly randomized health
         maxAge = rand.nextInt(3) + humanMaxAge; // slightly randomized max age
         gender = rand.nextBoolean(); // random gender
         hunger = 0;

         this.map[row][column] = new Human(column, row, proximity, health, maxAge, gender, hunger, this.humanTickHunger, this.humanTickHealth, this.humanStarvingHunger);
     }

     /**
      * updatTileIndexs
      * will list through the map and correct all the x and y values of the objects
      */

     public void updateTileIndexs() {

         for (int i = 0; i < (this.map.length); i++) {
             for (int j = 0; j < (this.map[i].length); j++ ) {
                 this.map[i][j].setX(j); // updates x of object to the map j
                 this.map[i][j].setY(i); // updates y of obejct to the map i
             }
         }
     }

     /**
      * updateTileGeneral
      * will list through the map and call the overridden update method of the object
      */

     public void updateTileGeneral() {

         for (Tile[] row : this.map) {
             for (Tile currentTile : row) {
                 if (currentTile instanceof Human) {
                     Human currentHuman = (Human) currentTile; // if human cast into human
                     currentHuman.updateHuman(map); // call update
                 } else if (currentTile instanceof Plant) {
                     Plant currentPlant = (Plant) currentTile; // if plant, cast as plant
                     currentPlant.updatePlant(map);// call update
                 } else if (currentTile instanceof FruitTree) {
                     FruitTree currentTree = (FruitTree) currentTile; // if tree, cast as a tree
                     currentTree.updateFruitTree(map); // call update
                 } else if (currentTile instanceof Zombie) {
                     Zombie currentZombie = (Zombie) currentTile; // if zombie, cast as zombie
                     currentZombie.updateZombie(map); // call update
                 } else if (currentTile instanceof Dirt) {
                     Dirt currentDirt = (Dirt) currentTile; // if dirt, cast as dirt
                     currentDirt.updateDirt(); //  call update
                 }

             }
         }


     }

     /**
      * getWidth
      * retuns width
      * @return The width of the map
      */
     public int getWidth() {
         return width;
     }

     /**
      * getLength
      * retuns the length
      * @return
      */
     public int getLength() {
         return length;
     }

     /**
      * getMap
      * gets the map
      * @return the map
      */
     public Tile[][] getMap() {
         return this.map;
    }

 }