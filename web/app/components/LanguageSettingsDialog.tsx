"use client";

import React, { useState, useEffect } from "react";
import { loadSelectedLanguage, saveSelectedLanguage } from "../lib/storage";
import { useTranslation } from "../lib/useTranslation";

interface LanguageSettingsDialogProps {
  onDismiss: () => void;
  onLanguageChanged?: (lang: string) => void;
}

const LANGUAGES = [
  { code: "en", name: "🇬🇧 English" },
  { code: "hi", name: "🇮🇳 Hindi (हिंदी)" },
  { code: "kn", name: "🇮🇳 Kannada (ಕನ್ನಡ)" },
  { code: "bn", name: "🇮🇳 Bengali (বাংলা)" },
  { code: "or", name: "🇮🇳 Odia (ଓଡ଼ିଆ)" },
  { code: "ta", name: "🇮🇳 Tamil (தமிழ்)" },
  { code: "mr", name: "🇮🇳 Marathi (मराठी)" },
  { code: "te", name: "🇮🇳 Telugu (తెలుగు)" },
  { code: "gu", name: "🇮🇳 Gujarati (ગુજરાતી)" },
  { code: "ml", name: "🇮🇳 Malayalam (മലയാളം)" },
  { code: "pa", name: "🇮🇳 Punjabi (ਪੰਜਾਬੀ)" },
  { code: "as", name: "🇮🇳 Assamese (অসমীয়া)" },
  { code: "pt", name: "🇵🇹 Portuguese (Português)" },
  { code: "fr", name: "🇫🇷 French (Français)" },
  { code: "de", name: "🇩🇪 German (Deutsch)" },
  { code: "ar", name: "🇸🇦 Arabic (العربية)" },
  { code: "id", name: "🇮🇩 Indonesian (Bahasa Indonesia)" },
  { code: "tr", name: "🇹🇷 Turkish (Türkçe)" },
  { code: "it", name: "🇮🇹 Italian (Italiano)" },
  { code: "ja", name: "🇯🇵 Japanese (日本語)" },
  { code: "ko", name: "🇰🇷 Korean (한국어)" },
  { code: "zh", name: "🇨🇳 Chinese (中文)" },
  { code: "nl", name: "🇳🇱 Dutch (Nederlands)" },
  { code: "ru", name: "🇷🇺 Russian (Русский)" },
  { code: "vi", name: "🇻🇳 Vietnamese (Tiếng Việt)" }
];

export const LanguageSettingsDialog: React.FC<LanguageSettingsDialogProps> = ({
  onDismiss,
  onLanguageChanged
}) => {
  const { t } = useTranslation();
  const [selectedLang, setSelectedLang] = useState("en");

  useEffect(() => {
    setSelectedLang(loadSelectedLanguage());
  }, []);

  const handleSelectLanguage = (code: string) => {
    setSelectedLang(code);
    saveSelectedLanguage(code);
    if (onLanguageChanged) onLanguageChanged(code);
  };

  return (
    <div style={{
      position: "fixed",
      inset: 0,
      backgroundColor: "rgba(0,0,0,0.75)",
      backdropFilter: "blur(6px)",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      zIndex: 1000,
      padding: "16px"
    }}>
      <div style={{
        backgroundColor: "#31005A",
        color: "#FFF",
        borderRadius: "24px",
        padding: "24px",
        maxWidth: "500px",
        width: "100%",
        boxShadow: "0 15px 35px rgba(0,0,0,0.6)",
        border: "1.5px solid rgba(255,255,255,0.2)"
      }}>
        <h2 style={{ fontSize: "20px", fontWeight: "bold", marginBottom: "18px", textAlign: "center" }}>
          {t.audioSettingsTitle}
        </h2>

        {/* Active Voice Badge */}
        <div style={{
          marginBottom: "20px",
          padding: "12px 16px",
          borderRadius: "16px",
          backgroundColor: "rgba(0, 230, 118, 0.15)",
          border: "1px solid rgba(0, 230, 118, 0.3)",
          display: "flex",
          alignItems: "center",
          gap: "12px"
        }}>
          <div style={{ fontSize: "24px" }}>👩</div>
          <div>
            <div style={{ fontSize: "14px", fontWeight: "bold", color: "#00E676" }}>
              {t.ladyVoiceActive}
            </div>
            <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.7)" }}>
              {t.ladyVoiceDesc}
            </div>
          </div>
        </div>

        {/* Speech Language Selection */}
        <div style={{ marginBottom: "16px" }}>
          <label style={{ fontSize: "13px", fontWeight: "700", textTransform: "uppercase", letterSpacing: "0.5px", color: "rgba(255,255,255,0.7)", display: "block", marginBottom: "8px" }}>
            {t.announcementLang}
          </label>
          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "10px", maxHeight: "45vh", overflowY: "auto", paddingRight: "4px" }}>
            {LANGUAGES.map((lang) => {
              const isSelected = selectedLang === lang.code;
              return (
                <button
                  key={lang.code}
                  onClick={() => handleSelectLanguage(lang.code)}
                  style={{
                    padding: "10px",
                    borderRadius: "12px",
                    backgroundColor: isSelected ? "rgba(255, 215, 0, 0.25)" : "rgba(255,255,255,0.08)",
                    color: isSelected ? "#FFD700" : "#FFF",
                    border: isSelected ? "1.5px solid #FFD700" : "1px solid rgba(255,255,255,0.15)",
                    cursor: "pointer",
                    fontWeight: isSelected ? "bold" : "normal",
                    fontSize: "13px",
                    textAlign: "center",
                    transition: "all 0.2s ease"
                  }}
                >
                  {lang.name}
                </button>
              );
            })}
          </div>
        </div>

        <button
          onClick={onDismiss}
          style={{
            marginTop: "12px",
            width: "100%",
            padding: "12px",
            backgroundColor: "#00E676",
            color: "#000000",
            border: "none",
            borderRadius: "14px",
            fontWeight: "bold",
            fontSize: "15px",
            cursor: "pointer"
          }}
        >
          {t.done}
        </button>
      </div>
    </div>
  );
};
