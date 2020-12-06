package org.yankov.mso.database;

import org.yankov.mso.datamodel.Piece;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface EntityCollections<T extends Piece> {

    void clear();

    void initialize();

    Map<Class, Collection> getCollections();

    void addEntities(Class c, Collection entities);

    List<T> getPieces();

    boolean addPiece(T piece);

}
