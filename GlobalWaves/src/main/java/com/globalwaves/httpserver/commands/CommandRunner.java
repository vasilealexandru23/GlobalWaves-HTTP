package com.globalwaves.httpserver.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.globalwaves.httpserver.core.Database;

/* Command pattern. */
public interface CommandRunner {
    /**
     * Execute the command.
     * @param database      The database.
     * @return              The command output.
     */
    ObjectNode execute(Database database);
}
