package org.forum.auxiliary.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ControllerBaseUrlConstants {

    public static final String FOR_INDEX_CONTROLLER = "/";

    public static final String FOR_AUTH_CONTROLLER = "/auth";

    public static final String FOR_ROLES_AUTHORITIES_CONTROLLER = "/roles-authorities";

    public static final String FOR_ROLES_CONTROLLER = "/roles-authorities/roles";

    public static final String FOR_AUTHORITIES_CONTROLLER = "/roles-authorities/authorities";

    public static final String FOR_USERS_CONTROLLER = "/users";

    public static final String FOR_USERS_ACTIONS_CONTROLLER = "/users/activities";

    public static final String FOR_USERS_CONTRIBUTIONS_CONTROLLER = "/users/{id}/contributions";

    public static final String FOR_SECTIONS_CONTROLLER = "/sections";

    public static final String FOR_TOPICS_CONTROLLER = "/sections/{sectionId}/topics";

    public static final String FOR_MESSAGES_CONTROLLER = "/sections/{sectionId}/topics/{topicId}/messages";

    public static final String FOR_SORTING_CONTROLLER = "/sort";

    public static final String FOR_SEARCHING_CONTROLLER = "/search";

}
