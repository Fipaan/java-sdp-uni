public class StringParser {
    private String container;
    public String getContainer() {
        return container;
    }

    private String str;
    public String getStr() {
        return str;
    }

    public StringParser(String str) {
        this.container = "";
        this.str       = str;
    }
    public StringParser(String str, String container) {
        this.container = container;
        this.str       = str;
    }
    public StringParser trim() {
        str = str.trim();
        return this;
    }
    public boolean isStrEmpty() { return str.length() == 0; }
    public boolean extractStarts(String prefix) {
        String newStr = StringUtils.expectStartsAndExtract(str, prefix);
        if (newStr == null) return false;
        str = newStr;
        container = prefix;
        return true;
    }
    public boolean extractEnds(String suffix) {
        String newStr = StringUtils.expectEndsAndExtract(str, suffix);
        if (newStr == null) return false;
        str = newStr;
        container = suffix;
        return true;
    }
    public boolean extractWrapper(String prefix, String suffix) {
        String newStr = StringUtils.expectWrapperAndExtract(str, prefix, suffix);
        if (newStr == null) return false;
        str = newStr;
        container = "";
        return true;
    }
    public boolean extractStartsContainer(String prefix, String suffix) {
        int iContainer = StringUtils.endOfContainer(str, prefix, suffix);
        if (iContainer == -1) return false;
        container = StringUtils.extractContainer(str, prefix, suffix);
        assert container != null;
        str = str.substring(iContainer);
        return true;
    }
}
