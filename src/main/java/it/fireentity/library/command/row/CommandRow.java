package it.fireentity.library.command.row;


import it.fireentity.library.interfaces.Argument;
import it.fireentity.library.interfaces.ArgumentRow;
import it.fireentity.library.interfaces.FlaggedArgument;

import java.util.*;

public class CommandRow implements ArgumentRow {
    private final LinkedHashMap<String, Argument> argumentsList = new LinkedHashMap<>();
    private final List<Argument> nonOptionalArguments = new ArrayList<>();

    @Override
    public void addArgument(Argument argument) {
        argumentsList.put(argument.getArgumentName(), argument);

        if (!argument.isOptional()) {
            nonOptionalArguments.add(argument);
        }
    }

    @Override
    public boolean isSpecified(String argumentName) {
        if (argumentsList.get(argumentName) != null) {
            return argumentsList.get(argumentName).isSpecified();
        }
        return false;
    }

    public List<Argument> getArguments() {
        return new ArrayList<>(argumentsList.values());
    }

    public <T> Optional<T> getOne(String argumentName) {
        return argumentsList.get(argumentName).getResult();
    }

    public boolean evalRaw(String sender, List<String> args) {
        //Get the iterator of arguments
        CustomIterator<String> stringArguments = new CustomIterator<>(args);
        Iterator<Argument> abstractArguments = argumentsList.values().iterator();

        int totalEvaluatedArguments = 0;
        while (stringArguments.currentArgument().isPresent() && abstractArguments.hasNext()) {
            String currentArgument = stringArguments.currentArgument().get();
            Argument currentArgumentInstance = abstractArguments.next();

            //Check if the argument is a flagged argument
            if (currentArgumentInstance instanceof FlaggedArgument) {
                FlaggedArgument flaggedArgument = (FlaggedArgument) currentArgumentInstance;

                //Check if the argument is optional
                if (!flaggedArgument.isOptional()) {
                    if (!currentArgument.equals(flaggedArgument.getFlag())) {
                        return false;
                    }

                    totalEvaluatedArguments += flaggedArgument.eval(stringArguments, sender, currentArgument, stringArguments.getNextElementsList(currentArgumentInstance.getArguments()));
                    stringArguments.goNext(flaggedArgument.getArguments());

                } else {
                    if (currentArgument.equals(flaggedArgument.getFlag())) {
                        totalEvaluatedArguments += flaggedArgument.eval(stringArguments, sender, currentArgument, stringArguments.getNextElementsList(currentArgumentInstance.getArguments()));
                        stringArguments.goNext(currentArgumentInstance.getArguments());
                    }
                }
            } else {
                totalEvaluatedArguments += currentArgumentInstance.eval(stringArguments,sender,currentArgument, stringArguments.getNextElementsList(currentArgumentInstance.getArguments()));
            }
        }

        if (!checkNonOptionalArguments()) {
            return false;
        }

        return args.size() == totalEvaluatedArguments;
    }

    private boolean checkNonOptionalArguments() {
        for (Argument abstractArgument : nonOptionalArguments) {
            if (!abstractArgument.isEvaluated()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Resent all arguments content
     */
    public void resetAll() {
        for (Argument abstractArgument : argumentsList.values()) {
            abstractArgument.reset();
        }
    }
}
