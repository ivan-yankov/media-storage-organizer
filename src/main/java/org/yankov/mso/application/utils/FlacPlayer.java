package org.yankov.mso.application.utils;

import org.jflac.FLACDecoder;
import org.jflac.PCMProcessor;
import org.jflac.metadata.StreamInfo;
import org.jflac.util.ByteData;
import org.yankov.mso.application.ApplicationContext;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class FlacPlayer implements PCMProcessor {

    private static final String CLASS_NAME = FlacPlayer.class.getName();

    public static final String FLAC_PLAY_ERROR = CLASS_NAME + "-flac-play-error";

    private static FlacPlayer instance;

    private SourceDataLine line;
    private List<LineListener> listeners;
    private byte[] bytes;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    private FlacPlayer() {
        this.listeners = new ArrayList<>();
    }

    public static FlacPlayer getInstance() {
        if (instance == null) {
            instance = new FlacPlayer();
        }
        return instance;
    }

    public void addListener(LineListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LineListener listener) {
        listeners.remove(listener);
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void processStreamInfo(StreamInfo streamInfo) {
        try {
            AudioFormat audioFormat = streamInfo.getAudioFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            line = (SourceDataLine) AudioSystem.getLine(info);
            listeners.forEach(line::addLineListener);
            line.open(audioFormat, AudioSystem.NOT_SPECIFIED);
            line.start();
        } catch (LineUnavailableException e) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, resourceBundle.getString(FLAC_PLAY_ERROR), e);
        }
    }

    @Override
    public void processPCM(ByteData byteData) {
        line.write(byteData.getData(), 0, byteData.getLen());
    }

    public void play() {
        if (bytes == null || bytes.length == 0) {
            return;
        }

        try {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            FLACDecoder decoder = new FLACDecoder(is);
            decoder.addPCMProcessor(this);
            decoder.decode();
            close();
        } catch (IOException e) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, resourceBundle.getString(FLAC_PLAY_ERROR), e);
        }
    }

    public void stop() {
        if (line != null) {
            line.stop();
            close();
        }
    }

    public boolean isPlaying() {
        return line != null && line.isActive();
    }

    private void close() {
        if (line != null) {
            line.drain();
            line.close();
            listeners.clear();
        }
    }

}
