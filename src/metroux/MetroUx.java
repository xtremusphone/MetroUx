package metroux;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class MetroUx extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        try {      
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
            Parent root = loader.load();
            Scene primaryScene = new Scene(root);
            stage.setScene(primaryScene); 
            FXMLDocumentController controller = loader.getController();
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Open Music Folder");
            File file = chooser.showDialog(stage);
            controller.loadAlbums(file);
            controller.populateAlbum(stage);
            stage.setTitle("Created by Amir Arif : Use at your own risk");
            stage.getIcons().add(new Image("file:///" + new File(".").getCanonicalPath() + "\\icon.png"));
            stage.show();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed to Open Scene");            
            ex.getCause();
        }
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
