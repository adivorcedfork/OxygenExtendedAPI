package oxygen.api.game.emote;

import oxygen.api.wrappers.InterfaceComponent;

public class Emotes {

    public static boolean perform(Emote emote) {
        InterfaceComponent emoteComp = emote.getComponent();
        if (emoteComp != null) {
            return emoteComp.interact(emote.getAction());
        }
        return false;
    }
}
