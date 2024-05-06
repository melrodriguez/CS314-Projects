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
 */

/*
 * Question.
 *
 * 1. The assignment presents three ways to rank teams using graphs.
 * The results, especially for the last two methods are reasonable.
 * However if all results from all college football teams are included
 * some unexpected results occur. 
 * 
 * Explain the unexpected results. You may
 * have to do some research on the various college football divisions to
 * make an informed answer. (What are the divisions within college
 * football? Who do teams play? How would this affect the 
 * structure of the graph?)
  */

public class GraphAndRankTester {

    /**
     * Runs tests on Graph classes and FootballRanker class.
     * Experiments involve results from college football
     * teams. Central nodes in the graph are compared to
     * human rankings of the teams.
     * @param args None expected.
     */
    public static void main(String[] args)  {
        graphTests();
    }

    // tests on various simple Graphs
    private static void graphTests() {
        graph0Tests();
        graph1Tests();
    }

    /* The graph used here is the same one from the example
     * used to show Dijktra's algorithm from the slides on
     * Graphs. It may be useful to print out the priority queue
     * at the top of the main while loop in the dijktra method
     * to see if it matches the one in the slides. Note, the Java
     * PriorityQueue (which you CAN use in the dijkstra method)
     * breaks ties in an arbitrary manner, so the order of equal
     * elements in the priority queue may be different than those
     * shown in the slides. (And that is not a problem.)
     */
    private static void graph0Tests() {
        System.out.println("Graph #0 Tests:");
        // first a simple path test
        // Graph #0
        String [][] g1Edges =  {{"A", "B", "1"},
                        {"B", "C", "5"},
                        {"A", "C", "1"},
                        {"B", "D", "12"},
                        {"C", "D", "4"}};
        Graph g1 = getGraph(g1Edges, false);

        g1.dijkstra("A");
        String actualPath = g1.findPath("D").toString();
        String expected = "[A, C, D]";
        if (actualPath.equals(expected)) {
            System.out.println("Passed dijkstra path test graph 0.");
        } else {
            System.out.println("Failed dijkstra path test graph 0. Expected: " + expected + " actual " + actualPath);
        }

        // now do all paths unweighted
        String[] expectedPaths = {"Name: B                    cost per path: 1.0000, num paths: 3",
                        "Name: C                    cost per path: 1.0000, num paths: 3",
                        "Name: A                    cost per path: 1.3333, num paths: 3",
                        "Name: D                    cost per path: 1.3333, num paths: 3"};
        doAllPathsTests("Graph 0", g1, false, 2, 2.0, expectedPaths);

        // now do all paths weighted
        expectedPaths = new String[] {  "Name: A                    cost per path: 2.3333, num paths: 3",
                        "Name: C                    cost per path: 2.3333, num paths: 3",
                        "Name: B                    cost per path: 3.0000, num paths: 3",
                        "Name: D                    cost per path: 5.0000, num paths: 3"};

        doAllPathsTests("Graph 0", g1, true, 3, 6.0, expectedPaths);
    }

    private static void graph1Tests() {
        System.out.println("Graph #1 Tests:");
        // first a simple path test
        // Graph #1
        String [][] g1Edges =  {{"A", "B", "11"},
                        {"B", "C", "2"},
                        {"C", "D", "4"},
                        {"A", "D", "12"},
                        {"A", "E", "2"}};
        Graph g1 = getGraph(g1Edges, false);

        g1.dijkstra("A");
        String actualPath = g1.findPath("C").toString();
        String expected = "[A, B, C]";
        if (actualPath.equals(expected)) {
            System.out.println("Passed dijkstra path test graph 1.");
        } else {
            System.out.println("Failed dijkstra path test graph 1. Expected: " + expected + " actual " + actualPath);
        }

        // now do all paths unweighted
        String[] expectedPaths = {"Name: A                    cost per path: 1.2500, num paths: 4",
                        "Name: B                    cost per path: 1.5000, num paths: 4",
                        "Name: D                    cost per path: 1.5000, num paths: 4",
                        "Name: C                    cost per path: 1.7500, num paths: 4",
                        "Name: E                    cost per path: 2.0000, num paths: 4"};
        doAllPathsTests("Graph 1", g1, false, 3, 3.0, expectedPaths);

        // now do all paths weighted
        expectedPaths = new String[] {  "Name: B                    cost per path: 8.0000, num paths: 4",
                        "Name: C                    cost per path: 8.5000, num paths: 4",
                        "Name: D                    cost per path: 9.0000, num paths: 4",
                        "Name: A                    cost per path: 9.5000, num paths: 4",
                        "Name: E                    cost per path: 11.0000, num paths: 4",
                        "Name: G                    cost per path: 8.5000, num paths: 6",
                        "Name: E                    cost per path: 12.1667, num paths: 6"};
        doAllPathsTests("Graph 1", g1, true, 3, 15.0, expectedPaths);
    }

    // return a Graph based on the given edges
    private static Graph getGraph(String[][] edges, boolean directed) {
        Graph result = new Graph();
        for (String[] edge : edges) {
            result.addEdge(edge[0], edge[1], Double.parseDouble(edge[2]));
            // If edges are for an undirected graph add edge in other direction too.
            if (!directed) {
                result.addEdge(edge[1], edge[0], Double.parseDouble(edge[2]));
            }
        }
        return result;
    }

    // Tests the all paths method. Run each set of tests twice to ensure the Graph
    // is correctly reseting each time
    private static void doAllPathsTests(String graphNumber, Graph g, boolean weighted,
                    int expectedDiameter, double expectedCostOfLongestShortestPath,
                    String[] expectedPaths) {

        System.out.println("\nTESTING ALL PATHS INFO ON " + graphNumber + ". ");
        for (int i = 0; i < 2; i++) {
            System.out.println("Test run = " + (i + 1));
            System.out.println("Find all paths weighted = " + weighted);
            g.findAllPaths(weighted);
            int actualDiameter = g.getDiameter();
            double actualCostOfLongestShortesPath = g.costOfLongestShortestPath();
            if (actualDiameter == expectedDiameter) {
                System.out.println("Passed diameter test.");
            } else {
                System.out.println("FAILED diameter test. "
                                + "Expected = "  + expectedDiameter +
                                " Actual = " + actualDiameter);
            }
            if (actualCostOfLongestShortesPath == expectedCostOfLongestShortestPath) {
                System.out.println("Passed cost of longest shortest path. test.");
            } else {
                System.out.println("FAILED cost of longest shortest path. "
                                + "Expected = "  + expectedCostOfLongestShortestPath +
                                " Actual = " + actualCostOfLongestShortesPath);
            }
            testAllPathsInfo(g, expectedPaths);
            System.out.println();
        }

    }

    // Compare results of all paths info on Graph to expected results.
    private static void testAllPathsInfo(Graph g, String[] expectedPaths) {
        int index = 0;

        for (AllPathsInfo api : g.getAllPaths()) {
            if (expectedPaths[index].equals(api.toString())) {
                System.out.println(expectedPaths[index] + " is correct!!");
            } else {
                System.out.println("ERROR IN ALL PATHS INFO: ");
                System.out.println("index: " + index);
                System.out.println("EXPECTED: " + expectedPaths[index]);
                System.out.println("ACTUAL: " + api.toString());
                System.out.println();
            }
            index++;
        }
        System.out.println();
    }

    // Test the FootballRanker on the given file.
    private static void doRankTests(FootballRanker ranker) {
        System.out.println("\nTESTS ON FOOTBALL TEAM GRAPH WITH FootBallRanker CLASS: \n");
        double actualError = ranker.doUnweighted(false);
        if (actualError == 13.7) {
            System.out.println("Passed unweighted test");
        } else {
            System.out.println("FAILED UNWEIGHTED ROOT MEAN SQUARE ERROR TEST. Expected 13.7, actual: " + actualError);
        }

        actualError = ranker.doWeighted(false);
        if (actualError == 12.6) {
            System.out.println("Passed weigthed test");
        } else {
            System.out.println("FAILED WEIGHTED ROOT MEAN SQUARE ERROR TEST. Expected 12.6, actual: " + actualError);
        }


        actualError = ranker.doWeightedAndWinPercentAdjusted(false);
        if (actualError == 6.3) {
            System.out.println("Passed unweighted win percent test");
        } else {
            System.out.println("FAILED WEIGHTED  AND WIN PERCENT ROOT MEAN SQUARE ERROR TEST. Expected 6.3, actual: " + actualError);
        }
    }
}
