package it.fireentity.library.chatpaging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter(value = AccessLevel.PROTECTED)
public class PageTexture implements Cloneable {
    private int maxPageSize;
    private String onePageStartMessage;
    private String onePageEndMessage;
    private String nextButton;
    private String backButton;
    private String endMessage;
    private String startMessage;
    private String noLineFound;

    public PageTexture(String nextButton, String backButton, String endMessage, String startMessage, String noLineFound, int maxPageSize, String onePageStartMessage, String onePageEndMessage) {
        this.startMessage = startMessage;
        this.nextButton = nextButton;
        this.backButton = backButton;
        this.endMessage = endMessage;
        this.maxPageSize = maxPageSize;
        this.noLineFound = noLineFound;
        this.onePageStartMessage = onePageStartMessage;
        this.onePageEndMessage = onePageEndMessage;
    }

    @Override
    public PageTexture clone() {
        return new PageTexture(nextButton,backButton,endMessage,startMessage,noLineFound,maxPageSize,onePageStartMessage,onePageEndMessage);
    }
}
