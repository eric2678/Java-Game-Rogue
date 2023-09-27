package src.Displayable.Structure;

import java.util.ArrayList;
import java.util.List;

import src.Displayable.Creatures.*;

public class Passage extends Structure {
    private int room1;
    private int room2;
    private int visible;
    private List<Integer> posXs = new ArrayList<>();
    private List<Integer> posYs = new ArrayList<>();
    // private int posX;
    // private int posY;

    public Passage(int _room1, int _romm2){
        room1 = _room1;
        room2 = _romm2;
    }

    public void setVisible(int _visible){
        visible = _visible;
    }

    public void setPosX(int _posX){
        posXs.add(_posX);
    }

    public void setPosY(int _posY){
        posYs.add(_posY);
    }

    public List<Integer> getPosXs(){
        return posXs;
    }

    public List<Integer> getPosYs(){
        return posYs;
    }

    @Override
    public String toString(){
        String str = "Creating passage...\n";
        str += "Room1 Num: " + room1 + "\n";
        str += "Room2 Num: " + room2 + "\n";
        str += "\tvisible: "+ visible + "\n";
        for(int i=0; i<posXs.size(); i++){
            str += "\tposition " + i + "\n";
            str += "\t\tposX: "+ posXs.get(i) + "\n";
            str += "\t\tposY: "+ posYs.get(i) + "\n";
        }
        str += "Passage Created" + "\n";
        return str;
    }
}
