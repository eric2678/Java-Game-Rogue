package src.Displayable.Item;

public class Sword extends Item{
    private String original_name;

    public Sword(String _name, String _OwnerName, int _room, int _serial){
        setName(_name);
        original_name = _name;
        setOwnerName(_OwnerName);
        setRoom(_room);
        setID(_serial);
        setSpecies("Sword");
    }


    // public void wield() {
    //     name += "(w)";
    // }

    // public void unwield() { // unwield the sword and set the name to origianl name(unlabeled)
    //     setName(this.original_name);
    // }
    
    @Override
    public String toString() {
        String str = "\tSpawning Sword...\n";
        str += "\tSpecies: " + getSpecies() + "\n";
        str += "\tname: " + getName() + "\n";
        str += "\troom: " + getRoom() + "\n";
        str += "\tserial: " + getID() + "\n";
        str += super.toString();
        str += "\tSword Spawned" + "\n";
        return str;
    }
}
