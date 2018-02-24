package org.yankov.mso.datamodel;

import java.util.List;

public interface DatabaseUploader<T extends PieceProperties> {

    void uploadToDatabase(List<T> items);

}
