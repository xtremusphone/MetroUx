/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metroux;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    
    private final int ALBUM_WIDTH_MAX = 100;
    private final int H_GAP = 10;
    private final int V_GAP = 10;
    private final String FOLDER_PATH = "C:\\Users\\User\\Downloads\\Music";
    private File[] music_list;
    private String name;
    
    private VBox albumgrid;
    private Stage primstg;    
    private MediaPlayer player;
    
    @FXML
    private ScrollPane album_list;
    
    @FXML
    private HBox music_control;
    
    @FXML
    private Button play_button;
    
    @FXML
    private Button stop_button;
    
    @FXML
    private Button pause_button;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void populateAlbum(Stage primaryStage){
        this.primstg = primaryStage;
        
        
        //checking if the directory is there
        int album_val;
        if(music_list.length == 0){
            System.out.println("Unable to load files...Exiting");
            return;
        }
        
        //Setting up pane for button container
        albumgrid = new VBox();
        albumgrid.setPadding(new Insets(H_GAP,H_GAP,H_GAP,H_GAP));
        albumgrid.setSpacing(V_GAP);
        
        
        //populating the music list and assigning to a new button for each song
        for(File x:music_list){
            Button albums;
            
            //checking if the file is an mp3 file
            if(!x.getName().endsWith(".mp3"))
                continue;
            
            DropShadow sd = new DropShadow(3, Color.AQUAMARINE);
            
            //adjusting button settings e.g. name and effects
            albums = new Button(x.getName().replace(".mp3",""));
            System.out.println(albums);
            albums.setId(x.getName());
            albums.setMinSize(albumgrid.getMaxWidth(), ALBUM_WIDTH_MAX);
            albums.setPrefSize(albumgrid.getMaxWidth(), ALBUM_WIDTH_MAX);
            albums.setMaxSize(albumgrid.getMaxWidth(), ALBUM_WIDTH_MAX);
            
            //creating new media object.
            Media mdia = new Media(Paths.get(FOLDER_PATH +"/"+ x.getName()).toUri().toString());
            
            //setting up action if the button is clicked
            albums.setOnAction((ActionEvent event) -> {
                primaryStage.setTitle("Now playing - " + ((Button)event.getSource()).getText());
                if(player != null)
                    player.dispose();
                Media temp = mdia;
                player = new MediaPlayer(temp);
                player.setVolume(0.5);
                player.play();
                if(((ImageView)((Button)event.getSource()).getGraphic()) !=  null){
                    primaryStage.getIcons().clear();
                    primaryStage.getIcons().add(((ImageView)((Button)event.getSource()).getGraphic()).getImage());
                }
                else{
                    try {
                        primaryStage.getIcons().clear();
                        primaryStage.getIcons().add(new Image("file:///" + new File(".").getCanonicalPath() + "\\icon.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            //adding the effects to the button
            albums.setEffect(sd);
            albums.setId(albums.getText());
            
            //adding buttons to the pane container
            albumgrid.getChildren().add(albums);
            
           //Media temp = new Media(Paths.get(FOLDER_PATH + "/" + x.getName()).toUri().toString());
           
           //getting the metadata of the file
            mdia.getMetadata().addListener((MapChangeListener.Change<? extends String,? extends Object> change) -> {
                if(change.wasAdded()){
                    metaData(change.getKey(), change.getValueAdded(),x.getName().replace(".mp3", ""));
                }
            });
            
        }   
        
        play_button.setOnAction((ActionEvent event) -> {
            player.play();
        });
        
        pause_button.setOnAction((ActionEvent event) -> {
            player.pause();
        });
        
        stop_button.setOnAction((ActionEvent event) -> {
            player.stop();
        });
        
        //setting up the stage where the pane can be displayed
        album_list.setContent(albumgrid);
        album_list.getStylesheets().add(MetroUx.class.getResource("materialbutton.css").toExternalForm());
        music_control.getStylesheets().add(MetroUx.class.getResource("materialbutton.css").toExternalForm());
    }
    
    public int loadAlbums(File file){
        File path = file;
        music_list = path.listFiles();
        if(!path.exists()){
            return -1;
        }
        return music_list.length;
    }
    
    public void metaData(String tag,Object val,String file){
        if(tag.equals("image")){
            //System.out.println("There is image");
            //System.out.println(file);
            for(Object x:albumgrid.getChildren().toArray()){
                if(((Button)x).getGraphic() != null){
                    continue;
                }
                if(((Button)x).getText().equals(file)){
                    Image img = (Image)val;
                    ImageView temp = new ImageView((Image)val);
                    temp.setFitWidth(ALBUM_WIDTH_MAX);
                    temp.setFitHeight(ALBUM_WIDTH_MAX);
                    ((Button)x).setGraphic(temp);
                    break;
                }
            }
        }
        
        if(tag.equals("artist")){
            for(Object x:albumgrid.getChildren().toArray()){
                if(((Button)x).getId().equals(file)){
                    ((Button)x).setText(((Button)x).getText() + "\n" + (String)val);
                }
            }
        }
    }
}
