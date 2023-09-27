package src.DungeonXMLHandler;


import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import src.Displayable.Displayable;
import src.Displayable.Dungeon;
import src.Displayable.Creatures.*;
import src.Displayable.Structure.*;
import src.Displayable.Item.*;
import src.Action.*;
import src.Action.CreatureAction.*;
import src.Action.CreatureAction.MonsterAction.*;
import src.Action.CreatureAction.PlayerAction.*;
import src.Action.ItemAction.*;


public class DungeonXMLHandler extends DefaultHandler {
    // the two lines that follow declare a DEBUG flag to control
    // debug print statements and to allow the class to be easily
    // printed out.  These are not necessary for the parser.
    private static final int DEBUG = 1;
    private static final String CLASSID = "DungeonXMLHandler";

    // data can be called anything, but it is the variables that
    // contains information found while parsing the xml file
    private StringBuilder data = null;

    // When the parser parses the file it will add references to
    // Room objects to this array so that it has a list of 
    // all specified rooms.  Had we covered containers at the
    // time I put this file on the web page I would have made this
    // an ArrayList of Rooms (ArrayList<Room>) and not needed
    // to keep tract of the length and maxRooms.  You should use
    // an ArrayList in your project.
    private Dungeon dungeons;
    private List<Displayable> displayables = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private List<Passage> passages = new ArrayList<>();

    // The XML file contains a list of Rooms, and within each 
    // Room a list of activities (clubs and classes) that the
    // room participates in.  When the XML file initially
    // defines a room, many of the fields of the object have
    // not been filled in.  Additional lines in the XML file 
    // give the values of the fields.  Having access to the 
    // current Room and Creature allows setters on those 
    // objects to be called to initialize those fields.
    private Passage passageBeingParsed = null;
    private Room roomBeingParsed = null;
    private Creature creatureBeingParsed = null;
    private Item itemBeingParsed = null;
    private Action actionBeingParsed = null;
    private CreatureAction creatureActionBeingParsed = null;
    private ItemAction itemActionBeingParsed = null;

    private String CreatureName = "Floor";
    private String ItemName;
    private int ActionRoomNumber;

    // Used by code outside the class to get the list of Room objects
    // that have been constructed.
    public Dungeon getDungeons() {
        return dungeons;
    }
    public List<Displayable> getDisplayables() {
        return displayables;
    }
    public List<Room> getRooms() {
        return rooms;
    }
    public List<Passage> getPassages() {
        return passages;
    }
    public List<Action> getActions(){
        return actions;
    }

    // A constructor for this class.  It makes an implicit call to the
    // DefaultHandler zero arg constructor, which does the real work
    // DefaultHandler is defined in org.xml.sax.helpers.DefaultHandler;
    // imported above, and we don't need to write it.  We get its 
    // functionality by deriving from it!
    public DungeonXMLHandler() {
    }

    // Control which label is currently running 
    // 1) Room
    // 2) Creature - Monster, Player
    // 3) Player
    // 4) Item
    private int control = 0;

    // startElement is called when a <some element> is called as part of 
    // <some element> ... </some element> start and end tags.
    // Rather than explain everything, look at the xml file in one screen
    // and the code below in another, and see how the different xml elements
    // are handled.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (DEBUG > 1) {
            System.out.println(CLASSID + ".startElement qName: " + qName);
        }

        if (qName.equalsIgnoreCase("Dungeon")) {
            String name = attributes.getValue("name");
            String width = attributes.getValue("width");
            String topHeight = attributes.getValue("topHeight");
            String gameHeight = attributes.getValue("gameHeight");
            String bottomHeight = attributes.getValue("bottomHeight");
            dungeons = new Dungeon(name,width,topHeight,gameHeight,bottomHeight);
        } else if(qName.equalsIgnoreCase("Rooms")) {
            // Null
        }else if (qName.equalsIgnoreCase("Room")) {
            control = 1;
            int roomNum = Integer.parseInt(attributes.getValue("room"));
            Room room = new Room();
            room.setID(roomNum);
            addRoom(room);
            addDisplayable(room);
            roomBeingParsed = room;
        }
        
        // ====================== Creature ======================
        else if (qName.equalsIgnoreCase("Monster")) {
            control = 2;
            String name = attributes.getValue("name");
            CreatureName = name;
            int roomNum = Integer.parseInt(attributes.getValue("room"));
            ActionRoomNumber = roomNum;
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Monster monster = null;
            monster = new Monster(name, roomNum, serial);
            creatureBeingParsed = monster;
            roomBeingParsed.addCreature(monster);

        }else if (qName.equalsIgnoreCase("Player")) {
            control = 2;
            String name = attributes.getValue("name");
            CreatureName = name;
            int roomNum = Integer.parseInt(attributes.getValue("room"));
            ActionRoomNumber = roomNum;
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Player player = new Player(name, roomNum, serial);
            creatureBeingParsed = player;
            roomBeingParsed.addCreature(player);
        }

        // ======================== Item ========================
        else if (qName.equalsIgnoreCase("Armor")) {
            control = 4;
            String name = attributes.getValue("name");
            ItemName = name;
            int roomNum = Integer.parseInt(attributes.getValue("room"));
            ActionRoomNumber = roomNum;
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Armor armor = new Armor(name, CreatureName, roomNum, serial);
            itemBeingParsed = armor;
            roomBeingParsed.addItem(armor);
        }else if (qName.equalsIgnoreCase("Sword")) {
            control = 4;
            String name = attributes.getValue("name");
            ItemName = name;
            int roomNum = Integer.parseInt(attributes.getValue("room"));
            ActionRoomNumber = roomNum;
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Sword sword = new Sword(name, CreatureName, roomNum, serial);
            itemBeingParsed = sword;
            roomBeingParsed.addItem(sword);
        }else if (qName.equalsIgnoreCase("Scroll")) {
            control = 4;
            String name = attributes.getValue("name");
            ItemName = name;
            int roomNum = Integer.parseInt(attributes.getValue("room"));
            ActionRoomNumber = roomNum;
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Scroll scroll = new Scroll(name, CreatureName, roomNum, serial);
            itemBeingParsed = scroll;
            roomBeingParsed.addItem(scroll);
        }

        // ======================= Action =======================
        else if (qName.equalsIgnoreCase("CreatureAction")) {
            control = 3;
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            CreatureAction creatureAction = null;
            switch (name) {
                case "Remove":
                    creatureAction = new Remove(name, CreatureName, type, ActionRoomNumber);
                    break;
                case "YouWin":
                    creatureAction = new YouWin(name, CreatureName, type, ActionRoomNumber);
                    break;
                case "Teleport":
                    creatureAction = new Teleport(name, CreatureName, type, ActionRoomNumber);
                    break;
                case "EndGame":
                    creatureAction = new EndGame(name, CreatureName, type, ActionRoomNumber);
                    break;
                case "ChangeDisplayedType":
                    creatureAction = new ChangeDisplayedType(name, CreatureName, type, ActionRoomNumber);
                    break;
                case "UpdateDisplay":
                    creatureAction = new UpdateDisplay(name, CreatureName, type, ActionRoomNumber);
                    break;
                case "DropPack":
                    creatureAction = new DropPack(name, CreatureName, type, ActionRoomNumber);
                    break;
                default:
                    System.out.println("Unknown Creature Action: " + name);
                    break;
            }
            
            creatureActionBeingParsed = creatureAction;
            roomBeingParsed.addCreatureAction(creatureAction);
            roomBeingParsed.addAction(1);
        }
        else if (qName.equalsIgnoreCase("ItemAction")) {
            control = 4;
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            ItemAction itemAction = null;
            switch(name){
                case "Hallucinate":
                    itemAction = new Hallucinate(name, ItemName, type, ActionRoomNumber);
                    break;
                case "BlessArmor":
                    itemAction = new BlessArmor(name, ItemName, type, ActionRoomNumber);
                    break;
                default:
                    System.out.println("Unknown Item Action: " + name);
                    break;
            }
            itemActionBeingParsed = itemAction;
            roomBeingParsed.addItemAction(itemAction);
            roomBeingParsed.addAction(2);
        }
        // ===================== Passage ======================
        else if (qName.equalsIgnoreCase("Passages")) {
            // Null
        } else if (qName.equalsIgnoreCase("Passage")) {
            control = 3;
            int room1 = Integer.parseInt(attributes.getValue("room1"));
            int room2 = Integer.parseInt(attributes.getValue("room2"));
            Passage passage = new Passage(room1, room2);
            addPassage(passage);
            addDisplayable(passage);
            passageBeingParsed = passage;
        }
        /***************************************************************
         * instructor, credit, name, meetingTIme, meetingDay, number 
         * and location are handled in endElement.
         **************************************************************/
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Room room;
        Creature creature;
        Passage passage;
        Action action;
        Item item;

        if(qName.equalsIgnoreCase("visible")) {
            if (control == 1){
                room = (Room) roomBeingParsed;
                room.setVisible(Integer.parseInt(data.toString()));
            }else if(control == 2){
                creature = (Creature) creatureBeingParsed;
                creature.setVisible(Integer.parseInt(data.toString()));
            }else if(control == 3){
                passage = (Passage) passageBeingParsed;
                passage.setVisible(Integer.parseInt(data.toString()));
            }else if(control == 4){
                item = (Item) itemBeingParsed;
                item.setVisible(Integer.parseInt(data.toString()));
            }
        }else if (qName.equalsIgnoreCase("posX")) {
            if (control == 1){
                room = (Room) roomBeingParsed;
                room.setPosX(Integer.parseInt(data.toString()));
            }else if(control == 2){
                creature = (Creature) creatureBeingParsed;
                creature.setPosX(Integer.parseInt(data.toString()));
            }else if(control == 3){
                passage = (Passage) passageBeingParsed;
                passage.setPosX(Integer.parseInt(data.toString()));
            }else if(control == 4){
                item = (Item) itemBeingParsed;
                item.setPosX(Integer.parseInt(data.toString()));
            }
        }else if (qName.equalsIgnoreCase("posY")) {
            if (control == 1){
                room = (Room) roomBeingParsed;
                room.setPosY(Integer.parseInt(data.toString()));
            }else if(control == 2){
                creature = (Creature) creatureBeingParsed;
                creature.setPosY(Integer.parseInt(data.toString()));
            }else if(control == 3){
                passage = (Passage) passageBeingParsed;
                passage.setPosY(Integer.parseInt(data.toString()));
            }else if(control == 4){
                item = (Item) itemBeingParsed;
                item.setPosY(Integer.parseInt(data.toString()));
            }
        }else if (qName.equalsIgnoreCase("width")) {
            room = (Room) roomBeingParsed;
            room.setWidth(Integer.parseInt(data.toString()));
        }else if (qName.equalsIgnoreCase("height")) {
            room = (Room) roomBeingParsed;
            room.setHeight(Integer.parseInt(data.toString()));
        }else if (qName.equalsIgnoreCase("type")) {
            creature = (Creature) creatureBeingParsed;
            creature.setType(data.toString());
        }else if (qName.equalsIgnoreCase("hp")) {
            creature = (Creature) creatureBeingParsed;
            creature.setHp(Integer.parseInt(data.toString()));
        }else if (qName.equalsIgnoreCase("hpMoves")) {
            creature = (Creature) creatureBeingParsed;
            creature.setHpMoves(Integer.parseInt(data.toString()));
        }else if (qName.equalsIgnoreCase("maxhit")) {
            creature = (Creature) creatureBeingParsed;
            creature.setMaxhit(Integer.parseInt(data.toString()));
        }

        // ======================= Action =======================
        else if (qName.equalsIgnoreCase("actionMessage")) {
            if(control == 3){
                action = (Action) creatureActionBeingParsed;
                action.setMessage(data.toString());
            }else if(control == 4){
                action = (ItemAction) itemActionBeingParsed;
                action.setMessage(data.toString());
            }
        }else if (qName.equalsIgnoreCase("actionIntValue")) {
            if(control == 3){
                action = (Action) creatureActionBeingParsed;
                action.setIntValue(Integer.parseInt(data.toString()));
            }else if(control == 4){
                action = (Action) itemActionBeingParsed;
                action.setIntValue(Integer.parseInt(data.toString()));
            }
        }else if (qName.equalsIgnoreCase("actionCharValue")) {
            if(control == 3){
                action = (Action) creatureActionBeingParsed;
                action.setCharValue((data.toString()).charAt(0));
            }else if(control == 4){
                action = (Action) itemActionBeingParsed;
                action.setCharValue((data.toString()).charAt(0));
            }
        }

        // ======================== Item ========================
        else if (qName.equalsIgnoreCase("ItemIntValue")) {
            item = (Item) itemBeingParsed;
            item.setItemIntValue(Integer.parseInt(data.toString()));
        }

        // ================ Repeat Label to close ================
        else if (qName.equalsIgnoreCase("Dungeon")) {
            // Null
        }else if (qName.equalsIgnoreCase("Rooms")) {
            // Null
        }else if (qName.equalsIgnoreCase("Room")) {
            roomBeingParsed = null;
            control = 0;
        }else if (qName.equalsIgnoreCase("Scroll")) {
            itemBeingParsed = null;
            control = 0;
        }else if (qName.equalsIgnoreCase("Armor")) {
            itemBeingParsed = null;
            control = 0;
        }else if (qName.equalsIgnoreCase("Sword")) {
            itemBeingParsed = null;
            control = 0;
        }else if (qName.equalsIgnoreCase("CreatureAction")) {
            // Null
        }else if (qName.equalsIgnoreCase("Monster")) {
            creatureBeingParsed = null;
            control = 0;
        }else if (qName.equalsIgnoreCase("Player")) {
            CreatureName = "Floor";
            creatureBeingParsed = null;
            control = 0;
        }else if (qName.equalsIgnoreCase("Passages")) {
            // Null
        }else if (qName.equalsIgnoreCase("Passage")) {
            passageBeingParsed = null;
            control = 0;
        }
    }

    private void addDisplayable(Displayable displayable) {
        displayables.add(displayable);
    }
    private void addRoom(Room room) {
        rooms.add(room);
    }
    private void addPassage(Passage passage) {
        passages.add(passage);
    }
    

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".characters: " + new String(ch, start, length));
            System.out.flush();
        }
    }

    @Override
    public String toString() {
        String str = "DungeonXMLHandler\n";
        str += dungeons.toString() + "\n";
        for (Room room : rooms) {
            str += room.toString() + "\n";
        }
        for (Passage passage: passages){
            str += passage.toString() + "\n";
        }
        str += "   roomBeingParsed: " + roomBeingParsed.toString() + "\n";
        str += "   creatureBeingParsed: " + creatureBeingParsed.toString() + "\n";
        str += "   actionBeingParsed: " + actionBeingParsed.toString() + "\n";
        return str;
    }
}
