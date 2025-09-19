public class MainMenuLightAbstract implements MainMenuFactoryAbstract {
    private Builder<TextField> nameField     = new TextFieldNameLightBuilder();
    private Builder<TextField> passwordField = new TextFieldPasswordLightBuilder();
    private Builder<Checkbox>  staySignInBox = new CheckboxStaySignInLightBuilder();
    private Builder<Button>    loginButton   = new ButtonLoginLightBuilder();
    MainMenuLightAbstract() {}
    public TextField buildNameField()     { return nameField.build(); }
    public TextField buildPasswordField() { return passwordField.build(); }
    public Checkbox  buildStaySignInBox() { return staySignInBox.build(); }
    public Button    buildLoginButton()   { return loginButton.build(); }
}
