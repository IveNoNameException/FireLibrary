package it.fireentity.library.locales;

import it.fireentity.library.interfaces.Cacheable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Message implements Cacheable<String> {
    @Getter private final String key;
    private String message;
    private List<String> arguments;

    public Message(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public Message(String key) {
        this.key = key;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    public void addArguments(String ... args) {
        arguments = Arrays.asList(args);
    }

    public void addArguments(List<String> args) {
        arguments = args;
    }

    public List<String> getArguments() {
        if(arguments == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(arguments);
    }

    public Optional<String> evaluate(String ... args) {
        if(args==null) {
            return Optional.ofNullable(message);
        }

        if(message == null) {
            return Optional.empty();
        }
        String evaluatedMessage = message;
        for(int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                evaluatedMessage = evaluatedMessage.replace("{" + i + "}", args[i]);
            }
        }
        return Optional.of(evaluatedMessage);
    }

}
