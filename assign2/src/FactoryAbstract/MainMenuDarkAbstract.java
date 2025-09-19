public class MainMenuDarkAbstract implements MainMenuFactoryAbstract {
    private Builder<TextField> nameField     = new TextFieldNameDarkBuilder();
    private Builder<TextField> passwordField = new TextFieldPasswordDarkBuilder();
    private Builder<Checkbox>  staySignInBox = new CheckboxStaySignInDarkBuilder();
    private Builder<Button>    loginButton   = new ButtonLoginDarkBuilder();
    MainMenuDarkAbstract() {}
    public TextField buildNameField()     { return nameField.build(); }
    public TextField buildPasswordField() { return passwordField.build(); }
    public Checkbox  buildStaySignInBox() { return staySignInBox.build(); }
    public Button    buildLoginButton()   { return loginButton.build(); }
}
