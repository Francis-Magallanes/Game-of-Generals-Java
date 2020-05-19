package sample;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * This class will be for the managing of the board, both the front-end process and back-end process
 */
public class PlayingBoard {

    private GridPane boardGridPane;
    private VBox[][] boardCellLayout;

    private Piece[][] boardArr;

    //these are for the coloring the cells and changing the cell if the cell is selected
    private final String FIRSTUNSELECTEDCOLORCODE = "-fx-background-color: #FFFD82;";
    private final String SECONDUNSELECTEDCOLORCODE = "-fx-background-color: #2D3047;";
    private final String FIRSTSELECTEDCOLORCODE = "-fx-background-color: #2C1B0C ;";
    private final String SECONDSELECTEDCOLORCODE = "-fx-background-color: #BEC0C8 ;";


    PlayingBoard(Piece[][] layoutWhite, Piece[][] layoutBlack, GridPane boardGridPane){
        //TODO: Initialize the front end stuff and te back end stuff

        this.boardGridPane = boardGridPane;
        InitializeBoardLayout();

    }

    PlayingBoard(GridPane boardGridPane){

        this.boardGridPane = boardGridPane;
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

    public ArrayList<int[]> getMovesPieceAt(int colIndex, int rowIndex){
        //TODO: determine the all possible moves and return the all possible moves

        return null;
    }

    public void ShowPossibleMoves(){
        //TODO: do the front-end stuff for the showing of the possible moves
    }

    public void MovePieceAt(int colIndex, int rowIndex){
        //TODO: do the front end stuff of the moving also the challenging

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

                boardCellLayout[j][i].setAlignment(Pos.CENTER);
                boardCellLayout[j][i].getChildren().add(imageView);

                if((i+j)%2 == 0 ) boardCellLayout[j][i].setStyle(FIRSTUNSELECTEDCOLORCODE);
                else boardCellLayout[j][i].setStyle(SECONDUNSELECTEDCOLORCODE);

                boardGridPane.add(boardCellLayout[j][i] , j , i);
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

    }
}
