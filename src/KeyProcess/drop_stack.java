package src.KeyProcess;

import src.Displayable.Item.Item;
import java.util.List;
import java.util.ArrayList;

public class Drop_stack {

    private int x;
    private int y;
    private List<Item> stack = new ArrayList<>();

    public void setPosXY(int _x, int _y){
        x = _x;
        y = _y;
    }

    public int getPosX() {
        return x;
    }

    public int getPosY(){
        return y;
    }

    public void addItemToFloor(Item drop) {
        stack.add(drop);
    }

    public Item pickUpFromFloor() {
        Item item = stack.get(stack.size()-1);
        stack.remove(stack.size()-1);
        return item;
    }

    public List<Item> getStack() {
        return stack;
    }
    
}
