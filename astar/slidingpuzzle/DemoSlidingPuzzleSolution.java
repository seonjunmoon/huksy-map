package astar.slidingpuzzle;

import astar.AStarSolver;
import astar.LazySolver;
import astar.ShortestPathsSolver;
import astar.SolutionPrinter;

/**
 * Showcases how the AStarSolver can be used for solving sliding puzzles.
 */
public class DemoSlidingPuzzleSolution {

    public static void main(String[] args) {
        BoardState start = BoardState.readBoard("data/puzzles/BasicPuzzle1.txt");
        System.out.println(start);
        int N = start.size();
        BoardState goal = BoardState.solved(N);

        BoardGraph spg = new BoardGraph();

        ShortestPathsSolver<BoardState> solver;
        try {
            solver = new AStarSolver<>(spg, start, goal, 20);
        } catch (UnsupportedOperationException e) {
            System.out.println("AStarSolver doesn't seem to be implemented yet; using LazySolver instead.");
            solver = new LazySolver<>(spg, start, goal, 20);
        }
        SolutionPrinter.summarizeSolution(solver, "\n");
    }
}
