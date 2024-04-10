package method.qr.kiarelemb.utils;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class QRAudioUtils {

    public static void playMusicFromURL(URL url) {
        try {
            //创建相当于音乐播放器的对象
            Clip clip = AudioSystem.getClip();

            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
            //播放器打开这个文件
            clip.open(audioInput);
            clip.start();//只播放一次
            //循环播放
//            clip.loop(Clip.LOOP_CONTINUOUSLY);
            audioInput.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Clip playMusicLoop(URL url) {
        Clip clip = null;
        try {
            //创建相当于音乐播放器的对象
            clip = AudioSystem.getClip();

            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
            //播放器打开这个文件
            clip.open(audioInput);
            //循环播放
            clip.loop(Clip.LOOP_CONTINUOUSLY);
//            audioInput.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return clip;
    }

    public static Clip getMusicPlayObject(URL url) {
        Clip clip = null;
        try {
            //创建相当于音乐播放器的对象
            clip = AudioSystem.getClip();
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
            //播放器打开这个文件
            clip.open(audioInput);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return clip;
    }

    public static Clip getMusicPlayObject(File musicFile) {
        Clip clip = null;
        try {
            //创建相当于音乐播放器的对象
            clip = AudioSystem.getClip();
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
            //播放器打开这个文件
            clip.open(audioInput);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return clip;
    }

    public static Sequencer playMidiFile(String filePath) {
        Sequencer midi = null;
        try {
            File sound = new File(filePath);
            Sequence seq = MidiSystem.getSequence(sound);
            midi = MidiSystem.getSequencer();
            midi.open();
            midi.setSequence(seq);
//            if (!midi.isRunning()) {
            midi.start();
//            }
        } catch (Exception ignore) {
        }
        return midi;
    }

    /**
     *
     * @param filePath mid音频文件路径
     * @return 取得mid文件的对象
     */
    public static Sequencer getMidiFilePlayerObject(String filePath) {
        Sequencer midi = null;
        try {
            File sound = new File(filePath);
            Sequence seq = MidiSystem.getSequence(sound);
            midi = MidiSystem.getSequencer();
            midi.open();
            midi.setSequence(seq);
        } catch (Exception ignore) {
        }
        return midi;
    }
}