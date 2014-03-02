/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SoundRecorder;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.stage.FileChooser;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class SampleController implements Initializable 
{    
    boolean isFresh = true;
    static RecordSound mr;
    AudioFormat f;
    @FXML
    Button play, pause, stop;
    @FXML
    private void recordSound(ActionEvent evt) throws InterruptedException 
    {
        play.setEffect(new InnerShadow());
        pause.setEffect(null);
        stop.setEffect(null);
        if(isFresh==true)
        {
            f = new AudioFormat(8000, 8, 2, true, true);
            mr = new RecordSound (f); 
            mr.start();  
        }
        else
        {
            mr.start();  
        }
        //Thread.sleep(10000); 
    }
    
    @FXML
    private void stopRecording(ActionEvent evt) throws InterruptedException
    {
        stop.setEffect(new InnerShadow());
        play.setEffect(null);
        pause.setEffect(null);
        isFresh =  true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Sound Clip");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WAV", "*.WAV")
        );
        File f = fileChooser.showSaveDialog(null);
        if (f != null) 
        {
            mr.stop();  
            SaveData sd = new SaveData();  
            Thread.sleep(1000);  
            sd.saveToFile(f.toString(), AudioFileFormat.Type.WAVE, mr.getAudioInputStream()); 
            try
            {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new File(sd.fileName)));
                clip.start();
                while (!clip.isRunning())
                {
                    Thread.sleep(10);
                }
                while (clip.isRunning())
                {
                    Thread.sleep(10);
                }
                clip.close();
            }
            catch (Exception exc)
            {
                exc.printStackTrace(System.out);
            }
        }
    }
    
    @FXML
    private void pauseRecord(ActionEvent evt)
    {
        pause.setEffect(new InnerShadow());
        play.setEffect(null);
        stop.setEffect(null);
        isFresh = false;
        mr.pause();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
