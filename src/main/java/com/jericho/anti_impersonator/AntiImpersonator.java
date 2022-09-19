package com.jericho.anti_impersonator;

import com.jericho.anti_impersonator.listeners.EventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;

import static com.jericho.anti_impersonator.libraries.JsonParser.getToken;

public class AntiImpersonator {

    private final ShardManager shardManager;

    public static void print(String message) {
        System.out.println(message);
    }


    /**
     * Constructor for the AntiImpersonator class.
     * Loads environment variables and builds the bot shard manager.
     *
     * @throws LoginException If the token is invalid.
     */
    public AntiImpersonator() throws LoginException, IOException {

        String token = String.valueOf(getToken());

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Watching you"));

        // enable intents
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);
        shardManager = builder.build();

        // Register listeners:
        shardManager.addEventListener(new EventListener());
    }

    /**
     * Returns the shard manager for the bot.
     *
     * @return the ShardManager instance for the bot.
     */
    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) throws IOException {
        try {
            AntiImpersonator bot = new AntiImpersonator();
        } catch (LoginException e) {
            print("ERROR: Provided bot token is invalid");
        }
    }
}
