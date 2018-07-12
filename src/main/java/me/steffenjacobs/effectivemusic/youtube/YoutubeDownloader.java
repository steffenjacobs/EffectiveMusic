package me.steffenjacobs.effectivemusic.youtube;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoFileInfo;
import com.github.axet.vget.info.VideoInfo;
import com.github.axet.vget.vhs.VimeoInfo;
import com.github.axet.vget.vhs.YouTubeInfo;
import com.github.axet.wget.DirectMultipart;
import com.github.axet.wget.SpeedInfo;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.DownloadInfo.Part;
import com.github.axet.wget.info.URLInfo.States;
import com.github.axet.wget.info.ex.DownloadInterruptedError;

import me.steffenjacobs.effectivemusic.audio.VLCMediaPlayerAdapter;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;

/** @author Steffen Jacobs */

@Component
public class YoutubeDownloader {

	@Autowired
	private Mp4ToMp3Converter mp4ToMp3Converter;

	@Autowired
	VLCMediaPlayerAdapter vlcPlayer;

	private String downloadYoutube(String url) {
		DirectMultipart.THREAD_COUNT = 8;

		try {
			final AtomicBoolean stop = new AtomicBoolean(false);

			File path = new File("C:\\Temp\\");
			URL web = new URL(url);

			VGetParser user = VGet.parser(web);

			VideoInfo videoinfo = user.info(web);

			VGet v = new VGet(videoinfo, path);

			VGetStatus notify = new VGetStatus(videoinfo);

			v.extract(user, stop, notify);

			System.out.println("Title: " + videoinfo.getTitle());
			List<VideoFileInfo> list = videoinfo.getInfo();
			if (list != null) {
				for (VideoFileInfo d : list) {
					if (d.getContentType().equals("audio/webm")) {

						FileUtils.copyURLToFile(d.getSource(), new File("C:\\Temp\\" + videoinfo.getTitle() + ".webm"));
					}
				}
			}

			// v.download(user, stop, notify);

			return videoinfo.getTitle();
		} catch (DownloadInterruptedError e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void playYoutube(String path) {
		String title = downloadYoutube(path);
		String newPath = mp4ToMp3Converter.convertToMp3("C:\\Temp\\" + title + ".webm");
		vlcPlayer.playAudio(new TrackMetadata(newPath));
	}

	static class VGetStatus implements Runnable {
		VideoInfo videoinfo;
		long last;

		Map<VideoFileInfo, SpeedInfo> map = new HashMap<VideoFileInfo, SpeedInfo>();

		public VGetStatus(VideoInfo i) {
			this.videoinfo = i;
		}

		public SpeedInfo getSpeedInfo(VideoFileInfo dinfo) {
			SpeedInfo speedInfo = map.get(dinfo);
			if (speedInfo == null) {
				speedInfo = new SpeedInfo();
				speedInfo.start(dinfo.getCount());
				map.put(dinfo, speedInfo);
			}
			return speedInfo;
		}

		@Override
		public void run() {
			List<VideoFileInfo> dinfoList = videoinfo.getInfo();

			// notify app or save download state
			// you can extract information from DownloadInfo info;
			switch (videoinfo.getState()) {
			case EXTRACTING:
			case EXTRACTING_DONE:
			case DONE:
				if (videoinfo instanceof YouTubeInfo) {
					YouTubeInfo i = (YouTubeInfo) videoinfo;
					System.out.println(videoinfo.getState() + " " + i.getVideoQuality());
				} else if (videoinfo instanceof VimeoInfo) {
					VimeoInfo i = (VimeoInfo) videoinfo;
					System.out.println(videoinfo.getState() + " " + i.getVideoQuality());
				} else {
					System.out.println("downloading unknown quality");
				}
				for (VideoFileInfo d : videoinfo.getInfo()) {
					SpeedInfo speedInfo = getSpeedInfo(d);
					speedInfo.end(d.getCount());
					System.out.println(String.format("file:%d - %s (%s)", dinfoList.indexOf(d), d.targetFile, formatSpeed(speedInfo.getAverageSpeed())));
				}
				break;
			case ERROR:
				System.out.println(videoinfo.getState() + " " + videoinfo.getDelay());

				if (dinfoList != null) {
					for (DownloadInfo dinfo : dinfoList) {
						System.out.println("file:" + dinfoList.indexOf(dinfo) + " - " + dinfo.getException() + " delay:" + dinfo.getDelay());
					}
				}
				break;
			case RETRYING:
				System.out.println(videoinfo.getState() + " " + videoinfo.getDelay());

				if (dinfoList != null) {
					for (DownloadInfo dinfo : dinfoList) {
						System.out.println("file:" + dinfoList.indexOf(dinfo) + " - " + dinfo.getState() + " " + dinfo.getException() + " delay:" + dinfo.getDelay());
					}
				}
				break;
			case DOWNLOADING:
				long now = System.currentTimeMillis();
				if (now - 1000 > last) {
					last = now;

					String parts = "";

					for (VideoFileInfo dinfo : dinfoList) {
						SpeedInfo speedInfo = getSpeedInfo(dinfo);
						speedInfo.step(dinfo.getCount());

						List<Part> pp = dinfo.getParts();
						if (pp != null) {
							// multipart download
							for (Part p : pp) {
								if (p.getState().equals(States.DOWNLOADING)) {
									parts += String.format("part#%d(%.2f) ", p.getNumber(), p.getCount() / (float) p.getLength());
								}
							}
						}
						System.out.println(String.format("file:%d - %s %.2f %s (%s)", dinfoList.indexOf(dinfo), videoinfo.getState(), dinfo.getCount() / (float) dinfo.getLength(),
								parts, formatSpeed(speedInfo.getCurrentSpeed())));
					}
				}
				break;
			default:
				break;
			}
		}
	}

	public static String formatSpeed(long s) {
		if (s > 0.1 * 1024 * 1024 * 1024) {
			float f = s / 1024f / 1024f / 1024f;
			return String.format("%.1f GB/s", f);
		} else if (s > 0.1 * 1024 * 1024) {
			float f = s / 1024f / 1024f;
			return String.format("%.1f MB/s", f);
		} else {
			float f = s / 1024f;
			return String.format("%.1f kb/s", f);
		}
	}

}
