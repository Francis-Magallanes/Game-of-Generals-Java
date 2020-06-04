/*
    @author : Francis John Magallanes
    @since : May,14,2020
 */

package sample;


import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

/**
 * This class is for the managing of the display of pieces that will
 * placed in the board for the main GUI. It will also manage the
 * back end part of the program. It can also process the selection of piece
 * by the user that will be placed in the playing board (also called placement board).
 *
 */

public class PiecesDisplay {

    private GridPane displayBoard;
    private VBox[][]  layoutPiecesDisplay;//this is for the placement of the pieces in the "pieces panel"
    private Label[][] numPiecesLeftIndicator;//this will indicate how many pieces left to be displayed


    private boolean isWhite; //it will monitor whose turn that will place the pieces

    //it will monitor if there are previously selected pieces in the piecesDisplay Gridpane
    private boolean isThereSelectedPiece = false;
    private HashMap<PiecesHiearchy, Integer> numPiecesLeft;//this will monitor the number of the pieces that are not placed
    private PiecesHiearchy[][] piecesArr; //this will monitor the contains of each indices in the displayBoard(Gridpane)

    //these are for the coloring the cells and changing the cell if the cell is selected
    private final String UNSELECTEDCOLORCODE = "-fx-background-color: #8EAF98;";
    private final String SELECTEDCOLORCODE = "-fx-background-color: #D4EFDE ;";

    /*
    It will store the selected indices. For selectedIndices:
    at index 0, it will pertain to the column index or the index along x-axis
    at index 1, it will pertain to the row index or the index along y-axis
     */
    private int[] selectedIndices = {-1,-1};

    /**
     * This is the constructor of the object and it will set-up the inputted gridpane
     *
     * @param displayBoard : gridpane that will be used to display the pieces.
     * @param isWhite : boolean that checks whether the white player is setting up the board or not
     */
    public PiecesDisplay(GridPane displayBoard, boolean isWhite){
        numPiecesLeft = new HashMap<>();

        numPiecesLeftIndicator = new Label[3][5];
        layoutPiecesDisplay = new VBox[3][5];

        piecesArr = new PiecesHiearchy[3][5];
        this.displayBoard = displayBoard;
        this.isWhite = isWhite;

        displayBoard.setGridLinesVisible(true);
        PreparePieces();
    }

    /**
     * This method will process all the visuals for the selecting and unselecting a
     * piece for the display of the pieces. It will also set the selecting index (the array) depending
     * on the selected cell in the gridpane by the user.
     *
     * @param event : the event of the javafx use for the mouse event
     * @return : It will return the selected column index/ index along x-axis and row index / index along y-axis
     *           in an int array. It will return -1,-1 as the column index and the row index if the user unselect
     *           the current selected cell
     */
    public int[] PressPiece (MouseEvent event){

        for (Node node : displayBoard.getChildren()){

            if(node instanceof VBox){

                if(node.getBoundsInParent().contains(event.getX(),event.getY())){

                    if(!node.isDisable()){

                        //this will store the previous indices before overwrting the selectedIndices
                        //because of the method, UnselectSelectedPiece()
                        int [] tempIndices = {selectedIndices[0] , selectedIndices[1]};
                        int colIndexNode = GridPane.getColumnIndex(node);
                        int rowIndexNode = GridPane.getRowIndex(node);

                        if(isThereSelectedPiece){

                            UnselectSelectedPiece();

                        }

                        //this condition is for setting isTherePiecePressed to true if the user doesn't select the selected piece again
                        if((tempIndices[0] != colIndexNode || tempIndices[1] != rowIndexNode) && !isThereSelectedPiece){

                            isThereSelectedPiece = true;
                            node.setStyle(SELECTEDCOLORCODE);

                        }

                        if(isThereSelectedPiece){
                            selectedIndices[0] = colIndexNode;
                            selectedIndices[1] = rowIndexNode;
                        }

                    }


                }

            }

        }

        return selectedIndices;
    }

    /**
     * This method will unselect the selected piece through the selectedIndices and the isThereSelectedPiece
     */
    public void UnselectSelectedPiece(){

        if(isThereSelectedPiece){

            Node node = getNodeFromGridPane(selectedIndices[0] , selectedIndices[1]);

            if(node instanceof VBox){

                node.setStyle(UNSELECTEDCOLORCODE);

                isThereSelectedPiece = false;
                selectedIndices[0] = -1;
                selectedIndices[1] = -1;

            }

        }

    }

    /**
     * This method will get the value of the boolean variable isThereSelectedPiece
     *
     * @return: the value of the boolean variable isThereSelectedPiece
     */
    public boolean getIsThereSelectedPiece(){ return this.isThereSelectedPiece; }

    /**
     * This method will instantiated a Piece object based on the selected indices array
     * Note that it will not create a object if there is no selected indices. Also, it
     * will disable the cell if the number of pieces left goes down to zero. It will also
     * reduce the number of pieces left by one based on the piece at the selected indices.
     * It will also update the labels in the display pieces board
     *
     * @return: a Piece object based on the selected index.
     */
    public Piece getPieceAtSelectedIndices(){

        if(isThereSelectedPiece){

            int numLeft = numPiecesLeft.get(piecesArr[selectedIndices[1]][selectedIndices[0]]);
            PiecesHiearchy pieceRank = piecesArr[selectedIndices[1]][selectedIndices[0]];

            if( numLeft > 0){

                numPiecesLeft.replace(pieceRank, --numLeft);
                UpdateLabel(pieceRank, selectedIndices[0] , selectedIndices[1]);

                //this will set the VBox disable if the number of the specific peice is reached
                //thus, the user will no longer access the cell to pick that piece
                if(numLeft == 0) layoutPiecesDisplay[selectedIndices[1]][selectedIndices[0]].setDisable(true);
                return new Piece(pieceRank , isWhite);

            }

        }

        return null;
    }

    /**
     * This method will mimick the returning of the piece if the user overwrites the cell
     * at the placement board with another piece. It will also update the labels in the
     * display piece gridpane and increase the number of pieces left based on the input
     *
     * @param piece: the piece that is overwritten
     */
    public void ReturnPiece(Piece piece){

        int pieceLeft = numPiecesLeft.get(piece.getRank());

        int[] indexPiece = getIndicesOfPiece(piece.getRank());
        numPiecesLeft.replace(piece.getRank(), ++pieceLeft);
        UpdateLabel(piece.getRank(), indexPiece[0] , indexPiece[1]);

        if(pieceLeft > 0) layoutPiecesDisplay[indexPiece[1]][indexPiece[0]].setDisable(false);

    }

    /**
     * This method will loop through the piecesArr to find the indices
     * at which the PiecesHiearchy input is located at the piecesArr.
     *
     * @param ph: the query to be find in the piecesArr
     * @return : the indices at which the input is located in the piecesArr
     */
    public int[] getIndicesOfPiece(PiecesHiearchy ph){

        int[] indices = {-1,-1};

        for(int j = 0 ; j <3 ; j++){

            for(int i = 0 ; i<5; i++){
                indices[0] = i;
                indices[1] = j;
                if(piecesArr[j][i] == ph) return indices;
            }
        }

        return indices;
    }

    /**
     * this will check if all of the pieces are placed
     *
     * @return : it will return true when all of the pieces are placed. Otherwise, it will return false.
     */
    public boolean isAllPiecesPlaced(){

        for(PiecesHiearchy ph : numPiecesLeft.keySet()){

            if(numPiecesLeft.get(ph) > 0) return false;

        }

        return true;
    }

    /**
     *This method will prepare and display the image of the pieces that will be placed in the board
     */
    private void PreparePieces(){

        Image tempImage;

        //this will iterate through the enum class
        //Note that the indexing in the gridpane also correspond to the
        //indexing of the numPiecesLeft, layoutPiecesDisplay, and layourPiecesPlace
        // array
        int indexX = 0;
        int indexY = 0;

        for(PiecesHiearchy ph : PiecesHiearchy.values()) {

            if (ph == PiecesHiearchy.PRIVATE) {
                numPiecesLeft.put(ph, 6);
            } else if (ph == PiecesHiearchy.SPY) {
                numPiecesLeft.put(ph, 2);
            } else {
                numPiecesLeft.put(ph, 1);
            }

            if (isWhite) {
                tempImage = new Image("assets/WHITE PIECES/WHITE_" + ph + ".png");
            } else {
                tempImage = new Image("assets/BLACK PIECES/BLACK_" + ph + ".png");
            }

            piecesArr[indexY][indexX] = ph;

            ImageView imageView = new ImageView(tempImage);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(100);
            imageView.setFitHeight(30);
            numPiecesLeftIndicator[indexY][indexX] = new Label(ph.getStringValue() + " Left : " + numPiecesLeft.get(ph));


            layoutPiecesDisplay[indexY][indexX] = new VBox();
            layoutPiecesDisplay[indexY][indexX].getChildren().add(0,imageView);
            layoutPiecesDisplay[indexY][indexX].getChildren().add(1, numPiecesLeftIndicator[indexY][indexX]);
            layoutPiecesDisplay[indexY][indexX].setAlignment(Pos.CENTER);
            layoutPiecesDisplay[indexY][indexX].setStyle(UNSELECTEDCOLORCODE);

            displayBoard.add(layoutPiecesDisplay[indexY][indexX] , indexX , indexY);

            if(indexX < 4){
                ++indexX;
            }
            else{
                indexX = 0;
                ++indexY;
            }
        }

        displayBoard.setHgap(5);
        displayBoard.setVgap(5);

    }

    /**
     * This method will return the node at the indicated column and row based on the piecesDisplay gridpane
     *
     * This code is modified.
     * Credits to Sheyas Dave for the original code in stackoverflow
     * Retrieved from: https://stackoverflow.com/questions/17083974/how-get-column-index-and-row-index-in-gridpane-of-javafx
     *
     * @param col : index of the column / in this program the x index
     * @param row : index of the row / in this program the y index
     * @return : node at the col and row index of the gridpane
     */
    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : displayBoard.getChildren()) {

            if(!(node instanceof Group)){
                if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * It will update the label in the grid pane, piecesDisplay.
     *
     * @param ph : the pieceshiearchy to be updated
     * @param colIndex : the column index at which the label is located at the array
     * @param rowIndex: the row index at which the label is located at the array
     */
    private void UpdateLabel(PiecesHiearchy ph, int colIndex, int rowIndex){

        numPiecesLeftIndicator[rowIndex][colIndex].setText(ph.getStringValue() + " Left : " + numPiecesLeft.get(ph));

    }

}
