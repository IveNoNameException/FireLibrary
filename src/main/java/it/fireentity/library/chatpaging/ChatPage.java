package it.fireentity.library.chatpaging;

import it.fireentity.library.interfaces.Cacheable;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatPage implements Cacheable<Integer> {
    private final TextComponent endMessage = new TextComponent("");
    private final int maxLines;
    private final List<TextComponent> lines = new ArrayList<>();
    private ChatPage nextChatPage;
    private final ChatPage backChatPage;
    private final PageTexture pageTexture;
    @Getter
    private final PagesGroup pagesGroup;
    @Getter
    private final int pageIndex;


    public ChatPage(int maxLines, ChatPage backChatPage, PageTexture pageTexture, PagesGroup pagesGroup) {
        this.maxLines = maxLines;
        this.backChatPage = backChatPage;
        pageIndex = backChatPage == null ? 1 : backChatPage.getPageIndex() + 1;
        this.pageTexture = pageTexture;
        this.pagesGroup = pagesGroup;

        if (!getBackChatPage().isPresent()) {
            return;
        }

        TextComponent backButtonTextComponent = new TextComponent(pageTexture.getBackButton());
        //Check if the page has a backPage
        backButtonTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/crate chatPage " + pagesGroup.getKey() + " " + (getPageIndex() - 1)));

        //Adding the first part before the first button
        // [...] %backButton [...]
        String[] parts = pageTexture.getEndMessage().split("\\{0\\}");

        //Check if this is something before the backButton
        if (parts.length == 2) {
            //[...]
            endMessage.addExtra(parts[0]);
            //%backButton
            endMessage.addExtra(backButtonTextComponent);
            //[...]
            endMessage.addExtra(parts[1].split("\\{1\\}")[0]);
        }
    }

    public void addNextPage(ChatPage chatPage) {
        this.nextChatPage = chatPage;

        TextComponent nextButtonTextComponent = new TextComponent(this.pageTexture.getNextButton());
        nextButtonTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/crate chatPage " + pagesGroup.getKey() + " " + (getPageIndex() + 1)));

        //Split the string on the backButton
        // [... ] %backButton [... %nextButton% ...]
        String[] parts = pageTexture.getEndMessage().split("\\{0\\}");

        //Split the second part of the string on the nextButton
        // [...] %nextButton [...]
        parts = parts[1].split("\\{1\\}");

        //Check if this is something after the nextButton
        if (parts.length == 2) {
            //[...]
            if(!getBackChatPage().isPresent()) {
                endMessage.addExtra(parts[0]);
            }
            //%nextButton
            endMessage.addExtra(nextButtonTextComponent);
            //[...]
            endMessage.addExtra(parts[1]);
            return;
        } else if (parts.length != 1) {
            return;
        }

        endMessage.addExtra(nextButtonTextComponent);
    }

    public boolean addLine(TextComponent line) {
        if (lines.size() == maxLines) {
            return false;
        }
        lines.add(line);
        return true;
    }

    public boolean isFull() {
        return lines.size() == maxLines;
    }

    public List<TextComponent> getLines() {
        return lines;
    }

    public Optional<ChatPage> getNextChatPage() {
        return Optional.ofNullable(nextChatPage);
    }

    public Optional<ChatPage> getBackChatPage() {
        return Optional.ofNullable(backChatPage);
    }

    public TextComponent getEndMessage() {
        return endMessage;
    }

    @Override
    public Integer getKey() {
        return pageIndex;
    }
}
