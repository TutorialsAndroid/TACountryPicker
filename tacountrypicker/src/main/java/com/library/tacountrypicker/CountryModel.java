package com.library.tacountrypicker;

public class CountryModel {
    private String name;
    private String dialCode;
    private String emoji;

    public CountryModel(String name, String dialCode, String emoji) {
        this.name = name;
        this.dialCode = dialCode;
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public String getDialCode() {
        return dialCode;
    }

    public String getEmoji() {
        return emoji;
    }
}

