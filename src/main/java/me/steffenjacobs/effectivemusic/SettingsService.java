package me.steffenjacobs.effectivemusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.domain.SettingsDTO;

/** @author Steffen Jacobs */

@Component
public class SettingsService {

	private static final Logger LOG = LoggerFactory.getLogger(SettingsService.class);
	private static final File SETTINGS_FILE = new File("./settings.properties");

	private static final String SETTING_PLAYLIST_LOCATION = "playlistDefaultLocation";
	private static final String SETTING_PLAYLIST_LOCATION_DEFAULT = "./Playlists/";

	private Properties properties;

	public void loadSettings() {
		properties = new Properties();
		try {

			if (!SETTINGS_FILE.exists()) {
				if (SETTINGS_FILE.createNewFile()) {
					LOG.info("Created new settings file.");
				} else {
					LOG.error("Could not create settings file");
					return;
				}
			}
			properties.load(new FileInputStream(SETTINGS_FILE));
			LOG.info("Laoded settings from {}.", SETTINGS_FILE);

		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void saveSettings() {
		try {
			properties.store(new FileOutputStream(SETTINGS_FILE), null);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public SettingsDTO getSettings() {
		return new SettingsDTO(properties.getProperty(SETTING_PLAYLIST_LOCATION, SETTING_PLAYLIST_LOCATION_DEFAULT));
	}

	public void setPlaylistDefaultLocation(String path) {
		properties.setProperty(SETTING_PLAYLIST_LOCATION, path);
		saveSettings();
	}

	public String getPlaylistDefaultLocation() {
		return properties.getProperty(SETTING_PLAYLIST_LOCATION, SETTING_PLAYLIST_LOCATION_DEFAULT);
	}
}
