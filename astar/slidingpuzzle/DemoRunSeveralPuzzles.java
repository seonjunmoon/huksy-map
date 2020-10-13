package astar.slidingpuzzle;

import astar.AStarSolver;
import astar.LazySolver;
import astar.ShortestPathsSolver;
import astar.SolutionPrinter;

/**
 * Showcases how the AStarSolver can be used for solving sliding puzzles.
 * Runs several puzzles in a row.
 */
public class DemoRunSeveralPuzzles {
    private static String[] basicPuzzles = {
        "BasicPuzzle1.txt",
        "BasicPuzzle2.txt",
        "BasicPuzzle3.txt",
        "BasicPuzzle4.txt",
        "BasicPuzzle5.txt",
    };

    private static String[] hardPuzzles = {
        "HardPuzzle1.txt",
        "HardPuzzle2.txt",
        "HardPuzzle3.txt",
    };

    private static String[] elitePuzzles = {
        "ElitePuzzle1.txt",
        "ElitePuzzle2.txt",
        "ElitePuzzle3.txt",
    };

    public static void main(String[] args) {
        String[] puzzleFiles = hardPuzzles;

        System.out.println(puzzleFiles.length + " puzzle files being run.");
        for (String puzzleFile : puzzleFiles) {
            BoardState start = BoardState.readBoard("data/puzzles/" + puzzleFile);
            int N = start.size();
            BoardState goal = BoardState.solved(N);

            BoardGraph spg = new BoardGraph();
            System.out.println(puzzleFile + ":");
            ShortestPathsSolver<BoardState> solver;
            try {
                solver = new AStarSolver<>(spg, start, goal, 30);
            } catch (UnsupportedOperationException e) {
                System.out.println("AStarSolver doesn't seem to be implemented yet; using LazySolver instead.");
                solver = new LazySolver<>(spg, start, goal, 30);
            }
            SolutionPrinter.summarizeOutcome(solver);
        }
    }
}
