public class Main {
    public static void main(String[] args) {
        PhoneBuilder iphoneBuilder = new PhoneBuilder()
            .setManufacturer("Apple Inc")
            .setCountry("China");
        PhoneBuilder xiaomiBuilder = new PhoneBuilder()
            .setManufacturer("Xiaomi Inc")
            .setCountry("China");
            
        Phone iphone14Pro = iphoneBuilder.stash()
            .setName("Iphone 14 Pro")
            .setSize(6.1f, "inches")
            .setResolution(1179, 2556)
            .setRefreshRate(120)
            .setMegapixels(48.0f)
            .setAudioChannels(2)
            .setChargeFormat("USB-C")
            .setReleaseDate("September 16, 2022")
            .build();
        System.out.println(iphone14Pro);
        Phone redmiNote12 = xiaomiBuilder.stash()
            .setName("Redmi Note 12")
            .setResolution(1080, 2400)
            .setSize(6.67f, "inches")
            .setReleaseDate("January 11, 2023")
            .setMegapixels(48.0f)
            .setRefreshRate(120)
            .setAudioChannels(1)
            .setChargeFormat("Type-C")
            .build();
        System.out.println(redmiNote12);
    }
}
