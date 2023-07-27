package me.emiel04.wildwands.config;

public enum Lang {
    PREFIX("prefix","&lWildWands&r"),
    GIVEN_MESSAGE("given_message","You have been given %wand%"),
    NO_PERM("no_permission","You do not have permission to execute this command!"),
    WAND_BROKE("wand_broke", "Your wand broke!"),
    BROKEN_WAND("broken_wand", "You can't use a broken wand!"),
    RECEIVED_WAND("received_wand", "You have received %wand% with %uses% uses!"),
    RECEIVED_DUST("received_pixiedust", "You have received %name% with %uses% uses!"),
    GIVEN_DUST("given_pixiedust", "You have given %name% with %uses% uses to %target%!"),
    GIVEN_WAND("given_wand", "You have given %wand% with %uses% uses to %target%!"),
    SOLD_ITEMS("sold_items", "You have sold the items for &5&l%amount%&r&f! Uses left: &5&l%uses%"),
    SMELTED_ITEMS("smelted_items", "Smelt wand activated! Uses left: &5&l%uses%"),
    CONDENSED_ITEMS("condensed_items", "Condense wand activated! Uses left: &5&l%uses%");

    private final String message;
    private final String key;
    private Lang(String key, String message) {
        this.message = message;
        this.key = key;
    }

    public String getDefaultMessage(){
        return message;
    }
    public String getKey(){
        return key;
    }
}
