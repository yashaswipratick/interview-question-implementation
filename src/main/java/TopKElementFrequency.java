package src.main.java;

import java.util.*;

//problem statement - Given stream of integer, return Top K Frequenct element asked at any given point of time.
//Input - Stream of integers
//Requirement - return Top K Frequent element at any given point of time, requested by cuustomer.

//Assumption
// Stream of integer - data can also be very huge.
// system should be read efficient.
// K is always changing.


//Design level improovements.
//what if this code needs to be running on distributed system.
 // what are components to be considered.
 // cache - Hazelcast grid for key value pair.
 // kafka - publish the updated value to the consumers to keep updating the cache with current values.
 // db - dynamo db/rocks db.

//NFR components
 // System should be highle scalable and  reliable
 // Eventual consistency.
 // highle available(blue-green deployment)

//Code level improovements.
//How it will behave in multithreaded environment and what steps to consider to make it thread safe as well.
  //Assumption for thread safety.
  // 1. Synchronisation at object level(synchronise all the methods. performance can be an issue here).

public class TopKElementFrequency {

    HashMap<Integer, Integer> map = new HashMap<>();

    //maintaining current heap data.
    Map<Integer, Integer> inHeap = new HashMap<>();

    //maintaining the popped element from current heap.
    //The idea here is pq will always have the top last K element(min heap),
    //This is required to make read faster and also to handle the always changing K value.
    Set<Integer> poppedElemStorageFromCurHeap = new HashSet<>();


    //Always maintains the top K elements
    PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(Pair::getValue));

    //since maxPQBackUp is maxHeap and always holding max data at top, which will be popped out from the pq heap.
    //so lets say if K was 2 at first so PQ will be holding top 2 element and others will get stored in maxPQBackUp.
    //Now customer requested for top k = 4 element from the heap, so pq will always have top last K = 2 element maintained,
    // rest will be taken from max heap which is maxPQBackUp

    //Note: pqBackup we are not maintaining any Kth element, so it always make sure that at the top it will have max frequency element.
    PriorityQueue<Pair> maxPQBackUp = new PriorityQueue<>((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()));

    int K;


    public void add(int element) {
        map.put(element, map.getOrDefault(element, 0) + 1);
        addAndRemoveElementFromPQ(element, this.K);
    }

    public Map<Integer, Integer> getTopKElements(int K) {
        //Edge cases.
        if (map.size() < K) {
            System.out.println("Requested K elements are not present in the system. Please provide a valid K value. " +
                    "Current K value should be in range from 1 till " + map.size());
            return new HashMap<>();
        }

        //optimisation and handling edge cases.
        if (map.size() == K) {
            return map; // since the order of return is not required (Ascending or descending).
        }

        if (pq.size() > K) {
            removeElementFromPQ(this.K);
        } else {
            getTopKInCaseCurHeapIsSmallerThanK(K);
        }
        return inHeap;
    }

    private void getTopKInCaseCurHeapIsSmallerThanK(int K) {
        while (pq.size() <= K) {
            Pair poll = maxPQBackUp.poll();
            
            // O(log K) for adding the element and for heapify operation.
            pq.add(poll);
            
	    // O(1) for adding the element.
            inHeap.put(poll.getKey(), map.get(poll.getKey()));
        }
        removeElementFromPQ(K);
    }

    private void addAndRemoveElementFromPQ(int element, int K) {
        while (K > 0 && pq.size() > K) {
            Pair poll = pq.poll();
            // O(1) for removing the element.
            inHeap.remove(poll.getKey());
            if (poppedElemStorageFromCurHeap.contains(poll.getKey())) {
                // O(k) since for removing the element, all the elements has to be scanned.
                maxPQBackUp.removeIf(pair1 -> pair1.getKey() == poll.getKey());
                // O(log K) for adding the element and for heapify operation.
                maxPQBackUp.add(new Pair(poll.getKey(), poll.getValue()));
            } else {
                if (!poppedElemStorageFromCurHeap.contains(poll.getKey())) {
                    // O(log K) for adding the element and for heapify operation.
                    maxPQBackUp.add(new Pair(poll.getKey(), poll.getValue()));
                }
                poppedElemStorageFromCurHeap.add(poll.getKey());
            }
        }
        if (inHeap.containsKey(element)) {
            int count = map.get(element) - 1;
            
            // O(k) since for removing the element, all the elements has to be scanned.
            pq.remove(new Pair(element, count));
            
            // O(log K) for adding the element and for heapify operation.
            pq.add(new Pair(element, map.get(element)));
            
            // O(1) for adding the element.
            inHeap.put(element, map.get(element));
        } else {
            // O(log K) for adding the element and for heapify operation.
            pq.add(new Pair(element, map.get(element)));

            // O(1) for adding the element.
            inHeap.put(element, map.get(element));
        }
    }

    private void removeElementFromPQ(int K) {
        while (K > 0 && pq.size() > K) {
            Pair poll = pq.poll();
            inHeap.remove(poll.getKey());
            if (poppedElemStorageFromCurHeap.contains(poll.getKey())) {
                // O(k) since for removing the element, all the elements has to be scanned.
                maxPQBackUp.removeIf(pair1 -> pair1.getKey() == poll.getKey());
                // O(log K) for adding the element and for heapify operation.
                maxPQBackUp.add(new Pair(poll.getKey(), map.get(poll.getKey())));
            } else {
                if (!poppedElemStorageFromCurHeap.contains(poll.getKey())) {
                    // O(log K) for adding the element and for heapify operation.
                    maxPQBackUp.add(new Pair(poll.getKey(), poll.getValue()));
                }
                
  		// O(log K) for adding the element and for heapify operation.
                poppedElemStorageFromCurHeap.add(poll.getKey());
            }
        }
    }

    //1,1,1,1,2,2,2,3,3 - 2
    //3,3,3,3,2,2,2,3,3,4 - 3
    //1,1,1,2,,3,3,3,4,4,5,5,5,5,5,6,6,6,8,8,9,10,10,10,10,10,10,11,11,11,11,12,11 - 6
    //1,2,3 - 2
    public static void main(String[] args) {
        TopKElementFrequency frequency = new TopKElementFrequency();

        int[] nums1 = {1,1,1,1,2,2,2,3,3};
        for (int i = 0; i < nums1.length; i++) {
            frequency.add(nums1[i]);
        }
        frequency.K = 2;
        System.out.println(frequency.getTopKElements(frequency.K));
        System.out.println("===============================================================");

        int[] nums2 = {3,3,3,3,2,2,2,3,3,4};
        for (int i = 0; i < nums2.length; i++) {
            frequency.add(nums2[i]);
        }
        frequency.K = 3;
        System.out.println(frequency.getTopKElements(frequency.K));
        System.out.println("===============================================================");

        int[] nums3 = {1,1,1,2,3,3,3,4,4,5,5,5,5,5,6,6,6,8,8,9,10,10,10,10,10,10,11,11,11,11,12,11};
        for (int i = 0; i < nums3.length; i++) {
            frequency.add(nums3[i]);
        }
        frequency.K = 6;
        System.out.println(frequency.getTopKElements(frequency.K));
        System.out.println("===============================================================");

        /*int[] nums4 = {1,2,3};
        for (int i = 0; i < nums4.length; i++) {
            frequency.add(nums4[i]);
        }
        frequency.K = 2;
        System.out.println(frequency.getTopKElements(frequency.K));
        System.out.println("===============================================================");*/

    }
}
