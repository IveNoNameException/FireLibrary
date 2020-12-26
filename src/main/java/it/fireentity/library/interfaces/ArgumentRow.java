package it.fireentity.library.interfaces;


import java.util.Optional;

public interface ArgumentRow {
    void addArgument(Argument argument);
    boolean isSpecified(String argumentName);
    <T> Optional<T> getOne(String argumentName);
    void resetAll();
}
