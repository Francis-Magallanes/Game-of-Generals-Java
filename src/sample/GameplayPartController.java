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

    @FXML
    public void PressCellAction(MouseEvent event){

        int[] selectedIndices = GetSelectedIndices(event);

        if(selectedIndices[0] != -1 && selectedIndices[1] != -1){
            boardManager.ShowPossibleMoves(selectedIndices[0] , selectedIndices[1]);
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
