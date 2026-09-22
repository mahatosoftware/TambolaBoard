"use client";

import React from "react";
import { useTranslation } from "../lib/useTranslation";

interface HowToPlayDialogProps {
  onDismiss: () => void;
}

export const HowToPlayDialog: React.FC<HowToPlayDialogProps> = ({ onDismiss }) => {
  const { t } = useTranslation();

  return (
    <div style={{
      position: "fixed",
      inset: 0,
      backgroundColor: "rgba(0,0,0,0.7)",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      zIndex: 1000,
      padding: "16px"
    }}>
      <div style={{
        backgroundColor: "#6C0BA9",
        color: "#FFF",
        borderRadius: "20px",
        padding: "24px",
        maxWidth: "500px",
        width: "100%",
        boxShadow: "0 10px 25px rgba(0,0,0,0.5)",
        border: "1px solid rgba(255,255,255,0.2)"
      }}>
        <h2 style={{ fontSize: "22px", fontWeight: "bold", marginBottom: "16px", textAlign: "center" }}>
          {t.howToPlayTitle}
        </h2>
        
        <div style={{ fontSize: "14px", lineHeight: "1.6", display: "flex", flexDirection: "column", gap: "12px", maxHeight: "60vh", overflowY: "auto", paddingRight: "4px" }}>
          <p>
            <strong>1. Host Board Setup:</strong> Select game mode (Moderated or Host), choose the winning rules (Full House, Top Line, Early Five, etc.), and set point/prize quantities.
          </p>
          <p>
            <strong>2. Calling Numbers:</strong> Click &quot;Call Next Number&quot; or enable &quot;Auto Call&quot;. The app draws numbers randomly from 1 to 90 and speaks funny rhymes!
          </p>
          <p>
            <strong>3. Ticket Marking:</strong> Players cross off numbers on their 3x9 Tambola tickets.
          </p>
          <p>
            <strong>4. Claiming Prizes:</strong> When a player completes a pattern, click &quot;Claim Prize / Winner Board&quot;, select the prize, enter the winner&apos;s name, and check the claim box.
          </p>
          <p>
            <strong>5. Save & Share:</strong> View the winner list anytime or download a PDF summary!
          </p>
        </div>

        <button
          onClick={onDismiss}
          style={{
            marginTop: "20px",
            width: "100%",
            padding: "12px",
            backgroundColor: "#009E60",
            color: "#FFF",
            border: "none",
            borderRadius: "10px",
            fontWeight: "bold",
            fontSize: "16px",
            cursor: "pointer"
          }}
        >
          {t.gotIt}
        </button>
      </div>
    </div>
  );
};
