package it.fireentity.library;

import it.fireentity.library.chatpaging.PageTexture;
import it.fireentity.library.chatpaging.command.ChangePageCommand;
import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.enumerations.Config;
import it.fireentity.library.locales.Message;
import it.fireentity.library.player.PlayerCache;
import it.fireentity.library.utils.PluginFile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class APIFireLibrary extends AbstractPlugin {
    @Getter private static APIFireLibrary apiFireLibrary;
    private PlayerCache players;
    private ChangePageCommand changePageCommand;

    {
        apiFireLibrary = this;
    }


    @Override
    public void onStart() {
        players = new PlayerCache(this);
        getPageTexture().ifPresent(pageTexture -> changePageCommand = new ChangePageCommand(this, pageTexture));
    }

    @Override
    protected Optional<PageTexture> initializePageTexture() {
        Optional<Integer> maxPageSize = getLocales().getInteger(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_MAX_PAGE_SIZE.getPath());
        String nextButton = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_NEXT_BUTTON.getPath());
        String backButton = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_BACK_BUTTON.getPath());
        String endMessage = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_START_MESSAGE.getPath());
        String startMessage = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_START_MESSAGE.getPath());
        String noLineFound = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_NO_LINE_FOUND.getPath());
        String onePageStartMessage = getLocales().getString(it.fireentity.library.enumerations.Config.ONE_PAGE_START_MESSAGE.getPath());
        String onePageEndMessage = getLocales().getString(it.fireentity.library.enumerations.Config.ONE_PAGE_END_MESSAGE.getPath());

        return maxPageSize.map(integer -> new PageTexture(nextButton, backButton, endMessage, startMessage, noLineFound, integer, onePageStartMessage, onePageEndMessage));
    }

    @Override
    public CommandNode initializeMainCommandNode() {
        return new CommandNode(this, "FireLibAPI", false);
    }

    public ChangePageCommand getChangePageCommand() {
        return changePageCommand;
    }
}
