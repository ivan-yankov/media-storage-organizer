package org.yankov.mso.application.generic;

public interface UserInterfaceControls<ContentType> {

    void layout();

    ContentType getContent();

}
