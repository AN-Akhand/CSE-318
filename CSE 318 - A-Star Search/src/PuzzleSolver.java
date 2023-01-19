import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class PuzzleSolver {

    static int steps;

    static void printPath(PuzzleBoard goal){
        if(goal != null) {
            printPath(goal.parent);
            System.out.println(goal);
            steps++;
        }
    }

    static boolean isSolvable(String[][] board){
        int inversions = 0;
        int sliderRow = 0;
        int num;
        String[] array = Stream.of(board)
                .flatMap(Stream::of)
                .toArray(String[]::new);
        int size = array.length;
        int boardSize = board.length;
        for(int i = 0; i < size; i++){
            if(array[i].equals("*")){
                sliderRow = i / boardSize;
                continue;
            }
            num = Integer.parseInt(array[i]);
            for(int j = i + 1; j < size; j++){
                if(!array[j].equals("*") && num > Integer.parseInt(array[j])) inversions++;
            }
        }
        if(boardSize % 2 != 0){
            return inversions % 2 == 0;
        }
        else{
            if(((boardSize - sliderRow) % 2 == 0) && (inversions % 2 != 0))
                return true;
            else return ((boardSize - sliderRow) % 2 != 0) && (inversions % 2 == 0);
        }
    }

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int size = Integer.parseInt(reader.readLine());
        String[][] board = new String[size][size];
        for(int i = 0; i < size; i++){
            String line = reader.readLine();
            String[] nums = line.split(" ");
            System.arraycopy(nums, 0, board[i], 0, size);
        }

        System.out.println("1 for hamming 2 for manhattan");

        int hamOrMan = Integer.parseInt(reader.readLine());

        if(isSolvable(board)){
            System.out.println("Puzzle is solvable");
        }
        else{
            System.out.println("Puzzle is not solvable");
            return;
        }

        int expanded = 1;
        PuzzleBoard puzzleBoard = new PuzzleBoard(board, size);
        PuzzleBoard goal;
        HashSet<PuzzleBoard> closedSet = new HashSet<>();
        PriorityQueue<PuzzleBoard> openSet;

        if(hamOrMan == 1) {
            openSet = new PriorityQueue<>((o1, o2) -> {
                if(o1.g + o1.h == o2.g + o2.h){
                    return Integer.compare(o1.m, o2.m);
                }
                else return Integer.compare(o1.g + o1.h, o2.g + o2.h);
            });
        }
        else {
            openSet = new PriorityQueue<>((o1, o2) -> {
                if(o1.g + o1.m == o2.g + o2.m){
                    return Integer.compare(o1.h, o2.h);
                }
                else return Integer.compare(o1.g + o1.m, o2.g + o2.m);
            });
        }

        openSet.add(puzzleBoard);
        while(true){
            assert openSet.peek() != null;
            PuzzleBoard top = openSet.poll();
            if(top.h == 0) {
                goal = top;
                break;
            }
            closedSet.add(top);
            PuzzleBoard[] children = top.generateChildren();
            for(int i = 0; i < 4; i++){
                if(children[i] != null && !closedSet.contains(children[i])){
                    expanded++;
                    openSet.add(children[i]);
                }
            }
        }


        printPath(goal);
        System.out.println(steps - 1);
        System.out.println();

        System.out.println("Number of expanded nodes " + expanded);
        System.out.println("Number of explored nodes " + closedSet.size());
    }
}
