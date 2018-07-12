package me.steffenjacobs.effectivemusic.youtube;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoFileInfo;
import com.github.axet.vget.info.VideoInfo;

import me.steffenjacobs.effectivemusic.audio.VLCMediaPlayerAdapter;

/** @author Steffen Jacobs */

@Component
public class YoutubeManager {

	private static final Logger LOG = LoggerFactory.getLogger(YoutubeManager.class);

	@Autowired
	VLCMediaPlayerAdapter vlcPlayer;

	public String playYoutube(String url) throws MalformedURLException {

		final AtomicBoolean stop = new AtomicBoolean(false);

		File path = new File("C:\\Temp\\");
		URL web = new URL(url);

		VGetParser user = VGet.parser(web);

		VideoInfo videoinfo = user.info(web);

		VGet v = new VGet(videoinfo, path);
		v.extract(user, stop, new Runnable() {
			@Override
			public void run() {
			}
		});

		LOG.info("Title: {}", videoinfo.getTitle());
		List<VideoFileInfo> list = videoinfo.getInfo();
		if (list != null) {
			for (VideoFileInfo d : list) {
				if (d.getContentType().equals("audio/webm")) {
					vlcPlayer.playFromUrl(d.getSource());
				}
			}
		}

		return videoinfo.getTitle();
	}

	public URL getPlaybackUrl(String videoUrl) throws MalformedURLException {
		URL web = new URL(videoUrl);

		VGetParser user = VGet.parser(web);

		VideoInfo videoinfo = user.info(web);

		VGet v = new VGet(videoinfo, new File("C:\\Temp\\"));
		v.extract(user, new AtomicBoolean(false), new Runnable() {
			@Override
			public void run() {
			}
		});

		List<VideoFileInfo> list = videoinfo.getInfo();
		if (list != null) {
			for (VideoFileInfo d : list) {
				if (d.getContentType().equals("audio/webm")) {
					return d.getSource();
				}
			}
		}
		throw new MalformedURLException("Bad video URL");
	}
}
