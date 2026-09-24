"use client";

import React, { useState } from "react";
import { generateGameId } from "../lib/storage";
import { useTranslation } from "../lib/useTranslation";
import { Ticket, Sliders, ArrowRight, CheckCircle2, QrCode } from "lucide-react";
import { getOrCreateGame } from "../lib/tambola/tickets";

interface GameIdInputScreenProps {
  onSubmitGameId: (gameId: string) => void;
  onOpenTicketDistribution?: (gameId: string) => void;
  onBack: () => void;
}

export const GameIdInputScreen: React.FC<GameIdInputScreenProps> = ({
  onSubmitGameId,
  onOpenTicketDistribution,
  onBack
}) => {
  const { t } = useTranslation();
  const [gameIdInput, setGameIdInput] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [showChoiceModal, setShowChoiceModal] = useState(false);
  const [confirmedId, setConfirmedId] = useState("");

  const validateAndProceed = (id: string) => {
    const trimmed = id.trim().toUpperCase().replace(/[^A-Z0-9]/g, "");
    if (!trimmed) {
      setError("Please enter a 6-character Game Code");
      return false;
    }
    if (trimmed.length !== 6) {
      setError("Game Code must be exactly 6 alphanumeric characters (e.g. 3NA2RL)");
      return false;
    }
    setError(null);
    setConfirmedId(trimmed);
    // Initialize game in Firestore immediately
    getOrCreateGame(trimmed).catch(console.error);
    setShowChoiceModal(true);
    return true;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    validateAndProceed(gameIdInput);
  };

  const handleGenerateRandom = () => {
    const randomId = generateGameId();
    setGameIdInput(randomId);
    setError(null);
    validateAndProceed(randomId);
  };

  return (
    <div style={{
      minHeight: "100vh",
      backgroundColor: "#6C0BA9",
      backgroundImage: "radial-gradient(circle at 50% 20%, #7C4DFF 0%, #3A0066 50%, #1A0033 100%)",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      justifyContent: "space-between",
      alignItems: "center",
      padding: "24px",
      textAlign: "center",
      fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
    }}>
      <div style={{ flex: 1, display: "flex", flexDirection: "column", justifyContent: "center", alignItems: "center", maxWidth: "460px", width: "100%" }}>
        
        {/* Header Icon */}
        <div style={{
          width: "64px",
          height: "64px",
          borderRadius: "20px",
          background: "linear-gradient(135deg, #FFD700, #FF6F00)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          marginBottom: "16px",
          boxShadow: "0 4px 20px rgba(255,215,0,0.4)"
        }}>
          <QrCode style={{ width: "32px", height: "32px", color: "#FFF" }} />
        </div>

        <h1 style={{ fontSize: "28px", fontWeight: "900", marginBottom: "8px" }}>
          {t.enterGameIdTitle}
        </h1>
        <p style={{ fontSize: "14px", color: "rgba(255,255,255,0.75)", marginBottom: "28px" }}>
          Enter a 6-character alphanumeric Game Code (e.g. 3NA2RL)
        </p>

        <form onSubmit={handleSubmit} style={{ width: "100%", display: "flex", flexDirection: "column", gap: "16px" }}>
          <input
            type="text"
            value={gameIdInput}
            onChange={(e) => {
              const cleaned = e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, "").slice(0, 6);
              setGameIdInput(cleaned);
              setError(null);
            }}
            placeholder="e.g. 3NA2RL"
            maxLength={6}
            style={{
              padding: "18px",
              borderRadius: "18px",
              backgroundColor: "rgba(255,255,255,0.15)",
              backdropFilter: "blur(10px)",
              color: "#FFFFFF",
              border: error ? "2px solid #FF5252" : "2px solid rgba(255,255,255,0.4)",
              fontSize: "24px",
              fontWeight: "900",
              textAlign: "center",
              letterSpacing: "4px",
              outline: "none"
            }}
          />

          {error && (
            <div style={{ color: "#FF5252", fontSize: "13px", fontWeight: "bold" }}>
              {error}
            </div>
          )}

          <div style={{ display: "flex", gap: "12px" }}>
            <button
              type="button"
              onClick={handleGenerateRandom}
              style={{
                flex: 1,
                padding: "14px",
                borderRadius: "14px",
                background: "linear-gradient(135deg, #FF9800 0%, #CD7F32 100%)",
                color: "#FFF",
                border: "none",
                fontWeight: "800",
                cursor: "pointer",
                fontSize: "14px",
                boxShadow: "0 4px 15px rgba(205, 127, 50, 0.4)"
              }}
            >
              🎲 Generate Code
            </button>
            <button
              type="submit"
              style={{
                flex: 1.5,
                padding: "14px",
                borderRadius: "14px",
                background: "linear-gradient(135deg, #00E676 0%, #00B0FF 100%)",
                color: "#FFF",
                border: "none",
                fontWeight: "900",
                cursor: "pointer",
                fontSize: "16px",
                boxShadow: "0 4px 15px rgba(0, 230, 118, 0.4)"
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
            color: "rgba(255,255,255,0.7)",
            border: "1px solid rgba(255,255,255,0.3)",
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

      {/* Interactive Choice Modal: Distribute Tickets vs Setup Rules */}
      {showChoiceModal && (
        <div style={{
          position: "fixed",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.85)",
          backdropFilter: "blur(10px)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000,
          padding: "16px"
        }}>
          <div style={{
            backgroundColor: "#2A004A",
            borderRadius: "28px",
            padding: "32px 24px",
            maxWidth: "440px",
            width: "100%",
            border: "2px solid #FFD700",
            boxShadow: "0 20px 50px rgba(0,0,0,0.7)",
            textAlign: "center"
          }}>
            <div style={{
              width: "52px",
              height: "52px",
              borderRadius: "50%",
              backgroundColor: "rgba(0, 230, 118, 0.2)",
              color: "#69F0AE",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              margin: "0 auto 12px auto"
            }}>
              <CheckCircle2 style={{ width: "28px", height: "28px" }} />
            </div>

            <h2 style={{ fontSize: "22px", fontWeight: "900", color: "#FFF", marginBottom: "4px" }}>
              Game Code Ready!
            </h2>
            <div style={{
              fontSize: "32px",
              fontWeight: "900",
              color: "#FFD700",
              letterSpacing: "3px",
              marginBottom: "16px",
              textShadow: "0 0 15px rgba(255, 215, 0, 0.5)"
            }}>
              {confirmedId}
            </div>

            <p style={{ fontSize: "14px", color: "rgba(255,255,255,0.8)", marginBottom: "24px" }}>
              Where would you like to go next?
            </p>

            <div style={{ display: "flex", flexDirection: "column", gap: "14px" }}>
              {/* Option 1: Ticket Distribution */}
              {onOpenTicketDistribution && (
                <button
                  onClick={() => {
                    setShowChoiceModal(false);
                    onOpenTicketDistribution(confirmedId);
                  }}
                  style={{
                    padding: "16px",
                    borderRadius: "18px",
                    background: "linear-gradient(135deg, #00E5FF 0%, #0088FF 100%)",
                    color: "#000",
                    border: "none",
                    fontWeight: "900",
                    fontSize: "16px",
                    cursor: "pointer",
                    boxShadow: "0 6px 20px rgba(0, 229, 255, 0.4)",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    gap: "10px"
                  }}
                >
                  <Ticket style={{ width: "22px", height: "22px" }} />
                  🎟️ Distribute Tickets (QR / Digital / Paper)
                </button>
              )}

              {/* Option 2: Setup Game Rules */}
              <button
                onClick={() => {
                  setShowChoiceModal(false);
                  onSubmitGameId(confirmedId);
                }}
                style={{
                  padding: "16px",
                  borderRadius: "18px",
                  background: "linear-gradient(135deg, #7C4DFF 0%, #4A007E 100%)",
                  color: "#FFF",
                  border: "1.5px solid rgba(255, 255, 255, 0.3)",
                  fontWeight: "800",
                  fontSize: "15px",
                  cursor: "pointer",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  gap: "10px"
                }}
              >
                <Sliders style={{ width: "20px", height: "20px" }} />
                ⚙️ Set Up Game Rules ➔
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
