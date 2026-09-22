"use client";

import React from "react";
import { useTranslation } from "../lib/useTranslation";

interface GameModeSelectionScreenProps {
  onSelectModerated: () => void;
  onSelectUnmoderated: () => void;
  onBack: () => void;
}

export const GameModeSelectionScreen: React.FC<GameModeSelectionScreenProps> = ({
  onSelectModerated,
  onSelectUnmoderated,
  onBack
}) => {
  const { t } = useTranslation();

  return (
    <div style={{
      minHeight: "100vh",
      backgroundColor: "#6C0BA9",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      justifyContent: "space-between",
      alignItems: "center",
      padding: "24px",
      textAlign: "center"
    }}>
      <div style={{ flex: 1, display: "flex", flexDirection: "column", justifyContent: "center", alignItems: "center", maxWidth: "450px", width: "100%" }}>
        <h1 style={{ fontSize: "26px", fontWeight: "bold", marginBottom: "36px" }}>
          {t.selectGameModeTitle}
        </h1>

        <div style={{ display: "flex", flexDirection: "column", gap: "16px", width: "100%" }}>
          <button
            onClick={onSelectModerated}
            style={{
              padding: "18px",
              borderRadius: "20px",
              backgroundColor: "#009E60",
              color: "#FFFFFF",
              border: "none",
              fontSize: "18px",
              fontWeight: "bold",
              cursor: "pointer",
              boxShadow: "0 4px 12px rgba(0,0,0,0.3)"
            }}
          >
            📱 {t.moderatedMode}
          </button>

          <button
            onClick={onSelectUnmoderated}
            style={{
              padding: "18px",
              borderRadius: "20px",
              backgroundColor: "#4A007E",
              color: "#FFFFFF",
              border: "1px solid rgba(255,255,255,0.2)",
              fontSize: "18px",
              fontWeight: "bold",
              cursor: "pointer",
              boxShadow: "0 4px 12px rgba(0,0,0,0.3)"
            }}
          >
            🎲 {t.unmoderatedMode}
          </button>

          <button
            onClick={onBack}
            style={{
              marginTop: "16px",
              padding: "12px",
              borderRadius: "15px",
              backgroundColor: "transparent",
              color: "rgba(255,255,255,0.8)",
              border: "1px solid rgba(255,255,255,0.4)",
              fontSize: "15px",
              cursor: "pointer"
            }}
          >
            ⬅️ {t.back}
          </button>
        </div>
      </div>

      <footer style={{ fontSize: "12px", color: "rgba(255,255,255,0.6)" }}>
        Mahato Software • Made with ❤️ in India
      </footer>
    </div>
  );
};
