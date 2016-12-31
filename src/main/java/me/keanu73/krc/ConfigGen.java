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
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Marker;
import org.spongepowered.api.config.DefaultConfig;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Keanu on 29/12/2016.
 */
public class ConfigGen {

    @Inject
    @DefaultConfig(sharedRoot = true)
    public static Path config = FileSystems.getDefault().getPath("krc.conf"); // The actual path for the config, we don't want it to be null

    @Inject
    @DefaultConfig(sharedRoot = true)
    public static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(config).build(); // This loads the config, basically

    public static CommentedConfigurationNode rootNode = loader.createEmptyNode(ConfigurationOptions.defaults()); // Create an empty node.

    public static void createConfig() {
        try {
            if (Files.exists(config)) {
                rootNode = loader.load(); // Load the node anyway.
                KRC.logger.info("Configuration file loaded.");
            } else if (!Files.exists(config)) {
                KRC.logger.info("Creating configuration file...");
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
                KRC.logger.info("Configuration file completed.");
                loader.save(rootNode);
            }
        } catch (IOException ex) {
            KRC.logger.error((Marker) ex, "Whoops! Ran into an error."); // Shoot, we ran into an IOException.
            ex.printStackTrace();
        }
    }
}
