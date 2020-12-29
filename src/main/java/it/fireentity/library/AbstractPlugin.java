package it.fireentity.library;

import it.fireentity.library.events.EventManager;
import it.fireentity.library.locales.ReloadCommand;
import it.fireentity.library.chatpaging.PageTexture;
import it.fireentity.library.chatpaging.PagesGroup;
import it.fireentity.library.chatpaging.command.ChangePageCommand;
import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.enumerations.Config;
import it.fireentity.library.listener.ClickManager;
import it.fireentity.library.listener.GuiClickListener;
import it.fireentity.library.listener.GuiCloseListener;
import it.fireentity.library.inventories.PagesHandler;
import it.fireentity.library.locales.Locales;
import it.fireentity.library.locales.Message;
import it.fireentity.library.player.CustomPlayer;
import it.fireentity.library.player.PlayerCache;
import it.fireentity.library.storage.Cache;
import it.fireentity.library.utils.PluginFile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class AbstractPlugin extends JavaPlugin {
    private final Cache<String, PagesGroup> pages = new Cache<>();
    private ClickManager clickManager;
    private CommandNode mainNode;
    private Cache<String, PluginFile> pluginFileCache;
    private Cache<String, Command> commandsCache;
    private PagesHandler pagesHandler;
    private GuiClickListener guiClickListener;
    private GuiCloseListener guiCloseListener;
    private Locales locales;
    private EventManager eventManager;
    private PageTexture pageTexture;

    @Override
    public void onEnable() {
        initializeLocales();
        eventManager = new EventManager();
        pageTexture = initializePageTexture();
        pagesHandler = new PagesHandler(this);
        pluginFileCache = new Cache<>();
        commandsCache = new Cache<>();
        CustomPlayer.setPagesHandler(pagesHandler);
        for (PluginFile pluginFile : initializeConfigs()) {
            pluginFileCache.addValue(pluginFile);
        }
        initializeGuiListener();
        mainNode = initializeMainNode().orElse(null);
        if(mainNode !=null) {
            getCommand(mainNode.getKey()).setExecutor(mainNode);
        }

        for (Message msg : this.locales.getMessages()) {
            if (!msg.getMessage().isPresent()) {
                System.out.println(this.getName() + ": §c{Missing locale at §6" + msg.getKey() + "§c}§f");
                this.locales.writeIntoMissing(msg.getKey(), msg.getArguments());
            }
        }
        commandsCache.addValue(new ReloadCommand(this));
        for (Command command : initializeCommands()) {
            commandsCache.addValue(command);
        }

        initializeDatabaseUtility();
        initializeCaches();
        initializeListeners();
        onStart();
    }

    public void reloadLocale() {
        File locales = new File(this.getDataFolder(), "locales.json");
        File missingLocales = new File(this.getDataFolder(), "missingLocales.json");
        this.locales = new Locales(this, locales, missingLocales);
    }

    public void initializeGuiListener() {
        try {
            int delay = Integer.parseInt(locales.getString(Config.CLICK_GUI_DELAY.getPath()));
            clickManager = new ClickManager(this, delay, getLocales().getString("click_delay_error"));
            guiClickListener = new GuiClickListener(this, pagesHandler, clickManager);
            guiCloseListener = new GuiCloseListener(this, pagesHandler);

        } catch (NumberFormatException ignore) {

        }
    }

    public void initializeLocales() {
        File locales = new File(this.getDataFolder(), "locales.json");
        File missingLocales = new File(this.getDataFolder(), "missingLocales.json");
        this.locales = new Locales(this, locales, missingLocales);
        for (Message msg : internalInitializeMessageList()) {
            //Check if the message exists
            Optional<Message> message = this.locales.getMessage(msg.getKey());

            //Check if the passed message as parameters
            if (message.isPresent() && !msg.getArguments().isEmpty()) {
                message.get().addArguments(msg.getArguments());
            } else if (!message.isPresent()) {
                this.locales.addMessage(msg);
            }
        }

        for (Message msg : initializeMessageList()) {
            //Check if the message exists
            Optional<Message> message = this.locales.getMessage(msg.getKey());

            //Check if the passed message as parameters
            if (message.isPresent() && !msg.getArguments().isEmpty()) {
                message.get().addArguments(msg.getArguments());
            } else if (!message.isPresent()) {
                this.locales.addMessage(msg);
            }
        }
    }

    private PageTexture initializePageTexture() {

        Optional<Integer> maxPageSize = locales.getInteger(Config.CHAT_PAGINATION_MAX_PAGE_SIZE.getPath());
        String nextButton = locales.getString(Config.CHAT_PAGINATION_NEXT_BUTTON.getPath());
        String backButton = locales.getString(Config.CHAT_PAGINATION_BACK_BUTTON.getPath());
        String endMessage = locales.getString(Config.CHAT_PAGINATION_START_MESSAGE.getPath());
        String startMessage = locales.getString(Config.CHAT_PAGINATION_START_MESSAGE.getPath());
        String noLineFound = locales.getString(Config.CHAT_PAGINATION_NO_LINE_FOUND.getPath());
        String onePageStartMessage = locales.getString(Config.ONE_PAGE_START_MESSAGE.getPath());
        String onePageEndMessage = locales.getString(Config.ONE_PAGE_END_MESSAGE.getPath());

        return maxPageSize
                .map(integer -> new PageTexture(nextButton, backButton, endMessage, startMessage, noLineFound, integer, onePageStartMessage, onePageEndMessage))
                .orElse(null);

    }

    private List<Message> internalInitializeMessageList() {
        List<Message> pathList = new ArrayList<>();
        for (Config msg : Config.class.getEnumConstants()) {
            pathList.add(msg.getMessage());
        }
        return pathList;
    }

    public APIFireLibrary getAPIFireLibrary() {
        return APIFireLibrary.getApiFireLibrary();
    }

    protected abstract void onStart();

    protected abstract List<Message> initializeMessageList();

    protected abstract List<Command> initializeCommands();

    protected abstract List<PluginFile> initializeConfigs();

    protected abstract void initializeCaches();

    protected abstract void initializeDatabaseUtility();

    protected abstract void initializeListeners();

    protected abstract Optional<CommandNode> initializeMainNode();
}
