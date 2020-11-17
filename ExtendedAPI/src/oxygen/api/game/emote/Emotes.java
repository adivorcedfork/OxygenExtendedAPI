package oxygen.api.game.emote;

import oxygen.api.InterfaceAddress;
import oxygen.api.wrappers.InterfaceComponent;

public class Emotes {

    private static final int EMOTES_GROUP = 216;
    private static final int EMOTES_CHILD_INDEX = 1;

    private static final InterfaceAddress EMOTES = new InterfaceAddress(EMOTES_GROUP, EMOTES_CHILD_INDEX);

    public static boolean perform(Emote emote) {
        InterfaceComponent emoteComps = EMOTES.lookup();
        if (emoteComps == null) {
            return false;
        }
        for (InterfaceComponent emoteComp : emoteComps.getChildren()) {
            if (emoteComp.hasAction(emote.getName())) {
                return emoteComp.interact(emote.getName());
            }
        }
        return false;
    }
}
