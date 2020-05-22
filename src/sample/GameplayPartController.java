package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * This class for the controller of GameplayPart.fxml
 */
public class GameplayPartController {

    @FXML
    private GridPane boardGridPane;

    private PlayingBoard boardManager;

    private boolean isWhite;
    private boolean isThereSelectedCell = false; //this will monitor if there is currently selected cell

    /*
    It will store the selected indices. For selectedIndices:
   at index 0, it will pertain to the column index or the index along x-axis
   at index 1, it will pertain to the row index or the index along y-axis
    */
    private int[] selectedIndices = {-1,-1};

    @FXML
    public void PressCellAction(MouseEvent event){

        int[] tempIndices = selectedIndices; //this will store the current indices before it will overwrites
        selectedIndices = GetSelectedIndices(event);

        boardManager.PrintBoardArr();

        //this condition is for ensuring that the indices are positive
        if(selectedIndices[0] != -1 && selectedIndices[1] != -1){

            if(!isThereSelectedCell && boardManager.isTherePieceAt(selectedIndices[0],selectedIndices[1])){
                //cell with the pieces in are the only cells can be selected
                boardManager.SelectCell(selectedIndices[0], selectedIndices[1]);
                isThereSelectedCell = true;

            }
            else if(isThereSelectedCell){

                System.out.println("selectedindices[0]: " + selectedIndices[0] +" selectedIndices[1]: " + selectedIndices[1]);

                if(tempIndices[0] == selectedIndices[0] && tempIndices[1] == selectedIndices[1] && boardManager.isTherePieceAt(tempIndices[0], tempIndices[1])){
                    //this is when the user re-presses the same cell to the unselect the cell
                    boardManager.UnselectSelectedCell();
                    isThereSelectedCell = false;

                }
                else if (boardManager.isTherePieceAt(selectedIndices[0], selectedIndices[1])){
                    //this is when the user presses another cell that contains one of his pieces
                    boardManager.UnselectSelectedCell();
                    boardManager.SelectCell(selectedIndices[0] , selectedIndices[1]);

                }

                else if(boardManager.isEligibleMove(selectedIndices[0],selectedIndices[1])){
                    System.out.println("success");
                    //this is when the user makes the move
                    boardManager.MovePieceAt(selectedIndices[0], selectedIndices[1]);
                    isThereSelectedCell = false;

                }
                else{
                    boardManager.UnselectSelectedCell();
                    isThereSelectedCell = false;
                }

            }

        }


    }

    /**
     * This method is the communication to the stagingpartcontroller class
     * which is intended to pass the pieces array so that it will place the necessary
     * pieces at the correct indices of the playing board
     */
    public void InitializeBoard(Piece[][] whitePieceLayout, Piece[][] blackPieceLayout){

        isWhite = true;
        boardManager = new PlayingBoard(whitePieceLayout,blackPieceLayout, boardGridPane);

    }

    private int[] GetSelectedIndices(MouseEvent event){

        int[] selectedIndices = {-1,-1};

        for(Node node : boardGridPane.getChildren()){

            if(node instanceof VBox){

                if(node.getBoundsInParent().contains(event.getX() , event.getY())){

                    selectedIndices[0] = GridPane.getColumnIndex(node);
                    selectedIndices[1] = GridPane.getRowIndex(node);

                }

            }
        }

        return selectedIndices;
    }

}
