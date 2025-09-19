public class TextFieldPasswordDarkBuilder implements Builder<TextField> {
    public TextFieldPasswordDarkBuilder() {}
    public TextField build() {
        TextField element = new ElementBuilder<TextField>(new TextField())
            .setType("TextField")
            .setId(Engine.GetUniqueId())
            .setPos(Engine.GetSize().mult(0.1f, 0.15f)) 
            .setSize(Engine.GetSize().mult(0.8f, 0.1f))
            .setBackgroundColor(Color.WHITE())
            .setForegroundColor(Color.BLACK())
            .setEnabled(true)
            .build();
        element.setFocused(false);
        return element;
    }
}
