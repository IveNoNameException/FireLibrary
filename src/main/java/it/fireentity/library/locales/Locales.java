package it.fireentity.library.locales;

import com.google.gson.Gson;
import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.storage.Cache;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Locales {
    private final Cache<String, Message> messageCache = new Cache<>();
    private final File file;
    private final File missingLocales;
    private final AbstractPlugin plugin;

    public Locales(AbstractPlugin plugin, File locales, File missingLocales) {
        this.plugin = plugin;
        this.missingLocales = missingLocales;
        this.file = locales;

        //Check if the plugin folder exists
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        //Check if the file exists
        if (!locales.exists()) {
            try {
                locales.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Check if the missingLocales file exists
        if (!missingLocales.exists()) {
            try {
                missingLocales.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Clear the missings file
        try {
            Files.write(missingLocales.toPath(),"".getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Optional<Configuration> configuration = deserialize();
        if (!configuration.isPresent()) {
            return;
        }

        //Adding messages into the map
        for (Map.Entry<String, String> locale : configuration.get().getTranslations().entrySet()) {
            messageCache.addValue(new Message(locale.getKey(), locale.getValue()));
        }
    }

    public void writeIntoMissing(String line, List<String> args) {
        int i = 0;
        for (String argumentLine : args) {
            try {
                Files.write(missingLocales.toPath(), ("//" + i + " -> " + argumentLine + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
        try {
            Files.write(missingLocales.toPath(), (line + "\n\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeIntoMissing(String line) {
        writeIntoMissing(line, new ArrayList<>());
    }

    public Optional<Configuration> deserialize() {
        Gson gson = new Gson();
        try {
            List<String> contents = Files.readAllLines(file.toPath());
            return Optional.ofNullable(gson.fromJson(String.join("", contents), Configuration.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Message> getMessage(String path) {
        return messageCache.getValue(path);
    }

    public String getString(String path, String... args) {
        Optional<Message> message = messageCache.getValue(path);
        if (!message.isPresent()) {
            return "§c{Missing locale at §6" + path + "§c}§f";
        }
        Optional<String> evaluatedMessage = message.get().evaluate(args);
        return evaluatedMessage.map(s -> s.replace("&", "§")).orElseGet(() -> "§c{Missing locale at §6" + path + "§c}§f");
    }

    public boolean hasPath(String path) {
        return messageCache.getValue(path).isPresent();
    }

    public Optional<Double> getDouble(String path) {
        try {
            return Optional.of(Double.parseDouble(getString(path)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> getInteger(String path) {
        try {
            return Optional.of(Integer.parseInt(getString(path)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public void sendMessage(String path, CommandSender sender, String... args) {
        sender.sendMessage(getString(path, args));
    }

    public Message addMessage(String path) {
        Message message = messageCache.getValue(path).orElse(new Message(path));
        messageCache.addValue(message);
        return message;
    }

    public Message addMessage(Message message) {
        messageCache.addValue(message);
        return message;
    }

    public void sendMessage(String path, Player player, String... args) {
        player.sendMessage(getString(path, args));
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messageCache.getValues());
    }
}
