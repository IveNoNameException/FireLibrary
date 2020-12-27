package it.fireentity.library.locales;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.row.CommandRow;
import it.fireentity.library.events.LocalesReloadEvent;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends Command {
    public ReloadCommand(AbstractPlugin abstractPlugin) {
        super(abstractPlugin, abstractPlugin.getAPIFireLibrary().getPageTexture(),"reload", true, abstractPlugin.getMainNode());
    }

    @Override
    public void execute(CommandSender sender, List<String> args, CommandRow commandRow) {
        this.getPlugin().reloadLocale();
        this.getPlugin().getLocales().sendMessage(getSuccessPath(),sender);
        this.getPlugin().getEventManager().callEvent(new LocalesReloadEvent());
    }
}
