"use client";

import React, { useState, useEffect } from "react";
import { TicketDoc, listenToGame, GameDoc } from "../lib/tambola/tickets";
import { db, collection, addDoc, serverTimestamp } from "../lib/firebase";
import { Trophy, CheckCircle2, RotateCcw, AlertCircle, Volume2 } from "lucide-react";

interface DigitalTicketViewProps {
  gameId: string;
  ticket: TicketDoc;
  playerId: string;
  onExit?: () => void;
}

const CLAIM_RULES = [
  { id: "early_5", name: "Early 5", desc: "First 5 numbers marked anywhere on the ticket" },
  { id: "top_line", name: "Top Line", desc: "All 5 numbers in the top row (row0)" },
  { id: "middle_line", name: "Middle Line", desc: "All 5 numbers in the middle row (row1)" },
  { id: "bottom_line", name: "Bottom Line", desc: "All 5 numbers in the bottom row (row2)" },
  { id: "four_corners", name: "Four Corners", desc: "1st and last numbers of top and bottom rows" },
  { id: "full_house", name: "Full House", desc: "All 15 numbers completed on the ticket" }
];

export const DigitalTicketView: React.FC<DigitalTicketViewProps> = ({
  gameId,
  ticket,
  playerId,
  onExit
}) => {
  const [markedNumbers, setMarkedNumbers] = useState<number[]>(() => {
    if (typeof window !== "undefined") {
      try {
        const saved = localStorage.getItem(`tambola_marked_${ticket.id}`);
        if (saved) return JSON.parse(saved);
      } catch (e) {
        console.error("Error loading marked numbers:", e);
      }
    }
    return [];
  });

  const [gameDoc, setGameDoc] = useState<GameDoc | null>(null);
  const [selectedClaimRule, setSelectedClaimRule] = useState<string>("early_5");
  const [showClaimModal, setShowClaimModal] = useState(false);
  const [claimSubmitted, setClaimSubmitted] = useState(false);
  const [claimError, setClaimError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Save marked numbers locally
  useEffect(() => {
    if (typeof window !== "undefined") {
      try {
        localStorage.setItem(`tambola_marked_${ticket.id}`, JSON.stringify(markedNumbers));
      } catch (e) {
        console.error("Error saving marked numbers:", e);
      }
    }
  }, [ticket.id, markedNumbers]);

  // Listen to live game state for called numbers
  useEffect(() => {
    const unsub = listenToGame(gameId, (g) => {
      setGameDoc(g);
    });
    return () => unsub();
  }, [gameId]);

  const toggleMarkNumber = (num: number) => {
    if (num === 0) return;
    setMarkedNumbers((prev) =>
      prev.includes(num) ? prev.filter((n) => n !== num) : [...prev, num]
    );
  };

  const handleClearMarks = () => {
    if (window.confirm("Clear all marked numbers on your ticket?")) {
      setMarkedNumbers([]);
    }
  };

  const handleSubmitClaim = async () => {
    setIsSubmitting(true);
    setClaimError(null);
    try {
      const claimsRef = collection(db, "games", gameId.toUpperCase(), "claims");
      await addDoc(claimsRef, {
        markedData: {
          [ticket.id]: markedNumbers
        },
        playerName: ticket.name || "Player",
        playerUid: ticket.playerUid || null,
        status: "PENDING",
        timestamp: serverTimestamp(),
        ticketId: ticket.id,
        claimRule: selectedClaimRule,
        claimRuleName: CLAIM_RULES.find((r) => r.id === selectedClaimRule)?.name || selectedClaimRule
      });

      setClaimSubmitted(true);
      setShowClaimModal(false);
      setTimeout(() => setClaimSubmitted(false), 5000);
    } catch (err: any) {
      console.error("Failed to submit claim:", err);
      setClaimError("Failed to submit claim. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  };

  const gridRows = [ticket.grid.row0 || [], ticket.grid.row1 || [], ticket.grid.row2 || []];
  const calledNumbers = gameDoc?.calledNumbers || [];
  const lastNumber = gameDoc?.lastNumber ?? null;

  return (
    <div style={{
      minHeight: "100vh",
      background: "radial-gradient(circle at 50% 15%, #4A0072 0%, #2A004A 50%, #0D001A 100%)",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      padding: "16px",
      fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
    }}>
      <div style={{ maxWidth: "520px", width: "100%", display: "flex", flexDirection: "column", gap: "16px" }}>
        
        {/* Header Bar */}
        <header style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          backgroundColor: "rgba(255, 255, 255, 0.08)",
          backdropFilter: "blur(12px)",
          padding: "12px 18px",
          borderRadius: "20px",
          border: "1.5px solid rgba(255, 255, 255, 0.18)"
        }}>
          <div>
            <div style={{ fontSize: "11px", textTransform: "uppercase", letterSpacing: "1px", color: "rgba(255,255,255,0.7)", fontWeight: "bold" }}>
              TAMBOLA GAME
            </div>
            <div style={{ fontSize: "20px", fontWeight: "900", color: "#FFD700" }}>
              CODE: {gameId.toUpperCase()}
            </div>
          </div>

          <div style={{ textAlign: "right" }}>
            <div style={{ fontSize: "16px", fontWeight: "800", color: "#FFF" }}>
              {ticket.name || "Player"}
            </div>
            <div style={{ fontSize: "12px", color: "#69F0AE", fontWeight: "700" }}>
              Ticket #{ticket.ticketNumber} • ID: {playerId}
            </div>
          </div>
        </header>

        {/* Success Toast */}
        {claimSubmitted && (
          <div style={{
            backgroundColor: "rgba(0, 230, 118, 0.2)",
            border: "1.5px solid #00E676",
            padding: "12px 16px",
            borderRadius: "14px",
            color: "#69F0AE",
            fontSize: "14px",
            fontWeight: "bold",
            display: "flex",
            alignItems: "center",
            gap: "10px"
          }}>
            <CheckCircle2 style={{ width: "20px", height: "20px" }} />
            Claim submitted successfully! Host is verifying your ticket.
          </div>
        )}

        {/* Live Call Ticker */}
        <div style={{
          backgroundColor: "rgba(0, 0, 0, 0.4)",
          backdropFilter: "blur(10px)",
          padding: "12px 18px",
          borderRadius: "18px",
          border: "1px solid rgba(255, 215, 0, 0.3)",
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between"
        }}>
          <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
            <div style={{
              width: "46px",
              height: "46px",
              borderRadius: "50%",
              background: "linear-gradient(135deg, #00E5FF, #0088FF)",
              color: "#000",
              fontWeight: "900",
              fontSize: "20px",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              boxShadow: "0 0 12px rgba(0, 229, 255, 0.6)"
            }}>
              {lastNumber !== null ? lastNumber : "--"}
            </div>
            <div>
              <div style={{ fontSize: "10px", fontWeight: "bold", color: "rgba(255,255,255,0.6)", letterSpacing: "1px" }}>
                LAST CALLED
              </div>
              <div style={{ fontSize: "13px", fontWeight: "600", color: "#FFF" }}>
                {calledNumbers.length} / 90 Numbers Drawn
              </div>
            </div>
          </div>

          <div style={{
            padding: "6px 12px",
            borderRadius: "12px",
            backgroundColor: gameDoc?.status === "STARTED" ? "rgba(0, 230, 118, 0.2)" : "rgba(255, 215, 0, 0.2)",
            color: gameDoc?.status === "STARTED" ? "#69F0AE" : "#FFD700",
            fontSize: "12px",
            fontWeight: "800",
            border: `1px solid ${gameDoc?.status === "STARTED" ? "#00E676" : "#FFD700"}`
          }}>
            {gameDoc?.status === "STARTED" ? "LIVE GAME" : gameDoc?.status || "WAITING"}
          </div>
        </div>

        {/* 3x9 Tambola Ticket Display */}
        <div style={{
          backgroundColor: "#FFF",
          borderRadius: "20px",
          padding: "12px",
          boxShadow: "0 10px 30px rgba(0,0,0,0.5)",
          border: "3px solid #FFD700"
        }}>
          {/* Ticket Header */}
          <div style={{
            backgroundColor: "#2A004A",
            color: "#FFD700",
            padding: "8px 12px",
            borderRadius: "12px",
            textAlign: "center",
            fontWeight: "900",
            fontSize: "14px",
            letterSpacing: "1px",
            marginBottom: "10px",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center"
          }}>
            <span>🎟️ TICKET #{ticket.ticketNumber}</span>
            <span style={{ color: "#FFF", fontSize: "12px", fontWeight: "normal" }}>ID: {ticket.id}</span>
          </div>

          {/* 3x9 Grid */}
          <div style={{ display: "flex", flexDirection: "column", gap: "6px" }}>
            {gridRows.map((row, rIndex) => (
              <div key={rIndex} style={{ display: "grid", gridTemplateColumns: "repeat(9, 1fr)", gap: "5px" }}>
                {row.map((val, cIndex) => {
                  const isNumber = val > 0;
                  const isMarked = markedNumbers.includes(val);

                  let bg = isNumber ? (isMarked ? "#00C853" : "#F5F5F5") : "#E0E0E0";
                  let textColor = isMarked ? "#FFFFFF" : "#000000";
                  let border = isNumber ? (isMarked ? "2px solid #00E676" : "1px solid #B0BEC5") : "none";

                  return (
                    <button
                      key={cIndex}
                      onClick={() => isNumber && toggleMarkNumber(val)}
                      disabled={!isNumber}
                      style={{
                        aspectRatio: "1/1",
                        borderRadius: "8px",
                        backgroundColor: bg,
                        color: textColor,
                        border,
                        fontWeight: isMarked ? "900" : "700",
                        fontSize: "clamp(13px, 4.5vw, 18px)",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        cursor: isNumber ? "pointer" : "default",
                        userSelect: "none",
                        position: "relative",
                        boxShadow: isMarked ? "0 4px 10px rgba(0, 200, 83, 0.4)" : "none",
                        transition: "all 0.15s ease"
                      }}
                    >
                      {isNumber ? val : ""}
                    </button>
                  );
                })}
              </div>
            ))}
          </div>
        </div>

        {/* Player Action Buttons */}
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px" }}>
          <button
            onClick={() => setShowClaimModal(true)}
            style={{
              padding: "14px",
              borderRadius: "16px",
              background: "linear-gradient(135deg, #FF9800 0%, #E65100 100%)",
              color: "#FFF",
              border: "none",
              fontWeight: "900",
              fontSize: "15px",
              cursor: "pointer",
              boxShadow: "0 6px 20px rgba(255, 152, 0, 0.4)",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              gap: "8px"
            }}
          >
            <Trophy style={{ width: "20px", height: "20px" }} />
            CLAIM PRIZE
          </button>

          <button
            onClick={handleClearMarks}
            style={{
              padding: "14px",
              borderRadius: "16px",
              backgroundColor: "rgba(255, 255, 255, 0.12)",
              color: "#FFF",
              border: "1px solid rgba(255, 255, 255, 0.2)",
              fontWeight: "700",
              fontSize: "14px",
              cursor: "pointer",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              gap: "8px"
            }}
          >
            <RotateCcw style={{ width: "18px", height: "18px" }} />
            CLEAR MARKS
          </button>
        </div>

        {onExit && (
          <button
            onClick={onExit}
            style={{
              marginTop: "12px",
              padding: "10px",
              backgroundColor: "transparent",
              color: "rgba(255,255,255,0.7)",
              border: "1px solid rgba(255,255,255,0.2)",
              borderRadius: "12px",
              fontSize: "13px",
              cursor: "pointer",
              width: "100%"
            }}
          >
            Leave Game
          </button>
        )}
      </div>

      {/* Claim Modal */}
      {showClaimModal && (
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
            borderRadius: "24px",
            padding: "24px",
            maxWidth: "440px",
            width: "100%",
            border: "1.5px solid rgba(255,215,0,0.5)",
            boxShadow: "0 15px 35px rgba(0,0,0,0.6)"
          }}>
            <h3 style={{ fontSize: "20px", fontWeight: "bold", color: "#FFD700", marginBottom: "8px", textAlign: "center" }}>
              🏆 Claim Tambola Prize
            </h3>
            <p style={{ fontSize: "13px", color: "rgba(255,255,255,0.8)", marginBottom: "16px", textAlign: "center" }}>
              Select the winning pattern you wish to claim:
            </p>

            <div style={{ display: "flex", flexDirection: "column", gap: "10px", marginBottom: "20px" }}>
              {CLAIM_RULES.map((rule) => (
                <button
                  key={rule.id}
                  onClick={() => setSelectedClaimRule(rule.id)}
                  style={{
                    padding: "12px",
                    borderRadius: "14px",
                    backgroundColor: selectedClaimRule === rule.id ? "rgba(0, 230, 118, 0.2)" : "rgba(255,255,255,0.06)",
                    border: selectedClaimRule === rule.id ? "1.5px solid #00E676" : "1px solid rgba(255,255,255,0.12)",
                    color: selectedClaimRule === rule.id ? "#69F0AE" : "#FFF",
                    textAlign: "left",
                    cursor: "pointer",
                    display: "flex",
                    flexDirection: "column",
                    gap: "2px"
                  }}
                >
                  <div style={{ fontWeight: "bold", fontSize: "14px" }}>{rule.name}</div>
                  <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.7)" }}>{rule.desc}</div>
                </button>
              ))}
            </div>

            {claimError && (
              <div style={{ color: "#FF5252", fontSize: "13px", fontWeight: "bold", marginBottom: "12px", textAlign: "center" }}>
                {claimError}
              </div>
            )}

            <div style={{ display: "flex", gap: "12px" }}>
              <button
                onClick={() => setShowClaimModal(false)}
                disabled={isSubmitting}
                style={{
                  flex: 1,
                  padding: "12px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  color: "#FFF",
                  border: "none",
                  fontWeight: "bold",
                  cursor: "pointer"
                }}
              >
                Cancel
              </button>

              <button
                onClick={handleSubmitClaim}
                disabled={isSubmitting}
                style={{
                  flex: 1.5,
                  padding: "12px",
                  borderRadius: "12px",
                  backgroundColor: "#00E676",
                  color: "#000",
                  border: "none",
                  fontWeight: "bold",
                  cursor: isSubmitting ? "not-allowed" : "pointer"
                }}
              >
                {isSubmitting ? "Submitting..." : "Submit Claim"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
