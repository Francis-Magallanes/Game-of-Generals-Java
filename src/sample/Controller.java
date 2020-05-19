/*
  This is for the starting window of the program

  @author: Francis John Magallanes
  @since: May 11,2020
 */

package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    public void StartVersusPlayer(ActionEvent event)throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StagingPart.fxml"));
        Stage stage = (Stage)(((Node)(event.getTarget())).getScene().getWindow());
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("assets/css_for_staging_part.css");

        stage.setScene(scene);
        stage.setTitle("Prepare for Battle");
        stage.show();

    }
}
