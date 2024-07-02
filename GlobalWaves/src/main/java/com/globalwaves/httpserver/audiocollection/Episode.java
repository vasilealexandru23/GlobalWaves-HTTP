package com.globalwaves.httpserver.audiocollection;

import lombok.Getter;

@Getter
public final class Episode {
	private String name;
	private Integer duration;
	private String description;
	private int timeWatched;

	public Episode() {
	}

	public Episode(final String name, final Integer duration, final String description) {
		this.name = name;
		this.duration = duration;
		this.description = description;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDuration(final Integer duration) {
		this.duration = duration;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setTimeWatched(final int time) {
		this.timeWatched = time;
	}
}
