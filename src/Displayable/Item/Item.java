package src.Displayable.Item;

import src.Displayable.*; 
import src.Displayable.Creatures.Creature;

public class Item extends Displayable {

    private Creature Owner;
    private String ownerName;

    public void setOwner(Creature _Owner){
        Owner = _Owner;
    }

    public Creature getOwner(){
        return Owner;
    }

    public void setOwnerName(String _OwnerName){
        ownerName = _OwnerName;
    }

    public String getOwnerName(){
        return ownerName;
    }

    @Override
    public String toString(){
        String str = "\t\tSettings... \n";
        str += "\t\tOwner name: " + getOwnerName() + "\n";
        str += "\t\tvisible: " + getVisible() + "\n";
        str += "\t\tposX: " + getPosX() + "\n";
        str += "\t\tposY: " + getPosY() + "\n";
        str += "\t\ttype: " + getType() + "\n";
        str += "\t\tItemIntValue: " + getItemIntValue() + "\n";
        return str;
    }
}
