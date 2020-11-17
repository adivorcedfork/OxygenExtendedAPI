package oxygen.api.game.emote;

public enum Emote {
    BOW("Bow"),
    CRY("Cry"),
    CHEER("Cheer"),
    SHRUG("Shrug");

    private final String name;

    Emote(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
