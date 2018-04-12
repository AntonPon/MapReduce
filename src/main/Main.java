package main;


import java.util.*;
import java.util.concurrent.*;

import static java.lang.Math.pow;

public class Main {

    public static void main(String ... args){
      //  System.out.println("power: " + "-------" + " time");
        for (int power = 0; power<=  24; power ++) {
            long start = System.currentTimeMillis();
            int array_size = (int) pow(2, power);
            List<Integer> array = Collections.synchronizedList(new ArrayList<>());
            Random random = new Random();
            for (int i = 0; i < array_size; i++) {
                array.add(new Integer(random.nextInt(10)));
            }

            List<MapOutput<Integer, Integer>> result = runMapReduce(array);
            long end = System.currentTimeMillis();

            System.out.println(" power:  " + power + "  ------  time: "+ (end-start)/1000.0);
        }
    }

    // mapper part

    public static List<MapOutput<Integer, Integer>> runMapReduce(List<Integer> array){

        int coreNumber = Runtime.getRuntime().availableProcessors();
        List<List<MapOutput<Integer, Integer>>> buckets = Collections.synchronizedList(new ArrayList<>());
        int finalBucketsNumber = (coreNumber < array.size()? coreNumber: array.size());

        for (int i = 0; i < finalBucketsNumber; i++){
            buckets.add(Collections.synchronizedList(new ArrayList<>()));
        }

        int step = ((array.size()%finalBucketsNumber) == 0? array.size()/finalBucketsNumber: (int) Math.floor(array.size() / finalBucketsNumber));
        //System.out.println((array.size()%finalBucketsNumber) == 0);
        int start = 0;
        int end = step;


        Thread[] threads = new Thread[finalBucketsNumber];
        System.out.println("mappers number: " + finalBucketsNumber);
        for (int i = 0; i <finalBucketsNumber; i++){
            threads[i] = new Thread(new MapThread(start, end, array, buckets));
            start = end;
            end = ((i+1) == finalBucketsNumber? array.size(): end + step);
            //System.out.println(end);
            threads[i].start();
        }
        for(int i = 0; i < threads.length; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // reducer part

        List<MapOutput<Integer, Integer>> result = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(finalBucketsNumber);
        List<Future<List<MapOutput<Integer, Integer>>>> list = new ArrayList<>();
        System.out.println("reducers number: " + finalBucketsNumber);
        for (List<MapOutput<Integer, Integer>> ar: buckets){
            Callable<List<MapOutput<Integer, Integer>>> thread = new ReducerThread(ar);
            Future< List<MapOutput<Integer, Integer>>> future = executor.submit(thread);
            list.add(future);
        }

        for (Future< List<MapOutput<Integer, Integer>>> future: list){
            try {
                List<MapOutput<Integer, Integer>> lst = future.get();
                result.addAll(lst);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        executor.shutdown();
         return result;
    }
}
