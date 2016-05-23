package richbar.com.github.commandplot.util;

import java.io.File;
 
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
 
public class CustomConfig {
       
        private String configName;
        private File configFile;
        private FileConfiguration config;
        private Plugin plugin;
       
        public CustomConfig(Plugin plugin, String configName) {
                this.plugin = plugin;
                this.configName = configName;
                createIfNoExist();
        }
       
       
        public File getConfigFile() {
                return this.configFile;
        }
       
        public String getConfigName() {
                return this.configName;
        }
       
        public boolean doesConfigExist() {
                if(getConfigFile() == null) {
                        return false;
                }
                return getConfigFile().exists();
        }
       
       
        public boolean createIfNoExist() {
                configFile = new File(this.plugin.getDataFolder(), this.configName.replace("/", " + " + File.separator + " + "));
                if(!this.configFile.exists()) {
                        if(this.plugin.getResource(configName) != null) {
                                this.plugin.saveResource(configName, false);
                        }
                        reloadConfig();
                        return true;
                }
                reloadConfig();
                return false;
        }
       
       
        public void reloadConfig() {
                this.configFile = new File(this.plugin.getDataFolder(), this.configName);
                this.config = YamlConfiguration.loadConfiguration(this.configFile);
        }
       
        public boolean saveConfig() {
                if(config != null && configFile != null) {
                        try {
                                config.save(configFile);
                        } catch (Exception ex) {}
                }
                return false;
        }
       
        public FileConfiguration getConfig() {
                reloadConfig();
                return this.config;
        }
       
}