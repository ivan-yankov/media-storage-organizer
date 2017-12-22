package org.yankov.mso.application.utils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.yankov.mso.application.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class FileUtils {

    private static final String CLASS_NAME = FileUtils.class.getName();

    public static final String FILE_NOT_FOUND = CLASS_NAME + "-file-not-found";
    public static final String CANNOT_DETECT_AUDIO_FILE_DURATION = CLASS_NAME + "-cannot-detect-audio-file-duration";

    public static Optional<Duration> detectAudioFileDuration(File file) {
        if (file == null) {
            return Optional.empty();
        }

        ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

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

}
