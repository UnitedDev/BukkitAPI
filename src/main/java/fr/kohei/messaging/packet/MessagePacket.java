package fr.kohei.messaging.packet;

import fr.kohei.common.utils.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MessagePacket implements Packet {

    private final List<TextComponent> messages;
    private final int power;

}
