public class Phone {
    int width, height;
    float size; String sizeExt; // cm, mm, ...
    String releaseDate;
    String name, manufacturer, country;
    int refreshRate; float megapixels;
    int audioChannels;
    String chargeFormat;

    Phone() {}

    // This will make Builder works easier without repetition
    Phone(Phone other) {
        this.width         = other.width;
        this.height        = other.height;
        this.size          = other.size;
        this.sizeExt       = other.sizeExt;
        this.releaseDate   = other.releaseDate;
        this.name          = other.name;
        this.manufacturer  = other.manufacturer;
        this.country       = other.country;
        this.refreshRate   = other.refreshRate;
        this.megapixels    = other.megapixels;
        this.audioChannels = other.audioChannels;
        this.chargeFormat  = other.chargeFormat;
    }
    
    // Allows to easily inspect instance
    @Override
    public String toString() {
        return String.format("%s [%s %s, ratio: %dx%d (%.2f %s), made in %s, camera: %.2fMP (%d Hz), audio channels: %d, charge format: %s]",
                name, manufacturer, releaseDate,
                width, height,
                size, sizeExt,
                country,
                megapixels, refreshRate,
                audioChannels, chargeFormat);
    }
}
