/*
    @author : Francis John Magallanes
    @since : May,14,2020
 */

package sample;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;



/**
 * This class will manage all of the placing of the pieces into the board both the
 * frontend and backend of the program
 */

public class PiecesPlacement {


    private GridPane placementBoard;
    private VBox[][] layoutPlacement;//this is for the placement of the pieces in the board

    private boolean isWhite;
    private boolean isThereSelectedCell = false; //this variable will monitor the if there is selected cell in the board
    private Piece[][] placementBoardArr;//this will store the pieces in an array. It is also the back-end reresentation of the board

    //these are for the coloring the cells and changing the cell if the cell is selected
    private final String FIRSTUNSELECTEDCOLORCODE = "-fx-background-color: #FFFD82;";
    private final String SECONDUNSELECTEDCOLORCODE = "-fx-background-color: #2D3047;";
    private final String FIRSTSELECTEDCOLORCODE = "-fx-background-color: #2C1B0C ;";
    private final String SECONDSELECTEDCOLORCODE = "-fx-background-color: #BEC0C8 ;";

    /*
   It will store the selected indices. For selectedIndices:
   at index 0, it will pertain to the column index or the index along x-axis
   at index 1, it will pertain to the row index or the index along y-axis
    */
    private int[] selectedIndices = {-1,-1};

    /**
     * This is the constructor of the class and it will initialize the placement board
     * for the placing of the pieces in the board.
     *
     * @param placementBoard : gridpane for which the pieces will be placed
     * @param isWhite : boolean that checks whether the white player is setting up the board
     */
    public PiecesPlacement(GridPane placementBoard , boolean isWhite){

        this.placementBoard = placementBoard;

        this.placementBoardArr = new Piece[3][9];
        this.isWhite = isWhite;
        this.layoutPlacement= new VBox[3][9];

        PrepareBoard();
    }

    /**
     * This method will return the column and row index of the selected cell of the
     * placementBoard gridpane.
     *
     * @param event : mouse event
     * @return : an array that contains the column index and row index. at 0 index is the column index
     *           and at 1 is the row index
     */
    public int[] getIndexToPlaced (MouseEvent event){

        int[] indices = {-1,-1};

        for (Node node : placementBoard.getChildren()){

            if(node instanceof VBox){

                if(node.getBoundsInParent().contains(event.getX(),event.getY())){

                    indices[0] = GridPane.getColumnIndex(node);
                    indices[1] = GridPane.getRowIndex(node);
                }

            }

        }

        return indices;
    }

    /**
     * This method will place the selected piece from the display pieces gridpane into the
     * placementBoard gridpane and it will also process the storage of the piece object in the array
     *
     * @param piece : The piece object
     * @param colIndex : the column index of the board at which the piece will be placed
     * @param rowIndex : the row index of the board at which the piece will be placed
     */
    public void PlacePieceAt(Piece piece, int colIndex , int rowIndex){

        placementBoardArr[rowIndex][colIndex] = piece;

        setImageAt(piece.getImage() , colIndex , rowIndex);

    }

    /**
     * It will process all of the visuals in selecting the cell and storage of the indices
     * at which the cell is selected
     *
     * @param event : Mouseevent input of javafcx
     * @return: array that contains the selected indices
     */
    public int[] PressCell (MouseEvent event){

        for (Node node : placementBoard.getChildren()){

            if(node instanceof VBox){

                if(node.getBoundsInParent().contains(event.getX(),event.getY())){

                    //this will store the previous indices before overwrting the selectedIndices
                    //because of the method, UnselectSelectedPiece()
                    int [] tempIndices = {selectedIndices[0] , selectedIndices[1]};
                    int colIndexNode = GridPane.getColumnIndex(node);
                    int rowIndexNode = GridPane.getRowIndex(node);

                    //this condition is for the user to select the cell with piece or the user already
                    //selected a piece
                    if(placementBoardArr[rowIndexNode][colIndexNode] != null || this.isThereSelectedCell){

                        if(isThereSelectedCell){

                            //this condition is for setting isTherePiecePressed to true if the user doesn't select the selected piece again
                            if(tempIndices[0] != colIndexNode || tempIndices[1] != rowIndexNode){
                                //if the user press another cell after he selected a cell with a piece,
                                //it means that user wants to switch places with the piece

                                SwitchCellsPiece(colIndexNode , rowIndexNode);

                            }

                            UnselectSelectedCell();

                        }
                        else{
                            isThereSelectedCell = true;

                            if((colIndexNode + rowIndexNode)%2 == 0)node.setStyle(FIRSTSELECTEDCOLORCODE);
                            else node.setStyle(SECONDSELECTEDCOLORCODE);

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
     * This method will process the visuals of unselecting the cells. It will also
     * process the updating of isThereSelectedCell variable and selectedIndices array.
     */
    public void UnselectSelectedCell() {

        if(isThereSelectedCell){

            Node node = getNodeFromGridPane(selectedIndices[0] , selectedIndices[1]);

            if(node instanceof VBox){

                if((selectedIndices[0] + selectedIndices[1])%2 == 0)node.setStyle(FIRSTUNSELECTEDCOLORCODE);
                else node.setStyle(SECONDUNSELECTEDCOLORCODE);

                isThereSelectedCell = false;
                selectedIndices[0] = -1;
                selectedIndices[1] = -1;

            }

        }

    }

    /**
     * It will get the value of the isThereSelectedCell boolean varibale
     *
     * @return: value of the isThereSelectedCell variable
     */
    public boolean getIsThereSelectedCell(){ return this.isThereSelectedCell; }

    /**
     * It will check if the inputted indices has piece at the indices
     *
     * @param colIndex : column index at which it will check at the 2d array
     * @param rowIndex: row index at which it will check at the 2d array
     * @return: boolean, if there is piece at the inputted indices then it is true, else false
     */
    public boolean isTherePieceAt(int colIndex, int rowIndex){ return placementBoardArr[rowIndex][colIndex] != null;  }

    /**
     * It will get the piece object based on the inputted indices at the placementBoardArr.
     *
     * @param colIndex: column index at which it will get the object
     * @param rowIndex: row index at which it will get the object
     * @return: piece object at the specified indices of the placementBoardArr
     */
    public Piece getPieceAt(int colIndex, int rowIndex){
        return placementBoardArr[rowIndex][colIndex];
    }

    /**
     * This will return the value of the placementBoardArr
     *
     * @return: the value of the placementBoardArr
     */
    public Piece[][] getPlacementBoardArr (){return this.placementBoardArr;}

    /**
     * This will make the placementBoardArr into a string-like
     * @return : string for printing the placementBoardArr to monitor the back end part of the program
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(Piece[] pi : placementBoardArr){

            for(Piece p : pi){

                if(p == null){
                    sb.append("\t + null");
                }
                else {
                    sb.append("\t + ").append(p.getRank().getStringValue());
                }

            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * This method will set the picture inside the gridpane which indicates
     * that the user already placed the pieces
     *
     * @param image : image to be placed at the column index and row index of thr gridpane
     * @param colIndex : column index of the gridpane for which the image will be placed in the gridpane
     * @param rowIndex : row index of the gridpane for which the image will be placed in the gridpane
     */
    private void setImageAt(Image image, int colIndex, int rowIndex){

        Node node = layoutPlacement[rowIndex][colIndex].getChildren().get(0);

        if(node instanceof ImageView){

            ((ImageView) node).setImage(image);

        }

    }

    /**
     * This method will prepare the board of which the pieces will be placed.
     */
    private void PrepareBoard(){

        for(int j=0 ; j < 3 ; j++){

            for(int i = 0; i < 9 ; i++){
                layoutPlacement[j][i]= new VBox();

                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(50);
                imageView.setFitWidth(90);

                layoutPlacement[j][i].setAlignment(Pos.CENTER);

                layoutPlacement[j][i].getChildren().add(imageView);

                if((i+j)%2 == 0){

                    layoutPlacement[j][i].setStyle(FIRSTUNSELECTEDCOLORCODE);

                }
                else{

                    layoutPlacement[j][i].setStyle(SECONDUNSELECTEDCOLORCODE);

                }

                placementBoard.add(layoutPlacement[j][i], i , j);
            }
        }

        placementBoard.setVgap(4);
        placementBoard.setHgap(4);

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
        for (Node node : placementBoard.getChildren()) {

            if(!(node instanceof Group)){
                if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * This method will switch the contains of the selected cell (through the selectedCellIndices)
     * and the inputted indices. It will switch the piece both front-end and back-end of the program
     *
     * @param toColIndex : column index for which the cell will be switched
     * @param toRowIndex : row index for which the cell will be switched
     */
    private void SwitchCellsPiece(int toColIndex, int toRowIndex){
        int selectedRowIndex = selectedIndices[1];
        int selectedColumnIndex = selectedIndices[0];

        //back-end switching
        Piece tempPiece = placementBoardArr[selectedRowIndex][selectedColumnIndex];
        placementBoardArr[selectedRowIndex][selectedColumnIndex] = placementBoardArr[toRowIndex][toColIndex];
        placementBoardArr[toRowIndex][toColIndex] = tempPiece;

        //front-end switching or update
        Node nodeSelected = getNodeFromGridPane(selectedColumnIndex , selectedRowIndex);
        Node nodeTo = getNodeFromGridPane(toColIndex , toRowIndex);

        Image tempImage;
        if(nodeSelected instanceof VBox){

            if(placementBoardArr[selectedRowIndex][selectedColumnIndex] == null){
                ((ImageView)(((VBox) nodeSelected).getChildren().get(0))).setImage(null);
            }
            else{
                tempImage = placementBoardArr[selectedRowIndex][selectedColumnIndex].getImage();
                ((ImageView)(((VBox) nodeSelected).getChildren().get(0))).setImage(tempImage);
            }

        }

        if(nodeTo instanceof VBox){
            tempImage = placementBoardArr[toRowIndex][toColIndex].getImage();
            ((ImageView)(((VBox) nodeTo).getChildren().get(0))).setImage(tempImage);
        }

    }


}
