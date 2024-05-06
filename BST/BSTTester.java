/* CS 314 STUDENTS: FILL IN THIS HEADER.
 *
 * Student information for assignment:
 *
 *  On my honor, <Melody Rodriguez>, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  UTEID: mar9688
 *  email address: mar9688@utexas.edu
 *  TA name: Bersam
 *  Number of slip days I am using: 0
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Experimental Data BST
 * n = 1000: Average Time = 2.7351890000000003E-4, Average Height = 21, Average Size = 1000, Min Height = 11
 * n = 2000: Average Time = 3.9293599E-4, Average Height = 26, Average Size = 2100, Min Height = 12
 * n = 4000: Average Time = 6.637060990000001E-4, Average Height = 29, Average Size = 4210, Min Height = 13
 * n = 8000: Average Time = 0.0012383318099, Average Height = 32, Average Size = 8421, Min Height = 14
 * n = 16000: Average Time = 0.00225247538099, Average Height = 35, Average Size = 16842, Min Height = 15
 * n = 32000: Average Time = 0.004538146438099, Average Height = 37, Average Size = 33684, Min Height = 16
 * n = 64000: Average Time = 0.010797910043809901, Average Height = 41, Average Size = 67368, Min Height = 17
 * n = 128000: Average Time = 0.023394055304380986, Average Height = 44, Average Size = 134734, Min Height = 18
 * n = 256000: Average Time = 0.0542621659304381, Average Height = 47, Average Size = 269467, Min Height = 19
 * n = 512000: Average Time = 0.1451556614930438, Average Height = 51, Average Size = 538914, Min Height = 20
 * n = 1024000: Average Time = 0.4435336437493043, Average Height = 54, Average Size = 1077764, Min Height = 21
 *
 * * Experimental Data TreeSet
 * n = 1000: Average Time = 3.8459000000000003E-4
 * n = 2000: Average Time = 4.783159E-4
 * n = 4000: Average Time = 0.0010449361899999999
 * n = 8000: Average Time = 0.002794743219
 * n = 16000: Average Time = 0.005241897621900001
 * n = 32000: Average Time = 0.01467698136219
 * n = 64000: Average Time = 0.04172228303621901
 * n = 128000: Average Time = 0.11101701670362189
 * n = 256000: Average Time = 0.21526514907036218
 * n = 512000: Average Time = 0.5772203087070362
 * n = 1024000: Average Time = 1.3279698463707037
 *
 * It takes less time to add to the BST than it does to add to the Tree set if the nodes are random.
 *
 * Experimental BST in Ascending Order
 * n = 1000: Average Time = 0.0017120421, Average Height = 999, Average Size = 1000
 * n = 2000: Average Time = 0.0033153535100000006, Average Height = 1999, Average Size = 2100
 * n = 4000: Average Time = 0.012034031951, Average Height = 3999, Average Size = 4210
 * n = 8000: Average Time = 0.0502793102951, Average Height = 7999, Average Size = 8421
 * n = 16000: Average Time = 0.21668101742951001, Average Height = 15999, Average Size = 16842
 * n = 32000: Average Time = 0.870852364242951, Average Height = 31999, Average Size = 33684
 * n = 64000: Average Time = 3.551735390124295, Average Height = 63999, Average Size = 67368
 * n = 128000: Average Time = 14.2
 * n = 256000: Average Time = 56.8
 * n = 512000: Average Time = 227.2
 *
 * Experimental TreeSet in Ascending Order
 * n = 1000: Average Time = 0.0017183650000000001
 * n = 2000: Average Time = 0.0031722624999999996
 * n = 4000: Average Time = 0.01197701475
 * n = 8000: Average Time = 0.046548647275
 * n = 16000: Average Time = 0.1867159270275
 * n = 32000: Average Time = 0.80238997890275
 * n = 64000: Average Time = 3.1580527864902748
 *
 * The times are a little less in the Tree Set than the BST overall when adding in ascending order,
 * this is because the simple add method for a BST is very inefficient when the data is already
 * sorted.
 */
public class BSTTester {

    /**
     * The main method runs the tests.
     * @param args Not used
     */
    public static void main(String[] args) {
        BinarySearchTree<Integer> t = new BinarySearchTree<>();

        // Test 1 Add Method
        t.add(50);
        t.add(40);
        t.add(60);
        showTestResults(t.add(65) == true, 1);

        // Test 2 Add Method
        t.add(45);
        t.add(35);
        showTestResults(t.add(35) == false, 2);

        // Test 3 Remove Method
        t.remove(50);
        t.remove(40);
        showTestResults(t.remove(35) == true, 3);

        // Test 4 Remove Method
        t.add(50);
        t.add(40);
        showTestResults(t.remove(10) == false, 4);

        // Test 5 IsPresent Method
        showTestResults(t.isPresent(50) == true, 5);

        // Test 6 IsPresent Method
        showTestResults(t.isPresent(10) == false, 6);

        // Test 7 Size Method
        int expected = 5;
        showTestResults(t.size() == expected, 7);

        // Test 8 Size Method
        expected = 2;
        t.remove(50);
        t.remove(40);
        t.remove(45);
        showTestResults(t.size() == expected, 8);

        // Test 9 Height Method
        t.remove(60);
        t.remove(65);
        int[] nodes = { 65, 50, 30, 25, -10, 44, 100, 120, 70, 60 };
        for (int node : nodes) {
            t.add(node);
        }
        expected = 4;
        showTestResults(t.height() == expected, 9);

        // Test 10 Height Method
        t.add(130);
        t.add(140);
        t.add(150);
        expected = 5;
        showTestResults(t.height() == expected, 10);

        // Test 11 GetAll Method
        ArrayList<Integer> nodeList = new ArrayList<>();
        for (int node: nodes) {
            nodeList.add(node);
        }
        nodeList.add(130);
        nodeList.add(140);
        nodeList.add(150);
        Collections.sort(nodeList);
        showTestResults(t.getAll().equals(nodeList), 11);

        // Test 12 GetAll Method
        t = new BinarySearchTree<>();
        nodeList = new ArrayList<>();
        nodes = new int[]{ 30, 31, 50, 60, 100, 23, 55, 67, 70 };
        for (int node : nodes) {
            nodeList.add(node);
            t.add(node);
        }
        Collections.sort(nodeList);
        showTestResults(t.getAll().equals(nodeList), 12);

        // Test 13 Max Method
        expected = 100;
        showTestResults(t.max() == expected, 13);

        // Test 14 Max Method
        t.remove(100);
        expected = 70;
        showTestResults(t.max() == expected, 14);

        // Test 15 Min Method
        expected = 23;
        showTestResults(t.min() == expected, 15);

        // Test 16 Min Method
        t.remove(23);
        t.add(-10);
        expected = -10;
        showTestResults(t.min() == expected, 16);

        // Test 17 Iterative add
        t = new BinarySearchTree<>();
        BinarySearchTree<Integer> t2 = new BinarySearchTree<>();
        for (int node : nodes) {
            t.add(node);
            t2.iterativeAdd(node);
        }
        showTestResults(t.getAll().equals(t2.getAll()), 17);

        // Test 18 Iterative add
        showTestResults(t2.iterativeAdd(100) == false, 18);

        // Test 19 GetKth
        showTestResults(t.get(5) == nodeList.get(5), 19);

        // Test 20 GetKth
        showTestResults(t.get(7) == nodeList.get(7), 20);

        // Test 21 GetAllLessThan
        nodeList = new ArrayList<>();
        for (int node : nodes) {
            if (node < 55) {
                nodeList.add(node);
            }
        }
        Collections.sort(nodeList);
        showTestResults(t.getAllLessThan(55).equals(nodeList), 21);

        // Test 22 GetAllLessThan
        nodeList = new ArrayList<>();
        for (int node : nodes) {
            if (node < 67) {
                nodeList.add(node);
            }
        }
        Collections.sort(nodeList);
        showTestResults(t.getAllLessThan(67).equals(nodeList), 22);

        // Test 23 GetAllGreaterThan
        nodeList = new ArrayList<>();
        for (int node : nodes) {
            if (node > 67) {
                nodeList.add(node);
            }
        }
        Collections.sort(nodeList);
        showTestResults(t.getAllGreaterThan(67).equals(nodeList), 23);

        // Test 24 GetAllGreaterThan
        nodeList = new ArrayList<>();
        for (int node : nodes) {
            if (node > 55) {
                nodeList.add(node);
            }
        }
        Collections.sort(nodeList);
        showTestResults(t.getAllGreaterThan(55).equals(nodeList), 24);

        // Test 25 NumNodesAtDepth
        expected = 1;
        showTestResults(t.numNodesAtDepth(5) == expected, 25);

        // Test 26 NumNodesAtDepth
        expected = 2;
        showTestResults(t.numNodesAtDepth(4) == expected, 26);

        // Experiment
        // int n = 1000;
        // TreeSet<Integer> tree = new TreeSet<>();
        // Stopwatch watch;
        // double avgTime = 0;
        // int avgHeight = 0;
        // int avgSize = 0;

        // for (int i = 0; i <= 6; i++) {
        //     double avgtime = 0;
        //     for (int j = 0; j < 10; j++) {
        //         tree = new TreeSet<>();
        //         watch = new Stopwatch();
        //         watch.start();
        //         for(int k = 1; k <= n; k++) {
        //              t.iterativeAdd(k);
        //         }
        //         watch.stop();
        //         avgTime += watch.time();
        //     }
        //     avgTime /= 10;
        //     System.out.println("n = " + n + ": Average Time = " + avgTime);
        //     n*=2;
        // }
    }

    private static void showTestResults(boolean passed, int testNum) {
        if (passed) {
            System.out.println("Test " + testNum + " passed.");
        } else {
            System.out.println("TEST " + testNum + " FAILED.");
        }
    }
}
