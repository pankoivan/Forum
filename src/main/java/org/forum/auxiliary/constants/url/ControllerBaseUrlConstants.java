package org.forum.auxiliary.constants.url;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ControllerBaseUrlConstants {

    public static final String FOR_INDEX_CONTROLLER = "";

    public static final String FOR_AUTH_CONTROLLER = "/auth";

    public static final String FOR_ROLES_AUTHORITIES_CONTROLLER = "/roles-authorities";

    public static final String FOR_ROLES_CONTROLLER =
            FOR_ROLES_AUTHORITIES_CONTROLLER +
            "/roles";

    public static final String FOR_AUTHORITIES_CONTROLLER =
            FOR_ROLES_AUTHORITIES_CONTROLLER +
            "/authorities";

    public static final String FOR_USERS_CONTROLLER = "/users";

    public static final String FOR_USERS_ACTIONS_CONTROLLER =
            FOR_USERS_CONTROLLER +
            "/actions";

    public static final String FOR_USERS_CONTRIBUTIONS_CONTROLLER =
            FOR_USERS_CONTROLLER +
            "/" + UrlPartConstants.ID_PATTERN +
            "/contributions";

    public static final String FOR_SECTIONS_CONTROLLER = "/sections";

    public static final String FOR_TOPICS_CONTROLLER =
            FOR_SECTIONS_CONTROLLER +
            "/" + UrlPartConstants.SECTION_ID_PATTERN +
            "/topics";

    public static final String FOR_MESSAGES_CONTROLLER =
            FOR_TOPICS_CONTROLLER +
            "/" + UrlPartConstants.TOPIC_ID_PATTERN +
            "/messages";

    public static final String FOR_SORTING_CONTROLLER = "/sort";

    public static final String FOR_SEARCHING_CONTROLLER = "/search";

}
