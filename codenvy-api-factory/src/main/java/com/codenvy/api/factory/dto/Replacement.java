package com.codenvy.api.factory.dto;

import com.codenvy.dto.shared.DTO;

/**
 * Configuration for replacement. Contains part of text to search and another to replace one.
 *
 * @author Sergii Kabashniuk
 */
@DTO
public interface Replacement {
    String getFind();

    void setFind(String find);

    Replacement withFind(String find);

    String getReplace();

    void setReplace(String replace);

    Replacement withReplace(String replace);

    String getReplacemode();

    void setReplacemode(String replacemode);

    Replacement withReplacemode(String replacemode);

}
