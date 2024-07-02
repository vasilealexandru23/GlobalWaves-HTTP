package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.globalwaves.httpserver.audiocollection.AudioCollection;
import com.globalwaves.httpserver.audiocollection.Playlist;
import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;
import com.globalwaves.httpserver.users.UserNormal;


public final class FollowCommand extends Command implements CommandRunner {
    public FollowCommand(final String command, final String username,
            final Integer timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", checkFollow(database, getUsername()));
        return output;
    }

    private String checkFollow(final Database database, final String username) {
        String noSELECT = "Please select a source before following or unfollowing.";
        String noPLAYLIST = "The selected source is not a playlist.";

        /* Get the guaranteed normal user. */
        UserNormal user = ((UserNormal) database.findMyUser(username));

        /* Check if the user is online. */
        if (!user.isOnline()) {
            return username + " is offline.";
        }

        MusicPlayer player = user.getMusicPlayer();

        /* Check by select. */
        if (player.getUser().getLastSelect() == -1 || player.getSelectedTrack() == null) {
            return noSELECT;
        }

        /* Check by playlist. */
        if (player.getSelectedTrack().getType() != AudioCollection.AudioType.PLAYLIST) {
            return noPLAYLIST;
        }

        return ((Playlist) player.getSelectedTrack()).followPlaylist(player);
    }
}
