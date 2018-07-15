package me.steffenjacobs.effectivemusic.domain;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/** @author Steffen Jacobs */
public class TrackDTO {

	private String artist, album, title, comment, year, track, disc_no, composer, artist_sort;
	private long length;

	public TrackDTO() {

	}

	public TrackDTO(Tag tag, long trackLength) {
		artist = tag.getFirst(FieldKey.ARTIST);
		album = tag.getFirst(FieldKey.ALBUM);
		title = tag.getFirst(FieldKey.TITLE);
		comment = tag.getFirst(FieldKey.COMMENT);
		year = tag.getFirst(FieldKey.YEAR);
		track = tag.getFirst(FieldKey.TRACK);
		disc_no = tag.getFirst(FieldKey.DISC_NO);
		composer = tag.getFirst(FieldKey.COMPOSER);
		artist_sort = tag.getFirst(FieldKey.ARTIST_SORT);
		length = trackLength;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public String getTitle() {
		return title;
	}

	public String getComment() {
		return comment;
	}

	public String getYear() {
		return year;
	}

	public String getTrack() {
		return track;
	}

	public String getDisc_no() {
		return disc_no;
	}

	public String getComposer() {
		return composer;
	}

	public String getArtist_sort() {
		return artist_sort;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getLength() {
		return length;
	}

}
