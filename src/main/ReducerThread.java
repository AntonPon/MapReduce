package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ReducerThread implements Callable<List<MapOutput<Integer, Integer>>> {

    private Map<Integer, List<Integer>> reducingValues;

    ReducerThread(List<MapOutput<Integer, Integer>> values){

       reducingValues = new HashMap<>();
        for (MapOutput<Integer, Integer> mp: values){

            if (reducingValues.containsKey(mp.getKey())){
                reducingValues.get(mp.getKey()).add(mp.getValue());
            }else {
                List<Integer> array  = new ArrayList<>();
                array.add(mp.getValue());
                reducingValues.put(mp.getKey(), array);
            }
        }
    }

    @Override
    public List<MapOutput<Integer, Integer>> call() throws Exception {
        List<MapOutput<Integer, Integer>> result = new ArrayList<>();

        for(Integer key: reducingValues.keySet()){
           int keyCount = 0;
           for(Integer number: reducingValues.get(key)){
               keyCount += number.intValue();

           }
           result.add(new MapOutput<>(key, new Integer(keyCount)));
        }


        return result;
    }
}
