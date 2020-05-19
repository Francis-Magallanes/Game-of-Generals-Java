/*
  This is for the preparation part of the game of the generals

  @author: Francis John Magallanes
  @since: May 11,2020
 */

package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * This class is for the controller of the fxml file, "StagingPart.fxml".
 */
public class StagingPartController {

    @FXML
    private GridPane preperationBoard;

    @FXML
    private GridPane piecesDisplay;

    @FXML
    private AnchorPane baseLayout;

    //these are the objects that will "manage" the preperationBoard and
    //piecesDisplay grid panes.
    private PiecesDisplay displayManager;
    private PiecesPlacement placementManager;

    private boolean isWhite; //it will monitor whose turn that will place the pieces

    //this will store the final layout of the each of the player's board
    private Piece[][] finalWhiteBoard;
    private Piece[][] finalBlackBoard;

    /**
     * This will initialize the GUI for the image and all necessary components for the
     * preparation of the battale for game of the generals
     *
     */
    @FXML
    public void initialize(){

        isWhite = true;
        initializeBoardObjects();

    }

    /**
     * This function is for the mouse event when the user selects a piece in the
     * gridpane for the display of the pieces
     *
     * @param event : Mouse event input for javafx
     */
    @FXML
    public void SelectPiece(MouseEvent event){

        displayManager.PressPiece(event);

        if( displayManager.getIsThereSelectedPiece() && placementManager.getIsThereSelectedCell()){
            placementManager.UnselectSelectedCell();
        }

    }

    /**
     * This function is for the when the user is pressing the board for
     * the placement of the piece. In this method, it will place the
     * selected piece in the placement board(playing board) and also, it can
     * switch the selected piece in the placement board to other cell of the
     * placement board
     *
     * @param event : MouseEvent input for javafx
     */
    @FXML
    public void PlacementBoardAction(MouseEvent event){

        int[] selectedPlaceIndices = placementManager.getIndexToPlaced(event);

        if(selectedPlaceIndices[0] != -1 && selectedPlaceIndices[1] != -1 &&displayManager.getIsThereSelectedPiece()){

            Piece selectedPiece = displayManager.getPieceAtSelectedIndices();

            if( !placementManager.isTherePieceAt(selectedPlaceIndices[0] ,selectedPlaceIndices[1]) ){

                placementManager.PlacePieceAt(selectedPiece , selectedPlaceIndices[0] , selectedPlaceIndices[1]);

            }
            else{

                Piece returnPiece = placementManager.getPieceAt(selectedPlaceIndices[0], selectedPlaceIndices[1]);
                displayManager.ReturnPiece(returnPiece);

                placementManager.PlacePieceAt(selectedPiece , selectedPlaceIndices[0] , selectedPlaceIndices[1]);

            }

            displayManager.UnselectSelectedPiece();


        }
        else{

            placementManager.PressCell(event);

            System.out.println(placementManager.toString());
        }

    }

    /**
     *This function is for when the user presses the
     * reset button. This function will re-initialize the gridpane
     * and the other objects to mimick the reseting action
     *
     * @param event: Action event input for the javafx
     */
    @FXML
    public void ResetAction(ActionEvent event){

        //this will clear the children of the gridpane except for the gridpane
        preperationBoard.getChildren().retainAll(preperationBoard.getChildren().get(0));
        piecesDisplay.getChildren().retainAll(piecesDisplay.getChildren().get(0));

        //re-initializing the objects
        initializeBoardObjects();

    }

    /**
     *This function is for when the user presses the proceed button.
     * This function will check if the user placed all of the pieced.
     * It will also prepare the board for the next user to place their pieces.
     * Lastly, if the last user is finished the placing of their pieces,
     * then will prepare the playing board
     *
     * @param event : Action event input for the javafx
     */
    @FXML
    public void ProceedAction(ActionEvent event){

        if(displayManager.isAllPiecesPlaced()){

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION , "Do you want to proceed and let the next user setup their pieces? Note" +
                    " that this action cannot be reverted back");

            Optional<ButtonType> decision = confirmation.showAndWait();

            if( decision.isPresent() && decision.get() == ButtonType.OK){

                if(isWhite){

                    Alert message = new Alert(Alert.AlertType.INFORMATION , "Please give the terminal to the black player");
                    message.showAndWait();

                    finalWhiteBoard = placementManager.getPlacementBoardArr();
                    isWhite = false;
                    ResetAction(event);

                }
                else{

                    //TODO: prompt the gameboard with the whiteplayer as the first player
                }

            }

        }
        else{

            Alert error = new Alert(Alert.AlertType.ERROR , "Please place all pieces in the board before proceeding");

            error.showAndWait();
        }


    }

    /**
     * This function will instantiate the objects that will manage the
     * two grid pane
     */
    private void initializeBoardObjects(){

        displayManager = new PiecesDisplay(piecesDisplay , isWhite);
        placementManager = new PiecesPlacement(preperationBoard , isWhite);

    }


}
