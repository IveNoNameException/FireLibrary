package it.fireentity.library.command.argument;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.interfaces.Argument;
import it.fireentity.library.chatpaging.PageTexture;
import it.fireentity.library.chatpaging.PagesGroup;
import it.fireentity.library.command.row.CommandRow;
import it.fireentity.library.enumerations.Config;
import it.fireentity.library.interfaces.Cacheable;
import it.fireentity.library.locales.Message;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class Command implements Cacheable<String> {
    @Getter
    private final LinkedHashMap<String, AbstractArgument> arguments = new LinkedHashMap<>();
    @Getter
    @Setter
    private CommandNode commandNode;
    private final AbstractPlugin abstractPlugin;
    private final CommandRow commandRow = new CommandRow();
    private final PagesGroup pagesGroup;

    public Command(AbstractPlugin abstractPlugin, PageTexture pageTexture, String label, boolean allowedConsole) {
        this(abstractPlugin, pageTexture, label, allowedConsole, null);
    }

    public Command(AbstractPlugin abstractPlugin, String label, boolean allowedConsole) {
        this(abstractPlugin, null, label, allowedConsole, null);
    }

    public Command(AbstractPlugin abstractPlugin, String label, boolean allowedConsole, CommandNode superCommandNode) {
        this(abstractPlugin, null, label, allowedConsole, superCommandNode);
    }

    public Command(AbstractPlugin abstractPlugin, PageTexture pageTexture, String label, boolean allowedConsole, CommandNode superCommandNode) {
        this.abstractPlugin = abstractPlugin;
        if (superCommandNode == null) {
            commandNode = new CommandNode(abstractPlugin, label, allowedConsole, this);
        } else {
            commandNode = new CommandNode(abstractPlugin, label, allowedConsole, superCommandNode, this);
        }
        if (pageTexture != null) {
            pagesGroup = new PagesGroup(this.getCommandNode().getKey(), pageTexture);
            abstractPlugin.getAPIFireLibrary().getChangePageCommand().addPagesGroup(pagesGroup);
        } else {
            pagesGroup = null;
        }
        this.abstractPlugin.getCommandsCache().addValue(this);
    }

    public void addMessage(String path, String... args) {

        Optional<Message> message = abstractPlugin.getLocales().getMessage(path);
        if (args == null) {
            //Check if the message already exists
            //If the message exists check is the text is set
            if (message.isPresent() && !message.get().getMessage().isPresent()) {
                getPlugin().getLogger().severe(abstractPlugin.getName() + ": " + ChatColor.RED + "{Missing locale at " + ChatColor.GOLD + path + ChatColor.RED + "}" + ChatColor.RESET);
                abstractPlugin.getLocales().writeIntoMissing(path, message.get().getArguments());
            } else if (!message.isPresent()) {
                getPlugin().getLogger().severe(abstractPlugin.getName() + ": " + ChatColor.RED + "{Missing locale at " + ChatColor.GOLD + path + ChatColor.RED + "}" + ChatColor.RESET);
                abstractPlugin.getLocales().writeIntoMissing(path);
            }
        } else if (!message.isPresent()) {
            getPlugin().getLogger().severe(abstractPlugin.getName() + ": " + ChatColor.RED + "{Missing locale at " + ChatColor.GOLD + path + ChatColor.RED + "}" + ChatColor.RESET);
            abstractPlugin.getLocales().writeIntoMissing(path, Arrays.asList(args));
        }
    }

    public abstract void execute(CommandSender sender, List<String> args, CommandRow commandRow);

    public Optional<PagesGroup> getPagesGroup() {
        return Optional.ofNullable(pagesGroup);
    }

    public String getKey() {
        return commandNode.getKey();
    }

    public AbstractPlugin getPlugin() {
        return abstractPlugin;
    }

    public String getSuccessPath() {
        return commandNode.getStringPath() + ".success";
    }

    public String getPath() {
        return commandNode.getStringPath();
    }

    public Command addArgument(AbstractArgument argument) {
        arguments.put(argument.getArgumentName(), argument);
        commandRow.addArgument(argument);
        return this;
    }

    public void evaluate(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(commandNode.getPermission())) {
            abstractPlugin.getLocales().sendMessage(Config.INSUFFICIENT_PERMISSIONS.getPath(), sender);
            return;
        }

        if (commandRow.evalRaw(sender.getName(), args)) {
            execute(sender, args, commandRow);
            commandRow.resetAll();
            return;
        }

        String commandColor = abstractPlugin.getLocales().getString(Config.USAGE_PARAMETER_COLOR.getPath());
        sender.sendMessage("/" + commandNode.getUsage() + commandColor + getUsage());

        for (AbstractArgument argument : arguments.values()) {
            if (!argument.isEvaluated() && !argument.isOptional()) {
                abstractPlugin.getLocales().sendMessage(Config.INVALID_ARGUMENT.getPath(), sender, argument.getArgumentName());
                commandRow.resetAll();
                Optional<PagesGroup> pagesGroup = abstractPlugin.getAPIFireLibrary().getChangePageCommand().getPagesGroup(argument.getArgumentName());
                if (pagesGroup.isPresent()) {
                    pagesGroup.get().setLines(new ArrayList<>(argument.getPossibleValues()));
                    abstractPlugin.getAPIFireLibrary().getChangePageCommand().sendPage(sender, pagesGroup.get(), 1);
                } else {
                    PagesGroup pages = abstractPlugin.getAPIFireLibrary().getPageTexture().map(page -> new PagesGroup(argument.getArgumentName(), page)).orElse(null);
                    if (pages == null) {
                        return;
                    }
                    abstractPlugin.getAPIFireLibrary().getChangePageCommand().addPagesGroup(pages);
                    pages.setLines(new ArrayList<>(argument.getPossibleValues()));
                    abstractPlugin.getAPIFireLibrary().getChangePageCommand().sendPage(sender, pages, 1);
                }
                return;
            }
        }
        commandRow.resetAll();
    }

    public String getUsage() {
        StringBuilder usageLine = new StringBuilder();
        for (Argument argument : arguments.values()) {
            if (argument.isOptional()) {
                usageLine.append("[").append(argument.getArgumentName()).append("] ");
            } else {
                usageLine.append("<").append(argument.getArgumentName()).append("> ");
            }
        }

        return usageLine.toString();
    }
}
