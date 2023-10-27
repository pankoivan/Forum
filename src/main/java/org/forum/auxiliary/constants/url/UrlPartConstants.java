package org.forum.auxiliary.constants.url;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UrlPartConstants {

    public static final String USERS = "users";

    public static final String SECTIONS = "sections";

    public static final String TOPICS = "topics";

    public static final String MESSAGES = "messages";

    public static final String BANS = "bans";

    public static final String ID = "id";

    public static final String ID_PATTERN = "{" + ID + "}";

    public static final String USER_ID = "userId";

    public static final String USER_ID_PATTERN = "{" + USER_ID + "}";

    public static final String SECTION_ID = "sectionId";

    public static final String SECTION_ID_PATTERN = "{" + SECTION_ID + "}";

    public static final String TOPIC_ID = "topicId";

    public static final String TOPIC_ID_PATTERN = "{" + TOPIC_ID + "}";

    public static final String MESSAGE_ID = "messageId";

    public static final String MESSAGE_ID_PATTERN = "{" + MESSAGE_ID + "}";

    public static final String PAGE = "page";

    public static final String PAGE_NUMBER = "pageNumber";

    public static final String PAGE_NUMBER_PATTERN = "{" + PAGE_NUMBER + "}";

    public static final String PAGE_PAGE_NUMBER_PATTERN = PAGE + PAGE_NUMBER_PATTERN;

}
