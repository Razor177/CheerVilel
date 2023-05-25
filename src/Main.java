/**
 * File Name - [Main.java]
 * Description - It will just ask the user if they want default or custom settings and pass in those values to the params when initiating Game
 * @Author - Michael Khart
 * @Date - May 18, 2023
 */

import java.util.Scanner;

 class Main {
     public static void main(String[] args) {

         int width;
         int length;
         double grass;
         int initialHumans;
         int initialZombies;
         int fruitTreeMaxAge;
         int plantMaxAge;
         int zombieTickHealth;
         int humanTickHunger;
         int humanTickHealth;
         int humanStarvingHunger;
         int humanHealth;
         int zombieHealth;
         int humanMaxAge;
         int zombieMaxAge;


         Scanner input = new Scanner(System.in); // just to let user pick between custom and default settings
         String userChoice;


         System.out.println("Welcome to CheerVille ");
         System.out.println("");
         System.out.println("Would u like to customize game settings or play with default ones? If you would like to customise settings, enter ''Continue'', otherwise, enter ''Default'' ");

         userChoice = input.next();

         while (!(userChoice.equalsIgnoreCase("Custom")) && !(userChoice.equalsIgnoreCase("Default"))) { // while they havent entered default or custom
             System.out.println("Please either enter ''Custom'' to set custom settings or enter ''Default'' if you would like to run default");
             userChoice = input.next();
         }

         if (userChoice.equalsIgnoreCase("Custom")) {
             System.out.println("You will now be prompted on game settings to edit");
             System.out.println("");

             System.out.println("What should the width/length of the game be? (int)");
             int dimentions = input.nextInt();
             width = dimentions;
             length = dimentions;

             System.out.println("What should the base random chance of grass spawning on a tile be? (double)");
             grass = input.nextDouble();

             System.out.println("What should the inital number of humans be? (int)");
             initialHumans = input.nextInt();

             System.out.println("What should the initial number of zombies be? (int)");
             initialZombies = input.nextInt();

             System.out.println("What should humans health be? (int)");
             humanHealth = input.nextInt();

             System.out.println("What should zombie health be? (int)");
             zombieHealth = input.nextInt();

             System.out.println("What should the max age for a human be? (int)");
             humanMaxAge = input.nextInt();

             System.out.println("What should the max age for a zombie be? (int)");
             zombieMaxAge = input.nextInt();

             System.out.println("What should the tick of damage to a zombie be? (int)");
             zombieTickHealth = input.nextInt();

             System.out.println("What should the tick hunger for humans be? (int)");
             humanTickHunger = input.nextInt();

             System.out.println("What should the tick of damage to a human be? (int)");
             humanTickHealth = input.nextInt();

             System.out.println("At what hunger should a human start to take damage? (int)");
             humanStarvingHunger = input.nextInt();

             System.out.println("What should the maximum age of a fruit tree be? (int)");
             fruitTreeMaxAge = input.nextInt();

             System.out.println("What should the max age of plants be? (int)");
             plantMaxAge = input.nextInt();


             System.out.println("Game Initiated - Custom Settings");


         } else {
             System.out.println("Game Initiated - Default Settings");

             width = 250;
             length = 250;
             grass = 0.99;
             initialHumans = 250;
             initialZombies = 100;
             fruitTreeMaxAge = 100;
             plantMaxAge = 10;
             zombieTickHealth = 2;
             humanTickHunger = 7;
             humanTickHealth = 1;
             humanStarvingHunger = 100;
             humanHealth = 100;
             zombieHealth = 100;
             humanMaxAge = 50;
             zombieMaxAge = 100;


         }

        Game attempt = new Game(length, width, grass, initialHumans, initialZombies, fruitTreeMaxAge, plantMaxAge, zombieTickHealth, humanTickHunger, humanTickHealth, humanStarvingHunger, humanHealth, zombieHealth, humanMaxAge, zombieMaxAge); // passes in all custom or default settings for the game
        attempt.runGame();

        input.close();
     }

 }