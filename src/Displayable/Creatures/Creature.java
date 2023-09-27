package src.Displayable.Creatures;

import src.Displayable.*; 

public class Creature extends Displayable{
    @Override
    public String toString() {
        String str = "\t\tSettings... \n";
        str += "\t\tvisible: " + getVisible() + "\n";
        str += "\t\tposX: " + getPosX() + "\n";
        str += "\t\tposY: " + getPosY() + "\n";
        str += "\t\ttype: " + getType() + "\n";
        str += "\t\thp: " + getHp() + "\n";
        str += "\t\thpm: " + getHpMoves() + "\n";
        str += "\t\tmaxhit: " + getMaxhit() + "\n";
        return str;
    }
}
