package src.game;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import src.DungeonXMLHandler.DungeonXMLHandler;
import src.KeyProcess.KeyStrokePrinter;
import src.KeyProcess.ObjectDisplayGrid;
import src.Displayable.Char;
import src.Displayable.Displayable;
import src.Displayable.Dungeon;
import src.Displayable.Creatures.Player;
import src.Displayable.Structure.*;
import src.Action.*;

import static src.KeyProcess.ObjectDisplayGrid.endControl;
import static src.Displayable.Creatures.Player.packcontainer;
import static src.KeyProcess.ObjectDisplayGrid.item;


public class Rogue implements Runnable {

    private static final int DEBUG = 0;

    // Provided for step 2
    private boolean isRunning;
    public static final int FRAMESPERSECOND = 60;
    public static final int TIMEPERLOOP = 1000000000 / FRAMESPERSECOND;
    private static ObjectDisplayGrid displayGrid = null;
    private Thread keyStrokePrinter;
    // private static final int WIDTH = 80;
    // private static final int HEIGHT = 40;

    // All xml file data
    public static List<Displayable> displayables;
    public static Dungeon dungeons;
    public static List<Room> rooms;
    public static List<Passage> passages;
    public static List<Action> actions;
    
    // Map initial
    private List<Point> coordinate = new ArrayList<>();
    private int width;
    private int height;
    private int topHeight;
    private int gameHeight;
    private int bottomHeight;
    public static int playerHP;
    public static String[][] map;
    public static String[][] mapStructureBackup;
    public static int[][] mapWalkableArea;
    public static String[][] mapItem;
    public static String[][] mapHallucinate;
    public static String[][] mapBackup;

    // Switch variable
    private int control = 0;    // For passage
    private int control2 = 0;   // For alls

    public Rogue(int _width, int _topHeight, int _gameHeight, int _bottomHeight) {
        width = _width;
        topHeight = _topHeight;
        gameHeight = _gameHeight;
        bottomHeight = _bottomHeight;
        height = _topHeight + _gameHeight + _bottomHeight;
        displayGrid = new ObjectDisplayGrid(width, topHeight, gameHeight, bottomHeight);
    }

    public void setTopMessage(int score){
        for (int j = 0; j < topHeight; j++){
            for (int i = 0; i < width; i++) {
                String str1 = "HP: " + playerHP + "  Score: " + score;
                map[i][j] = " ";
                if(j == 0 && i < str1.length()){
                    map[i][j] = String.valueOf(str1.charAt(i));
                }
            }
        }
    }

    public void setBottonMessage(String packMessege, String infoMessege){
        for(int j = topHeight + gameHeight; j < height; j++){
            for (int i = 0; i < width; i++) {
                String str1 = "Pack: " + packMessege;
                String str2 = "Info: " + infoMessege;
                map[i][j] = " ";
                if(j == height-3 && i < str1.length()){
                    map[i][j] = String.valueOf(str1.charAt(i));
                }else if(j == height-1 && i < str2.length()){
                    map[i][j] = String.valueOf(str2.charAt(i));
                }
            }
        }
    }

    @Override
    public void run() {
        displayGrid.fireUp();
        map = new String[width][height];
        mapStructureBackup = new String[width][height];
        mapWalkableArea = new int[width][height];
        mapItem = new String[width][height];
        mapHallucinate = new String[width][height];
        mapBackup = new String[width][height];
        
        // For getting passage coordinate
        for(int i = 0; i<passages.size(); i++){
            int totalpassage = passages.get(i).getPosXs().size();
            for(int j = 0; j<totalpassage-1; j++){
                int xStart = passages.get(i).getPosXs().get(j), xEnd = passages.get(i).getPosXs().get(j+1);
                int yStart = passages.get(i).getPosYs().get(j), yEnd = passages.get(i).getPosYs().get(j+1);
                if(xStart == xEnd){
                    if(yStart > yEnd){
                        int temp = yStart;
                        yStart = yEnd;
                        yEnd = temp;
                        control = 1;
                    }
                    for(int h = 0, x = xStart, y; h<yEnd - yStart; h++){
                        if(control == 1){
                            y = passages.get(i).getPosYs().get(j) - h;
                        }else{
                            y = passages.get(i).getPosYs().get(j) + h;
                        }
                        coordinate.add(new Point(x,y));
                    }
                }else if(yStart == yEnd){
                    if(xStart > xEnd){
                        int temp = xStart;
                        xStart = xEnd;
                        xEnd = temp;
                        control = 1;
                    }
                    for(int h = 0, x, y = passages.get(i).getPosYs().get(j); h<xEnd - xStart; h++){
                        if(control == 1){
                            x = passages.get(i).getPosXs().get(j) - h;
                        }else{
                            x = passages.get(i).getPosXs().get(j) + h;
                        }
                        coordinate.add(new Point(x,y));
                    }
                }
                control = 0;
            }
        }

        // Get all coordinate informarion to Map array
        // for (int step = 1; step < WIDTH / 2; step *= 2) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for(int k=0; k<rooms.size(); k++){
                    int roomXStart = rooms.get(k).getPosX();
                    int roomXEnd = rooms.get(k).getPosX()+rooms.get(k).getWidth();
                    int roomYStart = rooms.get(k).getPosY();
                    int roomYEnd = rooms.get(k).getPosY()+rooms.get(k).getHeight();
                    if((i >= roomXStart && i < roomXEnd) && (j == roomYStart + topHeight || j == roomYEnd + topHeight - 1)){
                        map[i][j] = "X";
                        mapWalkableArea[i][j] = 9;
                        // displayGrid.addObjectToDisplay(new Char('-'), i, j);
                        control2 = 1;
                    } else if((j >= roomYStart + topHeight && j < roomYEnd + topHeight) && (i == roomXStart || i == roomXEnd- 1)){
                        // displayGrid.addObjectToDisplay(new Char('|'), i, j);
                        map[i][j] = "X";
                        mapWalkableArea[i][j] = 9;
                        control2 = 1;
                    }
                    if(i < roomXEnd-1 && i >roomXStart && j < roomYEnd + topHeight -1 && j > roomYStart + topHeight){
                        map[i][j] = ".";
                        mapWalkableArea[i][j] = rooms.get(k).getID();
                        control2 = 1;
                    }
                }
                if(control2 == 0){
                    // displayGrid.addObjectToDisplay(new Char(' '), i, j);
                    map[i][j] = " ";
                }
                control2 = 0;
                // print passage
                if(coordinate.size() != 0){
                    for(int k=0; k<coordinate.size(); k++){
                        if(i == coordinate.get(k).getX() && j == coordinate.get(k).getY() + topHeight){
                            // displayGrid.addObjectToDisplay(new Char('#'), i, j);
                            map[i][j] = "#";
                            mapWalkableArea[i][j] = 7;
                            coordinate.remove(k);
                        }
                    }
                }
                for(int l = 0; l<passages.size(); l++){
                    int totalpassage = passages.get(l).getPosXs().size();
                    int passageXStart = passages.get(l).getPosXs().get(0);
                    int passageXEnd = passages.get(l).getPosXs().get(totalpassage - 1);
                    int passageYStart = passages.get(l).getPosYs().get(0);
                    int passageYEnd = passages.get(l).getPosYs().get(totalpassage - 1);
                    if((i == passageXStart && j == passageYStart + topHeight) || (i == passageXEnd && j == passageYEnd + topHeight)){
                        // displayGrid.addObjectToDisplay(new Char('+'), i, j);
                        map[i][j] = "+";
                        mapWalkableArea[i][j] = 7;
                    }     
                }
            }
        }

        for (int j = 0; j < height; j++){
            for (int i = 0; i < width; i++) {
                mapStructureBackup[i][j] = map[i][j];
                mapItem[i][j] = map[i][j];
                if(mapWalkableArea[i][j] == 0){
                    mapWalkableArea[i][j] = 9;
                }
            }
        }

        // Add Player, Monster, Item to Map
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++){
                for(int k = 0; k < rooms.size(); k++){
                    for(int l = 0; l < rooms.get(k).getCreatures().size(); l++){
                        int creaturePosX = rooms.get(k).getCreatures().get(l).getPosX()+rooms.get(k).getPosX();
                        int creaturePosY = rooms.get(k).getCreatures().get(l).getPosY()+rooms.get(k).getPosY();
                        if(i == creaturePosX && j == creaturePosY + topHeight){
                            if(rooms.get(k).getCreatures().get(l).getName().equals("Player")){
                                rooms.get(k).getCreatures().get(l).setType("@");
                                map[i][j] = "@";
                                playerHP = rooms.get(k).getCreatures().get(l).getHp();
                            }else{
                                map[i][j] = rooms.get(k).getCreatures().get(l).getType();
                            }
                        }
                    }
                    
                }
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++){
                for(int k = 0; k < rooms.size(); k++){
                    for(int l = 0; l < rooms.get(k).getItems().size(); l++){
                        int itemPosX = rooms.get(k).getItems().get(l).getPosX()+rooms.get(k).getPosX();
                        int itemPosY = rooms.get(k).getItems().get(l).getPosY()+rooms.get(k).getPosY();
                        if(i == itemPosX && j == itemPosY + topHeight){
                            if(rooms.get(k).getItems().get(l).getOwnerName().equals("Player")){
                                if(rooms.get(k).getItems().get(l).getSpecies().equals("Sword")){
                                    Player.additem(rooms.get(k).getItems().get(l));
                                    rooms.get(k).getItems().get(l).setType("|");
                                }else if(rooms.get(k).getItems().get(l).getSpecies().equals("Scroll")){
                                    rooms.get(k).getItems().get(l).setType("?");
                                    Player.additem(rooms.get(k).getItems().get(l));
                                }else if(rooms.get(k).getItems().get(l).getSpecies().equals("Armor")){
                                    rooms.get(k).getItems().get(l).setType("]");
                                    Player.additem(rooms.get(k).getItems().get(l));
                                }
                            }else{
                                if(rooms.get(k).getItems().get(l).getSpecies().equals("Sword")){
                                    map[i][j] = "|";
                                    mapItem[i][j] = "|";
                                    rooms.get(k).getItems().get(l).setType("|");
                                }else if(rooms.get(k).getItems().get(l).getSpecies().equals("Scroll")){
                                    map[i][j] = "?";
                                    mapItem[i][j] = "?";
                                    rooms.get(k).getItems().get(l).setType("?");
                                }else if(rooms.get(k).getItems().get(l).getSpecies().equals("Armor")){
                                    map[i][j] = "]";
                                    mapItem[i][j] = "]";
                                    rooms.get(k).getItems().get(l).setType("]");
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int j = 0; j < height; j++){
            for (int i = 0; i < width; i++) {
                String temp = map[i][j];
                if(map[i][j].equals("@")){
                    temp = ".";
                }
                mapHallucinate[i][j] = temp;
                mapBackup[i][j] = temp;
            }
        }

        setTopMessage(0);
        setBottonMessage("", "");
        
        // Used for debug - print out Map array
        // System.out.println("=================== Debug ===================");
        // for (int j = 0; j < height; j++){
        //     for (int i = 0; i < width; i++) {
        //         // System.out.print(map[i][j] + " ");
        //         // System.out.print(mapItem[i][j] + " ");
        //         // System.out.print(mapWalkableArea[i][j] + " ");
        //         // System.out.print(mapHallucinate[i][j] + " ");
        //         System.out.print(mapBackup[i][j] + " ");

        //     }
        //     System.out.println(" ");
        // }
        // System.out.println("==================== End ====================");
        
        // Use to fix random "." on the ascii panel
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        while(true){
            synchronized(map){
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        displayGrid.addObjectToDisplay(new Char(map[i][j].charAt(0)), i, j);
                    }
                }
                if(!Thread.currentThread().isInterrupted() && endControl){
                    System.out.println("Name of thread: " + Thread.currentThread().getName());
                    break;
                }
                try {
                    map.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String fileName = null;
        switch (args.length) {
        case 1:
            fileName = args[0];
            break;
        default:
            System.out.println("java Test <xmlfilename>");
	        return;
        }

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DungeonXMLHandler handler = new DungeonXMLHandler();
            saxParser.parse(new File(fileName), handler);
            dungeons = handler.getDungeons();
            // displayables = handler.getDisplayables();
            rooms = handler.getRooms();
            passages = handler.getPassages();
            actions = handler.getActions();
            // For printing out data in xml files
            // for (Displayable displayable : displayables) {
            //     System.out.println(displayable);
            // }
            // for (Room room : rooms) {
            //     System.out.println(room);
            // }
            // for (Passage passage : passages) {
            //     System.out.println(passage);
            // }
            Rogue test = new Rogue(dungeons.getwidth(), dungeons.getTopHeight(), dungeons.getGameHeight(), dungeons.getBottomHeight());
            Thread testThread = new Thread(test);
            testThread.start();

            test.keyStrokePrinter = new Thread(new KeyStrokePrinter(displayGrid, new Object()));
            test.keyStrokePrinter.start();

            testThread.join();
            test.keyStrokePrinter.join();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
