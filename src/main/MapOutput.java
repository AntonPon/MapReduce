package main;

public class MapOutput {
    private int key;
    private int value;

    public MapOutput(int key, int value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    @Override
    public  int hashCode(){
        return key;
    }

    @Override
    public String toString(){
        return "key: " + key + " value: " + value;
    }

}
