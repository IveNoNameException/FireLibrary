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
    protected void onStart() {

    }

    @Override
    protected List<Message> initializeMessageList() {
        return new ArrayList<>();
    }

    @Override
    protected List<Command> initializeCommands() {
        List<Command> commands = new ArrayList<>();
        commands.add(changePageCommand = new ChangePageCommand(this,this.getPageTexture()));
        return commands;
    }

    @Override
    protected List<PluginFile> initializeConfigs() {
        return new ArrayList<>();
    }

    @Override
    protected void initializeCaches() {
        this.players = new PlayerCache(this);
    }

    @Override
    protected void initializeDatabaseUtility() {

    }

    @Override
    protected void initializeListeners() {

    }

    @Override
    protected Optional<CommandNode> initializeMainNode() {
        return Optional.empty();
    }

    public Optional<ChangePageCommand> getChangePageCommand() {
        return Optional.ofNullable(changePageCommand);
    }
}
