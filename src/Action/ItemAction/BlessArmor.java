package src.Action.ItemAction;

public class BlessArmor extends ItemAction{
    public BlessArmor(String _name, String _ItemName, String _type, int _roomNum){
        setActionName(_name);
        setOwnerName(_ItemName);
        setType(_type);
        setRoom(_roomNum);
    }
}
