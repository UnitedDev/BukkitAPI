package fr.uniteduhc.messaging.subscriber;

import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.uniteduhc.common.utils.messaging.pigdin.PacketListener;
import fr.uniteduhc.messaging.packet.AttemptLinkPacket;
import fr.uniteduhc.utils.ChatUtil;
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
