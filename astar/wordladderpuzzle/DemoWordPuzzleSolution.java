package astar.wordladderpuzzle;

import astar.AStarSolver;
import astar.LazySolver;
import astar.ShortestPathsSolver;
import astar.SolutionPrinter;

/**
 * Showcases how the AStarSolver can be used for solving word ladders.
 */
public class DemoWordPuzzleSolution {
    public static void main(String[] args) {
        String start = "horse";
        String goal = "nurse";

        WordGraph wg = WordGraph.readWords("data/puzzles/words10000.txt");

        ShortestPathsSolver<String> solver;
        try {
            solver = new AStarSolver<>(wg, start, goal, 10);
        } catch (UnsupportedOperationException e) {
            System.out.println("AStarSolver doesn't seem to be implemented yet; using LazySolver instead.");
            solver = new LazySolver<>(wg, start, goal, 10);
        }
        SolutionPrinter.summarizeSolution(solver, "->");
    }
}
