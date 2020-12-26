package it.fireentity.library.interfaces;


import java.util.List;

public interface FlaggedArgument extends Argument {
    String getFlag();
    FlaggedArgument addConflictFlag(String flag);
    FlaggedArgument addConflictFlags(String ... flag);
    List<String> getConflictFlag();
}
