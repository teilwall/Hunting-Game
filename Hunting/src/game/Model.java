package game;

public class Model {

    private final int size;
    private Player actualPlayer;
    private final Player[][] table;
    public int moves;
    private int currRow;
    private int currCol;
    private int fugitiveX;
    private int fugitiveY;
    
    public Model(int size) {
        this.size = size;
        this.moves = 4*size;
        actualPlayer = Player.FUGITIVE;

        table = new Player[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if(i == j && j == size/2) {
                    table[i][j] = Player.FUGITIVE;
                    fugitiveX = i;
                    fugitiveY = j;
                } else if(i == 0 && j == 0 || i == 0 && j == size-1 || i == size-1 && j == 0 || i == size-1 && j == size-1){
//                    System.out.println("hunter");
                    table[i][j] = Player.HUNTER;
                }else {
                    table[i][j] = Player.NOBODY;  
                }
            }
        }
    }
    
    public boolean selectPlayer(int i, int j){
        if(table[i][j] != actualPlayer){
            return false;
        }
        this.currRow = i;
        this.currCol = j;
        return true;
    }
    
    public boolean canMove(int row, int col) {
        return (row == currRow-1 && col == currCol) || (row == currRow+1 && col == currCol) || (row == currRow && col == currCol-1) || (row == currRow && col == currCol+1);
    }

    public boolean step(int row, int column) {
        if( canMove(row, column) && table[row][column] == Player.NOBODY) {
            if(actualPlayer == Player.HUNTER){
                this.moves--;
            }
            table[currRow][currCol] = Player.NOBODY;
            table[row][column] = actualPlayer;
            if(actualPlayer.equals(Player.FUGITIVE)){
                fugitiveX = row;
                fugitiveY = column;
            }
//            System.out.println(row + " " + column);
            actualPlayer = actualPlayer.equals(Player.HUNTER) ? Player.FUGITIVE : Player.HUNTER; 
            return true;
        }
        return false;
    }

    public boolean isSurrounded(int row, int col) {
        return (row-1 < 0 || table[row-1][col].equals(Player.HUNTER)) && (size <= row+1 || table[row+1][col].equals(Player.HUNTER)) && (col-1 < 0 || table[row][col-1].equals(Player.HUNTER)) && (col+1 >= size || table[row][col+1].equals(Player.HUNTER));
   
    }
    public Player findWinner() {
        if(moves > 0 && isSurrounded(fugitiveX, fugitiveY)) {
            return Player.HUNTER;
        }
        if(moves <= 0 && !isSurrounded(fugitiveX, fugitiveY)) {
            return Player.FUGITIVE;
        }
        return Player.NOBODY;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }
    
    public boolean check(int i, int j) {
        return table[i][j] == Player.NOBODY;
    }
}
