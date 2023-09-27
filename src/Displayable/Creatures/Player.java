package src.Displayable.Creatures;

import src.Displayable.Item.Item;
import java.util.List;
import java.util.ArrayList;

public class Player extends Creature {
    private String name;
    private int room;
    private int serial;
    private Item sword;
    private Item weapon;
    public static ArrayList<Item> packcontainer = new ArrayList<Item>();
    // private List<Item> wear = new ArrayList<Item>();
    // private List<Item> wield = new ArrayList<Item>();

    public Player(String _name, int _room, int _serial){
        name = _name;
        room = _room;
        serial = _serial;
    }

    public void setWeapon(Item _sword, Item _weapon) {
        sword = _sword;
        weapon = _weapon;
    }

    public void setRoom(int _room){
        room = _room;
    }

    public String getName(){
        return name;
    }

    public Item getsword() {
        return sword;
    }

    public Item getweapon() {
        return weapon;
    }

    public static void additem(Item item) {
        packcontainer.add(item);
    }

    public static Item drop_item(int index) {
        Item drop = packcontainer.get(index);
        packcontainer.remove(index);
        return drop;
    }

    
    
    @Override
    public String toString() {
        String str = "\tPlayer Created...\n";
        str += "\tname: " + name + "\n";
        str += "\troom: " + room + "\n";
        str += "\tserial: " + serial + "\n";
        str += super.toString();
        str += "\tPlayer Created" + "\n";
        return str;
    }
}
