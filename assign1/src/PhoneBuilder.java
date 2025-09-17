class PhoneBuilder {
    Phone phone;
    PhoneBuilder() {
        phone = new Phone();
    }
    
    PhoneBuilder(Phone base) {
        phone = new Phone(base);
    }
    PhoneBuilder stash() {
        return new PhoneBuilder(phone);
    }

    PhoneBuilder setResolution(int width, int height) {
        this.phone.width = width;
        this.phone.height = height;
        return this;
    }
    PhoneBuilder setSize(float size, String ext) {
        this.phone.size = size;
        this.phone.sizeExt = ext;
        return this;
    }
    PhoneBuilder setName(String name)                 { this.phone.name          = name;          return this; }
    PhoneBuilder setReleaseDate(String releaseDate)   { this.phone.releaseDate   = releaseDate;   return this; }
    PhoneBuilder setManufacturer(String manufacturer) { this.phone.manufacturer  = manufacturer;  return this; }
    PhoneBuilder setCountry(String country)           { this.phone.country       = country;       return this; }
    PhoneBuilder setMegapixels(float megapixels)      { this.phone.megapixels    = megapixels;    return this; }
    PhoneBuilder setRefreshRate(int refreshRate)      { this.phone.refreshRate   = refreshRate;   return this; }
    PhoneBuilder setAudioChannels(int audioChannels)  { this.phone.audioChannels = audioChannels; return this; }
    PhoneBuilder setChargeFormat(String chargeFormat) { this.phone.chargeFormat  = chargeFormat;  return this; }
    Phone build() { return phone; }
}
