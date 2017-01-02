/*
 * Copyright (c) 2017 Keanu (Keanu73) <keanu@keanu73.me>
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

package me.keanu73.krc.config;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
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
    public static Path config = FileSystems.getDefault().getPath("config", "krc.conf"); // The actual path for the config, we don't want it to be null

    @Inject
    private static Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    public static ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(config).build();

    public static CommentedConfigurationNode node;

    public static String botname;
    public static String server;
    public static String channel;
    public static Boolean nickserv;
    public static String nickservnick;
    public static String nickservpassword;
    public static Boolean connect;

    public static void createConfig() {
        try {
            if (Files.notExists(config)) {
                Asset asset = Sponge.getAssetManager().getAsset(Sponge.getPluginManager().getPlugin("krc").get(), "krc.conf").get();
                asset.copyToFile(config);
            } else if (Files.exists(config)) {
                node = loader.load();
                botname = node.getNode("irc", "BotName").getString();
                server = node.getNode("irc", "IRCServer").getString();
                channel = node.getNode("irc", "Channel").getString();
                nickserv = node.getNode("irc", "NickServ").getBoolean();
                nickservnick = node.getNode("irc", "NickServNick").getString();
                nickservpassword = node.getNode("irc", "NickServPassword").getString();
                connect = node.getNode("irc", "connect").getBoolean();
            }
        } catch (IOException ex) {
            logger.error("Whoops! We ran into an error. Contact Keanu73.", ex);
        }
    }
}
