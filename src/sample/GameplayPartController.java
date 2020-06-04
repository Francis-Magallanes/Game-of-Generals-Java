package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

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
    public void PressCellAction(MouseEvent event) throws IOException {

        int[] tempIndices = selectedIndices; //this will store the current indices before it will overwrites
        selectedIndices = GetSelectedIndices(event);

        boardManager.PrintBoardArr();
        System.out.println("selectedIndices[0]:" + selectedIndices[0] + " selectedIndices[1]: " + selectedIndices[1]);

        //this condition is for ensuring that the indices are positive
        if(selectedIndices[0] != -1 && selectedIndices[1] != -1){

            if(boardManager.isTherePieceAt(selectedIndices[0],selectedIndices[1]) && isWhite == boardManager.getPieceAt(selectedIndices[0]
                    , selectedIndices[1]).isWhite()){
                //this will check if the selected cell contains a piece and that piece belongs to him

                if(!isThereSelectedCell){
                    //this will make sure that the user will select an unselected piece of his own.
                    boardManager.SelectCell(selectedIndices[0], selectedIndices[1]);
                    isThereSelectedCell = true;

                }
                else if(isThereSelectedCell){
                    //this conditional is responsible for the other actions if there is selected piece

                    if(tempIndices[0] == selectedIndices[0] && tempIndices[1] == selectedIndices[1]){
                        //this is when the user re-presses the same cell to the unselect the cell
                        boardManager.UnselectSelectedCell();
                        isThereSelectedCell = false;
                    }
                    else {
                        //this is when the user presses another cell that contains one of his pieces
                        boardManager.UnselectSelectedCell();
                        boardManager.SelectCell(selectedIndices[0] , selectedIndices[1]);
                    }

                }

            }
            else if(isThereSelectedCell){
                //this is for when the user selected a cell and there is previously selected piece

                if(boardManager.isEligibleMove(selectedIndices[0], selectedIndices[1])) {
                    //this is when the user makes the move if the selected cell is a valid move
                    boardManager.MovePieceAt(selectedIndices[0], selectedIndices[1]);
                    isThereSelectedCell = false;

                    //this will check if the player wins after he makes a move
                    if(boardManager.isWinner(isWhite)){
                        boardManager.ShowAllPieces();
                        EndGame(event);
                         return;
                    }

                    //it will hide first the pieces and show the prompt before the turn over
                    boardManager.HideAllPieces();
                    Alert turnover;
                    if(isWhite){
                        turnover = new Alert(Alert.AlertType.INFORMATION,"Please hand over the terminal to the black player");
                        turnover.showAndWait();
                    }
                    else{
                        turnover = new Alert(Alert.AlertType.INFORMATION,"Please hand over the terminal to the white player");
                        turnover.showAndWait();
                    }

                    //this will change the player and shows the pieces of the next player
                    isWhite = !isWhite;

                    //rotation of board only happens at the back-end
                    boardManager.RotateBoard();
                    //thus, it is a need to update the front-end through this line of code
                    boardManager.ShowPiecesPlayer(isWhite);

                    //this will check if the player wins before he move
                    if(boardManager.isWinner(isWhite)){
                        boardManager.ShowAllPieces();
                        EndGame(event);
                        return;
                    }

                }
                else{

                    /*
                    this will unselect the selected piece if the user didn't make any move on the selected
                    piece or te user made any invalid move
                     */
                    boardManager.UnselectSelectedCell();
                    isThereSelectedCell = false;

                }

            }

        }

    }

    @FXML
    public void ShowEliminatedPieces(ActionEvent event){
        HashMap<PiecesHiearchy , Integer> eliminatedPieces;

        if(isWhite){
            eliminatedPieces = boardManager.getWhiteEliminatedPieces();
            Scene scene = new Scene(ProcessViewElimPieces(eliminatedPieces, isWhite) , 300 ,500);


            Stage stage = new Stage();
            stage.setTitle("Eliminated Pieces");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        }
        else{
            eliminatedPieces = boardManager.getBlackEliminatedPieces();

            Scene scene = new Scene(ProcessViewElimPieces(eliminatedPieces, isWhite) , 300 ,500);


            Stage stage = new Stage();
            stage.setTitle("Eliminated Pieces");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.show();

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

        //it will hide first the pieces and show the prompt before it start of the game
        boardManager.HideAllPieces();
        Alert turnover = new Alert(Alert.AlertType.INFORMATION,"Please hand over the terminal to the white player");
        turnover.showAndWait();

        boardManager.ShowPiecesPlayer(isWhite);
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

    private void EndGame(MouseEvent event) throws IOException{
        Alert congratulation = new Alert(Alert.AlertType.INFORMATION , "You won have the game!");
        congratulation.showAndWait();

        Alert info = new Alert(Alert.AlertType.INFORMATION, "The game is ended. Returning to the main window");
        info.showAndWait();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Stage stage = (Stage)(((Node)(event.getTarget())).getScene().getWindow());
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("Game of Generals");
        stage.show();
    }

    private VBox ProcessViewElimPieces(HashMap<PiecesHiearchy, Integer> eliminatedPieces, boolean isWhite){

        ScrollPane scrollContainer = new ScrollPane();
        scrollContainer.setPrefSize(290 , 450);

        VBox viewPieces = new VBox(20);
        viewPieces.setAlignment(Pos.CENTER);
        viewPieces.setPrefWidth(290);

        if(eliminatedPieces.isEmpty()){

            viewPieces.getChildren().addAll(new Label("There are no eliminated pieces as of now"));
            viewPieces.setPrefSize(280 ,400);
        }
        else{

            for(PiecesHiearchy ph : eliminatedPieces.keySet()){

                VBox imageLabelContainer = new VBox();
                imageLabelContainer.setAlignment(Pos.CENTER);
                imageLabelContainer.setFillWidth(true);
                imageLabelContainer.setSpacing(3);

                //this will create the corresponding imageview and label
                ImageView iv;
                if (isWhite) {
                    iv = new ImageView(new Image("assets/WHITE PIECES/WHITE_" + ph + ".png"));
                } else {
                    iv = new ImageView(new Image("assets/BLACK PIECES/BLACK_" + ph + ".png"));
                }

                Label rankLabel = new Label("Rank: " + ph.getStringValue());
                Label numPieceElLabel = new Label ("Number of Pieces Eliminated: " + eliminatedPieces.get(ph));

                //this will put the created imageview and label to the sub-vbox
                imageLabelContainer.getChildren().add(iv);
                imageLabelContainer.getChildren().add(rankLabel);
                imageLabelContainer.getChildren().add(numPieceElLabel);

                //this will add the sub-vbox to the main vbox
                viewPieces.getChildren().add(imageLabelContainer);
            }

        }


        scrollContainer.setContent(viewPieces);


        VBox baseContainer = new VBox(20);
        baseContainer.setAlignment(Pos.CENTER);
        baseContainer.setPrefSize(290,490);

        Button button = new Button("Close");
        button.setOnAction(event -> {

            ((Node)(event.getTarget())).getScene().getWindow().hide();// this will hide the window once the button is pressed.

        });
        button.setPrefSize(150 , 60);

        baseContainer.getChildren().addAll(scrollContainer , button);
        VBox.setMargin(button , new Insets(15,10,15,10));


        return baseContainer;
    }

}
