package org.yankov.mso.application.ui.input;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.ui.controls.LabeledTextField;
import org.yankov.mso.application.ui.converters.AlbumStringConverter;
import org.yankov.mso.datamodel.generic.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class AlbumInputControls extends ArtifactInputControls<Album> {

    private static final String CLASS_NAME = AlbumInputControls.class.getName();

    public static final String TITLE = CLASS_NAME + "-title";
    public static final String PRODUCTION_SIGNATURE = CLASS_NAME + "-production-signature";
    public static final String COLLECTION_SIGNATURE = CLASS_NAME + "-collection-signature";
    public static final String NOTE = CLASS_NAME + "-note";
    public static final String TITLE_UNDEFINED = CLASS_NAME + "-title-undefined";
    public static final String COLLECTION_SIGNATURE_UNDEFINED = CLASS_NAME + "-collection-signature-undefined";

    private LabeledTextField title;
    private LabeledTextField productionSignature;
    private LabeledTextField collectionSignature;
    private LabeledTextField note;

    public AlbumInputControls(String id) {
        super(id);
    }

    @Override
    protected Pane createActionsControls() {
        title = new LabeledTextField(getResourceBundle().getString(TITLE), "", null);
        title.layout();

        productionSignature = new LabeledTextField(getResourceBundle().getString(PRODUCTION_SIGNATURE), "", null);
        productionSignature.layout();

        collectionSignature = new LabeledTextField(getResourceBundle().getString(COLLECTION_SIGNATURE), "", null);
        collectionSignature.layout();

        note = new LabeledTextField(getResourceBundle().getString(NOTE), "", null);
        note.layout();

        VBox actionControlsContainer = new VBox();
        actionControlsContainer.setSpacing(getWhiteSpace());
        actionControlsContainer.getChildren().add(title.getContainer());
        actionControlsContainer.getChildren().add(productionSignature.getContainer());
        actionControlsContainer.getChildren().add(collectionSignature.getContainer());
        actionControlsContainer.getChildren().add(note.getContainer());

        return actionControlsContainer;
    }

    @Override
    protected List<Album> collectExistingArtifacts() {
        return new ArrayList<>(ApplicationContext.getInstance().getFolkloreEntityCollections().getAlbums());
    }

    @Override
    protected StringConverter<Album> getStringConverter() {
        return new AlbumStringConverter();
    }

    @Override
    protected boolean validateUserInput() {
        if (title.getTextField().getText().isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(TITLE_UNDEFINED));
            return false;
        }

        if (collectionSignature.getTextField().getText().isEmpty()) {
            ApplicationContext.getInstance().getLogger()
                              .log(Level.SEVERE, getResourceBundle().getString(COLLECTION_SIGNATURE_UNDEFINED));
            return false;
        }

        return true;
    }

    @Override
    protected boolean addNewArtifact() {
        Album album = new Album();
        setArtifactProperties(album);
        return ApplicationContext.getInstance().getFolkloreEntityCollections().addAlbum(album);
    }

    @Override
    protected void cleanup() {
        title.getTextField().setText("");
        productionSignature.getTextField().setText("");
        collectionSignature.getTextField().setText("");
        note.getTextField().setText("");
    }

    @Override
    protected void setArtifactProperties(Album artifact) {
        artifact.setTitle(title.getTextField().getText());
        artifact.setProductionSignature(productionSignature.getTextField().getText());
        artifact.setCollectionSignature(collectionSignature.getTextField().getText());
        artifact.setNote(note.getTextField().getText());
    }

    @Override
    protected void extractArtifactProperties(Album artifact) {
        title.getTextField().setText(artifact.getTitle());
        productionSignature.getTextField().setText(artifact.getProductionSignature());
        collectionSignature.getTextField().setText(artifact.getCollectionSignature());
        note.getTextField().setText(artifact.getNote());
    }

    @Override
    protected void dataModelChanged() {
    }

}
