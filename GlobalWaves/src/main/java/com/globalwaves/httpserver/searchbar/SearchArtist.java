package com.globalwaves.httpserver.searchbar;

import com.globalwaves.httpserver.users.UserArtist;
import com.globalwaves.httpserver.users.UserTypes;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class SearchArtist extends Search {
	private ArrayList<UserArtist> results;

	public void setResults(final ArrayList<UserArtist> results) {
		this.results = results;
	}

	/**
	 * Function that searches for an artist.
	 * @param allUsersCreated           all users created
	 * @param myFilters                 the filters applied
	 */
	public void searchArtist(final ArrayList<UserTypes> allUsersCreated, final Filters myFilters) {
		results = new ArrayList<>();
		for (UserTypes user : allUsersCreated) {
			if (user.getUserType() == UserTypes.UserType.ARTIST
					&& user.getUsername().startsWith(myFilters.getName())) {
				results.add((UserArtist) user);
				if (results.size() == maxSearch) {
					break;
				}
			}
		}
	}

	/**
	 * Function that returns an artist from the results.
	 * @param selectIndex        the index of the artist
	 * @return                   the artist
	 */
	public UserArtist getArtist(final int selectIndex) {
		if (selectIndex >= results.size()) {
			return null;
		}
		return results.get(selectIndex);
	}
}
