package de.codelix.firsttimespawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;

public class FirstTimeSpawn extends JavaPlugin implements Listener {
    public static FirstTimeSpawn INSTANCE;
    private DBConfig dbConfig;
    private Location spawnLocation;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.saveDefaultConfig();

        ConfigurationSection db = getConfig().getConfigurationSection("mysql");
        if (db == null) {
            throw new IllegalStateException("Failed to load config");
        }

        String host = db.getString("host");
        String port = db.getString("port");
        String user = db.getString("user");
        String password = db.getString("password");
        String database = db.getString("database");
        if (host == null) {
            throw new IllegalStateException("Failed to load config: Host missing in db section");
        }
        if (port == null) {
            throw new IllegalStateException("Failed to load config: Host missing in db section");
        }
        if (user == null) {
            throw new IllegalStateException("Failed to load config: Host missing in db section");
        }
        if (password == null) {
            throw new IllegalStateException("Failed to load config: Host missing in db section");
        }
        if (database == null) {
            throw new IllegalStateException("Failed to load config: Host missing in db section");
        }

        this.dbConfig = new DBConfig("jdbc:mysql://"+host+":"+port+"/" + database + "?autoReconnect=true", user, password);
        try {
            this.createTable();
            this.spawnLocation = this.loadSpawnLocation();
        } catch (SQLException e) {
            throw new RuntimeException("Could not connect to Database", e);
        }

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
    }

    private Location loadSpawnLocation() throws SQLException {
        try(PreparedStatement stmt = this.getConnection().prepareStatement("""
            SELECT * FROM spawn_location;
        """)) {
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next()) {
                this.getLogger().warning("Could not load spawn Location from Database. Did you already set one?");
                return null;
            }
            UUID uuid = UUID.fromString(resultSet.getString("world_uuid"));
            double x = resultSet.getDouble("x");
            double y = resultSet.getDouble("y");
            double z = resultSet.getDouble("z");
            float yaw = resultSet.getFloat("yaw");
            float pitch = resultSet.getFloat("pitch");
            World world = this.getServer().getWorld(uuid);
            if (world == null) {
                this.getLogger().warning("Invalid world uuid stored in spawn location");
                return null;
            }
            return new Location(world, x, y, z, yaw, pitch);
        }
    }

    public void storeSpawnLocation(Location location) throws SQLException {
        try(PreparedStatement stmt = this.getConnection().prepareStatement("""
            INSERT INTO spawn_location (world_uuid, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE x=?, y=?, z=?, yaw=?, pitch=?;
        """)) {
            UUID worldUUID = this.spawnLocation.getWorld().getUID();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            float yaw = location.getYaw();
            float pitch = location.getPitch();
            stmt.setString(1, worldUUID.toString());
            stmt.setDouble(2, x);
            stmt.setDouble(3, y);
            stmt.setDouble(4, z);
            stmt.setFloat(5, yaw);
            stmt.setFloat(6, pitch);
            stmt.setDouble(7, x);
            stmt.setDouble(8, y);
            stmt.setDouble(9, z);
            stmt.setFloat(10, yaw);
            stmt.setFloat(11, pitch);
            stmt.execute();
            this.spawnLocation = location;
        }
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean firstTime = !player.hasPlayedBefore();
        if (firstTime && this.spawnLocation != null) {
            player.teleport(this.spawnLocation);
        }
    }

    private void createTable() throws SQLException {
        try(PreparedStatement stmt = this.getConnection().prepareStatement("""
            CREATE TABLE IF NOT EXISTS spawn_location (
                world_uuid VARCHAR(36) NOT NULL PRIMARY KEY,
                x DOUBLE NOT NULL,
                y DOUBLE NOT NULL,
                z DOUBLE NOT NULL,
                yaw FLOAT NOT NULL,
                pitch FLOAT NOT NULL
            );
        """)) {
            stmt.execute();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbConfig.getUrl(), this.dbConfig.getUsername(), this.dbConfig.getPassword());
    }

    @Override
    public void onDisable() {

    }
}