package com.globalwaves.httpserver.pages;

import com.globalwaves.httpserver.audiocollection.Episode;
import com.globalwaves.httpserver.audiocollection.Podcast;
import com.globalwaves.httpserver.users.UserHost;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class HostPage extends UserPage {
	private ArrayList<Podcast> podcasts;
	private ArrayList<UserHost.Announcement> announcements = new ArrayList<>();

	public HostPage(final ArrayList<Podcast> podcasts,
					final ArrayList<UserHost.Announcement> announcements) {
		this.podcasts = podcasts;
		this.announcements = announcements;
	}

	/**
	 * Function that prints the current page.
	 */
	public String printCurrentPage() {
		String result = "Podcasts:\n\t[";

		/* Add podcasts and episodes. */
		boolean firstPodcast = true;
		for (Podcast podcast : podcasts) {
			if (!firstPodcast) {
				result += "]\n, ";
			}
			result += podcast.getName() + ":\n\t[";
			firstPodcast = false;
			boolean firstEpisode = true;
			for (Episode episode : podcast.getEpisodes()) {
				if (!firstEpisode) {
					result += ", ";
				}
				result += episode.getName() + " - " + episode.getDescription();
				firstEpisode = false;
			}
		}

		result += "]\n]\n\nAnnouncements:\n\t[";

		/* Add announcements. */
		boolean firstAnnouncement = true;
		for (UserHost.Announcement announcement : announcements) {
			if (!firstAnnouncement) {
				result += ", ";
			}
			result += announcement.getNameAnnouncement() + ":\n\t" + announcement.getDescription();
			firstAnnouncement = false;
		}

		result += "\n]";

		return result;
	}
}