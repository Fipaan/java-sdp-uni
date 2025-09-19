public class ButtonLoginDarkBuilder implements Builder<Button> {
    public ButtonLoginDarkBuilder() {}
    public Button build() {
        Button element = new ElementBuilder<Button>(new Button())
            .setType("Button")
            .setId(Engine.GetUniqueId())
            .setPos(Engine.GetSize().mult(0.1f, 0.3f)) 
            .setSize(Engine.GetSize()
                           .mult(0.8f, 0.1f)
                           .sub(Engine.GetSize().y * 0.1f, 0.0f))
            .setBackgroundColor(Color.WHITE())
            .setForegroundColor(Color.BLACK())
            .setEnabled(true)
            .build();
        element.setText("Start");      
        element.setAction(() -> System.out.println("App is started"));
        return element;
    }
}
