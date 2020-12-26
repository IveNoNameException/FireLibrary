package it.fireentity.library.command.argument.implementations;

import it.fireentity.library.command.argument.AbstractArgument;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NumericArgument extends AbstractArgument {
    public NumericArgument(String argumentName, boolean isOptional) {
        super(argumentName, isOptional, 1);
    }

    @Override
    public Object parseForPlayerSender(String commandSender, String currentArgument, List<String> args) {
        if(args.size() == 0) {
            return null;
        }

        try {
            return Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Object parseForConsoleSender(String currentArgument, List<String> args) {
        if(args.size() == 0) {
            return null;
        }

        try {
            return Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Collection<TextComponent> getPossibleValues() {
        return new ArrayList<>();
    }
}
