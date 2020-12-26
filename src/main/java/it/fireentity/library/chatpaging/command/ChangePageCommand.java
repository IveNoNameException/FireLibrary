package it.fireentity.library.chatpaging.command;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.chatpaging.PageTexture;
import it.fireentity.library.chatpaging.PagesGroup;
import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.argument.implementations.NumericArgument;
import it.fireentity.library.chatpaging.ChatPage;
import it.fireentity.library.command.row.CommandRow;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class ChangePageCommand extends Command {

    public ChangePageCommand(AbstractPlugin superCrates, PageTexture pageTexture) {
        super(superCrates,pageTexture,"chatPage",false,superCrates.getMainNode());
        this.addArgument(new PagesGroupsArgument(false, this))
                .addArgument(new NumericArgument("pageIndex", false));
    }

    @Override
    public void execute(CommandSender sender, List<String> args, CommandRow commandRow) {
        Optional<PagesGroup> pagesGroup = Optional.empty();
        if(commandRow.isSpecified("pagesGroup")) {
            pagesGroup = commandRow.getOne("pagesGroup");
        }

        Optional<Integer> pageIndex = Optional.empty();
        if(commandRow.isSpecified("pageIndex")) {
            pageIndex = commandRow.getOne("pageIndex");
        }

        if(!pagesGroup.isPresent() || !pageIndex.isPresent()) {
            return;
        }

        Optional<ChatPage> page = pagesGroup.get().getPages().getValue(pageIndex.get());

        if(!page.isPresent()) {
            return;
        }

        sendPage(sender,pagesGroup.get(),pageIndex.get());
    }

    public void sendPage(CommandSender sender, PagesGroup pagesGroup, Integer index) {
        Optional<ChatPage> page = pagesGroup.getPages().getValue(index);

        if(!page.isPresent()) {
            return;
        }

        pagesGroup.send((Player)sender, index);
    }

    public Optional<PagesGroup> getPagesGroup(String key) {
        return getPlugin().getPages().getValue(key);
    }

    public void addPagesGroup(PagesGroup pagesGroup) {
        getPlugin().getPages().addValue(pagesGroup);
    }
}
