public class MainMenuLightMethod extends MainMenuFactoryMethod {
    private Builder<TextField> nameField     = new TextFieldNameLightBuilder();
    private Builder<TextField> passwordField = new TextFieldPasswordLightBuilder();
    private Builder<Checkbox>  staySignInBox = new CheckboxStaySignInLightBuilder();
    private Builder<Button>    loginButton   = new ButtonLoginLightBuilder();
    MainMenuLightMethod() {}
    @Override
    protected TextField buildNameField()     { return nameField.build(); }
    @Override
    protected TextField buildPasswordField() { return passwordField.build(); }
    @Override
    protected Checkbox  buildStaySignInBox() { return staySignInBox.build(); }
    @Override
    protected Button    buildLoginButton()   { return loginButton.build(); }
}
