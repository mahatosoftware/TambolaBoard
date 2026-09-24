"use client";

import React, { useState, useEffect } from "react";
import QRCode from "qrcode";
import { X, Tv, Users } from "lucide-react";

interface HostQrScreenProps {
  gameId: string;
  joinedCount: number;
  onClose: () => void;
}

export const HostQrScreen: React.FC<HostQrScreenProps> = ({
  gameId,
  joinedCount,
  onClose
}) => {
  const [qrUrl, setQrUrl] = useState<string | null>(null);

  useEffect(() => {
    const origin = typeof window !== "undefined" ? window.location.origin : "https://tambola.app";
    const joinLink = `${origin}/join/${gameId.toUpperCase()}`;

    QRCode.toDataURL(joinLink, {
      width: 400,
      margin: 1,
      color: {
        dark: "#000000",
        light: "#FFFFFF"
      }
    })
      .then((url) => setQrUrl(url))
      .catch((err) => console.error("Error generating QR code:", err));
  }, [gameId]);

  return (
    <div style={{
      position: "fixed",
      inset: 0,
      backgroundColor: "#0D001A",
      backgroundImage: "radial-gradient(circle at 50% 30%, #4A0072 0%, #1A0033 60%, #0D001A 100%)",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "space-between",
      padding: "32px 24px",
      zIndex: 2000,
      fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
    }}>
      {/* Top Bar */}
      <div style={{ width: "100%", maxWidth: "1200px", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <div style={{ display: "flex", alignItems: "center", gap: "10px", fontSize: "16px", fontWeight: "bold", color: "#FFD700" }}>
          <Tv style={{ width: "24px", height: "24px" }} />
          TAMBOLA HOST DISPLAY
        </div>

        <button
          onClick={onClose}
          style={{
            padding: "10px 18px",
            borderRadius: "14px",
            backgroundColor: "rgba(255,255,255,0.15)",
            color: "#FFF",
            border: "1px solid rgba(255,255,255,0.3)",
            fontWeight: "bold",
            cursor: "pointer",
            display: "flex",
            alignItems: "center",
            gap: "8px"
          }}
        >
          <X style={{ width: "18px", height: "18px" }} />
          Close TV View
        </button>
      </div>

      {/* Main TV Card */}
      <div style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        textAlign: "center",
        maxWidth: "600px",
        width: "100%",
        backgroundColor: "rgba(255, 255, 255, 0.05)",
        backdropFilter: "blur(20px)",
        padding: "36px",
        borderRadius: "32px",
        border: "2px solid rgba(255, 215, 0, 0.4)",
        boxShadow: "0 20px 60px rgba(0,0,0,0.6)"
      }}>
        <h1 style={{
          fontSize: "clamp(32px, 5vw, 52px)",
          fontWeight: "900",
          letterSpacing: "2px",
          margin: "0 0 8px 0",
          background: "linear-gradient(135deg, #FFFFFF 0%, #FFD700 100%)",
          WebkitBackgroundClip: "text",
          WebkitTextFillColor: "transparent"
        }}>
          JOIN TAMBOLA
        </h1>

        <p style={{ fontSize: "18px", color: "rgba(255,255,255,0.85)", marginBottom: "24px", fontWeight: "500" }}>
          Scan to get your digital ticket
        </p>

        {/* Big High-Contrast QR Code */}
        {qrUrl ? (
          <div style={{
            backgroundColor: "#FFFFFF",
            padding: "16px",
            borderRadius: "24px",
            boxShadow: "0 10px 30px rgba(0,0,0,0.5), 0 0 40px rgba(0, 229, 255, 0.4)",
            marginBottom: "24px"
          }}>
            <img src={qrUrl} alt="Join Game QR" style={{ width: "260px", height: "260px", display: "block" }} />
          </div>
        ) : (
          <div style={{ width: "260px", height: "260px", backgroundColor: "rgba(255,255,255,0.1)", borderRadius: "24px", marginBottom: "24px", display: "flex", alignItems: "center", justifyContent: "center" }}>
            Loading QR Code...
          </div>
        )}

        {/* Game Code Display */}
        <div style={{ fontSize: "14px", fontWeight: "bold", color: "rgba(255,255,255,0.7)", letterSpacing: "1px", textTransform: "uppercase" }}>
          GAME CODE
        </div>

        <div style={{
          fontSize: "clamp(36px, 6vw, 64px)",
          fontWeight: "900",
          color: "#FFD700",
          letterSpacing: "4px",
          margin: "4px 0 16px 0",
          textShadow: "0 0 20px rgba(255, 215, 0, 0.6)"
        }}>
          {gameId.toUpperCase()}
        </div>

        <div style={{ fontSize: "14px", color: "rgba(255,255,255,0.75)", lineHeight: "1.5" }}>
          Can&apos;t scan? Open <strong>Tambola Tickets</strong> on your phone and enter:
          <br />
          <strong style={{ color: "#00E5FF", fontSize: "18px" }}>{gameId.toUpperCase()}</strong>
        </div>
      </div>

      {/* Real-time Joined Players Pill */}
      <div style={{
        display: "inline-flex",
        alignItems: "center",
        gap: "10px",
        backgroundColor: "rgba(0, 230, 118, 0.2)",
        border: "1.5px solid #00E676",
        padding: "10px 24px",
        borderRadius: "30px",
        fontSize: "18px",
        fontWeight: "900",
        color: "#69F0AE",
        boxShadow: "0 4px 20px rgba(0, 230, 118, 0.3)"
      }}>
        <Users style={{ width: "22px", height: "22px" }} />
        {joinedCount} Players Joined
      </div>
    </div>
  );
};
