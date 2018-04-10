package main;


import java.util.*;

import static java.lang.Math.pow;

public class Main {

    public static void main(String ... args){
        int power = 3;
        int array_size = (int) pow(2, power);
        List<Integer> array = Collections.synchronizedList(new ArrayList<>());
        Random random = new Random();
        for (int i = 0; i < array_size; i++){
            array.add(new Integer(random.nextInt(10)));
        }


            System.out.println(array );


        runMapReduce(array);
    }

    public static Map<Integer, Integer> runMapReduce(List<Integer> array){
        int coreNumber = Runtime.getRuntime().availableProcessors();
        List<List<MapOutput>> buckets = Collections.synchronizedList(new ArrayList<>());
        int finalBucketsNumber = (coreNumber < array.size()? coreNumber: array.size());

        for (int i = 0; i < finalBucketsNumber; i++){
            buckets.add(Collections.synchronizedList(new ArrayList<>()));
        }

        int step = ((array.size()%finalBucketsNumber) == 0? array.size()/finalBucketsNumber: (int) Math.floor(array.size() % finalBucketsNumber));
        //System.out.println((array.size()%finalBucketsNumber) == 0);
        int start = 0;
        int end = step;


        Thread[] threads = new Thread[finalBucketsNumber];
        for (int i = 0; i <finalBucketsNumber; i++){
            threads[i] = new Thread(new MapThread(start, end, array, buckets));
            start = end;
            end = ((i+1) == finalBucketsNumber? array.size(): end + step);
            //System.out.println(start);
            threads[i].start();
        }
        for(int i = 0; i < threads.length; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("results");
        for(List<MapOutput>lst: buckets){
            //System.out.println(lst.size());
            for(MapOutput mp: lst){

                System.out.println(mp);
            }
        }
        return null;
    }
}
