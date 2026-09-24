"use client";

import React, { useState } from "react";
import Image from "next/image";
import { 
  Sparkles, 
  Play, 
  RotateCcw, 
  Trophy, 
  BookOpen, 
  Settings, 
  Volume2, 
  Ticket, 
  CheckCircle2, 
  Zap,
  ChevronRight,
  ShieldCheck,
  X
} from "lucide-react";
import { HowToPlayDialog } from "./HowToPlayDialog";
import { LanguageSettingsDialog } from "./LanguageSettingsDialog";
import { TicketVisualizer } from "./TicketVisualizer";
import { useTranslation } from "../lib/useTranslation";

interface HomeScreenProps {
  onNewGame: () => void;
  onJoinGame?: () => void;
  onContinue: () => void;
  onViewWinners: () => void;
  hasSavedGame: boolean;
}

export const HomeScreen: React.FC<HomeScreenProps> = ({
  onNewGame,
  onJoinGame,
  onContinue,
  onViewWinners,
  hasSavedGame
}) => {
  const { t } = useTranslation();
  const [showHowToPlay, setShowHowToPlay] = useState(false);
  const [showSettings, setShowSettings] = useState(false);
  const [showTicketPreview, setShowTicketPreview] = useState(false);

  // Preload speech synthesis voices on home screen mount
  React.useEffect(() => {
    if (typeof window !== "undefined" && "speechSynthesis" in window) {
      window.speechSynthesis.getVoices();
      if (window.speechSynthesis.onvoiceschanged !== undefined) {
        window.speechSynthesis.onvoiceschanged = () => {
          window.speechSynthesis.getVoices();
        };
      }
    }
  }, []);

  // Floating 3D Tambola Spheres configuration
  const floatingBalls = [
    { num: 7, top: "6%", left: "6%", size: 68, grad: "radial-gradient(circle at 35% 35%, #00E676 0%, #004D40 100%)", shadow: "0 10px 25px rgba(0,230,118,0.4)", anim: "animate-float-slow" },
    { num: 12, top: "14%", right: "8%", size: 76, grad: "radial-gradient(circle at 35% 35%, #FFD700 0%, #E65100 100%)", shadow: "0 10px 25px rgba(255,215,0,0.4)", anim: "animate-float-reverse" },
    { num: 45, top: "46%", left: "3%", size: 58, grad: "radial-gradient(circle at 35% 35%, #E040FB 0%, #4A148C 100%)", shadow: "0 10px 25px rgba(224,64,251,0.4)", anim: "animate-float-fast" },
    { num: 77, bottom: "20%", left: "7%", size: 72, grad: "radial-gradient(circle at 35% 35%, #FF4081 0%, #880E4F 100%)", shadow: "0 10px 25px rgba(255,64,129,0.4)", anim: "animate-float-slow" },
    { num: 88, bottom: "12%", right: "7%", size: 80, grad: "radial-gradient(circle at 35% 35%, #00E5FF 0%, #006064 100%)", shadow: "0 10px 25px rgba(0,229,255,0.4)", anim: "animate-float-reverse" },
    { num: 90, top: "58%", right: "4%", size: 56, grad: "radial-gradient(circle at 35% 35%, #FF5252 0%, #B71C1C 100%)", shadow: "0 10px 25px rgba(255,82,82,0.4)", anim: "animate-float-fast" },
  ];

  return (
    <div style={{
      minHeight: "100vh",
      background: "radial-gradient(circle at 50% 20%, #7C4DFF 0%, #31005A 50%, #0D001A 100%)",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      justifyContent: "space-between",
      alignItems: "center",
      padding: "24px 16px",
      textAlign: "center",
      position: "relative",
      overflow: "hidden",
      fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
    }}>
      {/* Background Ambient Glowing Lights */}
      <div style={{
        position: "absolute",
        top: "-100px",
        left: "50%",
        transform: "translateX(-50%)",
        width: "500px",
        height: "500px",
        borderRadius: "50%",
        background: "radial-gradient(circle, rgba(124, 77, 255, 0.35) 0%, rgba(0, 0, 0, 0) 70%)",
        pointerEvents: "none",
        zIndex: 0
      }} />

      <div style={{
        position: "absolute",
        bottom: "-150px",
        right: "-100px",
        width: "600px",
        height: "600px",
        borderRadius: "50%",
        background: "radial-gradient(circle, rgba(0, 229, 255, 0.18) 0%, rgba(0, 0, 0, 0) 70%)",
        pointerEvents: "none",
        zIndex: 0
      }} />

      {/* Floating 3D Ambient Tambola Balls */}
      {floatingBalls.map((ball) => (
        <div
          key={ball.num}
          className={ball.anim}
          style={{
            position: "absolute",
            top: ball.top,
            bottom: ball.bottom,
            left: ball.left,
            right: ball.right,
            width: `${ball.size}px`,
            height: `${ball.size}px`,
            borderRadius: "50%",
            background: ball.grad,
            boxShadow: ball.shadow,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            fontWeight: "900",
            fontSize: `${Math.round(ball.size * 0.38)}px`,
            color: "#FFFFFF",
            textShadow: "0 2px 4px rgba(0,0,0,0.5)",
            border: "1.5px solid rgba(255, 255, 255, 0.4)",
            pointerEvents: "none",
            zIndex: 1,
            userSelect: "none"
          }}
        >
          {/* Sphere Gloss Reflection */}
          <div style={{
            position: "absolute",
            top: "12%",
            left: "18%",
            width: "35%",
            height: "35%",
            borderRadius: "50%",
            background: "radial-gradient(circle, rgba(255,255,255,0.7) 0%, rgba(255,255,255,0) 80%)",
            pointerEvents: "none"
          }} />
          {ball.num}
        </div>
      ))}

      {/* Main Container */}
      <div style={{
        flex: 1,
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        maxWidth: "480px",
        width: "100%",
        zIndex: 2,
        margin: "auto 0"
      }}>
        
        {/* Edition Pill Badge */}
        <div className="glass-pill" style={{
          display: "inline-flex",
          alignItems: "center",
          gap: "6px",
          padding: "6px 16px",
          borderRadius: "30px",
          fontSize: "12px",
          fontWeight: "700",
          letterSpacing: "1px",
          textTransform: "uppercase",
          color: "#FFE082",
          marginBottom: "18px",
          boxShadow: "0 4px 15px rgba(0,0,0,0.2)"
        }}>
          <Sparkles style={{ width: "14px", height: "14px", color: "#FFD700" }} />
          {t.proHostEdition}
        </div>

        {/* Glowing Logo Container */}
        <div style={{
          position: "relative",
          width: "156px",
          height: "156px",
          marginBottom: "18px",
          borderRadius: "32px",
          padding: "8px",
          background: "linear-gradient(135deg, rgba(255,255,255,0.3) 0%, rgba(255,255,255,0.08) 100%)",
          backdropFilter: "blur(16px)",
          border: "2px solid rgba(255, 255, 255, 0.4)",
          boxShadow: "0 15px 35px rgba(0,0,0,0.5), 0 0 40px rgba(255, 215, 0, 0.4)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          overflow: "hidden"
        }}>
          <Image
            src="/app_logo.png"
            alt="Tambola Board Logo"
            width={140}
            height={140}
            style={{ objectFit: "contain", borderRadius: "24px", filter: "drop-shadow(0 6px 12px rgba(0,0,0,0.4))" }}
            priority
          />
        </div>

        {/* Title */}
        <h1 style={{
          fontSize: "36px",
          fontWeight: "900",
          marginBottom: "6px",
          letterSpacing: "1.5px",
          background: "linear-gradient(135deg, #FFFFFF 0%, #FFE57F 50%, #E040FB 100%)",
          WebkitBackgroundClip: "text",
          WebkitTextFillColor: "transparent",
          filter: "drop-shadow(0 3px 6px rgba(0,0,0,0.4))"
        }}>
          {t.appTitle}
        </h1>

        <p style={{
          fontSize: "14px",
          color: "rgba(255, 255, 255, 0.85)",
          marginBottom: "20px",
          fontWeight: "500",
          letterSpacing: "0.2px"
        }}>
          {t.appTagline}
        </p>

        {/* Feature Highlights Strip */}
        <div style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          gap: "12px",
          marginBottom: "26px",
          flexWrap: "wrap"
        }}>
          <span className="glass-pill" style={{ padding: "4px 12px", borderRadius: "12px", fontSize: "12px", display: "flex", alignItems: "center", gap: "4px", color: "#E0F7FA" }}>
            <Volume2 style={{ width: "13px", height: "13px", color: "#00E5FF" }} /> {t.voiceCallerPill}
          </span>
          <span className="glass-pill" style={{ padding: "4px 12px", borderRadius: "12px", fontSize: "12px", display: "flex", alignItems: "center", gap: "4px", color: "#FFF9C4" }}>
            <Zap style={{ width: "13px", height: "13px", color: "#FFD700" }} /> {t.autoDrawPill}
          </span>
          <span className="glass-pill" style={{ padding: "4px 12px", borderRadius: "12px", fontSize: "12px", display: "flex", alignItems: "center", gap: "4px", color: "#F8BBD0" }}>
            <Trophy style={{ width: "13px", height: "13px", color: "#FF4081" }} /> {t.winnerPdfPill}
          </span>
        </div>

        {/* Glassmorphism Action Dashboard */}
        <div className="glass-panel" style={{
          display: "flex",
          flexDirection: "column",
          gap: "14px",
          width: "100%",
          padding: "24px",
          borderRadius: "28px"
        }}>
          {/* Start New Game Button */}
          <button
            onClick={onNewGame}
            className="btn-hover-lift"
            style={{
              padding: "18px 20px",
              borderRadius: "18px",
              background: "linear-gradient(135deg, #00E676 0%, #00B0FF 100%)",
              color: "#FFFFFF",
              border: "none",
              cursor: "pointer",
              boxShadow: "0 8px 25px rgba(0, 230, 118, 0.45)",
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              textAlign: "left"
            }}
          >
            <div style={{ display: "flex", alignItems: "center", gap: "14px" }}>
              <div style={{
                width: "44px",
                height: "44px",
                borderRadius: "12px",
                backgroundColor: "rgba(255, 255, 255, 0.25)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
              }}>
                <Play style={{ width: "24px", height: "24px", fill: "#FFFFFF", color: "#FFFFFF" }} />
              </div>
              <div>
                <div style={{ fontSize: "18px", fontWeight: "800", letterSpacing: "0.3px" }}>
                  {t.startNewGame}
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255, 255, 255, 0.9)", fontWeight: "500" }}>
                  {t.startNewGameSub}
                </div>
              </div>
            </div>
            <ChevronRight style={{ width: "22px", height: "22px", opacity: 0.8 }} />
          </button>

          {/* Join Game as Player Button */}
          {onJoinGame && (
            <button
              onClick={onJoinGame}
              className="btn-hover-lift"
              style={{
                padding: "16px 20px",
                borderRadius: "18px",
                background: "linear-gradient(135deg, #00E5FF 0%, #0088FF 100%)",
                color: "#FFFFFF",
                border: "none",
                cursor: "pointer",
                boxShadow: "0 6px 20px rgba(0, 229, 255, 0.35)",
                display: "flex",
                alignItems: "center",
                justifyContent: "space-between",
                textAlign: "left"
              }}
            >
              <div style={{ display: "flex", alignItems: "center", gap: "14px" }}>
                <div style={{
                  width: "42px",
                  height: "42px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(255, 255, 255, 0.25)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center"
                }}>
                  <Ticket style={{ width: "22px", height: "22px", color: "#FFFFFF" }} />
                </div>
                <div>
                  <div style={{ fontSize: "16px", fontWeight: "700" }}>
                    Join Game / Get Ticket
                  </div>
                  <div style={{ fontSize: "12px", color: "rgba(255, 255, 255, 0.9)", fontWeight: "500" }}>
                    Scan QR or enter Game Code to get your ticket
                  </div>
                </div>
              </div>
              <ChevronRight style={{ width: "20px", height: "20px", opacity: 0.8 }} />
            </button>
          )}

          {/* Continue Last Game Button */}
          <button
            onClick={onContinue}
            disabled={!hasSavedGame}
            className={hasSavedGame ? "btn-hover-lift" : ""}
            style={{
              padding: "16px 20px",
              borderRadius: "18px",
              background: hasSavedGame
                ? "linear-gradient(135deg, rgba(124,77,255,0.6) 0%, rgba(49,0,90,0.8) 100%)"
                : "rgba(255, 255, 255, 0.04)",
              color: hasSavedGame ? "#FFFFFF" : "rgba(255, 255, 255, 0.3)",
              border: hasSavedGame ? "1.5px solid rgba(255, 215, 0, 0.4)" : "1px solid rgba(255, 255, 255, 0.08)",
              cursor: hasSavedGame ? "pointer" : "not-allowed",
              boxShadow: hasSavedGame ? "0 6px 20px rgba(124, 77, 255, 0.3)" : "none",
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              textAlign: "left",
              transition: "all 0.2s ease"
            }}
          >
            <div style={{ display: "flex", alignItems: "center", gap: "14px" }}>
              <div style={{
                width: "42px",
                height: "42px",
                borderRadius: "12px",
                backgroundColor: hasSavedGame ? "rgba(255, 255, 255, 0.15)" : "rgba(255, 255, 255, 0.05)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
              }}>
                <RotateCcw style={{ width: "22px", height: "22px", color: hasSavedGame ? "#FFD700" : "inherit" }} />
              </div>
              <div>
                <div style={{ fontSize: "16px", fontWeight: "700", display: "flex", alignItems: "center", gap: "8px" }}>
                  {t.continueLastGame}
                  {hasSavedGame && (
                    <span className="animate-badge-pulse" style={{
                      backgroundColor: "#FFD700",
                      color: "#000000",
                      fontSize: "10px",
                      fontWeight: "900",
                      padding: "2px 8px",
                      borderRadius: "10px",
                      letterSpacing: "0.5px"
                    }}>
                      {t.resumeBadge}
                    </span>
                  )}
                </div>
                <div style={{ fontSize: "12px", color: hasSavedGame ? "rgba(255, 255, 255, 0.75)" : "rgba(255, 255, 255, 0.3)" }}>
                  {hasSavedGame ? t.continueLastGameSub : t.noSavedSession}
                </div>
              </div>
            </div>
            <ChevronRight style={{ width: "20px", height: "20px", opacity: hasSavedGame ? 0.8 : 0.3 }} />
          </button>

          {/* View Winners Leaderboard Button */}
          <button
            onClick={onViewWinners}
            className="btn-hover-lift"
            style={{
              padding: "16px 20px",
              borderRadius: "18px",
              background: "linear-gradient(135deg, #FFB300 0%, #FF6F00 100%)",
              color: "#FFFFFF",
              border: "none",
              cursor: "pointer",
              boxShadow: "0 6px 20px rgba(255, 179, 0, 0.35)",
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              textAlign: "left"
            }}
          >
            <div style={{ display: "flex", alignItems: "center", gap: "14px" }}>
              <div style={{
                width: "42px",
                height: "42px",
                borderRadius: "12px",
                backgroundColor: "rgba(255, 255, 255, 0.25)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
              }}>
                <Trophy style={{ width: "22px", height: "22px", color: "#FFFFFF" }} />
              </div>
              <div>
                <div style={{ fontSize: "16px", fontWeight: "700" }}>
                  {t.viewWinners}
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255, 255, 255, 0.9)", fontWeight: "500" }}>
                  {t.viewWinnersSub}
                </div>
              </div>
            </div>
            <ChevronRight style={{ width: "20px", height: "20px", opacity: 0.8 }} />
          </button>

          {/* 3-Column Toolbar: How to Play, Settings & Ticket Pattern */}
          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: "10px", marginTop: "4px" }}>
            <button
              onClick={() => setShowHowToPlay(true)}
              className="btn-hover-lift"
              style={{
                padding: "12px 8px",
                borderRadius: "14px",
                backgroundColor: "rgba(255, 255, 255, 0.12)",
                color: "#FFFFFF",
                border: "1px solid rgba(255, 255, 255, 0.2)",
                fontSize: "13px",
                fontWeight: "600",
                cursor: "pointer",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: "6px"
              }}
            >
              <BookOpen style={{ width: "18px", height: "18px", color: "#80DEEA" }} />
              {t.howToPlay}
            </button>

            <button
              onClick={() => setShowSettings(true)}
              className="btn-hover-lift"
              style={{
                padding: "12px 8px",
                borderRadius: "14px",
                backgroundColor: "rgba(255, 255, 255, 0.12)",
                color: "#FFFFFF",
                border: "1px solid rgba(255, 255, 255, 0.2)",
                fontSize: "13px",
                fontWeight: "600",
                cursor: "pointer",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: "6px"
              }}
            >
              <Settings style={{ width: "18px", height: "18px", color: "#FFD54F" }} />
              {t.settings}
            </button>

            <button
              onClick={() => setShowTicketPreview(true)}
              className="btn-hover-lift"
              style={{
                padding: "12px 8px",
                borderRadius: "14px",
                backgroundColor: "rgba(255, 255, 255, 0.12)",
                color: "#FFFFFF",
                border: "1px solid rgba(255, 255, 255, 0.2)",
                fontSize: "13px",
                fontWeight: "600",
                cursor: "pointer",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: "6px"
              }}
            >
              <Ticket style={{ width: "18px", height: "18px", color: "#F48FB1" }} />
              {t.ticketSample}
            </button>
          </div>
        </div>
      </div>

      {/* Footer Branding */}
      <footer style={{ marginTop: "20px", fontSize: "12px", color: "rgba(255, 255, 255, 0.65)", zIndex: 2, display: "flex", alignItems: "center", gap: "6px" }}>
        <ShieldCheck style={{ width: "14px", height: "14px", color: "#00E676" }} />
        Mahato Software • Crafted with ❤️ for Tambola lovers
      </footer>

      {/* Dialog Modals */}
      {showHowToPlay && <HowToPlayDialog onDismiss={() => setShowHowToPlay(false)} />}
      {showSettings && <LanguageSettingsDialog onDismiss={() => setShowSettings(false)} />}
      
      {/* Sample Ticket Pattern Dialog Modal */}
      {showTicketPreview && (
        <div style={{
          position: "fixed",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.75)",
          backdropFilter: "blur(8px)",
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
            maxWidth: "460px",
            width: "100%",
            boxShadow: "0 15px 35px rgba(0,0,0,0.6)",
            border: "1.5px solid rgba(255,255,255,0.2)",
            position: "relative"
          }}>
            <button
              onClick={() => setShowTicketPreview(false)}
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

            <h3 style={{ fontSize: "20px", fontWeight: "bold", marginBottom: "12px", textAlign: "center", display: "flex", alignItems: "center", justifyContent: "center", gap: "8px" }}>
              <Ticket style={{ color: "#F48FB1" }} /> Standard Tambola Ticket
            </h3>
            
            <p style={{ fontSize: "13px", color: "rgba(255,255,255,0.8)", marginBottom: "16px", textAlign: "center" }}>
              Each standard 3x9 ticket contains 15 numbers (5 per row across 9 column ranges).
            </p>

            <TicketVisualizer highlightedIndices={[0, 4, 11, 15, 20]} />

            <button
              onClick={() => setShowTicketPreview(false)}
              style={{
                marginTop: "20px",
                width: "100%",
                padding: "12px",
                backgroundColor: "#00E676",
                color: "#000",
                border: "none",
                borderRadius: "14px",
                fontWeight: "bold",
                fontSize: "15px",
                cursor: "pointer"
              }}
            >
              Close Ticket Preview
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
