package fr.kohei.messaging.subscriber;

import fr.kohei.common.cache.ProfileData;
import fr.kohei.common.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.messaging.pigdin.PacketListener;
import fr.kohei.messaging.packet.AttemptLinkPacket;
import fr.kohei.utils.ChatUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AttemptLinkSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(AttemptLinkPacket packet) {
        ProfileData profile = packet.getProfileData();
        Player player = Bukkit.getPlayer(profile.getDisplayName());

        if(player == null) return;

        player.sendMessage(" ");
        player.sendMessage(ChatUtil.prefix("&fIl semblerait que l'utilisateur &c" + packet.getDiscordDisplay() +
                " &fessaye de lier son compte Discord à votre compte Minecraft."));
        TextComponent text = new TextComponent(ChatUtil.translate("&a&l[CONFIRMER LA LIAISON]"));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptlink id;" + packet.getDiscordId()));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                new TextComponent(ChatUtil.translate("&aCliquez ici pour confirmer la liaison."))
        }));
        player.spigot().sendMessage(text);

        player.sendMessage(" ");

    }

}
