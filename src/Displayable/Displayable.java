package src.Displayable;

import java.util.List;
import java.util.ArrayList;

import src.Displayable.Structure.*;
import src.Displayable.Creatures.*;
import src.Displayable.Item.*;
import src.Action.CreatureAction.*;


public class Displayable {
    private String name;
    private int room;
    private int id;
    private int visible;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private String type;
    private int hp;
    private int hpm;
    private int maxhit;
    private int itemValue;
    private String species;
    //private String creature;
    private CreatureAction deathaction;
    private CreatureAction hitaction;
    private List<Creature> creatures = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    public void setName(String _name) {
        name = _name;
    }

    public void setRoom(int _room) {
        room = _room;
    } 

    public void setID(int _id){
        id = _id;
    }

    public void setVisible(int _visible) {
        visible = _visible;
    }
    
    public void setPosX(int _posX) {
        posX = _posX;
    }

    public void setPosY(int _posY) {
        posY = _posY;
    }

    public void setWidth(int _width){
        width = _width;
    }

    public void setHeight(int _height){
        height = _height;
    }

    public void setType(String _type) {
        type = _type;
    }

    public void setHp(int _hp) {
        hp = _hp;
    }

    public void setHpMoves(int _hpm) {
        hpm = _hpm;
    }

    public void setMaxhit(int _maxhit) {
        maxhit = _maxhit;
    }

    public void setItemIntValue(int _itemValue){
        itemValue = _itemValue;
    }

    public void setSpecies(String _species){
        species = _species;
    }

    public String getName(){
        return name;
    }
    public int getRoom() {
        return room;
    }

    public int getID(){
        return id;
    }

    public int getVisible() {
        return visible;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public String getType() {
        return type;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getHp() {
        return hp;
    }

    public int getHpMoves() {
        return hpm;
    }

    public int getMaxhit() {
        return maxhit;
    }

    public CreatureAction getDeathAction() {
        return deathaction;
    }

    public CreatureAction getHitAction() {
        return hitaction;
    }

    public int getItemIntValue(){
        return itemValue;
    }

    public String getSpecies(){
        return species;
    }

    public void addCreature(Creature creature){
        creatures.add(creature);
    }
    public List<Creature> getCreatures(){
        return creatures;
    }

    public void addItem(Item item){
        items.add(item);
    }
    public List<Item> getItems(){
        return items;
    }
}
