package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.globalwaves.httpserver.core.Database;
import com.globalwaves.httpserver.musicplayer.MusicPlayer;

public final class AddMerchCommand extends Command implements CommandRunner {
    private Integer price;
    private String nameMerch;
    private String description;

    public AddMerchCommand(final String command, final String username, final Integer timestamp,
            final Integer price, final String nameMerch, final String description) {
        super(command, username, timestamp);
        this.price = price;
        this.nameMerch = nameMerch;
        this.description = description;
    }

    @Override
    public ObjectNode execute(final Database database) {
        ObjectNode output = new ObjectMapper().createObjectNode();

        outputCommand(output);
        MusicPlayer.setTimestamp(getTimestamp());

        output.put("message", database.addMerch(getUsername(), price, nameMerch, description));
        return output;
    }
}
