package org.yankov.mso.application.utils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.Record;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class FlacProcessor {

    private static final String CLASS_NAME = FlacProcessor.class.getName();

    private static final String FLAC = "FLAC";

    public static final String FILE_NOT_FOUND = CLASS_NAME + "-file-not-found";
    public static final String CANNOT_DETECT_AUDIO_FILE_DURATION = CLASS_NAME + "-cannot-detect-audio-file-duration";
    public static final String FLAC_ERROR = CLASS_NAME + "-flac-error";
    public static final String UNABLE_READ_FILE = CLASS_NAME + "-unable-read-file";
    public static final String FILE_LOADED = CLASS_NAME + "-file-loaded";

    private final File file;
    private final ResourceBundle resourceBundle = ApplicationContext.getInstance().getFolkloreResourceBundle();

    public FlacProcessor(File file) {
        this.file = file;
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

    public Optional<Record> loadRecordFromFile() {
        if (file == null) {
            return Optional.empty();
        }

        try {
            Record record = new Record();
            record.setDataFormat(FLAC);
            record.setBytes(Files.readAllBytes(file.toPath()));
            ApplicationContext.getInstance().getLogger()
                              .log(Level.INFO, resourceBundle.getString(FILE_LOADED) + ": " + file.getName());
            return Optional.of(record);
        } catch (IOException e) {
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE,
                                                             resourceBundle.getString(UNABLE_READ_FILE) + ": " + file
                                                                     .getAbsolutePath());
            return Optional.empty();
        }
    }

}
