package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This is a test class which runs the different classes to test the feature's functionality
 */
public class test extends Application {

    public static void main (String[] args) {
        launch(args);
    }

    //this will create a test layout for the white and black pieces layout
    private static ArrayList<Piece[][]> GenerateTestData(){

        ArrayList<Piece[][]> al = new ArrayList<>();

        Piece[][] piecesWhiteLayout = new Piece[3][9];
        Piece[][] piecesBlackLayout = new Piece[3][9];

        int i = 0;
        int j = 0;

        for(PiecesHiearchy ph : PiecesHiearchy.values()){

            if(ph == PiecesHiearchy.PRIVATE){

                for(int p = 0 ; p < 6 ; p++){

                    piecesWhiteLayout[j][i] = new Piece(ph , true);
                    piecesBlackLayout[j][i] = new Piece(ph , false);

                    i++;
                }

            }
            else if( ph == PiecesHiearchy.SPY){

                for(int p = 0 ; p < 2 ; p++){

                    piecesWhiteLayout[j][i] = new Piece(ph , true);
                    piecesBlackLayout[j][i] = new Piece(ph , false);

                    i++;
                }

            }
            else{

                piecesWhiteLayout[j][i] = new Piece(ph , true);
                piecesBlackLayout[j][i] = new Piece(ph , false);

                i++;
            }

            if(i>8){
                i = 0;
                j++;
            }

        }

        al.add(0,piecesWhiteLayout);
        al.add(1, piecesBlackLayout);
        return al;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println(1%4);
        ArrayList<Piece[][]> testData = GenerateTestData();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameplayPart.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("assets/css_for_staging_part.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Gamplay - Game of the Generals");
        primaryStage.show();

        GameplayPartController gc = fxmlLoader.getController();
        gc.InitializeBoard(testData.get(0), testData.get(1));
    }

}
