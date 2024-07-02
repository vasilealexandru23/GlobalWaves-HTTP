package com.globalwaves.httpserver.pages;

import com.globalwaves.httpserver.audiocollection.Album;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class ArtistPage extends UserPage {
	@Getter
	public final class Event {
		private String name;
		private String date;
		private String description;

		public Event(final String name, final String date, final String description) {
			this.name = name;
			this.date = date;
			this.description = description;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public void setDate(final String date) {
			this.date = date;
		}

		public void setDescription(final String description) {
			this.description = description;
		}
	}

	public final class Merch {
		@Getter
		private String name;
		@Getter
		private Integer price;
		@Getter
		private String description;

		public Merch(final String name, final String description, final Integer price) {
			this.name = name;
			this.description = description;
			this.price = price;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public void setPrice(final Integer price) {
			this.price = price;
		}

		public void setDescription(final String description) {
			this.description = description;
		}
	}

	private ArrayList<Event> events = new ArrayList<>();
	private ArrayList<Merch> merchs = new ArrayList<>();
	private ArrayList<Album> albums;

	public ArtistPage(final ArrayList<Album> albums) {
		this.albums = albums;
	}

	@Override
	public String printCurrentPage() {
		StringBuilder result = new StringBuilder("Albums:\n\t[");

		/* Add albums. */
		boolean firstAlbum = true;
		for (Album album : albums) {
			if (firstAlbum) {
				result.append(album.getName());
				firstAlbum = false;
			} else {
				result.append(", ").append(album.getName());
			}
		}

		result.append("]\n\nMerch:\n\t[");

		/* Add merch. */
		boolean firstMerch = true;
		for (Merch merch : merchs) {
			if (firstMerch) {
				result.append(merch.getName());
				firstMerch = false;
				result.append(" - ").append(merch.getPrice()).append(":\n\t").append(merch.getDescription());
			} else {
				result.append(", ").append(merch.getName());
				result.append(" - ").append(merch.getPrice()).append(":\n\t").append(merch.getDescription());
			}
		}

		result.append("]\n\nEvents:\n\t[");

		/* Add events. */
		boolean firstEvent = true;
		for (Event event : events) {
			if (firstEvent) {
				result.append(event.getName());
				firstEvent = false;
				result.append(" - ").append(event.getDate()).append(":\n\t").append(event.getDescription());
			} else {
				result.append(", ").append(event.getName());
				result.append(" - ").append(event.getDate()).append(":\n\t").append(event.getDescription());
			}
		}

		result.append("]");
		return result.toString();
	}
}