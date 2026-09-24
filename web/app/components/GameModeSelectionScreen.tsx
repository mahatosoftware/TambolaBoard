"use client";

import React, { useState } from "react";
import { useTranslation } from "../lib/useTranslation";
import { signInWithGoogleHost } from "../lib/firebase";

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
  const [isSigningIn, setIsSigningIn] = useState(false);
  const [authError, setAuthError] = useState<string | null>(null);

  const handleModeratedClick = async () => {
    setIsSigningIn(true);
    setAuthError(null);
    try {
      await signInWithGoogleHost();
      onSelectModerated();
    } catch (err: any) {
      console.error("Moderated Auth Error:", err);
      if (err.code === "auth/popup-closed-by-user") {
        setAuthError("Google Sign-In was cancelled. Please try again to continue in Moderated Mode.");
      } else if (err.code === "auth/unauthorized-domain") {
        setAuthError("Unauthorized Domain: Please add your domain/IP to Firebase Console -> Authentication -> Settings -> Authorized Domains.");
      } else {
        setAuthError(err.message || "Failed to sign in with Google. Please try again.");
      }
    } finally {
      setIsSigningIn(false);
    }
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
        <h1 style={{ fontSize: "26px", fontWeight: "bold", marginBottom: "36px" }}>
          {t.selectGameModeTitle}
        </h1>

        <div style={{ display: "flex", flexDirection: "column", gap: "16px", width: "100%" }}>
          {authError && (
            <div style={{
              backgroundColor: "rgba(255, 82, 82, 0.2)",
              border: "1px solid #FF5252",
              padding: "12px",
              borderRadius: "14px",
              color: "#FF8A80",
              fontSize: "13px",
              fontWeight: "bold",
              marginBottom: "8px"
            }}>
              {authError}
            </div>
          )}

          <button
            onClick={handleModeratedClick}
            disabled={isSigningIn}
            style={{
              padding: "18px",
              borderRadius: "20px",
              backgroundColor: isSigningIn ? "rgba(0, 158, 96, 0.6)" : "#009E60",
              color: "#FFFFFF",
              border: "none",
              fontSize: "18px",
              fontWeight: "bold",
              cursor: isSigningIn ? "not-allowed" : "pointer",
              boxShadow: "0 4px 12px rgba(0,0,0,0.3)",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              gap: "10px"
            }}
          >
            {isSigningIn ? (
              <span>Signing in with Google...</span>
            ) : (
              <span>📱 {t.moderatedMode} (Sign in with Google)</span>
            )}
          </button>

          <button
            onClick={onSelectUnmoderated}
            disabled={isSigningIn}
            style={{
              padding: "18px",
              borderRadius: "20px",
              backgroundColor: "#4A007E",
              color: "#FFFFFF",
              border: "1px solid rgba(255,255,255,0.2)",
              fontSize: "18px",
              fontWeight: "bold",
              cursor: isSigningIn ? "not-allowed" : "pointer",
              boxShadow: "0 4px 12px rgba(0,0,0,0.3)"
            }}
          >
            🎲 {t.unmoderatedMode}
          </button>

          <button
            onClick={onBack}
            disabled={isSigningIn}
            style={{
              marginTop: "16px",
              padding: "12px",
              borderRadius: "15px",
              backgroundColor: "transparent",
              color: "rgba(255,255,255,0.8)",
              border: "1px solid rgba(255,255,255,0.4)",
              fontSize: "15px",
              cursor: isSigningIn ? "not-allowed" : "pointer"
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
