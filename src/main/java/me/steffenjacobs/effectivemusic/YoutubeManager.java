package me.steffenjacobs.effectivemusic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoFileInfo;
import com.github.axet.vget.info.VideoInfo;

/** @author Steffen Jacobs */

@Component
public class YoutubeManager {

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

		System.out.println("Title: " + videoinfo.getTitle());
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
}
