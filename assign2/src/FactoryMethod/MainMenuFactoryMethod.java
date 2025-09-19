public abstract class MainMenuFactoryMethod {
    public final void renderScreen(String prefix) {
        Button    loginButton   = buildLoginButton();
        TextField nameField     = buildNameField();
        TextField passwordField = buildPasswordField();
        Checkbox  staySignInBox = buildStaySignInBox();

        System.out.println(prefix + "=== Rendering screen: " + this.getClass().getSimpleName() + " ===");
        System.out.println(StringTools.prefixLines(prefix + "    ", loginButton));
        System.out.println(StringTools.prefixLines(prefix + "    ", nameField));
        System.out.println(StringTools.prefixLines(prefix + "    ", passwordField));
        System.out.println(StringTools.prefixLines(prefix + "    ", staySignInBox));
    }

    protected abstract TextField buildNameField();
    protected abstract TextField buildPasswordField();
    protected abstract Checkbox  buildStaySignInBox();
    protected abstract Button    buildLoginButton();
}
