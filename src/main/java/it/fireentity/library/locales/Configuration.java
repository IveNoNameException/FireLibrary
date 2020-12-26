package it.fireentity.library.locales;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class Configuration {
    private final HashMap<String, String> translations;

    public Optional<String> getMessage(String path) {
        return Optional.ofNullable(getTranslations().get(path)).map(msg -> msg.replace("&", "§"));
    }
}
