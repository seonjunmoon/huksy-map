package astar.example;

import astar.AStarSolver;
import astar.LazySolver;
import astar.ShortestPathsSolver;
import astar.SolutionPrinter;
import astar.slidingpuzzle.BoardState;

/**
 * Showcases how the AStarSolver can solve the example from lecture.
 */
public class DemoLectureExampleSolution {
    public static void main(String[] args) {
        WeightedDirectedGraph wdg = new WeightedDirectedGraph(7);
        /* Edges from vertex 0. */
        wdg.addEdge(0, 1, 2);
        wdg.addEdge(0, 2, 1);

        wdg.addEdge(1, 2, 5);
        wdg.addEdge(1, 3, 11);
        wdg.addEdge(1, 4, 3);

        wdg.addEdge(2, 5, 15);

        wdg.addEdge(3, 4, 2);

        wdg.addEdge(4, 2, 1);
        wdg.addEdge(4, 5, 4);
        wdg.addEdge(4, 6, 5);

        wdg.addEdge(6, 3, 1);
        wdg.addEdge(6, 5, 1);

        int start = 0;
        int goal = 6;

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
