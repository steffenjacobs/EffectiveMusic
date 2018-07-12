package me.steffenjacobs.effectivemusic.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

/** @author Steffen Jacobs */
@Component
public class Mp4ToMp3Converter {

	public String convertToMp3(String path) {
		try {
			String line;

			// ffmpeg -i input.mp4 output.avi as it's on www.ffmpeg.org
			String newFilePath = path.substring(0, path.length() - 4) + "mp3";
			String cmd = "C:\\Temp\\ffmpeg.exe -i \"" + path + "\" \"" + newFilePath + "\"";
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			p.waitFor();
			System.out.println("Video converted successfully!");
			in.close();
			return newFilePath;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

}
