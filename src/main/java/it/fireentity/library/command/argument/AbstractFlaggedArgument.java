package it.fireentity.library.command.argument;


import it.fireentity.library.command.row.CustomIterator;
import it.fireentity.library.interfaces.FlaggedArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFlaggedArgument extends AbstractArgument implements FlaggedArgument {
    private final String flag;
    private final List<String> conflictFlags = new ArrayList<>();

    public AbstractFlaggedArgument(String flag, String argumentName, boolean isOptional, int argumentsToEvaluate) {
        super(argumentName, isOptional, argumentsToEvaluate);
        this.flag = flag;
    }

    @Override
    public String getFlag() {
        return flag;
    }

    @Override
    public FlaggedArgument addConflictFlag(String flag) {
        this.conflictFlags.add(flag);
        return this;
    }

    @Override
    public FlaggedArgument addConflictFlags(String... flags) {
        this.conflictFlags.addAll(Arrays.asList(flags));
        return this;
    }

    @Override
    public List<String> getConflictFlag() {
        return conflictFlags;
    }

    @Override
    public int evalPlayerSender(CustomIterator<String> stringArguments, String sender, String currentArgument, List<String> args) {
        isEvaluated = true;
        this.object = parseForPlayerSender(sender, currentArgument, args);
        if(object != null) {
            stringArguments.next();
            return 1 + getArguments();
        } else {
            return 0;
        }
    }

    @Override
    public int evalConsoleSender(CustomIterator<String> stringArguments, String currentArgument, List<String> args) {
        isEvaluated = true;
        this.object = parseForConsoleSender(currentArgument, args);
        if(object != null) {
            stringArguments.next();
            return 1 + getArguments();
        } else {
            return 0;
        }
    }
}
