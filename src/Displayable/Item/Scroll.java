package src.Displayable.Item;

public class Scroll extends Item{

    public Scroll(String _name, String _OwnerName, int _room, int _serial){
        setName(_name);
        setOwnerName(_OwnerName);
        setRoom(_room);
        setID(_serial);
        super.setSpecies("Scroll");
    }

    @Override
    public String toString() {
        String str = "\tSpawning Scroll...\n";
        str += "\tSpecies: " + getSpecies() + "\n";
        str += "\tname: " + getName() + "\n";
        str += "\troom: " + getRoom() + "\n";
        str += "\tserial: " + getID() + "\n";
        str += super.toString();
        str += "\tScroll Spawned" + "\n";
        return str;
    }
}
