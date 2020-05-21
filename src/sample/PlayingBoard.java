package sample;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.util.ArrayList;

/**
 * This class will be for the managing of the board, both the front-end process and back-end process
 */
public class PlayingBoard{

    private GridPane boardGridPane;
    private VBox[][] boardCellLayout;

    private Piece[][] boardArr;

    private final String SHOWPOSSIBLEMOVECODE = "-fx-background-color: #FCFDFF;";


    //these are for the coloring the cells and changing the cell if the cell is selected
    private final String FIRSTUNSELECTEDCOLORCODE = "-fx-background-color: #FFFD82;";
    private final String SECONDUNSELECTEDCOLORCODE = "-fx-background-color: #2D3047;";
    private final String FIRSTSELECTEDCOLORCODE = "-fx-background-color: #2C1B0C ;";
    private final String SECONDSELECTEDCOLORCODE = "-fx-background-color: #BEC0C8 ;";
    private final String SHOWPOSSIBLETAKENCODE = "-fx-background-color: #F17A82;";
    /*
   It will store the selected indices. For selectedIndices:
   at index 0, it will pertain to the column index or the index along x-axis
   at index 1, it will pertain to the row index or the index along y-axis
    */
    private int[] selectedIndices = {-1,-1};

    PlayingBoard(Piece[][] layoutWhite, Piece[][] layoutBlack, GridPane boardGridPane){
        //TODO: Initialize the front end stuff and te back end stuff

        this.boardGridPane = boardGridPane;

        TransferToBoardArr(layoutWhite,layoutBlack);

        InitializeBoardLayout();

    }

    public Piece[][] RotateBoard(){
        //TODO: rotate the board and return the result array

        return null;
    }

    public Piece getPieceAt(int colIndex, int rowIndex){
        //TODO: get the value at the specified index and return the object found at that indices

        return null;
    }




    public void ShowPossibleMoves(int selectedColIndex, int selectedRowIndex){
        //TODO: do the front-end stuff for the showing of the possible moves

        setSelectedIndicesCell(selectedColIndex, selectedRowIndex);

        ArrayList<int[]> sp = getMovesPieceAtSelectedCell();

        for(int[] moves : sp){

            if(boardArr[moves[1]][moves[0]] == null)boardCellLayout[moves[1]][moves[0]].setStyle(SHOWPOSSIBLEMOVECODE);
            else boardCellLayout[moves[1]][moves[0]].setStyle(SHOWPOSSIBLETAKENCODE);

        }

    }

    public void MovePieceAt(int colIndex, int rowIndex){
        //TODO: do the front end stuff of the moving also the challenging

    }



    private ArrayList<int[]> getMovesPieceAtSelectedCell(){
        //TODO: determine the all possible moves and return the all possible moves
        ArrayList<int []> possibleMoves = new ArrayList<>();
        int x = selectedIndices[0];
        int y = selectedIndices[1];

        if(x-1 >= 0){
            //this is for the left possible move

            if(boardArr[y][x-1] != null){

                if(boardArr[y][x].isWhite() != boardArr[y][x-1].isWhite()){
                    int[] arr = {x -1 , y};
                    possibleMoves.add(arr);
                }

            }
            else{
                int[] arr = {x -1 , y};
                possibleMoves.add(arr);
            }

        }

        if(x + 1 <= 8){
            //this is for the right possible move
            if( boardArr[y][x+1] != null){

                if(boardArr[y][x].isWhite() != boardArr[y][x+1].isWhite()){
                    int[] arr = {x + 1 , y};
                    possibleMoves.add(arr);
                }

            }
            else{
                int[] arr = {x + 1 , y};
                possibleMoves.add(arr);
            }

        }

        if(y - 1 >= 0){
            //this is for the down possible move

            if(boardArr[y -1][x] != null){

                if(boardArr[y][x].isWhite() != boardArr[y -1][x].isWhite()){
                    int[] arr = {x , y -1};
                    possibleMoves.add(arr);
                }

            }
            else{
                int[] arr = {x , y -1};
                possibleMoves.add(arr);
            }

        }

        if(y + 1 <= 7 ){
            //this is for the up possible move
            if(boardArr[y+1][x] != null){

                if(boardArr[y][x].isWhite() != boardArr[y+1][x].isWhite()){
                    int[] arr = {x , y + 1};
                    possibleMoves.add(arr);
                }

            }
            else{
                int[] arr = {x , y + 1};
                possibleMoves.add(arr);
            }

        }

        return possibleMoves;
    }

    /**
     * This method will initialize the board's layout such as the imageview
     * and the vbox
     */
    private void InitializeBoardLayout(){

        boardCellLayout = new VBox[8][9];

        for(int j = 0 ; j < 8 ; j++){

            for(int i = 0 ; i < 9; i++){

                boardCellLayout[j][i] = new VBox();

                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(55);
                imageView.setFitWidth(85);

                //this will set the image
                if(this.boardArr[j][i] != null) {
                    imageView.setImage(boardArr[j][i].getImage());
                }

                boardCellLayout[j][i].setAlignment(Pos.CENTER);
                boardCellLayout[j][i].getChildren().add(imageView);

                if((i+j)%2 == 0 ) boardCellLayout[j][i].setStyle(FIRSTUNSELECTEDCOLORCODE);
                else boardCellLayout[j][i].setStyle(SECONDUNSELECTEDCOLORCODE);

                boardGridPane.add(boardCellLayout[j][i] , i , j);
            }

        }

        boardGridPane.setHgap(4);
        boardGridPane.setVgap(4);
    }

    /**
     * This transfer the inputed array in to the local board array, boardArr
     * @param layoutWhite: array that contains the pieces object of the white player at the specified indices
     * @param layoutBlack: array that contains the pieces object of the black player at the specified indices
     */
    private void TransferToBoardArr(Piece[][] layoutWhite, Piece[][] layoutBlack){

        int colSize = 9;
        int rowSize = 8;

        this.boardArr = new Piece[rowSize][colSize];

        //this will transfer the input to the local gameplay array
        for(int j=0 ; j < 3 ; j++ ){

            for(int i = 0 ; i < 9 ; i++){

                boardArr[j][i] = layoutBlack[j][i];
                boardArr[rowSize-j-1][i] = layoutWhite[j][i];
            }

        }

    }

    /**
     * This will set the selected indices array based on where the mouse is pressed.
     * It will also make the visual indicator for the selecting
     * @param colIndex: column index of the selected cell
     * @param rowIndex: row index of the selected cell
     */
    private void setSelectedIndicesCell(int colIndex, int rowIndex){

        selectedIndices[0]= colIndex;
        selectedIndices[1]= rowIndex;

    }

    private void UnselectSelectedCell(){

    }

}
