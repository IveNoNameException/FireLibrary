package it.fireentity.library.command.argument;


import it.fireentity.library.command.row.CustomIterator;
import it.fireentity.library.interfaces.Argument;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractArgument implements Argument {

    private final String argumentName;
    protected  Object object = null;
    protected final boolean isOptional;
    protected boolean isEvaluated;
    protected final int argumentsToEvaluate;

    public AbstractArgument(String argumentName, boolean isOptional, int argumentsToEvaluate) {
        this.isEvaluated = false;
        this.argumentName = argumentName;
        this.isOptional = isOptional;
        this.argumentsToEvaluate = argumentsToEvaluate;
    }

    @Override
    public <T> Optional<T> getResult() {
        return Optional.ofNullable((T) object);
    }

    @Override
    public void reset() {
        this.isEvaluated = false;
        object = null;
    }

    @Override
    public String getArgumentName() {
        return this.argumentName;
    }

    @Override
    public int getArguments() {
        return argumentsToEvaluate;
    }

    @Override
    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public boolean isEvaluated() {
        return isEvaluated;
    }

    @Override
    public boolean isSpecified() {
        return object != null;
    }

    @Override
    public int eval(CustomIterator<String> stringArguments, String sender, String currentArgument, List<String> args) {
        if(sender == null) {
            return evalConsoleSender(stringArguments, currentArgument, args);
        } else {
            return evalPlayerSender(stringArguments, sender, currentArgument, args);
        }
    }

    @Override
    public int evalPlayerSender(CustomIterator<String> stringArguments, String sender, String currentArgument, List<String> args) {
        this.object = parseForPlayerSender(sender, currentArgument, args);
        if(object != null) {
            isEvaluated = true;
            stringArguments.next();
            return getArguments();
        } else {
            isEvaluated = false;
            return 0;
        }
    }

    @Override
    public int evalConsoleSender(CustomIterator<String> stringArguments, String currentArgument, List<String> args) {
        this.object = parseForConsoleSender(currentArgument, args);
        if(object != null) {
            isEvaluated = true;
            stringArguments.next();
            return getArguments();
        } else {
            isEvaluated = false;
            return 0;
        }
    }

    public abstract Collection<TextComponent> getPossibleValues();
}
