package org.csc133.a3;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.*;
import com.codename1.util.MathUtil;

import java.util.ArrayList;
import java.util.Random;

//-----------------------------------------------------------------------------
public class GameWorld {
    // Create an object of SingleObject
    //
    private static GameWorld instance;
    private Dimension worldSize;
    private Random r;

    // Variables
    //
    private int fuel;
    private int ticks;
    private int randomTicks;
    private String winLossText;
    private int building0Dmg;
    private int building1Dmg;
    private int building2Dmg;
    private int previousTotalBuildingDmg;
    private int totalBuildingDmg;
    private double totalValue;
    private double totalDmg;

    // Objects
    //
    private Building building;
    private Helicopter helicopter;
    private Helipad helipad;
    private River river;

    // ArrayLists
    //
    private ArrayList<Fire> fireCollection;
    private ArrayList<Building> buildingCollection;
    private ArrayList<GameObject> gameObjectCollection;
    private ArrayList<GameObject> gameObjectCollectionDelete;

    // Constants
    //
    private static final int MAX_FUEL = 25000;
    private static final int MAX_BUILDING = 3;
    private int FIRE_AREA_BUDGET = 1000;

    // Make the constructor private
    //
    private GameWorld(){}

    // Get the only object available
    //
    public static GameWorld getInstance(){
        if (instance == null)
            instance = new GameWorld();
        return instance;
    }

    public void init() {
        ticks = 0;
        this.r = new Random();
        fuel = MAX_FUEL;
        winLossText = "";
        river = new River(worldSize);
        helipad = new Helipad(worldSize);
        //helicopter = new Helicopter(worldSize, helipad);
        helicopter = new Helicopter(worldSize);
        fireCollection = new ArrayList<>();
        buildingCollection = new ArrayList<>();

        building0Dmg = 0;
        building1Dmg = 0;
        building2Dmg = 0;
        previousTotalBuildingDmg = 0;
        totalBuildingDmg = 0;
        totalValue = 0;
        totalDmg = 0;

        gameObjectCollection = new ArrayList<>();
        gameObjectCollectionDelete = new ArrayList<>();

        // Add game objects into gameObjectCollection
        //
        gameObjectCollection.add(river);
        gameObjectCollection.add(helipad);
        // Adding three building with at least 6 fires
        for (int i = 0; i < MAX_BUILDING; i++) {
            building = new Building(worldSize, i);
            buildingCollection.add(building);
            gameObjectCollection.add(building);
            // Create at least two fires in each building
            //
            for (int j = 0; j < 2; j ++) {
                Fire temp = new Fire(worldSize,i);
                fireCollection.add(temp);
                gameObjectCollection.add(temp);
                building.setFireInBuilding(temp);
            }
        }
        // Add the rest of the fires until it reach the fireAreaBudget
        //
        int i = 0;
        while (getTotalFireArea() < FIRE_AREA_BUDGET) {
            Fire temp = new Fire(worldSize, i);
            fireCollection.add(temp);
            gameObjectCollection.add(temp);
            building = buildingCollection.get(i);
            building.setFireInBuilding(temp);
            i++;
            // Loop back to building 0
            if (i >= MAX_BUILDING)
                i = 0;
        }
        gameObjectCollection.add(helicopter);
        for (Building b: buildingCollection)
            totalValue += b.getValue();
    }

    void tick() {
        ticks++;
        for (GameObject go: gameObjectCollection) {
            if (go instanceof Fire) {
                Fire f = (Fire) go;
                if (f.getIsBurning() == true) {     // if fire in burning state
                    randomTicks = 7 + r.nextInt(8);
                    if (ticks % randomTicks == 0)
                        f.grow();
                }
                //f.isOverFire(helicopter);
                if (f.getWasExtinguished()) {   // If the fire was extinguished,
                    f.Extinguished();           // put fire into extinguished state
                    gameObjectCollectionDelete.add(f);
                }
            }
        }
        if (!gameObjectCollectionDelete.isEmpty()) {
            gameObjectCollection.remove(gameObjectCollectionDelete.
                    get(gameObjectCollectionDelete.size() - 1));
            fireCollection.remove(gameObjectCollectionDelete.
                    get(gameObjectCollectionDelete.size() - 1));
        }

        for (Fire f: fireCollection) {
            int buildingId = f.getBuildingId();
            if (buildingId == 0) {
                building0Dmg += Math.PI * MathUtil.pow(f.getSize()/2, 2);
            } else if (buildingId == 1) {
                building1Dmg += Math.PI * MathUtil.pow(f.getSize()/2, 2);
            } else if (buildingId == 2) {
                building2Dmg += Math.PI * MathUtil.pow(f.getSize()/2, 2);
            }
        }

        for (Building b: buildingCollection) {
            if (b.getId() == 0) {
                b.updateBuildingDmg(building0Dmg);
                building0Dmg = 0;
            } else if (b.getId() == 1) {
                b.updateBuildingDmg(building1Dmg);
                building1Dmg = 0;
            } else if (b.getId() == 2) {
                b.updateBuildingDmg(building2Dmg);
                building2Dmg = 0;
            }
        }

        helicopter.move();
        //helicopter.checkDrinkable(river);
        helicopter.fuel();

        // GameClear/GameOver screen
        // Set some tick delay preventing gameWorld init() bug
/*        if (!(ticks <= 10)) {
            if (checkWinCondition() || checkRanOutFuel()
                    || checkBuildingDestroy())
                drawDialog();
        }*/
    }

    public int getFuel() {
        return fuel;
    }

    public int getDisplayHeading() {
        double displayHeading;
        double temp = 90 + Math.toDegrees(helicopter.getHeading());
        displayHeading = temp % 360;
        if (displayHeading < 0)
            displayHeading = displayHeading + 360;
        return (int)displayHeading;
    }

    public int getCurrentSpeed() {
        return helicopter.getSpeed();
    }

    public int getCurrentFireNo() {
        return fireCollection.size();
    }

    public int getTotalFireSize() {
        int totalFireSize = 0;
        for (Fire f: fireCollection) {
            totalFireSize += f.getSize();
        }
        return totalFireSize;
    }

    public int getTotalFireArea() {
        int totalFireArea = 0;
        for (Fire f: fireCollection) {
            totalFireArea += Math.PI * MathUtil.pow(f.getSize()/2, 2);
        }
        return totalFireArea;
    }

    public int getBuildingArea() {
        int buildingArea = 0;
        for (Building b: buildingCollection)
            buildingArea += b.getSize();
        return buildingArea;
    }

    public int getTotalDmg() {
        int totalFireSize = getTotalFireArea();
        int buildingArea = getBuildingArea();
        int currentTotalBuildingDmg = totalFireSize*100/buildingArea;
        if (currentTotalBuildingDmg > previousTotalBuildingDmg) {
            totalBuildingDmg = currentTotalBuildingDmg;
            previousTotalBuildingDmg = currentTotalBuildingDmg;
        }
        return totalBuildingDmg;
    }

    public int score() {
        int score = 0;
        score = 100 - getTotalDmg();
        return score;
    }

    public int getLoss(){
        totalDmg = getTotalDmg();
        double loss = totalDmg/100;
        double totalLoss = totalValue * loss;
        return (int)totalLoss;
    }

    public void setFuel(int f) {
        fuel -= f;
    }

    // Action controller
    //
    public void accelerate() {
        helicopter.accelerate();
    }

    public void decelerate() {
        helicopter.decelerate();
    }

    public void changeHeadingLeft() {
        helicopter.steerLeft();
    }

    public void changeHeadingRight() {
        helicopter.steerRight();
    }

    public void drink() {
        helicopter.drink();
    }

    public void fight() {
        for (GameObject go: gameObjectCollection) {
            if (go instanceof Fire) {
                Fire f = (Fire) go;
                helicopter.fight(f);
            }
        }
        // In case player drop the water after all fires arraylist is empty
        //
        helicopter.miss();
    }

/*    private boolean checkWinCondition() {
        boolean inHelipad = helicopter.getLocation().getX() >=
                helipad.getLocation().getX() - helipad.getSize() / 2 &&
                helicopter.getLocation().getX() <=
                        helipad.getLocation().getX() + helipad.getSize() / 2 &&
                helicopter.getLocation().getY() >=
                        helipad.getLocation().getY() - helipad.getSize() / 2 &&
                helicopter.getLocation().getY() <=
                        helipad.getLocation().getY() + helipad.getSize() / 2;
        return inHelipad &&
                helicopter.getSpeed() == 0 &&
                getTotalFireSize() == 0;
    }*/

    private boolean checkRanOutFuel() {
        return fuel <= 0;
    }

    private boolean checkBuildingDestroy() {
        return getTotalDmg() > 100;
    }

/*    private void drawDialog() {
        if (checkWinCondition()) {
            winLossText = "You Won!\nScore : " + score() + "\nPlay again?";
        } else if (checkRanOutFuel()) {
            winLossText = "You ran out of fuel:(\nPlay again?";
        } else if (checkBuildingDestroy()) {
            winLossText = "All building were destroyed:(\nPlay again?";
        }
        if (Dialog.show("Game Over", winLossText,
                "Heck Yeah!", "Some Other Time")) {
            new Game().show();
        } else {
            quit();
        }
    }*/

    public void quit() {
        Display.getInstance().exitApplication();
    }

    public ArrayList<GameObject> getGameObjectCollection() {
        return gameObjectCollection;
    }

    public void setDimension(Dimension worldSize) {
        this.worldSize = worldSize;
    }
}
