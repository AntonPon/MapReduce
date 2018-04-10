package main;

import java.util.List;
import java.util.concurrent.Callable;

public class MapThread implements Runnable{

    private List<Integer> countValues;
    private int start;
    private int end;
    private List<List<MapOutput>> reducerLists;

    public MapThread(int start, int end, List<Integer> countValues, List<List<MapOutput>> reducerLists){
        this.start = start;
        this.end = end;
        this.countValues = countValues;
        this.reducerLists = reducerLists;
    }


    @Override
    public void run() {
        for(int i = start; i < end; i++){
            int number = countValues.get(i).intValue();
            int thread = number % reducerLists.size();
            reducerLists.get(thread).add(new MapOutput(number, 1));
        }

    }
}
