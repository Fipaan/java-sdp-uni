public class CheckboxStaySignInLightBuilder implements Builder<Checkbox> {
    public CheckboxStaySignInLightBuilder() {}
    public Checkbox build() {
        Checkbox element = new ElementBuilder<Checkbox>(new Checkbox())
            .setType("Checkbox")
            .setId(Engine.GetUniqueId())
            .setPos(Engine.GetSize()
                        .mult(0.1f, 0.3f)
                        .add(0.7f * Engine.GetSize().y, 0.0f)) 
            .setSize(Vector2.One().scale(0.1f * Engine.GetSize().y))
            .setBackgroundColor(Color.WHITE())
            .setForegroundColor(Color.BLACK())
            .setEnabled(true)
            .build();
        element.setChecked(false);
        element.setCheckedColor(Color.GREEN());
        element.setUncheckedColor(Color.RED());
        return element;
    }
}
