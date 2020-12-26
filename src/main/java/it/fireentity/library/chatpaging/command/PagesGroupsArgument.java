package it.fireentity.library.chatpaging.command;

import it.fireentity.library.command.argument.AbstractArgument;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PagesGroupsArgument extends AbstractArgument {
    private final ChangePageCommand changePageCommand;

    public PagesGroupsArgument(boolean isOptional, ChangePageCommand changePageCommand) {
        super("pagesGroup", isOptional, 1);
        this.changePageCommand = changePageCommand;
    }

    @Override
    public Collection<TextComponent> getPossibleValues() {
        return new ArrayList<>();
    }

    @Override
    public Object parseForConsoleSender(String currentArgument, List<String> args) {
        if(args.size() != 1) {
            return null;
        }
        return parse(args.get(0));
    }

    @Override
    public Object parseForPlayerSender(String commandSender, String currentArgument, List<String> args) {
        if(args.size() != 1) {
            return null;
        }
        return parse(args.get(0));
    }

    public Object parse(String groupName) {
        return changePageCommand.getPagesGroup(groupName).orElse(null);
    }

}
