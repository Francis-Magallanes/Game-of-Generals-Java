package sample;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

/**
 * This class for the controller of GameplayPart.fxml
 */
public class GameplayPartController {

    @FXML
    private GridPane boardGridPane;

    private PlayingBoard boardManager;

    @FXML
    public void initialize(){

        boardManager = new PlayingBoard(boardGridPane);
    }

    /**
     * This method is the communication to the stagingpartcontroller class
     * which is intended to pass the pieces array so that it will place the necessary
     * pieces at the correct indices of the playing board
     */
    public void InitializeBoard(Piece[][] whitePieceLayout, Piece[][] blackPieceLayout){


    }


}
