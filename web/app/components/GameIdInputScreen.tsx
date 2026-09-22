"use client";

import React, { useState } from "react";
import { generateGameId } from "../lib/storage";
import { useTranslation } from "../lib/useTranslation";

interface GameIdInputScreenProps {
  onSubmitGameId: (gameId: string) => void;
  onBack: () => void;
}

export const GameIdInputScreen: React.FC<GameIdInputScreenProps> = ({
  onSubmitGameId,
  onBack
}) => {
  const { t } = useTranslation();
  const [gameIdInput, setGameIdInput] = useState("");
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const trimmed = gameIdInput.trim().toUpperCase();
    if (!trimmed) {
      setError("Please enter a valid Game ID");
      return;
    }
    if (trimmed.length < 4) {
      setError("Game ID must be at least 4 characters");
      return;
    }
    setError(null);
    onSubmitGameId(trimmed);
  };

  const handleGenerateRandom = () => {
    const randomId = generateGameId();
    setGameIdInput(randomId);
    setError(null);
  };

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
        <h1 style={{ fontSize: "26px", fontWeight: "bold", marginBottom: "8px" }}>
          {t.enterGameIdTitle}
        </h1>
        <p style={{ fontSize: "14px", color: "rgba(255,255,255,0.7)", marginBottom: "24px" }}>
          {t.enterGameIdSub}
        </p>

        <form onSubmit={handleSubmit} style={{ width: "100%", display: "flex", flexDirection: "column", gap: "16px" }}>
          <input
            type="text"
            value={gameIdInput}
            onChange={(e) => {
              setGameIdInput(e.target.value.toUpperCase());
              setError(null);
            }}
            placeholder={t.gameIdPlaceholder}
            maxLength={10}
            style={{
              padding: "16px",
              borderRadius: "15px",
              backgroundColor: "rgba(255,255,255,0.15)",
              color: "#FFFFFF",
              border: error ? "2px solid #FF5252" : "2px solid rgba(255,255,255,0.4)",
              fontSize: "22px",
              fontWeight: "bold",
              textAlign: "center",
              letterSpacing: "3px",
              outline: "none"
            }}
          />

          {error && (
            <div style={{ color: "#FF5252", fontSize: "13px", fontWeight: "bold" }}>
              {error}
            </div>
          )}

          <div style={{ display: "flex", gap: "10px" }}>
            <button
              type="button"
              onClick={handleGenerateRandom}
              style={{
                flex: 1,
                padding: "12px",
                borderRadius: "12px",
                backgroundColor: "#CD7F32",
                color: "#FFF",
                border: "none",
                fontWeight: "bold",
                cursor: "pointer",
                fontSize: "14px"
              }}
            >
              🎲 Generate Code
            </button>
            <button
              type="submit"
              style={{
                flex: 1.5,
                padding: "12px",
                borderRadius: "12px",
                backgroundColor: "#009E60",
                color: "#FFF",
                border: "none",
                fontWeight: "bold",
                cursor: "pointer",
                fontSize: "16px"
              }}
            >
              {t.continueBtn} ➔
            </button>
          </div>
        </form>

        <button
          onClick={onBack}
          style={{
            marginTop: "24px",
            padding: "10px 20px",
            borderRadius: "12px",
            backgroundColor: "transparent",
            color: "rgba(255,255,255,0.8)",
            border: "1px solid rgba(255,255,255,0.4)",
            fontSize: "14px",
            cursor: "pointer"
          }}
        >
          ⬅️ {t.back}
        </button>
      </div>

      <footer style={{ fontSize: "12px", color: "rgba(255,255,255,0.6)" }}>
        Mahato Software • Made with ❤️ in India
      </footer>
    </div>
  );
};
