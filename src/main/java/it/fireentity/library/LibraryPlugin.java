package it.fireentity.library;

import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.locales.Message;
import it.fireentity.library.utils.PluginFile;

import java.util.List;

public class LibraryPlugin extends AbstractPlugin{
    @Override
    protected List<Message> initializeMessageList() {
        return null;
    }

    @Override
    protected List<Command> initializeCommands() {
        return null;
    }

    @Override
    protected List<PluginFile> initializeConfigs() {
        return null;
    }

    @Override
    protected void initializeCaches() {

    }

    @Override
    protected void initializeDatabaseUtility() {

    }

    @Override
    protected void initializeListeners() {

    }

    @Override
    protected CommandNode initializeMainNode() {
        return null;
    }
}
