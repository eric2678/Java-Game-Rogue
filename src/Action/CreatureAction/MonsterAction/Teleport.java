package src.Action.CreatureAction.MonsterAction;

public class Teleport extends MonsterAction {
    public Teleport(String _name, String _CreatureName, String _type, int _roomNum){
        setActionName(_name);
        setOwnerName(_CreatureName);
        setType(_type);
        setRoom(_roomNum);
    }
}
