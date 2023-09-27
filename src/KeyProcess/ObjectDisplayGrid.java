package src.KeyProcess;

import static src.game.Rogue.actions;
import static src.game.Rogue.dungeons;
import static src.game.Rogue.map;
import static src.game.Rogue.mapStructureBackup;
import static src.game.Rogue.mapWalkableArea;
import static src.game.Rogue.mapItem;
import static src.game.Rogue.mapHallucinate;
import static src.game.Rogue.mapBackup;
import static src.game.Rogue.passages;
import static src.game.Rogue.rooms;
import static src.game.Rogue.playerHP;
import static src.Displayable.Creatures.Player.packcontainer;
import static src.Displayable.ENDGAME.setENDMAP;
import static src.Displayable.ENDGAME.ENDMAP;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import src.asciiPanel.AsciiPanel;
import src.Action.CreatureAction.CreatureAction;
import src.Action.CreatureAction.PlayerAction.DropPack;
import src.Displayable.*;
import src.Displayable.Structure.*;
import src.Displayable.Creatures.Creature;
import src.Displayable.Creatures.Player;
import src.Displayable.Item.*;

public class ObjectDisplayGrid extends JFrame implements KeyListener, InputSubject {

    private static final int DEBUG = 0;
    private static final String CLASSID = ".ObjectDisplayGrid";

    private static AsciiPanel terminal;
    private Char[][] objectGrid = null;

    private List<InputObserver> inputObservers = null;
    private List<Character> keyLog = new ArrayList<>();

    public static int width;
    public static int height;
    public static int topHeight;
    public static int gameHeight;
    public static int bottomHeight;

    private Random random = new Random();
    private static String monsterName = "";
    private static int monsterHP = 0;
    private static int playerAttack;
    private static int monsterAttack;
    private static int score = 0;
    private static int hp_move = 1;

    public static List<String> item = new ArrayList<>();
    private static List<Drop_stack> stackList = new ArrayList<>();
    private Drop_stack stack = null;

    private boolean controlOnlyUseAtBeginning = true;
    private boolean controlStackOrNotStack = false;
    private boolean controlItemNumber = false;
    private boolean controlRemove = false;
    private boolean showInventory = false;
    private boolean controlHCommand = true;
    private boolean controlHallucinate = false;
    public static boolean endControl = false;

    private int countHallucinateStep = 0;
    private int HallucinateStep = 0;

    private Item wear = null;
    private Item wield = null;

    private String wear_ori_name;
    private String wield_ori_name;

    private String[] type= {"S", "T", "H", "|", "]", "?"};


    public ObjectDisplayGrid(int _width, int _topHeight, int _gameHeight, int _bottomHeight) {
        width = _width;
        topHeight = _topHeight;
        gameHeight = _gameHeight;
        bottomHeight = _bottomHeight;
        height = _topHeight + _gameHeight + _bottomHeight;

        terminal = new AsciiPanel(width, height);
        objectGrid = new Char[width][height];

        initializeDisplay();
        terminal.addKeyListener(this);          // To make the keytyped work

        super.add(terminal);
        super.setSize(width * 9, height * 17);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // super.repaint();
        // terminal.repaint( );
        super.setVisible(true);
        terminal.setVisible(true);
        super.addKeyListener(this);
        inputObservers = new ArrayList<>();
        super.repaint();
    }

    public static boolean isNumeric(String string) {
        // System.out.println(String.format("Parsing string: \"%s\"", string));
        if(string == null || string.equals("")) {
            // System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }
        try {
            int intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }


    @Override
    public void registerInputObserver(InputObserver observer) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".registerInputObserver " + observer.toString());
        }
        inputObservers.add(observer);
    }

    public void printMap(String choice1, String choice2){
        System.out.println("=================== Debug ===================");
        for (int j = 0; j < height; j++){
            for (int i = 0; i < width-30; i++) {
                if(choice1.equals("map"))                   System.out.print(map[i][j] + " ");
                else if(choice1.equals("mapItem"))          System.out.print(mapItem[i][j] + " ");
                else if(choice1.equals("mapWalkableArea"))  System.out.print(mapWalkableArea[i][j] + " ");
                else if(choice1.equals("mapHallucinate"))   System.out.print(mapHallucinate[i][j] + " ");
                else if(choice1.equals("mapBackup"))        System.out.print(mapBackup[i][j] + " ");
            }
            System.out.print("");
            for (int i = 0; i < width-30; i++) {
                if(choice2.equals("map"))                   System.out.print(map[i][j] + " ");
                else if(choice2.equals("mapItem"))          System.out.print(mapItem[i][j] + " ");
                else if(choice2.equals("mapWalkableArea"))  System.out.print(mapWalkableArea[i][j] + " ");
                else if(choice2.equals("mapHallucinate"))   System.out.print(mapHallucinate[i][j] + " ");
                else if(choice2.equals("mapBackup"))        System.out.print(mapBackup[i][j] + " ");
                else;
            }
            System.out.println(" ");
        }
        System.out.println("==================== End ====================");
    }
    // ===================================================================================================
    // ==============                            Update BackupMap                           ==============
    // ===================================================================================================
    public void updateHallucinateMap(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++){
                mapHallucinate[i][j] = mapBackup[i][j];
            }
        }
    }
    // ===================================================================================================
    // ==============                            Update BackupMap                           ==============
    // ===================================================================================================

    // ===================================================================================================
    // ==============                              Set Messege                              ==============
    // ===================================================================================================
    public void ENDGAME(int i, int j, int endgame_way){ //endgameway = 0: end game manually, 1: died
        endControl = true;
        // setENDMAP();
        // for(int i = 0; i < width; i++){
        //     for(int j = topHeight-1; j < topHeight + gameHeight; j++){
        //         map[i][j] = ENDMAP[i][j];
        //     }
        // }
        if (endgame_way == 1) {
            List<List<CreatureAction>> creature_action = rooms.get(mapWalkableArea[i][j]-1).getCreatureList();
            for (int m = 0;m < creature_action.size();m++) {
                for (int n = 0;n < creature_action.get(m).size();n++) {
                    if (creature_action.get(m).get(n).getActionName().equals("EndGame") && creature_action.get(m).get(n).getOwnerName().equals("Player")) {
                        String str2 = creature_action.get(m).get(n).getMessage();
                        setScoreMessage(1, "");
                        setScoreMessage(2, str2);
                    }
                }
            }
            for (int m = 0;m < creature_action.size();m++) {
                for (int n = 0;n < creature_action.get(m).size();n++) {
                    if (creature_action.get(m).get(n).getActionName().equals("ChangeDisplayedType") && creature_action.get(m).get(n).getOwnerName().equals("Player")) {
                        List<Creature> creature = rooms.get(mapWalkableArea[i][j]-1).getCreatures();
                        for (int p = 0;p<creature.size();p++){
                            if(creature.get(p).getName().equals("Player")) {
                                creature.get(p).setType(String.valueOf(creature_action.get(m).get(n).getCharValue()));
                                map[i][j] = creature.get(p).getType();
                            }
                        }
                    }
                }
            }
        }
        map.notifyAll();
        return;
    }

    public void setTopMessage(String str1, String str2){
        for (int j = 0; j < topHeight; j++){
            for (int i = 0; i < width; i++) {
                map[i][j] = " ";
                if(j == 0 && i < str1.length()){
                    map[i][j] = String.valueOf(str1.charAt(i));
                }
                if(j == 1 && i < str2.length()){
                    map[i][j] = String.valueOf(str2.charAt(i));
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

    public String setInvetoryMessage(){
        String str1 = "";
        if(showInventory){ 
            if(packcontainer.size() == 0){
                str1 += "Nothing in the pack.";
            }
            for(int h = 0; h<packcontainer.size(); h++){
                str1 += (h+1) + "[" + packcontainer.get(h).getName() + "] ";
            }
        }
        else return "";
        return str1;
    }

    public void setScoreMessage(int controlTopOrBottom, String str2){
        if(controlTopOrBottom == 1){
            String str1 = "HP: " + playerHP + "  Score: " + score;
            setTopMessage(str1, str2);
        }else if(controlTopOrBottom == 2){
            String str1 = setInvetoryMessage();
            setBottonMessage(str1, str2);
        }
    }

    // ===================================================================================================
    // ==============                              Set Messege                              ==============
    // ===================================================================================================

    // ===================================================================================================
    // ==============                              Meet Monster                             ==============
    // ===================================================================================================
    
    public void hpmove() {
        hp_move++;
        if (hp_move % 5 == 0){
            playerHP++;
        }
    }

    public int MeetMonster(int i, int j, String character, int roomNum, String monsterTypeCondition, String monsterNameCondtion){
        for(int l = 0; l < rooms.get(roomNum).getCreatures().size(); l++){
            if(rooms.get(roomNum).getCreatures().get(l).getName().equals("Player")){
                playerAttack = random.nextInt(rooms.get(roomNum).getCreatures().get(l).getMaxhit() + 1);
                //weild sword add attack
                if (wield != null) {
                    // System.out.println("add attack");
                    playerAttack += wield.getItemIntValue();
                }
            }
            if(character.equals(monsterTypeCondition)){
                if(rooms.get(roomNum).getCreatures().get(l).getName().equals(monsterNameCondtion)){
                    monsterName = rooms.get(roomNum).getCreatures().get(l).getName();
                    monsterAttack = random.nextInt(rooms.get(roomNum).getCreatures().get(l).getMaxhit() + 1);
                    if (wear != null) {
                        monsterAttack -= wear.getItemIntValue();
                    }
                    monsterHP = rooms.get(roomNum).getCreatures().get(l).getHp();
                }
            }
        }
                    
        playerHP -= monsterAttack;
        monsterHP -= playerAttack;
       
        for(int l = 0; l < rooms.get(roomNum).getCreatures().size(); l++){
            if(rooms.get(roomNum).getCreatures().get(l).getName().equals("Player")){
                rooms.get(roomNum).getCreatures().get(l).setHp(playerHP);
            }
            if(character.equals(monsterTypeCondition)){
                if(rooms.get(roomNum).getCreatures().get(l).getName().equals(monsterNameCondtion)){
                    rooms.get(roomNum).getCreatures().get(l).setHp(monsterHP);
                }
            }
        } 

        if(monsterHP < 0){
            monsterHP = 0;
        }
        return monsterHP;
    }

    public void Movement(String character, int nextColumn, int nextRow){
        int controlMonsterHP = 0;
        OutForLoop:
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                for(int k = 0; k < rooms.size(); k++){
                    for(int l = 0; l < rooms.get(k).getCreatures().size(); l++){
                        if(map[i][j].equals("@")){
                            // If want to go into non-walkable area
                            if(mapWalkableArea[i+nextColumn][j+nextRow] == 9){
                                String str1 = setInvetoryMessage();
                                String str2;
                                if(!(mapBackup[i+nextColumn][j+nextRow].equals(".") || mapBackup[i+nextColumn][j+nextRow].equals("X"))) str2 = "That's a dead monster, can't pass!";
                                else        str2 = "You can't go further!";
                                setBottonMessage(str1, str2);
                                if(controlHallucinate)  countHallucinateStep++;
                                hpmove();
                                setScoreMessage(1, "");
                                map.notifyAll();
                            }
                            // If want to go into walkable area
                            else{
                                // Encounter Monster
                                Boolean drop = false;
                                if(controlHallucinate)  countHallucinateStep++;
                                if(mapBackup[i+nextColumn][j+nextRow].equals("S") || mapBackup[i+nextColumn][j+nextRow].equals("T") || mapBackup[i+nextColumn][j+nextRow].equals("H")){
                                    // System.out.println("encounter"+packcontainer.size());
                                    drop = DropPack(rooms.get(mapWalkableArea[i][j]-1).getCreatures(), rooms.get(mapWalkableArea[i][j]-1).getCreatureList(), rooms.get(mapWalkableArea[i][j]-1));
                                    if(mapBackup[i+nextColumn][j+nextRow].equals("S"))        controlMonsterHP = MeetMonster(i, j, mapBackup[i+nextColumn][j+nextRow], mapWalkableArea[i][j]-1, "S", "Snake");
                                    else if(mapBackup[i+nextColumn][j+nextRow].equals("T"))   controlMonsterHP = MeetMonster(i, j, mapBackup[i+nextColumn][j+nextRow], mapWalkableArea[i][j]-1, "T", "Troll");
                                    else if(mapBackup[i+nextColumn][j+nextRow].equals("H"))   controlMonsterHP = MeetMonster(i, j, mapBackup[i+nextColumn][j+nextRow], mapWalkableArea[i][j]-1, "H", "Hobgoblin");
                                    // End game when player dies
                                    if(playerHP <= 0){
                                        ENDGAME(i,j,1);
                                    }
                                    // display after attack
                                    else{
                                        if(drop == false) {
                                            setScoreMessage(1, monsterName + "'s HP: " + monsterHP);
                                            setScoreMessage(2, "Player attack: " + playerAttack + "   " +monsterName + " strike back: " + monsterAttack);
                                        }
                                        if(controlMonsterHP == 0){
                                            // map[i+nextColumn][j+nextRow] = map[i][j];
                                            List<List<CreatureAction>> creature_action = rooms.get(mapWalkableArea[i][j]-1).getCreatureList();
                                            OutForLoop1:
                                            for (int m = 0;m < creature_action.size();m++) {
                                                for (int n = 0;n < creature_action.get(m).size();n++) {
                                                    if (creature_action.get(m).get(n).getActionName().equals("Remove") && creature_action.get(m).get(n).getOwnerName().equals(monsterName)) {
                                                        controlRemove = true;
                                                        break OutForLoop1;
                                                    }
                                                }
                                            }
                                            if(controlRemove){
                                                mapBackup[i+nextColumn][j+nextRow] = ".";
                                                map[i+nextColumn][j+nextRow] = ".";
                                            }else{
                                                mapWalkableArea[i+nextColumn][j+nextRow] = 9;
                                            }
                                            if(controlHallucinate)  mapHallucinate[i+nextColumn][j+nextRow] = ".";
                                            else    updateHallucinateMap();
                                            hpmove();
                                            OutForLoop1:
                                            for (int m = 0;m < creature_action.size();m++) {
                                                for (int n = 0;n < creature_action.get(m).size();n++) {
                                                    if (creature_action.get(m).get(n).getActionName().equals("YouWin") && creature_action.get(m).get(n).getOwnerName().equals(monsterName)) {
                                                        String str2 = creature_action.get(m).get(n).getMessage();
                                                        setScoreMessage(2, str2);
                                                        break OutForLoop1;
                                                    }
                                                }
                                            }
                                        }else{
                                            Teleport(i, j, i+nextColumn, j+nextRow, mapWalkableArea[i][j]);
                                        }
                                        controlRemove = false;
                                        map.notifyAll();
                                        break OutForLoop;
                                    }
                                }
                                // Walkable area
                                else{
                                    setScoreMessage(1, "");
                                    setScoreMessage(2, "");
                                }
                                hpmove();
                                map[i+nextColumn][j+nextRow] = map[i][j];
                                if(controlHallucinate){
                                    map[i][j] = mapHallucinate[i][j];
                                }
                                else{
                                    map[i][j] = mapBackup[i][j];
                                    updateHallucinateMap();
                                }
                                map.notifyAll();
                                break OutForLoop;
                            }
                            break OutForLoop;   // Break after find "@"
                        }
                    }
                }
            }
        }
        if(countHallucinateStep == (HallucinateStep+1) && controlHallucinate){
            for (int i = 0; i < width; i++) {
                for (int j = topHeight-1; j < height - bottomHeight; j++){
                    if(map[i][j].equals("@"))   continue;
                    map[i][j] = mapBackup[i][j];
                }
            }
            updateHallucinateMap();
            map.notifyAll();
            controlHallucinate = false;
            countHallucinateStep = 0;
        }
    }
    // ===================================================================================================
    // ==============                              Meet Monster                             ==============
    // ===================================================================================================

    // ======================================================================= ============================
    // ==============                                Action                                 ==============
    // ===================================================================================================
    public void ActionItem(int index, String key){
        OutForLoop:
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(map[i][j].equals("@")){
                    for(int k = 0; k < rooms.size(); k++){
                        for(int l = 0; l < rooms.get(k).getItems().size(); l++){
                            // Pick up
                            boolean controlCondition1 = (mapWalkableArea[i][j] == rooms.get(k).getItems().get(l).getRoom()); // compare the item room with current room
                            boolean controlCondition2 = rooms.get(k).getItems().get(l).getType().equals(mapItem[i][j]);  // compare the item type with the current position
                            boolean controlCondition3 = key.equals("p");    // if it's put function
                            int posX = rooms.get(k).getItems().get(l).getPosX() + rooms.get(k).getPosX();
                            int posY = rooms.get(k).getItems().get(l).getPosY() + rooms.get(k).getPosY() + topHeight;
                            // if(controlCondition1) System.out.println("I'm in1");
                            // if(controlCondition2) System.out.println("I'm in2");
                            // if(controlCondition3) System.out.println("I'm in3");
                            if(controlCondition1 && controlCondition2 && controlCondition3 && i == posX && j == posY){
                                // System.out.println("I'm in4");
                                controlStackOrNotStack = false;
                                Item itemPicking = null;
                                for(int h = 0; h < stackList.size(); h++){
                                    int countIfAllItemPickedUpAtLeastOnce = 0;
                                    for(int p = 0; p < stackList.get(h).getStack().size(); p++){
                                        countIfAllItemPickedUpAtLeastOnce++; 
                                    }
                                    if(rooms.get(k).getItems().size() == countIfAllItemPickedUpAtLeastOnce ) controlStackOrNotStack = true;
                                    if(i == stackList.get(h).getPosX() && j == stackList.get(h).getPosY() + topHeight){
                                        itemPicking = stackList.get(h).pickUpFromFloor();
                                        Player.additem(itemPicking);
                                        if(stackList.get(h).getStack().size() == 0){
                                            if(mapWalkableArea[i][j] == 7 && (  (mapWalkableArea[i][j+1] != 9 && mapWalkableArea[i][j+1] != 7) || 
                                                                                (mapWalkableArea[i][j-1] != 9 && mapWalkableArea[i][j-1] != 7) || 
                                                                                (mapWalkableArea[i+1][j] != 9 && mapWalkableArea[i+1][j] != 7) || 
                                                                                (mapWalkableArea[i-1][j] != 9 && mapWalkableArea[i-1][j] != 7) ))
                                                                                    mapItem[i][j] = "+";
                                            else if(mapWalkableArea[i][j] == 7)  mapItem[i][j] = "#";
                                            else mapItem[i][j] = ".";
                                            stackList.remove(h);
                                        }else{
                                            mapItem[i][j] = stackList.get(h).getStack().get(stackList.get(h).getStack().size()-1).getType();
                                        }
                                        mapBackup[i][j] = mapItem[i][j];
                                        controlStackOrNotStack = true;
                                    }
                                }
                                if(!controlStackOrNotStack){
                                    itemPicking = rooms.get(k).getItems().get(l);
                                    Player.additem(rooms.get(k).getItems().get(l));
                                    if(mapWalkableArea[i][j] == 7 && (  (mapWalkableArea[i][j+1] != 9 && mapWalkableArea[i][j+1] != 7) || 
                                                                        (mapWalkableArea[i][j-1] != 9 && mapWalkableArea[i][j-1] != 7) || 
                                                                        (mapWalkableArea[i+1][j] != 9 && mapWalkableArea[i+1][j] != 7) || 
                                                                        (mapWalkableArea[i-1][j] != 9 && mapWalkableArea[i-1][j] != 7) ))
                                                                        mapItem[i][j] = "+";
                                    else if(mapWalkableArea[i][j] == 7)  mapItem[i][j] = "#";
                                    else mapItem[i][j] = ".";
                                    mapBackup[i][j] = mapItem[i][j];
                                }
                                String str1 = setInvetoryMessage();
                                String str2 = "Pick up " + itemPicking.getName();
                                setBottonMessage(str1, str2);
                                map.notifyAll();
                                break OutForLoop;
                            }
                            // Drop Item
                            else if(key.equals("d") && rooms.get(k).getItems().get(l).getName() == packcontainer.get(index).getName()){
                                Item itemDropping = Player.drop_item(index);  
                                drop_w_scord(itemDropping);                     // drop item and see if it is weild scord
                                itemDropping.setRoom(mapWalkableArea[i][j]);                   // set new room
                                itemDropping.setPosX(i - rooms.get(k).getPosX());              // update PosX
                                itemDropping.setPosY(j - rooms.get(k).getPosY() - topHeight);  // update PosY
                                if(mapItem[i][j].equals(".") || mapItem[i][j].equals("#") || mapItem[i][j].equals("+")){  // The first one
                                    stack = new Drop_stack();
                                    stackList.add(stack);
                                    stack.setPosXY(i, j-topHeight);
                                    stack.addItemToFloor(itemDropping);
                                }
                                else{
                                    for(int h = 0; h < stackList.size(); h++){
                                        if(stackList.get(h).getStack().size() == 0) stackList.remove(h);
                                        if(i == stackList.get(h).getPosX() && j == stackList.get(h).getPosY() + topHeight){
                                            stackList.get(h).addItemToFloor(itemDropping);
                                        }
                                    }
                                }
                                mapItem[i][j] = itemDropping.getType();
                                mapBackup[i][j] = mapItem[i][j];
                                String str1 = setInvetoryMessage();
                                String str2 = "Remove " + itemDropping.getName();
                                setBottonMessage(str1, str2);
                                map.notifyAll();
                                break OutForLoop;
                            }
                            // wear armor
                            else if(key.equals("w")){
                                wear_item(index);
                                break OutForLoop;
                            }
                            // weild scord
                            else if(key.equals("T")){
                                take_weapon(index);
                                break OutForLoop;
                            }
                            // put armor back
                            else if(key.equals("c")){
                                if (wear != null) {
                                    put_armor_back();
                                } else {
                                    String str1 = setInvetoryMessage();
                                    String str2 = "No Armor wearing!";
                                    setBottonMessage(str1, str2);
                                    map.notifyAll();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    // ===================================================================================================
    // ==============                                Action                                 ==============
    // ===================================================================================================

    // ===================================================================================================
    // ==============                                 Item                                  ==============
    // ===================================================================================================
    public void drop_w_scord(Item dropped) {
        if (dropped == wield) {
            wield.setName(wield_ori_name);
            wield = null;
        } else if (dropped == wear) {
            wear.setName(wear_ori_name);
            wear = null;
        }
    }

    public void take_weapon(int index) {//weild sword (when pressing "T"+number)
        if (wield == null) {
            if (Player.packcontainer.get(index).getType() == "|") {
                wield = packcontainer.get(index);
                wield_ori_name = wield.getName();
                wield.setName(wield.getName() + "(w)");
                String str1 = setInvetoryMessage();
                String str2 = "[" + wield.getName() + "] wield";
                setBottonMessage(str1, str2);
                map.notifyAll();
            } else { // user choose to wield item that is not sword
                String str1 = setInvetoryMessage();
                String str2 = "This is not a Sword!";
                setBottonMessage(str1, str2);
                map.notifyAll();
            }
        } else {
            String str1 = setInvetoryMessage();
            String str2 = "Already wield " + wield.getName();
            setBottonMessage(str1, str2);
            map.notifyAll();
        }
    }

    public void wear_item(int index) {//wear armor (when pressing "w"+number)
        if (wear == null) {
            if (packcontainer.get(index).getType() == "]") {
                wear = packcontainer.get(index);
                wear_ori_name = wear.getName();
                wear.setName(wear.getName() + "(a)");
                String str1 = setInvetoryMessage();
                String str2 = "[" + wear.getName() + "] worn";
                setBottonMessage(str1, str2);
                map.notifyAll();
            } else { // user choose to wear item that is not armor
                String str1 = setInvetoryMessage();
                String str2 = "This is not an Armor!";
                setBottonMessage(str1, str2);
                map.notifyAll();
            }
        } else {
            String str1 = setInvetoryMessage();
            String str2 = "Already worn " + wear.getName();
            setBottonMessage(str1, str2);
            map.notifyAll();
        }
    }

    public void put_armor_back() { // put weared armor back to pack (when pressing "c")
        wear.setName(wear_ori_name);
        String str1 = setInvetoryMessage();
        String str2 = "[" + wear.getName() + "] placed back to pack";
        setBottonMessage(str1, str2);
        map.notifyAll();
        wear = null;
    }

    public void read_scroll(int index) { //when pressing "r"
        OutForLoop:
        if (Player.packcontainer.get(index).getType() == "?") { //see if it is scroll
            int roomNumber = Player.packcontainer.get(index).getRoom() - 1;
            packcontainer.remove(index);
            for (int i = 0;i < rooms.get(roomNumber).getItemList().size();i++) {
                for (int j = 0;j < rooms.get(roomNumber).getItemList().get(i).size();j++) {
                    if (rooms.get(roomNumber).getItemList().get(i).get(j).getActionName().equals("BlessArmor")) {
                        if (wear != null) {
                            int fin = wear.getItemIntValue() + rooms.get(roomNumber).getItemList().get(i).get(j).getIntValue();
                            wear.setItemIntValue(fin);
                            if (fin >= 0) {
                                String new_name = "+" + fin + " Armor(a)";
                                wear.setName(new_name);
                                wear_ori_name = "+" + fin + " Armor";
                            } else {
                                String new_name = fin + " Armor(a)";
                                wear.setName(new_name);
                                wear_ori_name = fin + " Armor";
                            }
                            String str1 = setInvetoryMessage();
                            String str2 = wear.getName() + " blessed! " + rooms.get(roomNumber).getItemList().get(i).get(j).getIntValue() + " taken from its effectiveness";
                            setBottonMessage(str1, str2);
                            map.notifyAll();
                        } else {
                            String str1 = setInvetoryMessage();
                            String str2 = "scroll of cursing does nothing because armor not being used";
                            setBottonMessage(str1, str2);
                            map.notifyAll();
                        }
                        rooms.get(roomNumber).getItemList().get(i).remove(rooms.get(roomNumber).getItemList().get(i).get(j));
                        break OutForLoop;
                    } else if (rooms.get(roomNumber).getItemList().get(i).get(j).getActionName().equals("BlessSword")) {
                        if (wield != null) {
                            int fin = wield.getItemIntValue() + rooms.get(roomNumber).getItemList().get(i).get(j).getIntValue();
                            wield.setItemIntValue(fin);
                            if (fin >= 0) {
                                String new_name = "+" + fin + " Sword(w)";
                                wield.setName(new_name);
                                wield_ori_name = "+" + fin + " Sword";
                            } else {
                                String new_name = fin + " Sword(w)";
                                wield.setName(new_name);
                                wield_ori_name = fin + " Sword";
                            }
                            String str1 = setInvetoryMessage();
                            String str2 = wield.getName() + " blessed! " + rooms.get(roomNumber).getItemList().get(i).get(j).getIntValue() + " taken from its effectiveness";
                            setBottonMessage(str1, str2);
                            map.notifyAll();
                        } else {
                            String str1 = setInvetoryMessage();
                            String str2 = "scroll of cursing does nothing because sword not being used";
                            setBottonMessage(str1, str2);
                            map.notifyAll();
                        }
                        rooms.get(roomNumber).getItemList().get(i).remove(rooms.get(roomNumber).getItemList().get(i).get(j));
                        break OutForLoop;
                    } else if (rooms.get(roomNumber).getItemList().get(i).get(j).getActionName().equals("Hallucinate")) {
                        controlHallucinate = true;
                        updateHallucinateMap();
                        String str1 = setInvetoryMessage();
                        String str2 = "Hallucinate applied!";
                        setBottonMessage(str1, str2);
                        HallucinateStep =rooms.get(roomNumber).getItemList().get(i).get(j).getIntValue();
                        for(int l = 0; l < width; l++){
                            for(int h = topHeight-1; h < height-bottomHeight; h++){
                                Boolean condition1 = (mapBackup[l][h].equals("S") || mapBackup[l][h].equals("T") || mapBackup[l][h].equals("H"));
                                Boolean condition2 = (mapBackup[l][h].equals("|") || mapBackup[l][h].equals("?") || mapBackup[l][h].equals("]"));
                                // System.out.println(condition1 + " "+ condition2);
                                if(condition1 || condition2){
                                    int randomNumber = random.nextInt(6);
                                    mapHallucinate[l][h] = type[randomNumber];
                                    map[l][h] = mapHallucinate[l][h];
                                }
                            }
                        }
                        printMap("mapHallucinate", "choice2");
                        map.notifyAll();

                        rooms.get(roomNumber).getItemList().get(i).remove(rooms.get(roomNumber).getItemList().get(i).get(j));
                        break OutForLoop;
                    }
                }
            }
        } else {
            String str1 = setInvetoryMessage();
            String str2 = "This is not a scroll!";
            setBottonMessage(str1, str2);
            map.notifyAll();
        }
    }

    public void Teleport(int currentX, int currentY, int monsterX, int monsterY, int currentRoom) {
        OutForLoop:
        for(int i = 0; i < rooms.size(); i++){
            List<Creature> creatureListPerRoom = rooms.get(i).getCreatures();
            for(int j = 0; j < creatureListPerRoom.size(); j++){
                if(creatureListPerRoom.get(j).getType().equals(mapBackup[monsterX][monsterY])){
                    for(int l = 0; l < rooms.get(i).getCreatureList().size(); l++){
                        List<CreatureAction> creatureActionListPerCreature = rooms.get(i).getCreatureList().get(l);
                        for(int p = 0; p < creatureActionListPerCreature.size(); p++){
                            boolean condition1 = creatureActionListPerCreature.get(p).getActionName().equals("Teleport");
                            boolean condition2 = (creatureActionListPerCreature.get(p).getRoom() == currentRoom);
                            boolean condition3 = creatureListPerRoom.get(j).getName().equals(creatureActionListPerCreature.get(p).getOwnerName());
                            if(condition1 && condition2 && condition3){
                                int telx = 0;
                                int tely = 0;
                                while(mapBackup[telx][tely] != ".") {
                                    telx = random.nextInt(width);
                                    tely = random.nextInt(height-bottomHeight-1-topHeight);
                                }
                                Creature monsterSetting = creatureListPerRoom.get(j);
                                map[monsterX][monsterY] = ".";
                                mapBackup[monsterX][monsterY] = ".";
                                monsterSetting.setPosX(telx);
                                monsterSetting.setPosY(tely);
                                map[telx][tely] = monsterSetting.getType();
                                mapBackup[telx][tely] = monsterSetting.getType();
                                if(controlHallucinate){
                                    mapHallucinate[telx][tely] = mapHallucinate[monsterX][monsterY];
                                    mapHallucinate[monsterX][monsterY] = ".";
                                    map[telx][tely] = mapHallucinate[telx][tely];
                                }
                                monsterSetting.setRoom(mapWalkableArea[telx][tely]);
                                CreatureAction creatureActionSetting = creatureActionListPerCreature.get(p);
                                creatureActionSetting.setRoom(mapWalkableArea[telx][tely]);
                                setBottonMessage(setInvetoryMessage(), creatureActionSetting.getMessage());
                                map.notifyAll();
                                break OutForLoop;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean DropPack(List<Creature> creature, List<List<CreatureAction>> creature_action, Room room) {
        OutForLoop:
        for (int i = 0;i < creature_action.size();i++) {
            for (int j = 0;j < creature_action.get(i).size();j++) {
                if (creature_action.get(i).get(j).getActionName().equals("DropPack")) {
                    for (int k = 0;k<creature.size();k++) {
                        if (creature.get(k).getName().equals(creature_action.get(i).get(j).getOwnerName())) {
                            if (packcontainer.size() != 0) {
                                ActionItem(0, "d");
                                String str1 = setInvetoryMessage();
                                String str2 = creature_action.get(i).get(j).getMessage();
                                setBottonMessage(str1, str2);
                                map.notifyAll();
                                return true;
                            }
                            break OutForLoop;
                        }
                    }
                }
            }
        }
        return false;
    }    

    public void EmptyPack() {
        for(int i = 0; i < packcontainer.size(); i++){
            ActionItem(0, "d");
        }
    }
    
    // ===================================================================================================
    // ==============                                 Item                                  ==============
    // ===================================================================================================

    // ===================================================================================================
    // ==============                             Keyboard Input                            ==============
    // ===================================================================================================

    public void keyboardControlItemNumber(String key){
        String str1 = setInvetoryMessage();
        String str2 = "";
        setBottonMessage(str1, str2);
        map.notifyAll();
        String number = "";
        for(int i = 1; i < keyLog.size() - 1; i++){
            number += keyLog.get(i);
        }
        // System.out.println("Number entered " + number);
        if(Integer.parseInt(number) <= packcontainer.size()){
            // System.out.println(Integer.parseInt(number));
            if(key.equals("r")){
                read_scroll(Integer.parseInt(number)-1);
            }else{
                ActionItem(Integer.parseInt(number)-1, key);
            }
        }else{
            str1 = setInvetoryMessage();
            str2 = "There's no item in slot " + number;
            setBottonMessage(str1, str2);
            map.notifyAll();
        }
        keyLog.clear();
        controlItemNumber = false;
    }
    
    public void keyboardControlItemMovement(Character character){
        keyLog.clear();
        keyLog.add(character);
        if(character == 'p' || character == 'c'){
            ActionItem(item.size(), String.valueOf(character));
            keyLog.remove(character);
        } 
        else{
            String str1 = setInvetoryMessage();
            String str2 = "";
            if(packcontainer.size() != 0) str2 = "Enter number and press [Enter].";
            setBottonMessage(str1, str2);
            map.notifyAll();
            controlItemNumber = true;
        }
    }

    public void keyboardControlMovement(int i, int j, Character character){
        keyLog.clear();
        keyLog.add(character);
        Movement("@", i, j);
        keyLog.remove(character);
    }

    public void command_overall() { // when pressing "?"
        String str1 = setInvetoryMessage();
        String str2 = "h,j,k,l : move/ p,d : pick & drop item/ w,T : wear Armor & wield sword";
        setBottonMessage(str1, str2);
        map.notifyAll();
    }

    public void cmd_detail_info(char cmd) { // when pressing "H"
        String str1 = setInvetoryMessage();
        String str2 = "";
        if (cmd == 'h') str2 = "\"h\" for moving player left";
        else if (cmd == 'j') str2 = "\"j\" for moving player down";
        else if (cmd == 'k') str2 = "\"k\" for moving player up";
        else if (cmd == 'l') str2 = "\"l\" for moving player right";
        else if (cmd == 'p') str2 = "\"p\" for pick up item in the map";
        else if (cmd == 'd') str2 = "\"d\" for drop item in the map, enter pack index after \"d\"";
        else if (cmd == 'w') str2 = "\"w\" for wear Armor in pack, enter pack index after \"w\"";
        else if (cmd == 'c') str2 = "\"c\" for take off worn Armor";
        else if (cmd == 'T') str2 = "\"T\" for wield sword in pack, enter pack index after \"T\"";
        else if (cmd == 'r') str2 = "\"r\" for read scroll in pack, enter pack index after \"r\"";
        else if (cmd == 'E') str2 = "\"E\" for end the game manually";
        else if (cmd == 'i') str2 = "\"i\" for show the inventory";
        else if (cmd == 'I') str2 = "\"I\" for show the inventory";
        else if (cmd == '?') str2 = "\"?\" for seeing overall commands";
        else if (cmd == 'H') str2 = "\"H\" for detailed command info, enter command after \"H\"";
        else str2 = "Command not exist!";
        setBottonMessage(str1, str2);
        map.notifyAll();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".keyTyped entered" + e.toString());
        }
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());
        synchronized(map){
            keyLog.add(e.getKeyChar());
            if(controlHCommand){
                // System.out.println("Before: " + keyLog);
                // ================================= Movement =================================
                if(e.getKeyChar() == 'h')       keyboardControlMovement(-1, 0, e.getKeyChar()); // left
                else if(e.getKeyChar() == 'j')  keyboardControlMovement(0, 1, e.getKeyChar());  // down
                else if(e.getKeyChar() == 'k')  keyboardControlMovement(0, -1, e.getKeyChar()); // up
                else if(e.getKeyChar() == 'l')  keyboardControlMovement(1, 0, e.getKeyChar());  // right
                // else if(e.getKeyChar() == '4')  keyboardControlMovement(-1, 0, 'h'); // left
                // else if(e.getKeyChar() == '5')  keyboardControlMovement(0, 1, 'j');  // down
                // else if(e.getKeyChar() == '8')  keyboardControlMovement(0, -1, 'k'); // up
                // else if(e.getKeyChar() == '6')  keyboardControlMovement(1, 0, 'l');  // right
                // ================================= Inventory ================================
                else if(e.getKeyChar() == 'i' || e.getKeyChar() == 'I'){  // display inventory
                    keyLog.clear();
                    keyLog.add(e.getKeyChar());
                    showInventory = !showInventory;
                    String str1 = setInvetoryMessage();
                    setBottonMessage(str1, "");
                    map.notifyAll();
                    keyLog.remove((Character) e.getKeyChar());
                }
                // =================================== Item ===================================
                else if(e.getKeyChar() == 'c')  keyboardControlItemMovement('c');   // take off armor
                else if(e.getKeyChar() == 'p')  keyboardControlItemMovement('p');    // pick item
                else if(e.getKeyChar() == 'd')  keyboardControlItemMovement('d');   // drop item
                else if(e.getKeyChar() == 'w')  keyboardControlItemMovement('w');   // weild sword
                else if(e.getKeyChar() == 'T')  keyboardControlItemMovement('T');   // weild armor
                else if(e.getKeyChar() == 'r')  keyboardControlItemMovement('r');   // read scroll
                else if(e.getKeyChar() == '\n'){    // Choose number
                    if(keyLog.get(0) == 'd' )       keyboardControlItemNumber("d"); // drop item
                    else if(keyLog.get(0) == 'w' )  keyboardControlItemNumber("w"); // weild sword
                    else if(keyLog.get(0) == 'T' )  keyboardControlItemNumber("T"); // weild armor
                    else if(keyLog.get(0) == 'r' )  keyboardControlItemNumber("r"); // weild armor
                }else if(isNumeric(String.valueOf(e.getKeyChar())) && controlItemNumber == true){
                    // Null
                }
                // ================================== EndGame ==================================
                else if(e.getKeyChar() == 'E'){  // pick item
                    keyLog.clear();
                    keyLog.add(e.getKeyChar());
                    String str1 = setInvetoryMessage();
                    String str2 = "You sure you want to END the game? <Y|N>";
                    setBottonMessage(str1, str2);
                    map.notifyAll();
                    controlHCommand = false;
                }
                // =================================== Help ====================================
                else if(e.getKeyChar() == '?'){  // pick item
                    keyLog.clear();
                    keyLog.add(e.getKeyChar());
                    command_overall();
                    keyLog.remove((Character)'?');
                }
                else if(e.getKeyChar() == 'H'){  // pick item
                    keyLog.clear();
                    keyLog.add(e.getKeyChar());
                    String str1 = setInvetoryMessage();
                    String str2 = "Press any key for DETAILED information!";
                    setBottonMessage(str1, str2);
                    map.notifyAll();
                    controlHCommand = false;
                }
                // perform when nothing match the condition
                else    keyLog.clear();
                // printMap("mapBackup", "mapHallucinate");
                // System.out.println("After: " + keyLog);
            }else{
                if(keyLog.get(0) == 'H'){
                    cmd_detail_info(e.getKeyChar());
                    keyLog.clear();
                   
                }else if(keyLog.get(0) == 'E'){
                    if(e.getKeyChar() == 'Y'){
                        String str1 = setInvetoryMessage();
                        String str2 = "As you wish...";
                        setBottonMessage(str1, str2);
                        ENDGAME(0,0,0);
                    }else if(e.getKeyChar() == 'N'){
                        String str1 = setInvetoryMessage();
                        String str2 = "OK! Let's keep playing the game!!!";
                        setBottonMessage(str1, str2);
                        map.notifyAll();
                    }
                }
                controlHCommand = true;
            }
            
        }
        // System.out.println("Key Typed:"+e.getKeyChar());
    }
    

    private void notifyInputObservers(char ch) {
        for (InputObserver observer : inputObservers) {
            observer.observerUpdate(ch);
            if (DEBUG > 0) {
                System.out.println(CLASSID + ".notifyInputObserver " + ch);
            }
        }
    }

    // we have to override, but we don't use this
    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("Key Pressed:"+e.getKeyChar());
    }

    // we have to override, but we don't use this
    @Override
    public void keyReleased(KeyEvent e) {
        // System.out.println("Key Released:"+e.getKeyChar());
    }

    public final void initializeDisplay() {
        Char ch = new Char('.');
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                addObjectToDisplay(ch, i, j);
            }
        }
        terminal.repaint();
    }

    public void fireUp() {
        if (terminal.requestFocusInWindow()) {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow Succeeded");
        } else {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow FAILED");
        }
    }

    public void addObjectToDisplay(Char ch, int x, int y) {
        if ((0 <= x) && (x < objectGrid.length)) {
            if ((0 <= y) && (y < objectGrid[0].length)) {
                objectGrid[x][y] = ch;
                writeToTerminal(x, y);
            }
        }
    }
    
    private void writeToTerminal(int x, int y) {
        char ch = objectGrid[x][y].getChar();
        terminal.write(ch, x, y);
        terminal.repaint();
    }
}
