package src.Displayable.Creatures;

public class Monster extends Creature {
    private String name;
    private int room;
    private int serial;

    public Monster(String _name, int _room, int _serial){
        name = _name;
        room = _room;
        serial = _serial;
    }

    public String getName() {
        return name;
    }

    public int getRoom() {
        return room;
    }

    public int getSerial() {
        return serial;
    }

    @Override
    public String toString() {
        String str = "\tCreating Monster...\n";
        str += "\tname: " + name + "\n";
        str += "\troom: " + room + "\n";
        str += "\tserial: " + serial + "\n";
        str += super.toString();
        str += "\tMonster Created" + "\n";
        return str;
    }
}
