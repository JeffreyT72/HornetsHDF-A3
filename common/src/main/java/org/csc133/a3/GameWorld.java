package org.csc133.a3;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
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
    private Helipad helipad;
    private River river;
    private FlightPath flightPath;
    private FireDispatch fireDispatch;

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

    private GameWorld(){}

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
        fireCollection = new ArrayList<>();
        buildingCollection = new ArrayList<>();
        flightPath = new FlightPath(helipad.getTranslation(), worldSize);
        fireDispatch = new FireDispatch();
        building0Dmg = 0;
        building1Dmg = 0;
        building2Dmg = 0;
        previousTotalBuildingDmg = 0;
        totalBuildingDmg = 0;
        totalValue = 0;
        totalDmg = 0;

        gameObjectCollection = new ArrayList<>();
        gameObjectCollectionDelete = new ArrayList<>();
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
                Fire temp = new Fire(worldSize, i, fireDispatch);
                fireCollection.add(temp);
                gameObjectCollection.add(temp);
                building.setFireInBuilding(temp);
            }
        }
        // Add the rest of the fires until it reach the fireAreaBudget
        //
        int i = 0;
        while (getTotalFireArea() < FIRE_AREA_BUDGET) {
            Fire temp = new Fire(worldSize, i, fireDispatch);
            fireCollection.add(temp);
            gameObjectCollection.add(temp);
            building = buildingCollection.get(i);
            building.setFireInBuilding(temp);
            i++;
            // Loop back to building 0
            if (i >= MAX_BUILDING)
                i = 0;
        }

        gameObjectCollection.add(flightPath.getHelipadToRiver());
        gameObjectCollection.add(flightPath.getRiverToFire());
        gameObjectCollection.add(flightPath.getFireToRiver());

        gameObjectCollection.add(PlayerHelicopter.getInstance());

        for (Building b: buildingCollection)
            totalValue += b.getValue();
    }

    void tick() {
        ticks++;
        updateHelicopterTransforms();
        NonPlayerHelicopter.getInstance().nphAction();
        fireAction();
        removeFire();
        calculateBuildingDmg();
        updateBuildingDisplay();
        move(10);
        checkHelicopterOnRiver();
        fuelConsume();
        // GameClear/GameOver screen
        // Set some tick delay preventing gameWorld init() bug
        if (ticks >= 10) {
            checkWinLostCondition();
        }
    }

    private void calculateBuildingDmg() {
        for (Fire f: fireCollection) {
            int buildingId = f.getBuildingId();
            if (buildingId == 0) {
                building0Dmg += Math.PI * MathUtil.pow(f.getFireSize()/2, 2);
            } else if (buildingId == 1) {
                building1Dmg += Math.PI * MathUtil.pow(f.getFireSize()/2, 2);
            } else if (buildingId == 2) {
                building2Dmg += Math.PI * MathUtil.pow(f.getFireSize()/2, 2);
            }
        }
    }

    private void removeFire() {
        if (!gameObjectCollectionDelete.isEmpty()) {
            gameObjectCollection.remove(gameObjectCollectionDelete.
                    get(gameObjectCollectionDelete.size() - 1));
            fireCollection.remove(gameObjectCollectionDelete.
                    get(gameObjectCollectionDelete.size() - 1));
        }
    }

    private void fireAction() {
        // Chance to spawn new fire
        //
        int spawnRate = 1 + r.nextInt(300);
        if (spawnRate == 100) {
            if (!getFireCollection().isEmpty()) {
                int i = r.nextInt(3);   // random building
                Fire temp = new Fire(worldSize, i, fireDispatch);
                fireCollection.add(temp);
                gameObjectCollection.add(temp);
                building = buildingCollection.get(i);
                building.setFireInBuilding(temp);
            }
        }

        for (GameObject go: gameObjectCollection) {
            if (go instanceof Fire) {
                Fire f = (Fire) go;
                if (f.getIsBurning() == true) {     // if fire in burning state
                    randomTicks = 2 + r.nextInt(8);
                    if (ticks % randomTicks == 0)
                        f.grow();
                }
                f.checkIsOverFire(PlayerHelicopter.getInstance());
                f.checkIsOverFire(NonPlayerHelicopter.getInstance());
                if (f.getWasExtinguished()) {   // If the fire was extinguished
                    gameObjectCollectionDelete.add(f);
                }
            }
        }
    }

    private void fuelConsume() {
        PlayerHelicopter.getInstance().fuel();
        NonPlayerHelicopter.getInstance().fuel();
    }

    private void checkHelicopterOnRiver() {
        PlayerHelicopter.getInstance().
                checkIsOnRiver(river.getTranslation(), river.getDimension());
        NonPlayerHelicopter.getInstance().
                checkIsOnRiver(river.getTranslation(), river.getDimension());
    }

    private void updateBuildingDisplay() {
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
    }

    private void checkWinLostCondition() {
        if (checkWinCondition() ||
            checkRanOutFuel() ||
            checkBuildingDestroy() ||
            checkCrashed())
            drawDialog();
    }

    private boolean checkCrashed() {
        return NonPlayerHelicopter.getInstance().crashed();
    }

    private void updateHelicopterTransforms() {
        PlayerHelicopter.getInstance().updateLocalTransforms();
        NonPlayerHelicopter.getInstance().updateLocalTransforms();
    }

    public void setDimension(Dimension worldSize) {
        this.worldSize = worldSize;
    }

    public Dimension getDimension() {
        return worldSize;
    }

    public int getFuel() {
        return fuel;
    }

    public int getDisplayHeading() {
        double displayHeading;
        double temp = PlayerHelicopter.getInstance().getDisplayAngle();
        displayHeading = temp % 360;
        if (displayHeading < 0)
            displayHeading = displayHeading + 360;
        return (int)displayHeading;
    }

    public int getCurrentSpeed() {
        return PlayerHelicopter.getInstance().getSpeed();
    }

    public ArrayList<GameObject> getGameObjectCollection() {
        return gameObjectCollection;
    }

    public ArrayList<Fire> getFireCollection() {
        return fireCollection;
    }

    public Transform getHelipadLocation() {
        return helipad.getTranslation();
    }

    public String getCurrentState() {
        return PlayerHelicopter.getInstance().currentState();
    }

    public void engineStartStop() {
        PlayerHelicopter.getInstance().startOrStopEngine();
    }

    public FlightPath getFlightPath() {
        return flightPath;
    }

    public int getCurrentFireNo() {
        return fireCollection.size();
    }

    public int getTotalFireSize() {
        int totalFireSize = 0;
        for (Fire f: fireCollection) {
            totalFireSize += f.getFireSize();
        }
        return totalFireSize;
    }

    public int getTotalFireArea() {
        int totalFireArea = 0;
        for (Fire f: fireCollection) {
            totalFireArea += Math.PI * MathUtil.pow(f.getFireSize()/2, 2);
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

    // Action controller
    //
    public void accelerate() {
        PlayerHelicopter.getInstance().accelerate();
    }

    public void decelerate() {
        PlayerHelicopter.getInstance().decelerate();
    }

    public void changeHeadingLeft() {
        PlayerHelicopter.getInstance().steerLeft();
    }

    public void changeHeadingRight() {
        PlayerHelicopter.getInstance().steerRight();
    }

    private void move(long elapsedTimeInMillis) {
        PlayerHelicopter.getInstance().move(elapsedTimeInMillis);
    }

    public void drink() {
        PlayerHelicopter.getInstance().drink();
    }

    public void fight(Helicopter helicopter) {
        for (GameObject go: gameObjectCollection) {
            if (go instanceof Fire) {
                Fire f = (Fire) go;
                if (!f.getWasExtinguished() &&
                    isOverFire(f, helicopter) &&
                    helicopter.getWater() > 0) {
                    f.fight(helicopter.getWater());
                }
            }
        }
        helicopter.miss();
    }

    private boolean isOverFire(Fire f, Helicopter helicopter) {
        return f.checkIsOverFire(helicopter);
    }

    private boolean checkWinCondition() {
        boolean inHelipad =
             PlayerHelicopter.getInstance().getTranslation().getTranslateX() >=
                helipad.getTranslation().getTranslateX() -
                helipad.getDimension().getWidth() / 2 &&
             PlayerHelicopter.getInstance().getTranslation().getTranslateX() <=
                helipad.getTranslation().getTranslateX() +
                helipad.getDimension().getWidth() / 2 &&
             PlayerHelicopter.getInstance().getTranslation().getTranslateY() >=
                helipad.getTranslation().getTranslateY() -
                helipad.getDimension().getHeight() / 2 &&
             PlayerHelicopter.getInstance().getTranslation().getTranslateY() <=
                helipad.getTranslation().getTranslateY() +
                helipad.getDimension().getHeight() / 2;
        return inHelipad &&
                PlayerHelicopter.getInstance().getSpeed() == 0 &&
                getTotalFireSize() == 0;
    }

    private boolean checkRanOutFuel() {
        return PlayerHelicopter.getInstance().getFuel() <= 0;
    }

    private boolean checkBuildingDestroy() {
        return getTotalDmg() > 100;
    }

    private void drawDialog() {
        if (checkWinCondition()) {
            winLossText = "You Won!\nScore : " + score() + "\nPlay again?";
        } else if (checkRanOutFuel()) {
            winLossText = "You ran out of fuel:(\nPlay again?";
        } else if (checkBuildingDestroy()) {
            winLossText = "All building were destroyed:(\nPlay again?";
        } else if (checkCrashed()) {
            winLossText = "Opps! Crashed:(\nPlay again?";
        }
        if (Dialog.show("Game Over", winLossText,
                "Heck Yeah!", "Some Other Time")) {
            //new Game().show();
            restart();
        } else {
            quit();
        }
    }

    private void restart() {
        PlayerHelicopter.getInstance().reset();
        NonPlayerHelicopter.getInstance().reset();
        init();
    }

    public void quit() {
        Display.getInstance().exitApplication();
    }

    public void updateSelectedFire(Transform fire) {
        flightPath.updateSelectedFire(fire);
    }

    public void spawnNPH() {
        gameObjectCollection.add(NonPlayerHelicopter.getInstance());
    }
}
