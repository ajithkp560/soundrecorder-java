package SoundRecorder;
// Designed By Ajith Kp [ ajithkp560 ]
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SoundRecorder extends Application 
{    
    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root = FXMLLoader.load(getClass().getResource("design.fxml"));        
        Scene scene = new Scene(root);     
        stage.setTitle("Sound Recorder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}