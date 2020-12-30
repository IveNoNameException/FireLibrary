package it.fireentity.library;

import it.fireentity.library.chatpaging.command.ChangePageCommand;
import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
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
        getPageTexture().ifPresent(pageTexture -> new ChangePageCommand(this, pageTexture));
    }

    @Override
    public CommandNode initializeMainCommandNode() {
        return new CommandNode(this, "FireLibAPI", false);
    }

    public ChangePageCommand getChangePageCommand() {
        return changePageCommand;
    }
}
