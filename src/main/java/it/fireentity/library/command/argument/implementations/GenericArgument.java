package it.fireentity.library.command.argument.implementations;

import it.fireentity.library.command.argument.AbstractArgument;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GenericArgument<T> extends AbstractArgument {
    private final List<TextComponent> possibleValues;
    private final Action<T> action;
    public GenericArgument(String argumentName, boolean isOptional, int argumentsToEvaluate, Action<T> action, TextComponent ... values) {
        super(argumentName, isOptional, argumentsToEvaluate);
        this.possibleValues = Arrays.asList(values);
        this.action = action;
    }

    @Override
    public Object parseForPlayerSender(String commandSender, String currentArgument, List<String> args) {
        return action.action(args);
    }

    @Override
    public Object parseForConsoleSender(String currentArgument, List<String> args) {
        return action.action(args);
    }

    @Override
    public Collection<TextComponent> getPossibleValues() {
        return possibleValues;
    }



    public interface Action<T> {
        T action(List<String> argumentsToEvaluate);
    }
}