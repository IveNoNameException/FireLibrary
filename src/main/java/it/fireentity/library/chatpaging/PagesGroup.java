package it.fireentity.library.chatpaging;

import it.fireentity.library.storage.Cache;
import it.fireentity.library.interfaces.Cacheable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PagesGroup implements Cacheable<String> {
    @Getter
    private final Cache<Integer, ChatPage> pages = new Cache<>();
    private final String groupName;
    @Getter
    private final PageTexture pageTexture;
    private List<TextComponent> lines;

    public void setLines(List<TextComponent> lines) {
        this.lines = lines;
        pages.clear();

        ChatPage chatPage = new ChatPage(pageTexture.getMaxPageSize(), null, pageTexture, this);
        pages.addValue(chatPage);

        for (TextComponent line : lines) {
            //Check and add the iterated line to the current page
            if (!chatPage.addLine(line)) {
                ChatPage newChatPage = new ChatPage(pageTexture.getMaxPageSize(), chatPage, pageTexture, this);
                newChatPage.addLine(line);
                pages.addValue(newChatPage);
                chatPage.addNextPage(newChatPage);
                chatPage = newChatPage;
            }
        }
    }

    public void send(Player player, int index) {
        if (lines.isEmpty()) {
            return;
        }
        Optional<ChatPage> page = pages.getValue(index);
        if (page.isPresent()) {

            //Check if there is only one page
            if (pages.getValues().size() == 1) {
                player.sendMessage(pageTexture.getOnePageStartMessage());
            } else {
                player.sendMessage(pageTexture.getStartMessage()
                        .replace("{0}", Integer.toString(getPages().getKeys().size()))
                        .replace("{1}", Integer.toString(page.get().getPageIndex())));
            }
            for (TextComponent line : page.get().getLines()) {
                player.spigot().sendMessage(line);
            }

            if (pages.getValues().size() == 1) {
                player.sendMessage(pageTexture.getOnePageEndMessage());
            } else {
                player.spigot().sendMessage(page.get().getEndMessage());
            }
        }
    }

    public void setEndMessage(String endMessage) {
        pageTexture.setEndMessage(endMessage);
        if (lines != null) {
            setLines(lines);
        }
    }

    public void setBackButton(String backButton) {
        pageTexture.setBackButton(backButton);
        if (lines != null) {
            setLines(lines);
        }
    }

    public void setMaxPageSize(int maxPageSize) {
        pageTexture.setMaxPageSize(maxPageSize);
        if (lines != null) {
            setLines(lines);
        }
    }

    public void setNextButton(String nextButton) {
        pageTexture.setNextButton(nextButton);
        if (lines != null) {
            setLines(lines);
        }
    }

    public void setStartMessage(String startMessage) {
        pageTexture.setStartMessage(startMessage);
        if (lines != null) {
            setLines(lines);
        }
    }

    @Override
    public String getKey() {
        return groupName;
    }
}
