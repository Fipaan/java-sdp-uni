public class Main {
    public static void renderFactory(String prefix, MainMenuFactoryAbstract factory, String name) {
        TextField nameField     = factory.buildNameField();
        TextField passwordField = factory.buildPasswordField();
        Checkbox  staySignInBox = factory.buildStaySignInBox();
        Button    loginButton   = factory.buildLoginButton();
        System.out.println(prefix + name + ":");
        System.out.println(StringTools.prefixLines(prefix + "    ", nameField));
        System.out.println(StringTools.prefixLines(prefix + "    ", passwordField));
        System.out.println(StringTools.prefixLines(prefix + "    ", staySignInBox));
        System.out.println(StringTools.prefixLines(prefix + "    ", loginButton));
    }
    public static void abstractFactoryExample() {
        String prefix = "    ";
        MainMenuDarkAbstract darkFactory = new MainMenuDarkAbstract();
        renderFactory(prefix, darkFactory, "Dark Factory");
        MainMenuLightAbstract lightFactory = new MainMenuLightAbstract();
        renderFactory(prefix, lightFactory, "Light Factory");
    }
    public static void factoryMethodExample() {
        String prefix = "    ";
        MainMenuDarkMethod darkFactory = new MainMenuDarkMethod();
        darkFactory.renderScreen(prefix);
        MainMenuLightMethod lightFactory = new MainMenuLightMethod();
        lightFactory.renderScreen(prefix);
    }
    public static void main(String[] args) {
        System.out.println("===== ABSTRACT FACTORY EXAMPLE =====");
        abstractFactoryExample();
        System.out.println("===== METHOD   FACTORY EXAMPLE =====");
        factoryMethodExample();
    }
}
