package src.Action.CreatureAction.PlayerAction;

public class DropPack extends PlayerAction{
    public DropPack(String _name, String _CreatureName, String _type, int _roomNum){
        setActionName(_name);
        setOwnerName(_CreatureName);
        setType(_type);
        setRoom(_roomNum);
    }
}
