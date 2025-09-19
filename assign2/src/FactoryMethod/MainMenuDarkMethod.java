public class MainMenuDarkMethod extends MainMenuFactoryMethod {
    private Builder<TextField> nameField     = new TextFieldNameDarkBuilder();
    private Builder<TextField> passwordField = new TextFieldPasswordDarkBuilder();
    private Builder<Checkbox>  staySignInBox = new CheckboxStaySignInDarkBuilder();
    private Builder<Button>    loginButton   = new ButtonLoginDarkBuilder();
    MainMenuDarkMethod() {}
    @Override
    protected TextField buildNameField()     { return nameField.build(); }
    @Override
    protected TextField buildPasswordField() { return passwordField.build(); }
    @Override
    protected Checkbox  buildStaySignInBox() { return staySignInBox.build(); }
    @Override
    protected Button    buildLoginButton()   { return loginButton.build(); }
}
