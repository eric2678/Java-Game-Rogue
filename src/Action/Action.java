package src.Action;

import java.util.List;
import java.util.ArrayList;

import src.Action.CreatureAction.*;
import src.Action.ItemAction.*;
import src.Displayable.Structure.Room;

public class Action extends Room{
    private String actionName;
    private String message;
    private int value;
    private char character;

    public void setActionName(String _actionName){
        actionName = _actionName;
    }

    public void setMessage(String _message) {
        message = _message;
    }

    public void setIntValue(int _value) {
        value = _value;
    }
    
    public void setCharValue(char _character) {
        character = _character;
    }

    public String getActionName(){
        return actionName;
    }

    public String getMessage() {
        return message;
    }

    public int getIntValue() {
        return value;
    }
    
    public char getCharValue() {
        return character;
    }

    @Override
    public String toString() {
        String str = "\t\tCreating action...\n";
        str += "\t\t\tOwner Name: " + getOwnerName() + "\n";
        str += "\t\t\tOwner Room: " + getRoom() + "\n";
        str += "\t\t\tAction Name: " + getActionName() + "\n";
        str += "\t\t\tAction type: " + getType() + "\n";
        str += "\t\t\tmessage: " + getMessage() + "\n";
        str += "\t\t\tint value: " + getIntValue() + "\n";
        str += "\t\t\tchar value: " + getCharValue() + "\n";
        return str;
    }

}
