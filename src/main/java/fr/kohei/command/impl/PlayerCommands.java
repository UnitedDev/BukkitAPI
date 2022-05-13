package fr.kohei.command.impl;

import fr.kohei.BukkitAPI;
import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.DiscordWebhook;
import org.bukkit.entity.Player;

public class PlayerCommands {

    @Command(names = "chatreport")
    public static void report(Player sender, @Param(name = "name") String name, @Param(name = "message", wildcard = true) String message) {
        if (!name.startsWith("name:")) return;
        if (!message.startsWith("message:")) return;

        name = name.replaceFirst("name:", "");
        message = message.replaceFirst("message:", "");

        if (BukkitAPI.getChatReportManager().getCooldown().getOrDefault(sender.getUniqueId(), false)) {
            sender.sendMessage(ChatUtil.prefix("&cVeuillez attendre 120 secondes avant de report un autre message."));
            return;
        }

        BukkitAPI.getChatReportManager().setCooldown(sender.getUniqueId());
        sender.sendMessage(ChatUtil.prefix("&fVous avez &6report &fle message de &a" + name));

        DiscordWebhook webhook = new DiscordWebhook(DiscordWebhook.REPORT_CHANNEL);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Nouveau Report")
                .setDescription("**Message:** " + message)
                .addField("Fautif", name, true)
                .addField("Joueurs", sender.getName(), true)
                .setFooter("Chat Report", DiscordWebhook.LOGO_URL)
        );
        webhook.execute();
    }

}
