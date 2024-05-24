package omine.database;

import com.lib.utils.LocationSerializer;
import omine.objects.BrokenBlock;
import omine.runnables.BlockRegenTask;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class BlockDAO {

    private final String TABLE = "omine_blocks";
    private Logger logger;

    public BlockDAO(Logger logger) {
        this.logger = logger;
        createTable();
    }

    public void createTable() {
        CompletableFuture.runAsync(() -> {
                    String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                            "location VARCHAR(1000) NOT NULL PRIMARY KEY," +
                            "world VARCHAR(100) NOT NULL," +
                            "material VARCHAR(100) NOT NULL" +
                            ");";
                    try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                        statement.execute();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
        );
    }

    public void loadBrokenBlocksByWorld(String world, BlockRegenTask blockRegenTask) {
        CompletableFuture.runAsync(() -> {
                    String sql = "SELECT * FROM `" + TABLE + "` WHERE world = ?";
                    try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                        statement.setString(1, world);
                        ResultSet resultSet = statement.executeQuery();
                        while(resultSet.next()) {
                            Location location = LocationSerializer.deserialize(resultSet.getString(1));
                            BrokenBlock brokenBlock = new BrokenBlock(location, Material.matchMaterial(resultSet.getString(3)), 0, 0);
                            blockRegenTask.addBrokenBlock(location, brokenBlock);
                            delete(resultSet.getString(1));
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
        );
    }

    public void loadBrokenBlocks(BlockRegenTask blockRegenTask) {
        CompletableFuture.runAsync(() -> {
                    String sql = "SELECT * FROM `" + TABLE + "`";
                    try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                        ResultSet resultSet = statement.executeQuery();
                        while(resultSet.next()) {
                            Location location = LocationSerializer.deserialize(resultSet.getString(1));
                            BrokenBlock crop = new BrokenBlock(location, Material.matchMaterial(resultSet.getString(3)), 0, 0);
                            blockRegenTask.addBrokenBlock(location, crop);
                            delete(resultSet.getString(1));
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
        );
    }

    public void unloadBrokenBlocks(BlockRegenTask blockRegenTask) {
        blockRegenTask.getBrokenBlocks().forEach((location, block) -> {
            location.getChunk().load();
            location.getBlock().setType(block.getMaterial());
            location.getChunk().unload();
            String sql = "REPLACE INTO " + TABLE + " VALUES(?,?,?)";
            try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                statement.setString(1, LocationSerializer.serialize(location));
                statement.setString(2, location.getWorld().getName());
                statement.setString(3, block.getMaterial().name());
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void delete(String location) {
        CompletableFuture.runAsync(() -> {
                    String sql = "DELETE FROM " + TABLE + " WHERE location = ?";
                    try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(sql)) {
                        statement.setString(1, location);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
