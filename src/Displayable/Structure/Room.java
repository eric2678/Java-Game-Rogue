package src.Displayable.Structure;

import java.util.List;
import java.util.ArrayList;

import src.Displayable.Creatures.*;
import src.Displayable.Item.Item;
import src.Action.CreatureAction.CreatureAction;
import src.Action.ItemAction.ItemAction;
import src.Action.Action;

public class Room extends Structure{
    private String CreatureName;
    
    private List<CreatureAction> creatureActions = new ArrayList<>();
    private List<ItemAction> itemActions = new ArrayList<>();
    private List<List<CreatureAction>> creatureAddActions = new ArrayList<List<CreatureAction>>();
    private List<List<ItemAction>> itemAddActions = new ArrayList<List<ItemAction>>();
    
    public void setOwnerName(String _CreatureName){
        CreatureName = _CreatureName;
    }

    public String getOwnerName(){
        return CreatureName;
    }

    public void addCreatureAction(CreatureAction creatureAction){
        creatureActions.add(creatureAction);
    }

    public void addItemAction(ItemAction itemAction){
        itemActions.add(itemAction);
    }

    public void addAction(int control){
        if(control == 1)        creatureAddActions.add(creatureActions);
        else if(control == 2)   itemAddActions.add(itemActions);
    }

    public List<List<ItemAction>> getItemList(){
        return itemAddActions;
    }

    public List<List<CreatureAction>> getCreatureList() {
        return creatureAddActions;
    }

    @Override
    public String toString(){
        String str = "Creating room...\n";
        str += "Room Num: " + getID() + "\n";
        str += "visible: "+ getVisible() + "\n";
        str += "posX: "+ getPosX() + "\n";
        str += "posY: "+ getPosY() + "\n";
        str += "width: "+ getWidth() + "\n";
        str += "height: "+ getHeight() + "\n";
        int count = 0;
        for(Creature creature : getCreatures()){
            str += creature.toString() + "\n";
            // System.out.println(/*actions.get(count).size() + " " +*/count);
            for(int i = 0; i < creatureAddActions.get(count).size(); i++){
                if(creature.getName().equals(creatureAddActions.get(count).get(i).getOwnerName())){
                    str += creatureAddActions.get(count).get(i).toString() + "\n";
                    if(count != creatureAddActions.size()-1) count ++;
                }
            }
        }
        count = 0;
        for(Item item : getItems()){
            str += item.toString() + "\n";
            for(int i = 0; i < itemAddActions.get(count).size(); i++){
                if(item.getName().equals(itemAddActions.get(count).get(i).getOwnerName())){
                    str += itemAddActions.get(count).get(i).toString() + "\n";
                    if(count != itemAddActions.size()-1) count ++;
                }
            }
        }
        str += "Room Created" + "\n";
        return str;
    }
}
