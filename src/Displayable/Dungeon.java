package src.Displayable;

public class Dungeon {

    private String name;
    private String width;
    private String topHeight;
    private String gameHeight;
    private String bottomHeight;

    public Dungeon(String _name, String _width, String _topHeight, String _gameHeight, String _bottomHeight){
        name = _name;
        width = _width;
        topHeight = _topHeight;
        gameHeight = _gameHeight;
        bottomHeight = _bottomHeight;
    }

    public Integer getwidth(){
        return Integer.parseInt(width);
    }

    public Integer getTopHeight(){
        return Integer.parseInt(topHeight);
    }

    public Integer getGameHeight(){
        return Integer.parseInt(gameHeight);
    }

    public Integer getBottomHeight(){
        return Integer.parseInt(bottomHeight);
    }

    @Override
    public String toString() {
        String str = "Dungeon: \n";
        str += "\tname: " + name + "\n";
        str += "\twidth: " + width + "\n";
        str += "\ttopHeight: " + topHeight + "\n";
        str += "\tgameHeight: " + gameHeight + "\n";
        str += "\tbottomHeight: " + bottomHeight + "\n";
        return str;
    }
    
}
