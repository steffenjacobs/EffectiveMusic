package me.steffenjacobs.effectivemusic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.util.Pair;

/** @author Steffen Jacobs */

@Component
public class PlaylistStorageService {

	private static final Logger LOG = LoggerFactory.getLogger(PlaylistStorageService.class);

	public void savePlaylist(String path, List<TrackMetadata> playlist) throws IOException {
		File f = new File(path);

		StringBuilder sb = new StringBuilder("#EXTM3U");
		for (TrackMetadata track : playlist) {

			sb.append("\n\n#EXTINF:");
			sb.append(track.getTrackDTO().getLength() / 1000);
			sb.append(", ");
			sb.append(track.getTrackDTO().getArtist());
			sb.append(" - ");
			sb.append(track.getTrackDTO().getTitle());
			sb.append("\n");
			sb.append(track.getPath());
		}
		FileUtils.writeStringToFile(f, sb.toString(), Charset.forName("UTF-8"));
	}

	public Pair<String, List<String>> loadPlaylist(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			throw new NoSuchFileException(path);
		}

		System.setProperty("user.dir", file.getParent());
		final String title = FilenameUtils.removeExtension(file.getName());
		try (Stream<String> stream = Files.lines(file.toPath(), Charset.forName("UTF-8"))) {
			List<String> files = stream.filter(line -> !line.startsWith("#") && !line.isEmpty()).collect(Collectors.toList());
			return new Pair<>(title, files);
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
			throw e;
		}
	}

}
