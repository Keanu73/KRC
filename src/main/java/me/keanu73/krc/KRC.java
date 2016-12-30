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
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = KRC.id, name = KRC.name, version = KRC.version, description = KRC.desc, url = KRC.url, authors = {"Keanu73"}) // Plugin annotation
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

    // Getting the settings so we can use it for the IRC bot
    public String botname = ConfigGen.rootNode.getNode("config", "BotName").getString();
    public String server = ConfigGen.rootNode.getNode("config", "IRCServer").getString();
    public String channel = ConfigGen.rootNode.getNode("config", "Channel").getString();
    public Boolean nickserv = ConfigGen.rootNode.getNode("config", "NickServ").getBoolean();
    public String nickservnick = ConfigGen.rootNode.getNode("config", "NickServNick").getString();
    public String nickservpassword = ConfigGen.rootNode.getNode("config", "NickServPassword").getString();

    @Listener
    public void Init(GameInitializationEvent e) {

        Sponge.getCommandManager().register(this, CommandHandler.reload, "reload"); // Register commands.
    }

}
