package it.fireentity.library.enumerations;

import it.fireentity.library.locales.Message;

public enum Config {
    INVALID_COMMAND_SENDER,
    INSUFFICIENT_PERMISSIONS,
    USAGE_START,
    USAGE_END,
    USAGE_COLOR,
    USAGE_PARAMETER_COLOR,
    INVALID_ARGUMENT,
    CHAT_PAGINATION_MAX_PAGE_SIZE,
    CHAT_PAGINATION_NEXT_BUTTON,
    CHAT_PAGINATION_BACK_BUTTON,
    CHAT_PAGINATION_START_MESSAGE,
    CHAT_PAGINATION_NO_LINE_FOUND,
    ONE_PAGE_START_MESSAGE,
    ONE_PAGE_END_MESSAGE,
    CLICK_GUI_DELAY;

    private final Message message;

    Config() {
        this.message = new Message(this.getPath());
    }

    public Message getMessage() {
        return message;
    }

    public String getPath() {
        return this.name().toLowerCase();
    }
}
