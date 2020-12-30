package it.fireentity.library;

import it.fireentity.library.chatpaging.PageTexture;
import it.fireentity.library.chatpaging.PagesGroup;
import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.enumerations.Config;
import it.fireentity.library.events.EventManager;
import it.fireentity.library.inventories.PagesHandler;
import it.fireentity.library.listener.ClickManager;
import it.fireentity.library.listener.GuiClickListener;
import it.fireentity.library.listener.GuiCloseListener;
import it.fireentity.library.locales.Locales;
import it.fireentity.library.locales.Message;
import it.fireentity.library.locales.ReloadCommand;
import it.fireentity.library.storage.Cache;
import lombok.Getter;
import org.bukkit.ChatColor;
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
    private Cache<String, Command> commandsCache;
    private PagesHandler pagesHandler;
    private GuiClickListener guiClickListener;
    private GuiCloseListener guiCloseListener;
    private Locales locales;
    private EventManager eventManager;
    private PageTexture pageTexture;

    public Optional<PageTexture> getPageTexture() {
        return Optional.empty();
    }

    @Override
    public void onEnable() {
        locales = initializeLocales();
        eventManager = new EventManager();
        pagesHandler = new PagesHandler(this);
        commandsCache = new Cache<>();
        clickManager = initializeClickManager();
        guiClickListener = new GuiClickListener(this, pagesHandler, clickManager);
        guiCloseListener = new GuiCloseListener(this, pagesHandler);
        getCommand((mainNode = initializeMainCommandNode()).getKey()).setExecutor(mainNode);
        new ReloadCommand(this);
        initializePageTexture().ifPresent(page -> this.pageTexture = page);
        onStart();

        for (Message msg : this.locales.getMessages()) {
            if (!msg.getMessage().isPresent()) {
                getLogger().severe(this.getName() + ": " + ChatColor.RED + "{Missing locale at " + ChatColor.GOLD + msg.getKey() + ChatColor.RED + "}" + ChatColor.RESET);
                this.locales.writeIntoMissing(msg.getKey(), msg.getArguments());
            }
        }
    }

    public abstract CommandNode initializeMainCommandNode();

    public abstract void onStart();

    public APIFireLibrary getAPIFireLibrary() {
        return APIFireLibrary.getApiFireLibrary();
    }

    public ClickManager initializeClickManager() {
        int delay = Integer.parseInt(locales.getString(Config.CLICK_GUI_DELAY.getPath()));
        return new ClickManager(this, delay, getLocales().getString("click_delay_error"));
    }

    public void reloadLocale() {
        File locales = new File(this.getDataFolder(), "locales.json");
        File missingLocales = new File(this.getDataFolder(), "missingLocales.json");
        this.locales = new Locales(this, locales, missingLocales);
    }

    private List<Message> internalInitializeMessageList() {
        List<Message> pathList = new ArrayList<>();
        for (Config msg : Config.values()) {
            pathList.add(msg.getMessage());
        }
        return pathList;
    }

    private Optional<PageTexture> initializePageTexture() {

        Optional<Integer> maxPageSize = locales.getInteger(Config.CHAT_PAGINATION_MAX_PAGE_SIZE.getPath());
        String nextButton = locales.getString(Config.CHAT_PAGINATION_NEXT_BUTTON.getPath());
        String backButton = locales.getString(Config.CHAT_PAGINATION_BACK_BUTTON.getPath());
        String endMessage = locales.getString(Config.CHAT_PAGINATION_START_MESSAGE.getPath());
        String startMessage = locales.getString(Config.CHAT_PAGINATION_START_MESSAGE.getPath());
        String noLineFound = locales.getString(Config.CHAT_PAGINATION_NO_LINE_FOUND.getPath());
        String onePageStartMessage = locales.getString(Config.ONE_PAGE_START_MESSAGE.getPath());
        String onePageEndMessage = locales.getString(Config.ONE_PAGE_END_MESSAGE.getPath());

        return maxPageSize.map(integer -> new PageTexture(nextButton, backButton, endMessage, startMessage, noLineFound, integer, onePageStartMessage, onePageEndMessage));
    }

    public Locales initializeLocales() {
        File localesFiles = new File(this.getDataFolder(), "locales.json");
        File missingLocales = new File(this.getDataFolder(), "missingLocales.json");
        Locales locales = new Locales(this, localesFiles, missingLocales);
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
        return locales;
    }
}
