package it.fireentity.library.command.nodes;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.command.argument.Command;
import it.fireentity.library.enumerations.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class CommandNode extends Node<Command, String> implements CommandExecutor {
    private final boolean allowedConsole;
    private final String permission;
    private final String usage;
    private final String path;
    private final AbstractPlugin plugin;

    /**
     * Root node
     *
     * @param label
     * @param allowedConsole
     */
    public CommandNode(AbstractPlugin plugin, String label, boolean allowedConsole) {
        this(plugin, label, allowedConsole, null, null);
    }

    public CommandNode(AbstractPlugin plugin, String label, boolean allowedConsole, Command command) {
        this(plugin, label, allowedConsole, null, command);
    }

    /**
     * Medium node
     *
     * @param superCommandNode
     * @param label
     * @param allowedConsole
     */
    public CommandNode(AbstractPlugin plugin, String label, boolean allowedConsole, CommandNode superCommandNode) {
        this(plugin, label, allowedConsole, superCommandNode, null);

    }

    public CommandNode(AbstractPlugin plugin, String label, boolean allowedConsole, CommandNode superCommandNode, Command command) {
        super(label, superCommandNode, command);
        this.plugin = plugin;
        if (command != null) {
            command.setCommandNode(this);
        }
        this.usage = getUsage();
        this.allowedConsole = allowedConsole;
        this.path = this.getStringPath();
        this.permission = getStringPath();
    }

    public String getUsage() {
        if (usage != null) {
            return usage;
        }

        StringBuilder usage = new StringBuilder();
        if (this.getSuperNode().isPresent()) {
            for (String line : getPath().keySet()) {
                usage.append(line).append(" ");
            }
        }

        return usage.toString();
    }

    private String getUsage(Node<Command, String> node) {
        StringBuilder usage = new StringBuilder();
        for (String part : node.getPath().keySet()) {
            usage.append(part).append(" ");
        }
        return usage.toString();
    }

    public List<TextComponent> getSubNodesUsage() {
        List<TextComponent> subNodes = new ArrayList<>();
        for (Node<Command, String> node : this.getSubNodes()) {
            TextComponent textComponent;
            if (!node.getContent().isPresent()) {
                String usage = getUsage(node);
                textComponent = new TextComponent(usage + plugin.getLocales().getString(Config.USAGE_PARAMETER_COLOR.getPath()) + "<...>");
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + usage));
            } else {
                String usage = getUsage(node);
                textComponent = new TextComponent(usage + plugin.getLocales().getString(Config.USAGE_PARAMETER_COLOR.getPath()) + node.getContent().get().getUsage());
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + usage));
            }
            subNodes.add(textComponent);
        }

        return subNodes;
    }

    public String getPermission() {
        return permission;
    }


    public String getStringPath() {
        if (path != null) {
            return path;
        }

        Iterator<String> lines = getPath().keySet().iterator();
        if (!lines.hasNext()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder(lines.next());
        while (lines.hasNext()) {
            stringBuilder.append(".").append(lines.next());
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (!allowedConsole && commandSender.equals(Bukkit.getConsoleSender())) {
            plugin.getLocales().sendMessage(plugin.getLocales().getString(Config.INVALID_COMMAND_SENDER.getPath()), commandSender);
            return false;
        }

        if (strings.length == 0 && !getContent().isPresent()) {
            if (getSubNodes().isEmpty() && getContent().isPresent()) {
                String commandColor = plugin.getLocales().getString(Config.USAGE_PARAMETER_COLOR.getPath());
                commandSender.sendMessage("/" + getUsage() + " " + commandColor + getContent().get().getUsage());
                return false;
            }

            sendSubNodeUsage(commandSender);
            return false;
        }

        if (!getContent().isPresent()) {
            String commandLabel = strings[0];
            Optional<Node<Command, String>> subNode = getSubNode(commandLabel);
            if (subNode.isPresent() && subNode.get() instanceof CommandNode) {
                CommandNode node = (CommandNode) subNode.get();
                node.onCommand(commandSender, command, s, Arrays.copyOfRange(strings, 1, strings.length));
            } else {
                sendSubNodeUsage(commandSender);
                return false;
            }
        } else {
            this.getContent().get().evaluate(commandSender, Arrays.asList(strings));
        }
        return false;
    }


    private void sendSubNodeUsage(CommandSender commandSender) {
        plugin.getLocales().sendMessage(Config.USAGE_START.getPath(),commandSender);
        for (TextComponent line : getSubNodesUsage()) {
            TextComponent textComponent = new TextComponent("/");
            try{
                textComponent.setColor(ChatColor.valueOf(plugin.getLocales().getString(Config.USAGE_COLOR.getPath())));
            } catch (IllegalArgumentException ignore) {

            }
            textComponent.addExtra(line);
            commandSender.spigot().sendMessage(textComponent);
        }
        plugin.getLocales().sendMessage(Config.USAGE_END.getPath(),commandSender);
    }
}