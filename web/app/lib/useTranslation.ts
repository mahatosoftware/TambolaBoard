"use client";

import { useState, useEffect } from "react";
import { loadSelectedLanguage } from "./storage";
import { getTranslations, Translations } from "./translations";

export function useTranslation() {
  const [lang, setLang] = useState<string>("en");

  useEffect(() => {
    // Initial load
    setLang(loadSelectedLanguage());

    const handleLanguageChange = (event: Event) => {
      const customEvent = event as CustomEvent<string>;
      if (customEvent.detail) {
        setLang(customEvent.detail);
      } else {
        setLang(loadSelectedLanguage());
      }
    };

    window.addEventListener("tambola_language_changed", handleLanguageChange);
    return () => {
      window.removeEventListener("tambola_language_changed", handleLanguageChange);
    };
  }, []);

  const t: Translations = getTranslations(lang);

  return { t, lang };
}
