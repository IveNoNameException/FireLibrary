package it.fireentity.library.interfaces;


import it.fireentity.library.command.row.CustomIterator;

import java.util.List;
import java.util.Optional;

public interface Argument {
    void reset();
    int getArguments();
    boolean isEvaluated();
    boolean isSpecified();
    boolean isOptional();
    String getArgumentName();
    <T> Optional<T> getResult();
    int evalConsoleSender(CustomIterator<String> stringArguments, String currentArgument, List<String> args);
    int evalPlayerSender(CustomIterator<String> stringArguments, String sender, String currentArgument, List<String> args);
    int eval(CustomIterator<String> stringArguments, String sender, String currentArgument, List<String> args);
    Object parseForConsoleSender(String currentArgument, List<String> args);
    Object parseForPlayerSender(String commandSender, String currentArgument, List<String> args);
}
