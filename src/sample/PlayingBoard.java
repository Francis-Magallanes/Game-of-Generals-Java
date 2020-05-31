package sample;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.util.ArrayList;
import java.util.HashMap;

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

    //these will store the eliminated pieces
    private HashMap<PiecesHiearchy , Integer> blackEliminatedPieces;
    private HashMap<PiecesHiearchy , Integer> whiteEliminatedPieces;

    PlayingBoard(Piece[][] layoutWhite, Piece[][] layoutBlack, GridPane boardGridPane){
        //TODO: Initialize the front end stuff and te back end stuff

        this.boardGridPane = boardGridPane;

        blackEliminatedPieces = new HashMap<>();
        whiteEliminatedPieces = new HashMap<>();

        TransferToBoardArr(layoutWhite,layoutBlack);


        InitializeBoardLayout();

    }

    public Piece[][] RotateBoard(){
        //TODO: rotate the board and return the result array

        return null;
    }

    public Piece getPieceAt(int colIndex, int rowIndex){

        return boardArr[rowIndex][colIndex];
    }

    public void PrintBoardArr(){

        System.out.println();
        for(int j = 0 ; j < 8 ; j++){

            for(int i = 0 ; i < 9 ; i++){

                if(boardArr[j][i] != null) System.out.print("\t - " + boardArr[j][i].getRank().getStringValue());
                else System.out.print("\t - null" );
            }
            System.out.println();

        }

    }

    /**
     * This will set the selected indices array based on where the mouse is pressed.
     * It will also make the visual indicator for the selecting. It includes also the
     * showing of possible moves when a piece is selected.
     *
     * @param colIndex: column index of the selected cell
     * @param rowIndex: row index of the selected cell
     */
    public void SelectCell(int colIndex, int rowIndex){

        selectedIndices[0]= colIndex;
        selectedIndices[1]= rowIndex;

        if((colIndex+rowIndex)%2 == 0) {

            boardCellLayout[rowIndex][colIndex].setStyle(FIRSTSELECTEDCOLORCODE);
        }
        else {
            boardCellLayout[rowIndex][colIndex].setStyle(SECONDSELECTEDCOLORCODE);
        }

        ShowPossibleMoves();
    }

    /**
     * This will unselect the selected cell at the selected indices. It will also
     * set the selectedIndices to {-1,-1} , hide the possible moves and disable the select
     * indicator in the selected cell
     */
    public void UnselectSelectedCell(){

        HidePossibleMoves();

        if((selectedIndices[0]+selectedIndices[1])%2 == 0) boardCellLayout[selectedIndices[1]][selectedIndices[0]].setStyle(FIRSTUNSELECTEDCOLORCODE);
        else boardCellLayout[selectedIndices[1]][selectedIndices[0]].setStyle(SECONDUNSELECTEDCOLORCODE);

        selectedIndices[0] = -1;
        selectedIndices[1] = -1;
    }


    /**
     * This will make the selected piece move to the inputted indices
     *
     * @param toColIndex: the column index of the cell to which the user will put the selected piece
     * @param toRowIndex: the row index of the cell to which the user will put the selected piece
     *
     */
    public void MovePieceAt(int toColIndex, int toRowIndex){
        //TODO: do the capture the flag scenario. Also adjust the function to return boolean value to determine if there is a winner

        int[] tempIndices = {selectedIndices[0] , selectedIndices[1]};

        UnselectSelectedCell();

        if(isTherePieceAt(toColIndex,toRowIndex)){

            Piece winner = ChallengeResult(boardArr[tempIndices[1]][tempIndices[0]] , boardArr[toRowIndex][toColIndex]);

            if(winner == null){
                //the piece is of the same rank
                StoreLosePiece(boardArr[tempIndices[1]][tempIndices[0]]);
                boardArr[tempIndices[1]][tempIndices[0]] = null;
                ((ImageView)(boardCellLayout[tempIndices[1]][tempIndices[0]].getChildren().get(0))).setImage(null);

                StoreLosePiece(boardArr[toRowIndex][toColIndex]);
                boardArr[toRowIndex][toColIndex] = null;
                ((ImageView)(boardCellLayout[toRowIndex][toColIndex].getChildren().get(0))).setImage(null);
            }
            else if (winner == boardArr[tempIndices[1]][tempIndices[0]]){
                //the currently selected piece won
                StoreLosePiece(boardArr[toRowIndex][toColIndex]);
                MovePieceToEmptyCell(tempIndices[0] , tempIndices[1] , toColIndex , toRowIndex);
            }
            else if(winner == boardArr[toRowIndex][toColIndex]){
                //the currently selected piece lose
                StoreLosePiece(boardArr[tempIndices[1]][tempIndices[0]]);
                boardArr[tempIndices[1]][tempIndices[0]] = null;
                ((ImageView)(boardCellLayout[tempIndices[1]][tempIndices[0]].getChildren().get(0))).setImage(null);
            }

        }
        else{

           MovePieceToEmptyCell(tempIndices[0] , tempIndices[1] , toColIndex , toRowIndex);
        }

    }

    /**
     * this method will check if the input cell indices is a eligible move for the
     * selected piece at the selected cell
     *
     * @param toColIndex : the column index of the cell to which the user will put the selected piece
     * @param toRowIndex : the row index of the cell to which the user will put the selected piece
     * @return: true - if the input indices is eligble move based on the selected indices
     */
    public boolean isEligibleMove(int toColIndex, int toRowIndex){

        int[] inputIndices = {toColIndex , toRowIndex};

        ArrayList<int[]> possibleMoves = getMovesPieceAt(selectedIndices[0] , selectedIndices[1]);

        for(int[] moves : possibleMoves){
            if(inputIndices[0] == moves[0] && inputIndices[1] == moves[1]){
                return true;
            }
        }

        return  false;
    }

    /**
     * This will check if there is Piece object at the inputted indices
     *
     * @param colIndex: column index of the board at which you want to check the piece
     * @param rowIndex: row index of the board at which you want to check the piece
     * @return: True - if it is not null at the specified indices, False - if it is null at the specified indices
     */
    public boolean isTherePieceAt(int colIndex, int rowIndex){
        return boardArr[rowIndex][colIndex] != null;
    }


    public HashMap<PiecesHiearchy, Integer> getBlackEliminatedPieces() {
        return blackEliminatedPieces;
    }

    public HashMap<PiecesHiearchy, Integer> getWhiteEliminatedPieces() {
        return whiteEliminatedPieces;
    }

    /**
     * this method will check if the inputted player wins or not
     * @param isWhite: true - white player , false: black player
     * @return: if the current player is a winner
     */
    public boolean isWinner(boolean isWhite){

        if(isWhite){

            if(blackEliminatedPieces.containsKey(PiecesHiearchy.FLAG)) return true;

            int [] whiteFlagIndices = FindFlag(isWhite);

            if(whiteFlagIndices[1] == 7){

                if((whiteFlagIndices[0] - 1 >= 0)){

                    if(whiteFlagIndices[0] + 1 <= 8){
                        //this is for the middle part of the board

                        return boardArr[whiteFlagIndices[1]][whiteFlagIndices[0] - 1] == null &&
                                boardArr[whiteFlagIndices[1]][whiteFlagIndices[0] + 1] == null &&
                                boardArr[whiteFlagIndices[1] - 1][whiteFlagIndices[0]] == null;

                    }
                    else{
                        //this is for the right top corner of the board

                        return boardArr[whiteFlagIndices[1]][whiteFlagIndices[0] - 1] == null &&
                                boardArr[whiteFlagIndices[1] - 1][whiteFlagIndices[0]] == null;

                    }

                }
                else{
                    //this is for the left top corner corner of board
                    return boardArr[whiteFlagIndices[1]][whiteFlagIndices[0] + 1] == null &&
                            boardArr[whiteFlagIndices[1] - 1][whiteFlagIndices[0]] == null;

                }

            }

        }
        else{

            if(whiteEliminatedPieces.containsKey(PiecesHiearchy.FLAG)) return true;

            int [] blackFlagIndices = FindFlag(isWhite);

            if(blackFlagIndices[1] == 7){

                if((blackFlagIndices[0] - 1 >= 0)){

                    if(blackFlagIndices[0] + 1 <= 8){
                        //this is for the middle part of the board

                        return boardArr[blackFlagIndices[1]][blackFlagIndices[0] - 1] == null &&
                                boardArr[blackFlagIndices[1]][blackFlagIndices[0] + 1] == null &&
                                boardArr[blackFlagIndices[1] - 1][blackFlagIndices[0]] == null;

                    }
                    else{
                        //this is for the right top corner of the board
                        return boardArr[blackFlagIndices[1]][blackFlagIndices[0] - 1] == null &&
                                boardArr[blackFlagIndices[1] - 1][blackFlagIndices[0]] == null;

                    }

                }
                else{
                    //this is for the left top corner corner of board
                    return boardArr[blackFlagIndices[1]][blackFlagIndices[0] + 1] == null &&
                            boardArr[blackFlagIndices[1] - 1][blackFlagIndices[0]] == null;

                }

            }

        }

        return false;
    }

    public void ShowPiecesPlayer(boolean isWhite){
        Image imageHide;

        for(int j = 0 ; j < 8 ; j++){

            for(int i = 0; i < 9; i++){

               if(boardArr[j][i] != null){
                   if(boardArr[j][i].isWhite() == isWhite){

                       ImageView imageView = (ImageView) boardCellLayout[j][i].getChildren().get(0);
                       imageView.setImage(boardArr[j][i].getImage());

                   }
                   else{

                       if(isWhite == true){
                           //this will hide the black player piece
                           imageHide = new Image ("assets/BLACK_HIDE.png");
                       }
                       else{
                           //this will hide the white player pieces
                           imageHide = new Image ("assets/WHITE_HIDE.png");
                       }

                        ImageView imageView = (ImageView) boardCellLayout[j][i].getChildren().get(0);
                        imageView.setImage(imageHide);

                   }

               }

            }
        }
    }

    /**
     * This will show the cells for the possible moves of the player
     */
    private void ShowPossibleMoves(){
        //TODO: do the front-end stuff for the showing of the possible moves

        ArrayList<int[]> sp = getMovesPieceAt(selectedIndices[0],selectedIndices[1]);

        for(int[] moves : sp){

            if(boardArr[moves[1]][moves[0]] == null)boardCellLayout[moves[1]][moves[0]].setStyle(SHOWPOSSIBLEMOVECODE);
            else boardCellLayout[moves[1]][moves[0]].setStyle(SHOWPOSSIBLETAKENCODE);

        }

    }

    /**
     * This will hide the cells at which the cells is highlighted because of the show
     * possibeMoves method
     */
    private void HidePossibleMoves(){

        ArrayList<int[]> previousPossibleMoves = getMovesPieceAt(selectedIndices[0] , selectedIndices[1]);

        for(int[] moves : previousPossibleMoves){

            if((moves[0]+moves[1])%2 == 0) boardCellLayout[moves[1]][moves[0]].setStyle(FIRSTUNSELECTEDCOLORCODE);
            else boardCellLayout[moves[1]][moves[0]].setStyle(SECONDUNSELECTEDCOLORCODE);
        }

    }

    /**
     * This method will determine the possible moves of the selected pieces
     *
     * @param x: column index at which a piece is located
     * @param y: row index at which a piece is located
     * @return: an arraylist that contains the column and row indices at which
     * the player can move the piece located at the inputted indices. Note that
     * the column and row indices is in the form of array which the index 0 refers
     * to the column index while the index 1 refers to the row index.
     */
    private ArrayList<int[]> getMovesPieceAt(int x , int y){
        ArrayList<int []> possibleMoves = new ArrayList<>();

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
     * This method is the "arbiter" for the game. It will check which piece has the
     * higher rank between the inputted piece. This is implementation of the challenge
     * system.
     *
     * @param p1: piece to be compared
     * @param p2: piece to compared
     * @return: the piece with the higher rank. If both piece is of the same rank, it will return a null
     */
    private Piece ChallengeResult(Piece p1 , Piece p2){

        if(p1.getRank() == PiecesHiearchy.PRIVATE && p2.getRank() == PiecesHiearchy.SPY){
            return p1;
        }
        else if(p1.getRank() == PiecesHiearchy.SPY && p2.getRank() == PiecesHiearchy.PRIVATE){
            return p2;
        }
        else if(p1.getRankValue() > p2.getRankValue()){
            return p1;
        }
        else if (p1.getRankValue() < p2.getRankValue()){
            return  p2;
        }
        else{
            return null;
        }

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
     * This will move the piece to a empty cell.
     *
     * @param fromColIndex : column index of which the piece to be moved is located
     * @param fromRowIndex: row index of which the piece to be moved is located
     * @param toColIndex: column index for which the piece will be moved to
     * @param toRowIndex: row index for which the piece will be moved to
     */
    private void MovePieceToEmptyCell(int fromColIndex, int fromRowIndex, int toColIndex, int toRowIndex){

        boardArr[toRowIndex][toColIndex] = boardArr[fromRowIndex][fromColIndex];
        boardArr[fromRowIndex][fromColIndex] = null;

        Node image1 = boardCellLayout[toRowIndex][toColIndex].getChildren().get(0);
        Node image2 = boardCellLayout[fromRowIndex][fromColIndex].getChildren().get(0);

        if(image1 instanceof ImageView && image2 instanceof ImageView){
            ((ImageView) image1).setImage(boardArr[toRowIndex][toColIndex].getImage());
            ((ImageView) image2).setImage(null);
        }

    }

    /**
     * this will store the losing piece to the respective hashmaps
     * @param lose: the piece that loses the challange
     */
    private void StoreLosePiece(Piece lose){

        PiecesHiearchy rank = lose.getRank();

        if(lose.isWhite()){

            //this is for the white eliminated pieces
            if(whiteEliminatedPieces.containsKey(rank)){
                Integer num = whiteEliminatedPieces.get(rank);
                whiteEliminatedPieces.replace(rank , ++num);
            }
            else{
                whiteEliminatedPieces.put(rank , 1);
            }

        }
        else{

            //this is for the black eliminated pieces
            if(blackEliminatedPieces.containsKey(rank)){
                Integer num = blackEliminatedPieces.get(rank);
                blackEliminatedPieces.replace(rank , ++num);
            }
            else{
                blackEliminatedPieces.put(rank , 1);
            }

        }

    }

    /**
     * this method will identify at what indices the flag of the player located
     *
     * @param isWhite: true - if white player , false- if black player
     * @return: 1-d array with size of 2 , indices[0] - column index and indices[1] - row index
     * the array will be {-1,-1} if the flag is found
     */
    private int[] FindFlag(boolean isWhite){

        int[] indices = {-1 , -1};

        for(int j = 0 ; j < 8 ; j++){

            for(int i = 0; i < 9; i++){

                if(boardArr[j][i] != null)
                    if(boardArr[j][i].isWhite() == isWhite)
                        if(boardArr[j][i].getRank() == PiecesHiearchy.FLAG) {
                            indices[0] = i;
                            indices[1] = j;
                            return indices;
                        }
            }
        }

        return indices;
    }

}
