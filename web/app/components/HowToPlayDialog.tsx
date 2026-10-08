"use client";

import React, { useState } from "react";
import { useTranslation } from "../lib/useTranslation";
import { Crown, Zap, Ticket, Volume2, Trophy, FileText, CheckCircle2, X, Sparkles } from "lucide-react";

interface HowToPlayDialogProps {
  onDismiss: () => void;
}

export const HowToPlayDialog: React.FC<HowToPlayDialogProps> = ({ onDismiss }) => {
  const { t } = useTranslation();
  const [activeCategory, setActiveCategory] = useState<"MODES" | "CALLING" | "TICKETS" | "WINNERS">("MODES");

  return (
    <div style={{
      position: "fixed",
      inset: 0,
      backgroundColor: "rgba(0,0,0,0.8)",
      backdropFilter: "blur(8px)",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      zIndex: 1000,
      padding: "16px"
    }}>
      <div style={{
        backgroundColor: "#2A004A",
        color: "#FFF",
        borderRadius: "24px",
        padding: "24px",
        maxWidth: "520px",
        width: "100%",
        maxHeight: "88vh",
        display: "flex",
        flexDirection: "column",
        boxShadow: "0 15px 35px rgba(0,0,0,0.6)",
        border: "1.5px solid rgba(255, 215, 0, 0.4)",
        position: "relative"
      }}>
        {/* Close Button */}
        <button
          onClick={onDismiss}
          style={{
            position: "absolute",
            top: "16px",
            right: "16px",
            background: "rgba(255,255,255,0.1)",
            border: "none",
            color: "#FFF",
            borderRadius: "50%",
            width: "32px",
            height: "32px",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            cursor: "pointer"
          }}
        >
          <X style={{ width: "18px", height: "18px" }} />
        </button>

        {/* Title */}
        <h2 style={{ fontSize: "22px", fontWeight: "900", marginBottom: "4px", textAlign: "center", color: "#FFD700" }}>
          {t.howToPlayTitle}
        </h2>
        <p style={{ fontSize: "12px", color: "rgba(255,255,255,0.7)", marginBottom: "16px", textAlign: "center" }}>
          Everything you need to know about Hosting, Joining & Playing Tambola (Housie)
        </p>

        {/* Navigation Tabs */}
        <div style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr 1fr 1fr",
          gap: "4px",
          backgroundColor: "rgba(0,0,0,0.3)",
          padding: "4px",
          borderRadius: "14px",
          marginBottom: "16px"
        }}>
          <button
            onClick={() => setActiveCategory("MODES")}
            style={{
              padding: "8px 4px",
              borderRadius: "10px",
              backgroundColor: activeCategory === "MODES" ? "#00E5FF" : "transparent",
              color: activeCategory === "MODES" ? "#000" : "#FFF",
              border: "none",
              fontWeight: "bold",
              fontSize: "11px",
              cursor: "pointer"
            }}
          >
            🎮 Modes
          </button>
          <button
            onClick={() => setActiveCategory("CALLING")}
            style={{
              padding: "8px 4px",
              borderRadius: "10px",
              backgroundColor: activeCategory === "CALLING" ? "#FFD700" : "transparent",
              color: activeCategory === "CALLING" ? "#000" : "#FFF",
              border: "none",
              fontWeight: "bold",
              fontSize: "11px",
              cursor: "pointer"
            }}
          >
            🔊 Calling
          </button>
          <button
            onClick={() => setActiveCategory("TICKETS")}
            style={{
              padding: "8px 4px",
              borderRadius: "10px",
              backgroundColor: activeCategory === "TICKETS" ? "#00E676" : "transparent",
              color: activeCategory === "TICKETS" ? "#000" : "#FFF",
              border: "none",
              fontWeight: "bold",
              fontSize: "11px",
              cursor: "pointer"
            }}
          >
            🎟️ Tickets
          </button>
          <button
            onClick={() => setActiveCategory("WINNERS")}
            style={{
              padding: "8px 4px",
              borderRadius: "10px",
              backgroundColor: activeCategory === "WINNERS" ? "#FF4081" : "transparent",
              color: activeCategory === "WINNERS" ? "#000" : "#FFF",
              border: "none",
              fontWeight: "bold",
              fontSize: "11px",
              cursor: "pointer"
            }}
          >
            🏆 Winners
          </button>
        </div>

        {/* Tab Contents */}
        <div style={{ flex: 1, overflowY: "auto", display: "flex", flexDirection: "column", gap: "12px", paddingRight: "4px" }}>
          
          {activeCategory === "MODES" && (
            <>
              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(0,229,255,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#00E5FF", marginBottom: "6px", display: "flex", alignItems: "center", gap: "6px" }}>
                  <Crown style={{ width: "16px", height: "16px" }} />
                  1. Host / Moderated Game
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • Generates a 6-character Game Code (e.g. <code>3NA2RL</code>).<br/>
                  • Features Google Sign-In, real-time Firebase sync, host approval queue, QR code ticket distribution, and digital/paper ticket management.
                </div>
              </div>

              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(255,215,0,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#FFD700", marginBottom: "6px", display: "flex", alignItems: "center", gap: "6px" }}>
                  <Zap style={{ width: "16px", height: "16px" }} />
                  2. Express / Quick Game
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • Generates a 5-character local Game ID (e.g. <code>X7K9P</code>).<br/>
                  • Runs strictly locally in your browser without requiring Google sign-in or Firebase connection. Ideal for quick offline board calling!
                </div>
              </div>

              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(0,230,118,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#69F0AE", marginBottom: "6px", display: "flex", alignItems: "center", gap: "6px" }}>
                  <Ticket style={{ width: "16px", height: "16px" }} />
                  3. Join Game / Self Ticket
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • Players can join host games by scanning QR codes, entering 6-char Game Codes, or generating tickets on their own for unmoderated games.
                </div>
              </div>
            </>
          )}

          {activeCategory === "CALLING" && (
            <>
              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(255,215,0,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#FFD700", marginBottom: "6px", display: "flex", alignItems: "center", gap: "6px" }}>
                  <Volume2 style={{ width: "16px", height: "16px" }} />
                  Voice Caller & Funny Rhymes
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • Click <strong>&quot;Call Next Number&quot;</strong> or enable <strong>&quot;Auto Draw&quot;</strong> with customizable timer speed.<br/>
                  • Supports voice announcements in <strong>26 languages</strong> with funny number nicknames (e.g. &quot;Top of the house - 90&quot;, &quot;Two fat ladies - 88&quot;)!
                </div>
              </div>

              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(0,229,255,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#00E5FF", marginBottom: "6px" }}>
                  1-90 Board Grid
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • All called numbers light up dynamically on the host board.<br/>
                  • Track the last called number and total numbers drawn anytime.
                </div>
              </div>
            </>
          )}

          {activeCategory === "TICKETS" && (
            <>
              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(0,230,118,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#69F0AE", marginBottom: "6px", display: "flex", alignItems: "center", gap: "6px" }}>
                  <Ticket style={{ width: "16px", height: "16px" }} />
                  Standard 3x9 Tambola Ticket
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • Each ticket contains 15 numbers (5 numbers per row across 9 columns).<br/>
                  • Tap numbers on digital tickets to cross them off as numbers are called.<br/>
                  • Switch between multiple tickets seamlessly using ticket tabs!
                </div>
              </div>

              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(255,215,0,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#FFD700", marginBottom: "6px" }}>
                  Digital & Paper Options
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • Hosts can issue digital tickets via QR code or generate printable paper tickets.
                </div>
              </div>
            </>
          )}

          {activeCategory === "WINNERS" && (
            <>
              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(255,64,129,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#FF80AB", marginBottom: "6px", display: "flex", alignItems: "center", gap: "6px" }}>
                  <Trophy style={{ width: "16px", height: "16px" }} />
                  Winning Rules & Live Claims
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • Choose rules like <em>Early 5, Top Line, Middle Line, Bottom Line, Four Corners, Full House</em>, or custom rules.<br/>
                  • Digital ticket players can submit live claims directly to the host for instant verification.
                </div>
              </div>

              <div style={{ backgroundColor: "rgba(255,255,255,0.06)", borderRadius: "14px", padding: "14px", border: "1px solid rgba(255,215,0,0.3)" }}>
                <div style={{ fontWeight: "bold", fontSize: "14px", color: "#FFD700", marginBottom: "6px" }}>
                  Winner Board & PDF Export
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.85)", lineHeight: "1.5" }}>
                  • On the Winner Board, <strong>unclaimed prizes remain at the top</strong>, and <strong>claimed prizes move to the bottom</strong>.<br/>
                  • Download a PDF summary report of all game winners anytime!
                </div>
              </div>
            </>
          )}

        </div>

        <button
          onClick={onDismiss}
          style={{
            marginTop: "16px",
            width: "100%",
            padding: "12px",
            backgroundColor: "#009E60",
            color: "#FFF",
            border: "none",
            borderRadius: "14px",
            fontWeight: "bold",
            fontSize: "15px",
            cursor: "pointer",
            boxShadow: "0 4px 12px rgba(0,158,96,0.4)"
          }}
        >
          {t.gotIt || "Got It! Let's Play"}
        </button>
      </div>
    </div>
  );
};
