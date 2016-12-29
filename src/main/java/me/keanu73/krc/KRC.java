/*
 * Copyright (c) 2016 Keanu (Keanu73) <keanu@keanu73.me>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.keanu73.krc;

import com.google.inject.Inject;
import me.keanu73.krc.commands.CommandHandler;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Created by Keanu on 25/12/2016.
 */
@Plugin(id = KRC.id, name = KRC.name, version = KRC.version, description = KRC.desc, url = KRC.url, authors = {"Keanu73"})
public class KRC {
    public static final String id = "krc";
    public static final String name = "KRC";
    public static final String version = "0.0.1";
    public static final String desc = "IRC plugin for Sponge";
    public static final String url = "https://keanu73.me";

    @Inject
    public Logger logger;

    @Listener
    public void GameStart(GameStartedServerEvent event) {
        //logger.info("KRC is starting up the IRC bot...");
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    public Path config = FileSystems.getDefault().getPath("krc.conf"); // The actual path for the config, we don't want it to be null

    @Inject
    @DefaultConfig(sharedRoot = true)
    public ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(config).build(); // This loads the config, basically

    public CommentedConfigurationNode rootNode = loader.createEmptyNode(ConfigurationOptions.defaults()); // Create an empty node.

    // Getting the settings so we can use it for the IRC bot
    public String botname = rootNode.getNode("config", "BotName").getString();
    public String server = rootNode.getNode("config", "IRCServer").getString();
    public String channel = rootNode.getNode("config", "Channel").getString();
    public Boolean nickserv = rootNode.getNode("config", "NickServ").getBoolean();
    public String nickservnick = rootNode.getNode("config", "NickServNick").getString();
    public String nickservpassword = rootNode.getNode("config", "NickServPassword").getString();

    @Listener
    public void Init(GameInitializationEvent e) {
        try {
            if (!Files.exists(config)) { // If the config doesn't exist, then...
                logger.info("Creating configuration file...");
                Files.createFile(config); // Create the config file.
                rootNode = loader.load(); // Load the node.
                rootNode.getNode("config", "BotName").setValue("MinecraftServerBot"); // Initialize the node settings.
                rootNode.getNode("config", "BotName").setComment("The nickname for your bot.");
                rootNode.getNode("config", "IRCServer").setValue("irc.freenode.net");
                rootNode.getNode("config", "IRCServer").setComment("The IRC server it will connect to.");
                rootNode.getNode("config", "Channel").setValue("#minecraft");
                rootNode.getNode("config", "Channel").setComment("The channel it will join.");
                rootNode.getNode("config", "NickServ").setValue(false);
                rootNode.getNode("config", "NickServ").setComment("If NickServ login is enabled or not. Default is false.");
                rootNode.getNode("config", "NickServNick").setValue("MinecraftServerBot");
                rootNode.getNode("config", "NickServNick").setComment("The nickname for your NickServ login.");
                rootNode.getNode("config", "NickServPassword").setValue("yournickservpasswordhere");
                rootNode.getNode("config", "NickServPassword").setComment("Put your NickServ password here.");
                logger.info("Configuration file completed.");
                loader.save(rootNode);
            }
         rootNode = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    Sponge.getCommandManager().register(this, CommandHandler.reload, "reload");
    }


}
