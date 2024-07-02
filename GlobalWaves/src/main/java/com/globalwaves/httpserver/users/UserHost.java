package com.globalwaves.httpserver.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.audiocollection.AudioCollection;
import com.globalwaves.httpserver.audiocollection.Episode;
import com.globalwaves.httpserver.audiocollection.Podcast;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.pages.HostPage;
import lombok.Getter;

import java.util.ArrayList;

public final class UserHost extends UserTypes {
	public UserHost(final String username, final int age, final String city) {
		super(username, age, city);
		setUserType(UserType.HOST);
	}

	public final class Announcement {
		@Getter
		private String nameAnnouncement;
		@Getter
		private String description;

		Announcement(final String nameAnnouncement, final String description) {
			this.nameAnnouncement = nameAnnouncement;
			this.description = description;
		}

		public void setMessage(final String nameAnnouncement) {
			this.nameAnnouncement = nameAnnouncement;
		}

		public void setDescription(final String description) {
			this.description = description;
		}
	}

	@Getter
	private ArrayList<Announcement> announcements = new ArrayList<>();
	@Getter
	private ArrayList<Podcast> podcasts = new ArrayList<>();
	@Getter
	private HostPage hostPage = new HostPage(podcasts, announcements);

	public void setAnnouncements(final ArrayList<Announcement> announcements) {
		this.announcements = announcements;
	}

	/**
	 * Function that creates a new announcement and adds to the host's page.
	 */
	public void addAnnouncement(final String name, final String description) {
		announcements.add(new Announcement(name, description));
	}

	/**
	 * Funnction that puts in ouput the host's podcasts.
	 */
	public void showPodcasts(final ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();

		for (Podcast podcast : podcasts) {
			/* Add name. */
			ObjectNode newResult = objectMapper.createObjectNode();
			newResult.put("name", podcast.getName());

			/* Add episodes. */
			ArrayNode episodeOut = objectMapper.createArrayNode();
			for (Episode episode : podcast.getEpisodes()) {
				episodeOut.add(episode.getName());
			}

			/* Add to ouput. */
			newResult.put("episodes", episodeOut);
			output.add(newResult);
		}
	}

	@Override
	public String printCurrentPage() {
		return hostPage.printPage();
	}

	@Override
	protected boolean canBeRemoved(final Database database) {
		/* Check if other users interact with host's podcasts. */
		for (UserTypes user : database.getAllUsersCreated()) {
			if (user.getUserType() == UserType.USER) {

				UserNormal myUser = ((UserNormal) user);
				if (myUser.getSelectedPage() != null) {
					if (myUser.getSelectedPage() == this.hostPage) {
						return false;
					}
				}

				if (myUser.getMusicPlayer().getPlayback() == null) {
					continue;
				}

				myUser.getMusicPlayer().getPlayback().checkPlayback();
				AudioCollection currTrack = myUser.getMusicPlayer().getPlayback().getCurrTrack();
				if (currTrack == null) {
					continue;
				}

				if (currTrack.getType() == AudioCollection.AudioType.PODCAST) {
					Podcast currPodcast = (Podcast) currTrack;
					if (currPodcast.getOwner().equals(this.getUsername())) {
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public String removeUser(final Database database) {
		if (!canBeRemoved(database)) {
			return getUsername() + " can't be deleted.";
		}

		for (UserTypes user : database.getAllUsersCreated()) {
			if (user.getUserType() == UserType.USER) {

				UserNormal myUser = ((UserNormal) user);
				if (myUser.getSelectedPage() != null) {
					if (myUser.getSelectedPage() == this.hostPage) {
						myUser.setSelectedPage(null);
					}
				}
			}
		}

		for (Podcast podcast : podcasts) {
			database.getAllPodcastsCreated().remove(podcast);
		}

		database.getAllUsersCreated().remove(this);

		return getUsername() + " was successfully deleted.";
	}
}
