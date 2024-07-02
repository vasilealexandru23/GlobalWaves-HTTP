package com.globalwaves.httpserver.searchbar;

import com.globalwaves.httpserver.audiocollection.AudioCollection;
import com.globalwaves.httpserver.audiocollection.Playlist;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class SearchPlaylist extends Search {
	private ArrayList<Playlist> results;

	public void setResults(final ArrayList<Playlist> results) {
		this.results = results;
	}

	/**
	 * Function that searches for a playlist.
	 * @param who                   the user that searches
	 * @param createdPlaylists      the playlists created
	 * @param myfilters             the filters applied
	 */
	public void searchTrack(final String who,
							final ArrayList<Playlist> createdPlaylists,
							final Filters myfilters) {

		results = new ArrayList<>();

		/* Check public playlists. */
		for (Playlist playlist : createdPlaylists) {
			/* Check if this playlist is not visibly by who. */
			if (!playlist.isVisibility() && !playlist.getOwner().equals(who)) {
				continue;
			}

			boolean playlistworks = myfilters.getName() == null
					|| playlist.getName().startsWith(myfilters.getName());
			/* Check by name. */

			/* Check by owner. */
			if (myfilters.getOwner() != null
					&& !playlist.getOwner().equals(myfilters.getOwner())) {
				playlistworks = false;
			}

			if (playlistworks) {
				results.add(playlist);
				if (results.size() == maxSearch) {
					break;
				}
			}

		}
	}

	@Override
	public AudioCollection getSelect(final int selectIndex) {
		if (selectIndex >= results.size()) {
			return null;
		}
		return results.get(selectIndex);
	}
}
