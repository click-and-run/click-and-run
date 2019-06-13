package com.altissia.clickandrun.domain.enumeration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The Language enumeration.
 */
public enum Language {
    FR_FR(Locale.FRANCE, Type.STUDY),
    NL_NL(new Locale.Builder().setLanguage("nl").setRegion("NL").setLanguageTag("nl-NL").build(), Type.STUDY),
    DE_DE(Locale.GERMANY, Type.STUDY),
    EN_GB(Locale.UK, Type.STUDY),
    ES_ES(new Locale.Builder().setLanguage("es").setRegion("ES").setLanguageTag("es-ES").build(), Type.INTERFACE),
    IT_IT(Locale.ITALY,Type.INTERFACE),
    PT_BR(new Locale.Builder().setLanguage("pt").setRegion("BR").setLanguageTag("pt-BR").build(), Type.INTERFACE);

    private Locale locale;
    private Type type;

    Language(java.util.Locale locale, Type type) {
        this.locale = locale;
        this.type = type;
    }

    public Locale getLocale() {
        return locale;
    }

    public Type getType() {
        return type;
    }

    public boolean isInterfaceLanguage() {
        return true;
    }

    public boolean isStudyLanguage() {
        return type == Type.STUDY;
    }

    public static Optional<Language> fromLocale(Locale locale) {
        return Arrays.stream(Language.values())
            .filter(language -> language.getLocale().equals(locale))
            .findFirst();
    }

    public static boolean isInterfaceLanguage(Locale locale) {
        return fromLocale(locale).map(Language::isInterfaceLanguage).orElse(false);
    }

    public static boolean isStudyLanguage(Locale locale) {
        return fromLocale(locale).map(Language::isStudyLanguage).orElse(false);
    }

    public static List<Language> getInterfaceLanguages() {
        return Arrays.stream(Language.values())
            .filter(Language::isInterfaceLanguage)
            .collect(Collectors.toList());
    }

    public static List<Language> getStudyLanguages() {
        return Arrays.stream(Language.values())
            .filter(Language::isStudyLanguage)
            .collect(Collectors.toList());
    }

    /**
     * Type of support for the given Language instance.
     * Study language is also an interface
     */
    public enum Type {
        INTERFACE, STUDY
    }
}
