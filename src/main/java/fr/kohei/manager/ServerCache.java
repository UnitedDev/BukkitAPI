package fr.kohei.manager;

import fr.kohei.manager.server.CTFServer;
import fr.kohei.manager.server.LobbyServer;
import fr.kohei.manager.server.UHCServer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class ServerCache {

    private final JavaPlugin plugin;
    private ConcurrentHashMap<UUID, String> players;
    private final HashMap<Integer, UHCServer> uhcServers;
    private final HashMap<Integer, LobbyServer> lobbyServers;
    private final HashMap<Integer, CTFServer> ctfServers;

    public ServerCache(JavaPlugin plugin) {
        this.plugin = plugin;

        this.players = new ConcurrentHashMap<>();
        this.uhcServers = new HashMap<>();
        this.lobbyServers = new HashMap<>();
        this.ctfServers = new HashMap<>();
    }

    public void updateUhcServer(UHCServer server) {
        this.uhcServers.put(server.getPort(), server);
    }

    public void updateLobbyServer(LobbyServer server) {
        this.lobbyServers.put(server.getPort(), server);
    }

    public void updateCTFServer(CTFServer server) {
        this.ctfServers.put(server.getPort(), server);
    }

    public void attemptDeletePort(int port) {
        this.uhcServers.remove(port);
        this.lobbyServers.remove(port);
        this.ctfServers.remove(port);
    }
}
