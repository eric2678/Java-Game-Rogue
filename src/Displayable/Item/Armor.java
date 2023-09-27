package src.Displayable.Item;

public class Armor extends Item{
    private String original_name;

    public Armor(String _name, String _OwnerName, int _room, int _serial){
        setName(_name);
        original_name = _name;
        setOwnerName(_OwnerName);
        setRoom(_room);
        setID(_serial);
        setSpecies("Armor");
    }

    // public void worn() {
    //     name += "(a)";
    // }

    // public void unworn() { // unworn the armor and set the name to original name(unlabeled)
    //     setName(this.original_name);
    // }

    @Override
    public String toString() {
        String str = "\tSpawning Armor...\n";
        str += "\tSpecies: " + getSpecies() + "\n";
        str += "\tname: " + getName() + "\n";
        str += "\troom: " + getRoom() + "\n";
        str += "\tserial: " + getID() + "\n";
        str += super.toString();
        str += "\tArmor Spawned" + "\n";
        return str;
    }
}
