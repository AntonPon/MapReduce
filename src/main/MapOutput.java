package main;

public class MapOutput<K, V> {
    private K key;
    private V value;

    public MapOutput(K key, V value){
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public  int hashCode(){
        return key.hashCode();
    }

    @Override
    public String toString(){
        return "key: " + key + " value: " + value;
    }

}
