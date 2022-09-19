// Event Listener class
// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.anti_impersonator.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import javax.annotation.Nonnull;
import java.util.Arrays;

import static com.jericho.anti_impersonator.AntiImpersonator.print;
import static com.jericho.anti_impersonator.libraries.JsonParser.getWhitelistedNames;

public class EventListener extends ListenerAdapter {

    // get the whitelisted names from the settings.json file
    String[] whitelistedNames;

    {
        try {
            whitelistedNames = getWhitelistedNames();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        print("Guild ready: " + event.getGuild().getName());
        print("Bot name: " + event.getJDA().getSelfUser().getName());
    }

    public void privateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }

    @Override
    public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {

        User user = event.getUser();
        String oldNickname = user.getName();
        String newNickname = event.getNewNickname();

        // Check if the user has changed their nickname:
        if (newNickname != null) {

            // Check if member has higher or equal highest role than the bot:
            if (event.getMember().getRoles().get(0).getPosition() >= event.getGuild().getSelfMember().getRoles().get(0).getPosition()) {

                // Check if the new nickname is in the whitelist:
                if (Arrays.asList(whitelistedNames).contains(newNickname)) {
                    event.getGuild().modifyNickname(event.getMember(), oldNickname).queue();
                    privateMessage(user, "Your nickname has been changed to " + oldNickname + " because it is whitelisted.");
                }
            } else {
                print("ERROR: The bots highest role must be higher than the highest role of this member.");
            }
        }
    }
}