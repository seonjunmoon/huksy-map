package astar.example;

import astar.AStarSolver;
import astar.LazySolver;
import astar.ShortestPathsSolver;
import astar.SolutionPrinter;

/**
 * Showcases how the AStarSolver can solve the example from the spec.
 */
public class DemoAlternateExampleSolution {
    public static void main(String[] args) {
        WeightedDirectedGraph wdg = new WeightedDirectedGraph(6);
        /* Edges from vertex 0. */
        wdg.addEdge(0, 1, 50);
        wdg.addEdge(0, 2, 20);

        wdg.addEdge(1, 4, 20);

        wdg.addEdge(2, 3, 10);

        wdg.addEdge(3, 4, 70);

        wdg.addEdge(4, 3, 10);
        wdg.addEdge(4, 5, 100);

        int start = 0;
        int goal = 5;

        ShortestPathsSolver<Integer> solver;
        try {
            solver = new AStarSolver<>(wdg, start, goal, 10);
        } catch (UnsupportedOperationException e) {
            System.out.println("AStarSolver doesn't seem to be implemented yet; using LazySolver instead.");
            solver = new LazySolver<>(wdg, start, goal, 10);
        }
        SolutionPrinter.summarizeSolution(solver, " => ");
    }
}
