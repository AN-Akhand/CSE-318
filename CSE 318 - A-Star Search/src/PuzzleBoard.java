import java.util.Objects;

public class PuzzleBoard {
    String[][] board;
    int size;
    int sliderX;
    int sliderY;
    int g;
    int h;
    int m;
    PuzzleBoard parent;




    PuzzleBoard(String[][] board, int size){
        this.board = board;
        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                if(Objects.equals(board[i][j], "*")){
                    sliderX = j;
                    sliderY = i;
                }
            }
        }
        this.size = size;
        g = 0;
        h = 0;
        m = 0;
        getDistance();
    }

    PuzzleBoard(String[][] board, int x, int y, PuzzleBoard parent){
        this.board = board;
        sliderX = x;
        sliderY = y;
        this.parent = parent;
        this.size = parent.size;
        g = parent.g + 1;
        h = 0;
        m = 0;
        getDistance();
    }

    PuzzleBoard[] generateChildren(){
        PuzzleBoard[] children = new PuzzleBoard[4];
        children[0] = moveLeft();
        children[1] = moveRight();
        children[2] = moveUp();
        children[3] = moveDown();
        return children;
    }

    PuzzleBoard moveLeft(){

        if(sliderX == 0) return null;

        String[][] newBoard = cloneBoard();

        newBoard[sliderY][sliderX - 1] = board[sliderY][sliderX];
        newBoard[sliderY][sliderX] = board[sliderY][sliderX - 1];

        return new PuzzleBoard(newBoard, sliderX - 1, sliderY, this);
    }

    PuzzleBoard moveRight(){

        if(sliderX == size - 1) return null;

        String[][] newBoard = cloneBoard();

        newBoard[sliderY][sliderX + 1] = board[sliderY][sliderX];
        newBoard[sliderY][sliderX] = board[sliderY][sliderX + 1];

        return new PuzzleBoard(newBoard, sliderX + 1, sliderY, this);
    }

    PuzzleBoard moveUp(){

        if(sliderY == 0) return null;

        String[][] newBoard = cloneBoard();

        newBoard[sliderY - 1][sliderX] = board[sliderY][sliderX];
        newBoard[sliderY][sliderX] = board[sliderY - 1][sliderX];

        return new PuzzleBoard(newBoard, sliderX, sliderY - 1, this);
    }

    PuzzleBoard moveDown(){

        if(sliderY == size - 1) return null;

        String[][] newBoard = cloneBoard();

        newBoard[sliderY + 1][sliderX] = board[sliderY][sliderX];
        newBoard[sliderY][sliderX] = board[sliderY + 1][sliderX];

        return new PuzzleBoard(newBoard, sliderX, sliderY + 1, this);
    }

    private String[][] cloneBoard(){
        String[][] newBoard = new String[size][];
        for(int i = 0; i < size; i++)
            newBoard[i] = board[i].clone();
        return newBoard;
    }


    void getDistance(){
        int number = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                number++;
                if(Objects.equals(board[i][j], "*")) continue;
                int num = Integer.parseInt(board[i][j]) - 1;
                int r = num / size;
                int c = num % size;
                m += (Math.abs(r - i) + Math.abs(c - j));
                if(number != (num + 1)) h++;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                str.append(board[i][j]).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleBoard that = (PuzzleBoard) o;
        return toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
