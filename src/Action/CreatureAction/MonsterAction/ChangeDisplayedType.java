package src.Action.CreatureAction.MonsterAction;

public class ChangeDisplayedType extends MonsterAction{
    public ChangeDisplayedType(String _name, String _CreatureName, String _type, int _roomNum){
        setActionName(_name);
        setOwnerName(_CreatureName);
        setType(_type);
        setRoom(_roomNum);
    }
}
