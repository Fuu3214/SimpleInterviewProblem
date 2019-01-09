Descritption:

This is a solution for problem here: http://ensoftupdate.com/download/jobs/programming-exercise-0114.pdf

How to Run:
    1.Compile: javac <path-of-source-code>\*.java
    2.Run(after compile): java Main
    3.Input: Via standard input. Should follow correct format according to instruction.
    
OverView:
/**
 *To solve this problem:
 *	1.Add a source node, with edges from source to all possible starting node for Peggy
 *	2.Add a sink node, with edges from all possible starting node for Sam to sink
 *	3.Run a DFS starting from source node
 *	4.Reverse all edges, run DFS starting from sink node
 * 	5.Output intersection of visited nodes of both DFS search
 * 
 * Optimization:
 *	1.Use HashMap to convert String nodes to integer. Time for adding node: O(1);
 *	2.Use BitSet to store adjacent matrix. Memory use is reduced significantly.
 *	3.Use BitSet to store visited list and avoid list, DFS check for them each iteration. Time complexity 
 *    for each check: O(1);
 *	4.Created source and sink nodes so that we only run DFS twice
 *	5.Instead of reversing all edges, we modify DFS so that we actually run DFS on transpose of adjacent 
 *    matrix(But we didn't actually take transpose). 
 *	6.Create 2 threads for each DFS search so that we get result for 2 search concurrently.
 *	7.Intersection is computed by "and" operation between 2 BitSets. Time complexity O(1);
 *  
 *  Time complexity for search: O(2N) = O(N). Since we run DFS twice and for each node we visit it only once
 *  Total time complexity: O(M)<=O(N^2). It is because to build the graph we need to take every edge as input
 *  
 *  Running time on my test cases(My CPU: Core i7-7820HQ): 
 *  --Less than 30s on a 10000 nodes complete graph
 *  --Less than 5s on a 1000 nodes complete graph
 *  
 * @author Shang
 * 
 */

