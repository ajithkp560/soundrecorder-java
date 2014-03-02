package SoundRecorder;
// Thanks to http://ganeshtiwaridotcomdotnp.blogspot.in
// Edited By Ajith Kp [ ajithkp560 ]
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class RecordSound implements Runnable
{
    private boolean isPause = false;
    private AudioInputStream audioInputStream;  
    private AudioFormat format;  
    public TargetDataLine line;  
    public Thread thread;  
    private double duration;  
    public RecordSound(AudioFormat format)
    {  
        super();  
        this.format = format;  
    }  
    public void start() 
    {  
        if(isPause==false)
        {
            thread = new Thread(this);  
            thread.setName("Record");  
            thread.start();  
        }
        else
        {
            thread.resume();
            isPause = false;
        }
    }  
    public void pause()
    {
        thread.suspend();
        isPause = true;
    }
    public void stop()
    {  
        thread = null;  
        isPause = false;
    }  
    @Override  
    public void run()
    {  
        duration = 0;  
        line = getTargetDataLineForRecord();  
        final ByteArrayOutputStream out = new ByteArrayOutputStream();  
        final int frameSizeInBytes = format.getFrameSize();  
        final int bufferLengthInFrames = line.getBufferSize() / 8;  
        final int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;  
        final byte[] data = new byte[bufferLengthInBytes];  
        int numBytesRead;  
        line.start();  
        while (thread != null)
        {
            if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) 
            {  
                break;  
            }  
            out.write(data, 0, numBytesRead);  
        }  
        line.stop();  
        line.close();  
        line = null;  
        try
        {  
            out.flush();  
            out.close();  
        }
        catch (final IOException ex)
        {  
            ex.printStackTrace();  
        }  
        final byte audioBytes[] = out.toByteArray();  
        final ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);  
        audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);  
        final long milliseconds = (long) ((audioInputStream.getFrameLength()*1000) / format.getFrameRate());  
        duration = milliseconds / 1000.0;  
        System.out.println(duration);  
        try
        {  
            audioInputStream.reset();  
        }
        catch (final Exception ex) 
        {  
            ex.printStackTrace();  
            return;  
        }  
    }  
    private TargetDataLine getTargetDataLineForRecord()
    {  
        TargetDataLine line;  
        final DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);  
        if (!AudioSystem.isLineSupported(info)) 
        {  
            return null; 
        }  
        try
        {  
            line = (TargetDataLine) AudioSystem.getLine(info);  
            line.open(format, line.getBufferSize());  
        }
        catch (final Exception ex)
        {  
            return null;  
        }  
        return line;  
    }
    public AudioInputStream getAudioInputStream()
    {  
        return audioInputStream;  
    }
}