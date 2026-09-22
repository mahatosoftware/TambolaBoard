"use client";

import React, { useState, useEffect, useCallback, useRef } from "react";
import Image from "next/image";
import QRCode from "qrcode";
import { TambolaRule } from "../lib/rules";
import { PrizeItem, saveGameState, loadSelectedLanguage, loadVoiceGender } from "../lib/storage";
import { getFunnyPhrase, BCP47_MAP } from "../lib/funnyPhrases";
import { WinnerBoardModal } from "./WinnerBoardModal";
import { useTranslation } from "../lib/useTranslation";

interface GameScreenProps {
  gameId: string;
  initialCalledNumbers?: number[];
  initialLastNumber?: number | null;
  selectedRules: TambolaRule[];
  initialPrizes?: PrizeItem[];
  onReturnToMainMenu: () => void;
  onResetGame: () => void;
}

export const GameScreen: React.FC<GameScreenProps> = ({
  gameId,
  initialCalledNumbers = [],
  initialLastNumber = null,
  selectedRules,
  initialPrizes = [],
  onReturnToMainMenu,
  onResetGame
}) => {
  const { t } = useTranslation();
  const [calledNumbers, setCalledNumbers] = useState<number[]>(initialCalledNumbers);
  const [lastNumber, setLastNumber] = useState<number | null>(initialLastNumber);
  const [isAutoCalling, setIsAutoCalling] = useState(false);
  const [showWinnerModal, setShowWinnerModal] = useState(false);
  const [showResetDialog, setShowResetDialog] = useState(false);
  const [showUndoDialog, setShowUndoDialog] = useState(false);
  const [showExitDialog, setShowExitDialog] = useState(false);
  const [qrDataUrl, setQrDataUrl] = useState<string | null>(null);

  // Initialize prize list from rules if initialPrizes is empty
  const [prizes, setPrizes] = useState<PrizeItem[]>(() => {
    if (initialPrizes.length > 0) return initialPrizes;
    const generated: PrizeItem[] = [];
    selectedRules.forEach((rule) => {
      const qty = rule.quantity || 1;
      for (let i = 0; i < qty; i++) {
        generated.push({
          id: `${rule.id}-${i}-${Date.now()}`,
          ruleId: rule.id,
          ruleName: qty > 1 ? `${rule.name} #${i + 1}` : rule.name,
          winnerName: "",
          isClaimed: false
        });
      }
    });
    return generated;
  });

  // Generate QR Code for Game ID
  useEffect(() => {
    if (gameId) {
      QRCode.toDataURL(gameId, { width: 120, margin: 1 })
        .then((url) => setQrDataUrl(url))
        .catch((err) => console.error("QR Code Error:", err));
    }
  }, [gameId]);

  // Persist game state on changes
  useEffect(() => {
    saveGameState({
      gameId,
      calledNumbers,
      lastNumber,
      selectedRules,
      prizes
    });
  }, [gameId, calledNumbers, lastNumber, selectedRules, prizes]);

  // Preload speech synthesis voices on GameScreen mount
  useEffect(() => {
    if (typeof window !== "undefined" && "speechSynthesis" in window) {
      window.speechSynthesis.getVoices();
      if (window.speechSynthesis.onvoiceschanged !== undefined) {
        window.speechSynthesis.onvoiceschanged = () => {
          window.speechSynthesis.getVoices();
        };
      }
    }
  }, []);

  // Dedicated helper to locate female/lady voice
  const selectFemaleVoice = useCallback((lang: string, bcp47Code: string): SpeechSynthesisVoice | null => {
    if (typeof window === "undefined" || !("speechSynthesis" in window)) return null;
    let voices = window.speechSynthesis.getVoices();
    if (!voices || voices.length === 0) return null;

    const targetLang = lang.toLowerCase();
    const langPrefix = bcp47Code.toLowerCase().substring(0, 2);

    const femaleKeywords = [
      "veena", "aditi", "lekha", "samantha", "karen", "victoria", "zira", 
      "eva", "jenny", "aria", "moira", "fiona", "siri", "google", "female", "lady", "woman",
      "sangeeta", "neerja", "ananya", "shravanti", "alice", "amanda", "catherine", 
      "claire", "monica", "paulina", "sara", "yuki", "kyoko", "ting-ting", "sin-ji", 
      "meijia", "yuna", "sora", "nora", "helena", "laura", "carmela", "marisca", "amelie", "marie"
    ];

    const maleExclusions = [
      "rishi", "david", "mark", "george", "james", "male", "guy", "alex", "daniel", "fred", 
      "kalpana", "man", "boy", "thomas", "nicolas", "stefan", "pavel", "diego", "jorge", "tarik", "youssef"
    ];

    // 1. Target language match + Female keyword + Not male
    let preferred = voices.find((v) => {
      const name = v.name.toLowerCase();
      const voiceLang = v.lang.toLowerCase();
      const matchesLang = voiceLang.startsWith(targetLang) || voiceLang.startsWith(langPrefix);
      const isFemale = femaleKeywords.some((k) => name.includes(k));
      const isMale = maleExclusions.some((k) => name.includes(k));
      return matchesLang && isFemale && !isMale;
    });

    // 2. Target language match + Not male
    if (!preferred) {
      preferred = voices.find((v) => {
        const name = v.name.toLowerCase();
        const voiceLang = v.lang.toLowerCase();
        const matchesLang = voiceLang.startsWith(targetLang) || voiceLang.startsWith(langPrefix);
        const isMale = maleExclusions.some((k) => name.includes(k));
        return matchesLang && !isMale;
      });
    }

    // 3. Any Female voice overall
    if (!preferred) {
      preferred = voices.find((v) => {
        const name = v.name.toLowerCase();
        const isFemale = femaleKeywords.some((k) => name.includes(k));
        const isMale = maleExclusions.some((k) => name.includes(k));
        return isFemale && !isMale;
      });
    }

    // 4. Any voice that is not explicitly male
    if (!preferred) {
      preferred = voices.find((v) => {
        const name = v.name.toLowerCase();
        return !maleExclusions.some((k) => name.includes(k));
      }) || voices[0];
    }

    return preferred || null;
  }, []);

  // Speech announcement helper with exclusive lady (female) voice selection & intonation
  const speakNumber = useCallback((num: number) => {
    if (typeof window === "undefined" || !("speechSynthesis" in window)) return;
    try {
      window.speechSynthesis.cancel();
      const lang = loadSelectedLanguage();
      const bcp47Code = BCP47_MAP[lang] || "en-US";
      const phraseText = getFunnyPhrase(num, lang);

      const performSpeak = () => {
        const utterance = new SpeechSynthesisUtterance(phraseText);
        utterance.lang = bcp47Code;

        const femaleVoice = selectFemaleVoice(lang, bcp47Code);
        if (femaleVoice) {
          utterance.voice = femaleVoice;
        }

        utterance.rate = 0.90;
        utterance.pitch = 1.20; // High feminine pitch intonation
        utterance.volume = 1.0;
        window.speechSynthesis.speak(utterance);
      };

      const currentVoices = window.speechSynthesis.getVoices();
      if (currentVoices.length === 0) {
        // If voices are loading asynchronously on first call, wait for onvoiceschanged or fallback
        let spoken = false;
        const handleVoicesChanged = () => {
          if (!spoken) {
            spoken = true;
            if (typeof window !== "undefined" && "speechSynthesis" in window) {
              window.speechSynthesis.onvoiceschanged = null;
            }
            performSpeak();
          }
        };
        window.speechSynthesis.onvoiceschanged = handleVoicesChanged;
        setTimeout(() => {
          if (!spoken) {
            spoken = true;
            performSpeak();
          }
        }, 150);
      } else {
        performSpeak();
      }
    } catch (e) {
      console.error("Speech Synthesis Error:", e);
    }
  }, [selectFemaleVoice]);

  // Call Next Number
  const callNextNumber = useCallback(() => {
    if (calledNumbers.length >= 90) {
      setIsAutoCalling(false);
      return;
    }

    const available = Array.from({ length: 90 }, (_, i) => i + 1).filter(
      (n) => !calledNumbers.includes(n)
    );

    if (available.length === 0) {
      setIsAutoCalling(false);
      return;
    }

    const nextNum = available[Math.floor(Math.random() * available.length)];
    const updated = [...calledNumbers, nextNum];
    setCalledNumbers(updated);
    setLastNumber(nextNum);
    speakNumber(nextNum);
  }, [calledNumbers, speakNumber]);

  // Auto-Calling Timer Loop
  const autoCallRef = useRef<NodeJS.Timeout | null>(null);
  useEffect(() => {
    if (isAutoCalling) {
      autoCallRef.current = setInterval(() => {
        callNextNumber();
      }, 6000);
    } else if (autoCallRef.current) {
      clearInterval(autoCallRef.current);
    }
    return () => {
      if (autoCallRef.current) clearInterval(autoCallRef.current);
    };
  }, [isAutoCalling, callNextNumber]);

  // Undo Last Called Number
  const handleUndo = () => {
    if (calledNumbers.length === 0) return;
    const updated = calledNumbers.slice(0, -1);
    setCalledNumbers(updated);
    setLastNumber(updated.length > 0 ? updated[updated.length - 1] : null);
    setShowUndoDialog(false);
  };

  // Last 5 Numbers History
  const historyNumbers = calledNumbers.length > 1 ? calledNumbers.slice(0, -1).slice(-5) : [];

  return (
    <div style={{
      minHeight: "100vh",
      height: "100vh",
      maxHeight: "100vh",
      width: "100vw",
      maxWidth: "100vw",
      background: "radial-gradient(circle at 50% 20%, #6C0BA9 0%, #3A0066 50%, #1A0033 100%)",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "flex-start",
      padding: "10px 14px",
      overflow: "hidden",
      boxSizing: "border-box",
      fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
    }}>
      {/* Centered Responsive Container for TV, Laptop, Tablet & Mobile */}
      <div style={{
        width: "100%",
        maxWidth: "1480px",
        height: "100%",
        display: "flex",
        flexDirection: "column",
        margin: "0 auto",
        gap: "8px"
      }}>
      {/* Header Bar: Centered Title & Game ID */}
      <header style={{
        display: "grid",
        gridTemplateColumns: "1fr auto 1fr",
        alignItems: "center",
        padding: "10px 20px",
        background: "linear-gradient(135deg, rgba(255, 255, 255, 0.12) 0%, rgba(255, 255, 255, 0.04) 100%)",
        backdropFilter: "blur(20px)",
        WebkitBackdropFilter: "blur(20px)",
        borderRadius: "20px",
        border: "1.5px solid rgba(255, 255, 255, 0.25)",
        marginBottom: "10px",
        boxShadow: "0 8px 30px rgba(0, 0, 0, 0.4)",
        flexShrink: 0
      }}>
        {/* Left Side: Logo */}
        <div style={{ display: "flex", alignItems: "center", justifyContent: "flex-start" }}>
          <div style={{
            position: "relative",
            width: "52px",
            height: "52px",
            borderRadius: "14px",
            background: "linear-gradient(135deg, #FFD700 0%, #FF6F00 100%)",
            padding: "2.5px",
            boxShadow: "0 0 20px rgba(255, 215, 0, 0.5), 0 4px 12px rgba(0,0,0,0.5)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            flexShrink: 0
          }}>
            <div style={{
              width: "100%",
              height: "100%",
              borderRadius: "12px",
              overflow: "hidden",
              backgroundColor: "#2A004A",
              display: "flex",
              alignItems: "center",
              justifyContent: "center"
            }}>
              <Image
                src="/app_logo.png"
                alt="Tambola Board Logo"
                width={48}
                height={48}
                style={{ objectFit: "contain" }}
                priority
              />
            </div>
          </div>
        </div>

        {/* Center Column: Tambola Board Title & Game ID */}
        <div style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          textAlign: "center",
          gap: "4px"
        }}>
          <h1 style={{
            fontSize: "clamp(26px, 3.5vw, 40px)",
            fontWeight: "900",
            margin: 0,
            lineHeight: 1.05,
            background: "linear-gradient(135deg, #FFFFFF 0%, #FFE57F 45%, #E040FB 100%)",
            WebkitBackgroundClip: "text",
            WebkitTextFillColor: "transparent",
            letterSpacing: "1.5px",
            filter: "drop-shadow(0 4px 10px rgba(0,0,0,0.6))",
            whiteSpace: "nowrap"
          }}>
            🎲 Tambola Board
          </h1>

          {gameId && (
            <span style={{
              background: "linear-gradient(135deg, rgba(255,215,0,0.28), rgba(255,215,0,0.1))",
              padding: "4px 16px",
              borderRadius: "20px",
              fontSize: "14px",
              fontWeight: "900",
              border: "1.5px solid rgba(255,215,0,0.5)",
              color: "#FFD700",
              letterSpacing: "1.2px",
              boxShadow: "0 2px 8px rgba(0,0,0,0.3)",
              whiteSpace: "nowrap"
            }}>
              GAME ID: {gameId}
            </span>
          )}
        </div>

        {/* Right Side: Progress & QR */}
        <div style={{ display: "flex", alignItems: "center", justifyContent: "flex-end", gap: "12px" }}>
          <div style={{
            background: "linear-gradient(135deg, rgba(0, 230, 118, 0.25), rgba(0, 150, 75, 0.15))",
            border: "1.5px solid #00E676",
            padding: "6px 16px",
            borderRadius: "20px",
            fontSize: "13px",
            fontWeight: "800",
            color: "#69F0AE",
            boxShadow: "0 4px 15px rgba(0, 230, 118, 0.2)",
            whiteSpace: "nowrap"
          }}>
            Progress: {calledNumbers.length} / 90
          </div>

          {qrDataUrl && (
            <div style={{
              padding: "3px",
              backgroundColor: "#FFF",
              borderRadius: "8px",
              boxShadow: "0 4px 12px rgba(0,0,0,0.3)",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              flexShrink: 0
            }}>
              <img src={qrDataUrl} alt="Game QR" style={{ width: "38px", height: "38px", borderRadius: "5px" }} />
            </div>
          )}
        </div>
      </header>

      {/* Hero Control Dashboard */}
      <div style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        gap: "12px",
        flexWrap: "wrap",
        background: "rgba(255, 255, 255, 0.05)",
        backdropFilter: "blur(10px)",
        padding: "8px 14px",
        borderRadius: "16px",
        border: "1px solid rgba(255, 255, 255, 0.12)",
        marginBottom: "8px",
        boxShadow: "0 6px 20px rgba(0,0,0,0.25)",
        flexShrink: 0
      }}>
        {/* Giant Hero Last Called Number Display */}
        <div style={{
          display: "flex",
          alignItems: "center",
          gap: "14px",
          background: "linear-gradient(135deg, rgba(74, 0, 126, 0.8), rgba(42, 0, 74, 0.9))",
          padding: "6px 18px",
          borderRadius: "14px",
          border: "1.5px solid rgba(255, 215, 0, 0.5)",
          boxShadow: "0 4px 15px rgba(0,0,0,0.4)"
        }}>
          <div style={{ display: "flex", flexDirection: "column" }}>
            <span style={{ fontSize: "10px", fontWeight: "bold", color: "rgba(255,255,255,0.7)", letterSpacing: "1px" }}>
              CURRENT NUMBER
            </span>
            <span style={{ fontSize: "32px", fontWeight: "900", color: "#FFD700", lineHeight: "1", textShadow: "0 0 10px rgba(255,215,0,0.5)" }}>
              {lastNumber !== null ? lastNumber : "--"}
            </span>
          </div>
        </div>

        {/* Last 5 History Strip */}
        {historyNumbers.length > 0 && (
          <div style={{ display: "flex", alignItems: "center", gap: "6px" }}>
            <span style={{ fontSize: "10px", fontWeight: "bold", color: "rgba(255,255,255,0.6)", letterSpacing: "0.5px" }}>
              RECENT:
            </span>
            {historyNumbers.map((num, i) => (
              <div
                key={i}
                style={{
                  width: "30px",
                  height: "30px",
                  borderRadius: "50%",
                  background: "linear-gradient(135deg, #FFFFFF, #E0E0E0)",
                  color: "#3A0066",
                  fontWeight: "800",
                  fontSize: "13px",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  boxShadow: "0 2px 8px rgba(0,0,0,0.3)"
                }}
              >
                {num}
              </div>
            ))}
          </div>
        )}

        {/* Styled Action Buttons */}
        <div style={{ display: "flex", gap: "8px", flexWrap: "wrap", alignItems: "center" }}>
          <button
            onClick={callNextNumber}
            style={{
              padding: "9px 18px",
              borderRadius: "14px",
              background: "linear-gradient(135deg, #00C853 0%, #009E60 100%)",
              color: "#FFF",
              border: "none",
              fontWeight: "800",
              fontSize: "14px",
              cursor: "pointer",
              boxShadow: "0 4px 15px rgba(0, 200, 83, 0.4)",
              transition: "transform 0.1s ease, boxShadow 0.1s ease"
            }}
            onMouseDown={(e) => (e.currentTarget.style.transform = "scale(0.96)")}
            onMouseUp={(e) => (e.currentTarget.style.transform = "scale(1)")}
          >
            📢 {t.callNextNumber}
          </button>

          <button
            onClick={() => setIsAutoCalling(!isAutoCalling)}
            style={{
              padding: "9px 14px",
              borderRadius: "14px",
              background: isAutoCalling
                ? "linear-gradient(135deg, #FFB300 0%, #FF8F00 100%)"
                : "rgba(255, 255, 255, 0.15)",
              color: isAutoCalling ? "#000" : "#FFF",
              border: isAutoCalling ? "none" : "1px solid rgba(255, 255, 255, 0.25)",
              fontWeight: "700",
              fontSize: "13px",
              cursor: "pointer",
              boxShadow: isAutoCalling ? "0 4px 12px rgba(255, 179, 0, 0.4)" : "none"
            }}
          >
            {isAutoCalling ? `⏸ ${t.pauseAutoCall}` : `▶ ${t.autoCall}`}
          </button>

          <button
            onClick={() => setShowWinnerModal(true)}
            style={{
              padding: "9px 14px",
              borderRadius: "14px",
              background: "linear-gradient(135deg, #FF9800 0%, #CD7F32 100%)",
              color: "#FFF",
              border: "none",
              fontWeight: "700",
              fontSize: "13px",
              cursor: "pointer",
              boxShadow: "0 4px 12px rgba(205, 127, 50, 0.4)"
            }}
          >
            🏆 {t.viewWinners}
          </button>

          <button
            onClick={() => setShowUndoDialog(true)}
            disabled={calledNumbers.length === 0}
            style={{
              padding: "8px 12px",
              borderRadius: "12px",
              background: "rgba(255, 255, 255, 0.12)",
              color: calledNumbers.length > 0 ? "#FFF" : "rgba(255,255,255,0.3)",
              border: "1px solid rgba(255,255,255,0.15)",
              fontWeight: "600",
              fontSize: "12px",
              cursor: calledNumbers.length > 0 ? "pointer" : "not-allowed"
            }}
          >
            ↩ Undo
          </button>

          <button
            onClick={() => setShowResetDialog(true)}
            style={{
              padding: "8px 12px",
              borderRadius: "12px",
              background: "rgba(255, 82, 82, 0.2)",
              color: "#FF8A80",
              border: "1px solid rgba(255, 82, 82, 0.4)",
              fontWeight: "600",
              fontSize: "12px",
              cursor: "pointer"
            }}
          >
            🔄 {t.resetGame}
          </button>

          <button
            onClick={() => setShowExitDialog(true)}
            style={{
              padding: "8px 12px",
              borderRadius: "12px",
              background: "transparent",
              color: "rgba(255,255,255,0.7)",
              border: "1px solid rgba(255,255,255,0.25)",
              fontSize: "12px",
              cursor: "pointer"
            }}
          >
            🏠 {t.mainMenu}
          </button>
        </div>
      </div>

      {/* Vibrant 1..90 Grid Container (Zero-Scroll strictly fitted & centered) */}
      <main style={{
        flex: 1,
        minHeight: 0,
        width: "100%",
        margin: "0 auto",
        display: "grid",
        gridTemplateColumns: "repeat(10, 1fr)",
        gridTemplateRows: "repeat(9, 1fr)",
        gap: "6px",
        background: "rgba(0, 0, 0, 0.4)",
        backdropFilter: "blur(20px)",
        WebkitBackdropFilter: "blur(20px)",
        padding: "10px",
        borderRadius: "20px",
        border: "1.5px solid rgba(255, 255, 255, 0.2)",
        boxShadow: "0 8px 32px rgba(0, 0, 0, 0.5), inset 0 2px 10px rgba(0,0,0,0.5)",
        alignItems: "center",
        justifyItems: "center",
        overflow: "hidden"
      }}>
        {Array.from({ length: 90 }, (_, i) => i + 1).map((num) => {
          const isCalled = calledNumbers.includes(num);
          const isLast = lastNumber === num;

          let background = "rgba(42, 0, 74, 0.6)";
          let color = "rgba(255, 255, 255, 0.6)";
          let border = "1px solid rgba(255, 255, 255, 0.08)";
          let boxShadow = "none";
          let className = "";
          let transform = "scale(1)";

          if (isLast) {
            background = "linear-gradient(135deg, #00E5FF 0%, #00B0FF 100%)";
            color = "#000000";
            border = "2px solid #FFFFFF";
            boxShadow = "0 0 16px #00E5FF, inset 0 0 8px #FFF";
            className = "flash-number";
            transform = "scale(1.04)";
          } else if (isCalled) {
            background = "linear-gradient(135deg, #E040FB 0%, #AA00FF 100%)";
            color = "#FFFFFF";
            border = "1px solid rgba(255, 255, 255, 0.4)";
            boxShadow = "0 2px 8px rgba(224, 64, 251, 0.4)";
          }

          return (
            <div
              key={num}
              className={className}
              style={{
                background,
                color,
                border,
                boxShadow,
                transform,
                borderRadius: "8px",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                fontWeight: isCalled || isLast ? "900" : "800",
                fontSize: "clamp(14px, 2.8vh, 26px)",
                userSelect: "none",
                height: "100%",
                width: "100%",
                transition: "all 0.2s cubic-bezier(0.4, 0, 0.2, 1)"
              }}
            >
              {num}
            </div>
          );
        })}
      </main>
      </div>

      {/* Winner Board Modal */}
      {showWinnerModal && (
        <WinnerBoardModal
          gameId={gameId}
          prizes={prizes}
          onUpdatePrize={(updated) => setPrizes(updated)}
          onDismiss={() => setShowWinnerModal(false)}
        />
      )}

      {/* Reset Confirmation Dialog */}
      {showResetDialog && (
        <div style={{
          position: "fixed",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.8)",
          backdropFilter: "blur(6px)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000,
          padding: "16px"
        }}>
          <div style={{
            backgroundColor: "#3A0066",
            borderRadius: "20px",
            padding: "24px",
            maxWidth: "380px",
            width: "100%",
            textAlign: "center",
            border: "1.5px solid rgba(255,82,82,0.5)",
            boxShadow: "0 10px 30px rgba(0,0,0,0.5)"
          }}>
            <h3 style={{ fontSize: "20px", color: "#FF5252", marginBottom: "12px", fontWeight: "bold" }}>Reset Game Board?</h3>
            <p style={{ fontSize: "14px", color: "rgba(255,255,255,0.8)", marginBottom: "20px", lineHeight: "1.5" }}>
              This will clear all drawn numbers and restart the board session. Are you sure?
            </p>
            <div style={{ display: "flex", gap: "12px" }}>
              <button
                onClick={() => setShowResetDialog(false)}
                style={{
                  flex: 1,
                  padding: "11px",
                  borderRadius: "10px",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  color: "#FFF",
                  border: "none",
                  cursor: "pointer",
                  fontWeight: "600"
                }}
              >
                No, Cancel
              </button>
              <button
                onClick={() => {
                  setShowResetDialog(false);
                  setIsAutoCalling(false);
                  setCalledNumbers([]);
                  setLastNumber(null);
                  onResetGame();
                }}
                style={{
                  flex: 1,
                  padding: "11px",
                  borderRadius: "10px",
                  backgroundColor: "#FF5252",
                  color: "#FFF",
                  border: "none",
                  fontWeight: "bold",
                  cursor: "pointer",
                  boxShadow: "0 4px 12px rgba(255,82,82,0.4)"
                }}
              >
                Yes, Reset
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Undo Confirmation Dialog */}
      {showUndoDialog && (
        <div style={{
          position: "fixed",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.8)",
          backdropFilter: "blur(6px)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000,
          padding: "16px"
        }}>
          <div style={{
            backgroundColor: "#3A0066",
            borderRadius: "20px",
            padding: "24px",
            maxWidth: "380px",
            width: "100%",
            textAlign: "center",
            border: "1.5px solid rgba(255,215,0,0.5)",
            boxShadow: "0 10px 30px rgba(0,0,0,0.5)"
          }}>
            <h3 style={{ fontSize: "20px", color: "#FFD700", marginBottom: "12px", fontWeight: "bold" }}>Undo Last Number?</h3>
            <p style={{ fontSize: "14px", color: "rgba(255,255,255,0.8)", marginBottom: "20px", lineHeight: "1.5" }}>
              Remove number <strong style={{ color: "#FFD700" }}>{lastNumber}</strong> from called list?
            </p>
            <div style={{ display: "flex", gap: "12px" }}>
              <button
                onClick={() => setShowUndoDialog(false)}
                style={{
                  flex: 1,
                  padding: "11px",
                  borderRadius: "10px",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  color: "#FFF",
                  border: "none",
                  cursor: "pointer",
                  fontWeight: "600"
                }}
              >
                Cancel
              </button>
              <button
                onClick={handleUndo}
                style={{
                  flex: 1,
                  padding: "11px",
                  borderRadius: "10px",
                  backgroundColor: "#009E60",
                  color: "#FFF",
                  border: "none",
                  fontWeight: "bold",
                  cursor: "pointer"
                }}
              >
                Undo Now
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Exit Confirmation Dialog */}
      {showExitDialog && (
        <div style={{
          position: "fixed",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.8)",
          backdropFilter: "blur(6px)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000,
          padding: "16px"
        }}>
          <div style={{
            backgroundColor: "#3A0066",
            borderRadius: "20px",
            padding: "24px",
            maxWidth: "380px",
            width: "100%",
            textAlign: "center",
            border: "1.5px solid rgba(255,255,255,0.2)",
            boxShadow: "0 10px 30px rgba(0,0,0,0.5)"
          }}>
            <h3 style={{ fontSize: "20px", marginBottom: "12px", fontWeight: "bold" }}>Return to Main Menu?</h3>
            <p style={{ fontSize: "14px", color: "rgba(255,255,255,0.8)", marginBottom: "20px", lineHeight: "1.5" }}>
              Your current game state will be saved automatically so you can continue anytime.
            </p>
            <div style={{ display: "flex", gap: "12px" }}>
              <button
                onClick={() => setShowExitDialog(false)}
                style={{
                  flex: 1,
                  padding: "11px",
                  borderRadius: "10px",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  color: "#FFF",
                  border: "none",
                  cursor: "pointer",
                  fontWeight: "600"
                }}
              >
                Stay in Game
              </button>
              <button
                onClick={() => {
                  setShowExitDialog(false);
                  setIsAutoCalling(false);
                  onReturnToMainMenu();
                }}
                style={{
                  flex: 1,
                  padding: "11px",
                  borderRadius: "10px",
                  backgroundColor: "#009E60",
                  color: "#FFF",
                  border: "none",
                  fontWeight: "bold",
                  cursor: "pointer"
                }}
              >
                Exit to Menu
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
