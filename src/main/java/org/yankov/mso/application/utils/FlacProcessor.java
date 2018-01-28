package org.yankov.mso.application.utils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;
import org.yankov.mso.application.ApplicationContext;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class FlacProcessor implements PCMProcessor {

    private static final String CLASS_NAME = FlacProcessor.class.getName();

    public static final String FILE_NOT_FOUND = CLASS_NAME + "-file-not-found";
    public static final String CANNOT_DETECT_AUDIO_FILE_DURATION = CLASS_NAME + "-cannot-detect-audio-file-duration";
    public static final String FLAC_ERROR = CLASS_NAME + "-flac-error";

    private File file;
    private SourceDataLine line;
    private List<LineListener> listeners;

    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public FlacProcessor() {
        this.listeners = new ArrayList<>();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void addListener(LineListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LineListener listener) {
        listeners.remove(listener);
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
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE, resourceBundle.getString(FLAC_ERROR), e);
        }
    }

    @Override
    public void processPCM(ByteData byteData) {
        line.write(byteData.getData(), 0, byteData.getLen());
    }

    public Optional<Duration> detectDuration() {
        if (file == null) {
            return Optional.empty();
        }

        try {
            AudioFile audioFile = AudioFileIO.read(file);
            AudioHeader header = audioFile.getAudioHeader();
            int lengthInSec = header.getTrackLength();
            return Optional.of(Duration.ofSeconds(lengthInSec));
        } catch (IOException e) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, resourceBundle.getString(FILE_NOT_FOUND), e.getMessage());
            return Optional.empty();
        } catch (CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, resourceBundle.getString(CANNOT_DETECT_AUDIO_FILE_DURATION),
                                   e.getMessage());
            return Optional.empty();
        }
    }

    public void play() {
        if (file == null) {
            return;
        }

        try {
            FileInputStream is = new FileInputStream(file);
            FLACDecoder decoder = new FLACDecoder(is);
            decoder.addPCMProcessor(this);
            decoder.decode();
            close();
        } catch (IOException e) {
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE, resourceBundle.getString(FILE_NOT_FOUND), e);
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
