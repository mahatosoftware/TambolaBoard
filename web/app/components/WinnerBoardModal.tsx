"use client";

import React, { useState, useEffect } from "react";
import { PrizeItem } from "../lib/storage";
import { jsPDF } from "jspdf";
import { useTranslation } from "../lib/useTranslation";
import { TicketDoc, listenToTickets, updateGameSettings } from "../lib/tambola/tickets";
import { db, collection, onSnapshot, doc, updateDoc } from "../lib/firebase";
import { CheckCircle2, User, Ticket as TicketIcon, Trophy, Edit2 } from "lucide-react";

interface WinnerBoardModalProps {
  gameId: string;
  prizes: PrizeItem[];
  onUpdatePrize: (updatedPrizes: PrizeItem[]) => void;
  onDismiss: () => void;
}

export const WinnerBoardModal: React.FC<WinnerBoardModalProps> = ({
  gameId,
  prizes,
  onUpdatePrize,
  onDismiss
}) => {
  const { t } = useTranslation();
  const [localPrizes, setLocalPrizes] = useState<PrizeItem[]>(prizes);
  const [tickets, setTickets] = useState<TicketDoc[]>([]);
  const [pendingClaims, setPendingClaims] = useState<any[]>([]);
  const [customInputActive, setCustomInputActive] = useState<{ [ruleId: string]: boolean }>({});

  useEffect(() => {
    setLocalPrizes(prizes);
  }, [prizes]);

  // 1. Subscribe to all game tickets (Digital & Paper) for player selection
  useEffect(() => {
    if (!gameId) return;

    const unsubTickets = listenToTickets(gameId, (tList) => {
      setTickets(tList);
    });

    // Subscribe to incoming live player claims submitted digitally
    const claimsRef = collection(db, "games", gameId.toUpperCase(), "claims");
    const unsubClaims = onSnapshot(claimsRef, (snap) => {
      const list: any[] = [];
      snap.forEach((d) => {
        const data = d.data();
        if (data.status === "PENDING") {
          list.push({ id: d.id, ...data });
        }
      });
      setPendingClaims(list);
    }, (err) => console.warn("Claims listener warning:", err));

    return () => {
      unsubTickets();
      unsubClaims();
    };
  }, [gameId]);

  // Filter player options from assigned / issued paper and digital tickets
  const digitalPlayers = tickets
    .filter((t) => t.type === "Digital" && (t.status === "ASSIGNED" || t.status === "ACTIVE" || t.name))
    .map((t) => ({
      name: t.name || `Player (Ticket #${t.ticketNumber})`,
      label: `📱 ${t.name ? t.name : "Player"} (Digital #${t.ticketNumber})`,
      value: `${t.name ? t.name : "Player"} (Digital #${t.ticketNumber})`
    }));

  const paperPlayers = tickets
    .filter((t) => t.type === "Paper" && (t.status === "ASSIGNED" || t.status === "ACTIVE" || t.name))
    .map((t) => ({
      name: t.name || `Player (Ticket #${t.ticketNumber})`,
      label: `🎟️ ${t.name ? t.name : "Player"} (Paper #${t.id.split("-").pop() || t.ticketNumber})`,
      value: `${t.name ? t.name : "Player"} (Paper #${t.id.split("-").pop() || t.ticketNumber})`
    }));

  const handleNameChange = (id: string, newName: string) => {
    const updated = localPrizes.map((p) => (p.id === id ? { ...p, winnerName: newName } : p));
    setLocalPrizes(updated);
    onUpdatePrize(updated);
    if (gameId) {
      updateGameSettings(gameId, { prizes: updated }).catch((err) => console.error("Firestore prizes update error:", err));
    }
  };

  const handleClaimToggle = (id: string, isClaimed: boolean) => {
    const updated = localPrizes.map((p) => (p.id === id ? { ...p, isClaimed } : p));
    setLocalPrizes(updated);
    onUpdatePrize(updated);
    if (gameId) {
      updateGameSettings(gameId, { prizes: updated }).catch((err) => console.error("Firestore prizes update error:", err));
    }
  };

  const matchClaimToPrize = (claimRuleStr: string, claimRuleName?: string): PrizeItem | undefined => {
    const rawTarget = claimRuleName || claimRuleStr;
    if (!rawTarget) return undefined;

    // 1. Direct ID or Name match
    let match = localPrizes.find(
      (p) => p.id.toLowerCase() === claimRuleStr.toLowerCase() || p.ruleName.toLowerCase() === rawTarget.toLowerCase() || p.ruleName.toLowerCase() === claimRuleStr.toLowerCase()
    );
    if (match) return match;

    // 2. Fuzzy normalized match (e.g. "early_5", "early_five" vs "Early 5", "Early Five", "Early 5 / Jaldi 5")
    const normClaim = claimRuleStr.toLowerCase().replace(/[^a-z0-9]/g, "");
    const normClaimName = (claimRuleName || "").toLowerCase().replace(/[^a-z0-9]/g, "");

    match = localPrizes.find((p) => {
      const normPrize = p.ruleName.toLowerCase().replace(/[^a-z0-9]/g, "");
      if (normPrize === normClaim || normPrize === normClaimName) return true;
      if ((normClaim.includes("early") || normClaimName.includes("early")) && (normPrize.includes("early") || normPrize.includes("jaldi"))) return true;
      if ((normClaim.includes("corner") || normClaimName.includes("corner")) && normPrize.includes("corner")) return true;
      if ((normClaim.includes("fullhouse") || normClaimName.includes("fullhouse")) && normPrize.includes("fullhouse")) return true;
      if ((normClaim.includes("topline") || normClaimName.includes("topline")) && normPrize.includes("topline")) return true;
      if ((normClaim.includes("middleline") || normClaimName.includes("middleline")) && normPrize.includes("middleline")) return true;
      if ((normClaim.includes("bottomline") || normClaimName.includes("bottomline")) && normPrize.includes("bottomline")) return true;
      return false;
    });

    if (match) return match;

    // 3. Fallback: match first unclaimed prize
    return localPrizes.find((p) => !p.isClaimed);
  };

  // Approve a live player claim directly
  const handleApproveLiveClaim = async (claimItem: any) => {
    try {
      // Find matching prize rule using fuzzy matcher
      const targetPrize = matchClaimToPrize(claimItem.claimRule || "", claimItem.claimRuleName || "");
      if (targetPrize) {
        const winnerStr = `${claimItem.playerName || "Player"} (Digital #${claimItem.ticketId})`;
        const updated = localPrizes.map((p) => (p.id === targetPrize.id ? { ...p, winnerName: winnerStr, isClaimed: true } : p));
        setLocalPrizes(updated);
        onUpdatePrize(updated);

        if (gameId) {
          await updateGameSettings(gameId, { prizes: updated }).catch((err) => console.error("Firestore sync error:", err));
        }

        // Update claim status in Firestore
        const claimDocRef = doc(db, "games", gameId.toUpperCase(), "claims", claimItem.id);
        await updateDoc(claimDocRef, { status: "APPROVED" });
      } else {
        alert("No unclaimed prize rules available to match this claim.");
      }
    } catch (err) {
      console.error("Approve live claim error:", err);
    }
  };

  // Reject a live player claim
  const handleRejectLiveClaim = async (claimItem: any) => {
    try {
      const claimDocRef = doc(db, "games", gameId.toUpperCase(), "claims", claimItem.id);
      await updateDoc(claimDocRef, { status: "REJECTED" });
    } catch (err) {
      console.error("Reject live claim error:", err);
    }
  };

  const handleDownloadPdf = () => {
    const doc = new jsPDF();
    doc.setFontSize(20);
    doc.setTextColor(108, 11, 169); // Purple
    doc.text("Tambola Winner Board", 14, 22);

    doc.setFontSize(12);
    doc.setTextColor(50, 50, 50);
    doc.text(`Game ID: ${gameId || "N/A"}`, 14, 32);
    doc.text(`Date: ${new Date().toLocaleDateString()}`, 14, 40);

    let y = 52;
    doc.setFillColor(74, 0, 126);
    doc.rect(14, y, 180, 8, "F");
    doc.setTextColor(255, 255, 255);
    doc.setFont("helvetica", "bold");
    doc.text("PRIZE RULE", 18, y + 6);
    doc.text("WINNER NAME", 100, y + 6);
    doc.text("STATUS", 160, y + 6);

    y += 12;
    doc.setFont("helvetica", "normal");
    doc.setTextColor(0, 0, 0);

    localPrizes.forEach((prize) => {
      if (y > 270) {
        doc.addPage();
        y = 20;
      }
      doc.text(prize.ruleName.toUpperCase(), 18, y);
      doc.text(prize.winnerName || t.unclaimedStatus, 100, y);
      doc.text(prize.isClaimed ? `${t.claimedStatus} ✓` : t.unclaimedStatus, 160, y);
      y += 10;
    });

    doc.save(`Tambola_Winners_${gameId || "Board"}.pdf`);
  };

  return (
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
        backgroundColor: "#6C0BA9",
        color: "#FFF",
        borderRadius: "24px",
        padding: "24px",
        maxWidth: "580px",
        width: "100%",
        maxHeight: "88vh",
        display: "flex",
        flexDirection: "column",
        boxShadow: "0 15px 35px rgba(0,0,0,0.6)",
        border: "1.5px solid rgba(255,215,0,0.4)"
      }}>
        {/* Title */}
        <div style={{ textAlign: "center", marginBottom: "16px" }}>
          <h2 style={{ fontSize: "24px", fontWeight: "900", margin: "0 0 4px 0", color: "#FFD700" }}>
            🏆 {t.winnerBoardTitle}
          </h2>
          {gameId && (
            <div style={{ fontSize: "13px", color: "rgba(255,255,255,0.8)", fontWeight: "bold" }}>
              Game Code: <strong style={{ color: "#00E5FF" }}>{gameId}</strong>
            </div>
          )}
        </div>

        {/* Live Pending Claims Banner */}
        {pendingClaims.length > 0 && (
          <div style={{
            backgroundColor: "rgba(255, 152, 0, 0.15)",
            border: "1.5px solid #FF9800",
            borderRadius: "16px",
            padding: "12px 16px",
            marginBottom: "16px",
            display: "flex",
            flexDirection: "column",
            gap: "10px"
          }}>
            <div style={{ fontWeight: "bold", fontSize: "13px", color: "#FFB74D", display: "flex", alignItems: "center", gap: "6px" }}>
              <Trophy style={{ width: "16px", height: "16px" }} />
              Incoming Live Claims ({pendingClaims.length})
            </div>

            <div style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
              {pendingClaims.map((claim) => (
                <div
                  key={claim.id}
                  style={{
                    backgroundColor: "rgba(0,0,0,0.3)",
                    padding: "8px 12px",
                    borderRadius: "10px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between"
                  }}
                >
                  <div style={{ fontSize: "12px" }}>
                    <strong>{claim.playerName}</strong> claimed <span style={{ color: "#FFD700" }}>{claim.claimRuleName || claim.claimRule}</span> (Ticket #{claim.ticketId})
                  </div>
                  <div style={{ display: "flex", gap: "6px" }}>
                    <button
                      onClick={() => handleApproveLiveClaim(claim)}
                      style={{
                        padding: "4px 10px",
                        borderRadius: "8px",
                        backgroundColor: "#00E676",
                        color: "#000",
                        border: "none",
                        fontWeight: "bold",
                        fontSize: "12px",
                        cursor: "pointer"
                      }}
                    >
                      Approve
                    </button>
                    <button
                      onClick={() => handleRejectLiveClaim(claim)}
                      style={{
                        padding: "4px 10px",
                        borderRadius: "8px",
                        backgroundColor: "rgba(255, 82, 82, 0.2)",
                        color: "#FF8A80",
                        border: "1px solid rgba(255, 82, 82, 0.4)",
                        fontSize: "12px",
                        cursor: "pointer"
                      }}
                    >
                      Reject
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Table Header */}
        <div style={{
          display: "grid",
          gridTemplateColumns: "1fr 1.6fr 0.5fr",
          gap: "8px",
          padding: "10px 12px",
          backgroundColor: "#4A007E",
          borderRadius: "12px",
          fontSize: "12px",
          fontWeight: "bold",
          letterSpacing: "0.5px"
        }}>
          <span>PRIZE RULE</span>
          <span>SELECT WINNER (DIGITAL / PAPER)</span>
          <span style={{ textAlign: "center" }}>{t.claimedStatus.toUpperCase()}</span>
        </div>

        {/* Scrollable Rules List */}
        <div style={{ flex: 1, overflowY: "auto", display: "flex", flexDirection: "column", gap: "10px", margin: "12px 0", paddingRight: "4px" }}>
          {localPrizes.length === 0 ? (
            <div style={{ textAlign: "center", padding: "20px", color: "rgba(255,255,255,0.7)" }}>
              No rules or prizes configured for this game.
            </div>
          ) : (
            localPrizes.map((prize) => {
              const isCustomInput = customInputActive[prize.id];
              return (
                <div
                  key={prize.id}
                  style={{
                    display: "grid",
                    gridTemplateColumns: "1fr 1.6fr 0.5fr",
                    gap: "8px",
                    alignItems: "center",
                    padding: "10px 12px",
                    backgroundColor: prize.isClaimed ? "rgba(0, 230, 118, 0.12)" : "rgba(255,255,255,0.08)",
                    borderRadius: "12px",
                    border: prize.isClaimed ? "1px solid #00E676" : "1px solid rgba(255,255,255,0.12)"
                  }}
                >
                  <div style={{ fontWeight: "bold", fontSize: "13px", color: prize.isClaimed ? "#69F0AE" : "#FFF" }}>
                    {prize.ruleName.toUpperCase()}
                  </div>

                  {/* Player Select Dropdown or Custom Input */}
                  <div style={{ display: "flex", flexDirection: "column", gap: "4px" }}>
                    {!isCustomInput ? (
                      <div style={{ display: "flex", gap: "4px" }}>
                        <select
                          value={prize.winnerName}
                          onChange={(e) => {
                            if (e.target.value === "__CUSTOM__") {
                              setCustomInputActive((prev) => ({ ...prev, [prize.id]: true }));
                              handleNameChange(prize.id, "");
                            } else {
                              handleNameChange(prize.id, e.target.value);
                            }
                          }}
                          disabled={prize.isClaimed}
                          style={{
                            flex: 1,
                            padding: "8px",
                            borderRadius: "8px",
                            backgroundColor: prize.isClaimed ? "rgba(255,255,255,0.1)" : "rgba(255,255,255,0.2)",
                            color: "#FFF",
                            border: "1px solid rgba(255,255,255,0.3)",
                            fontSize: "13px",
                            outline: "none",
                            cursor: prize.isClaimed ? "not-allowed" : "pointer"
                          }}
                        >
                          <option value="" style={{ backgroundColor: "#2A004A", color: "#FFF" }}>
                            -- Select Winner Player --
                          </option>

                          {/* Fallback option if current winnerName is custom or not in roster */}
                          {prize.winnerName &&
                            !digitalPlayers.some((p) => p.value === prize.winnerName) &&
                            !paperPlayers.some((p) => p.value === prize.winnerName) && (
                              <option value={prize.winnerName} style={{ backgroundColor: "#2A004A", color: "#FFD700" }}>
                                👤 {prize.winnerName}
                              </option>
                          )}

                          {/* Digital Players */}
                          {digitalPlayers.length > 0 && (
                            <optgroup label="📱 Digital Ticket Players" style={{ backgroundColor: "#2A004A", color: "#00E5FF" }}>
                              {digitalPlayers.map((p, idx) => (
                                <option key={`d-${idx}`} value={p.value} style={{ backgroundColor: "#2A004A", color: "#FFF" }}>
                                  {p.label}
                                </option>
                              ))}
                            </optgroup>
                          )}

                          {/* Paper Players */}
                          {paperPlayers.length > 0 && (
                            <optgroup label="🎟️ Paper Ticket Players" style={{ backgroundColor: "#2A004A", color: "#FF9800" }}>
                              {paperPlayers.map((p, idx) => (
                                <option key={`p-${idx}`} value={p.value} style={{ backgroundColor: "#2A004A", color: "#FFF" }}>
                                  {p.label}
                                </option>
                              ))}
                            </optgroup>
                          )}

                          {/* Custom Name fallback */}
                          <option value="__CUSTOM__" style={{ backgroundColor: "#2A004A", color: "#FFD700" }}>
                            ✍️ Type Custom Name...
                          </option>
                        </select>

                        <button
                          type="button"
                          onClick={() => setCustomInputActive((prev) => ({ ...prev, [prize.id]: true }))}
                          disabled={prize.isClaimed}
                          style={{
                            padding: "6px",
                            borderRadius: "8px",
                            backgroundColor: "rgba(255,255,255,0.15)",
                            color: "#FFF",
                            border: "none",
                            cursor: "pointer"
                          }}
                          title="Type Custom Name"
                        >
                          <Edit2 style={{ width: "14px", height: "14px" }} />
                        </button>
                      </div>
                    ) : (
                      <div style={{ display: "flex", gap: "4px" }}>
                        <input
                          type="text"
                          value={prize.winnerName}
                          onChange={(e) => handleNameChange(prize.id, e.target.value)}
                          placeholder="Type Winner Name..."
                          disabled={prize.isClaimed}
                          style={{
                            flex: 1,
                            padding: "8px",
                            borderRadius: "8px",
                            border: "1px solid #00E5FF",
                            backgroundColor: "rgba(255,255,255,0.2)",
                            color: "#FFF",
                            outline: "none",
                            fontSize: "13px"
                          }}
                        />
                        <button
                          type="button"
                          onClick={() => setCustomInputActive((prev) => ({ ...prev, [prize.id]: false }))}
                          style={{
                            padding: "6px 10px",
                            borderRadius: "8px",
                            backgroundColor: "rgba(255,255,255,0.15)",
                            color: "#FFF",
                            border: "none",
                            fontSize: "11px",
                            cursor: "pointer"
                          }}
                        >
                          List
                        </button>
                      </div>
                    )}
                  </div>

                  {/* Claimed Checkbox */}
                  <div style={{ display: "flex", justifyContent: "center" }}>
                    <input
                      type="checkbox"
                      checked={prize.isClaimed}
                      onChange={(e) => handleClaimToggle(prize.id, e.target.checked)}
                      style={{ width: "20px", height: "20px", cursor: "pointer", accentColor: "#00E676" }}
                    />
                  </div>
                </div>
              );
            })
          )}
        </div>

        {/* Action Buttons */}
        <div style={{ display: "flex", gap: "12px", marginTop: "8px" }}>
          {localPrizes.length > 0 && (
            <button
              onClick={handleDownloadPdf}
              style={{
                flex: 1,
                padding: "12px",
                borderRadius: "14px",
                backgroundColor: "#CD7F32",
                color: "#FFF",
                border: "none",
                fontWeight: "bold",
                cursor: "pointer",
                fontSize: "14px",
                boxShadow: "0 4px 12px rgba(0,0,0,0.3)"
              }}
            >
              {t.downloadPdfSummary}
            </button>
          )}

          <button
            onClick={onDismiss}
            style={{
              flex: 1,
              padding: "12px",
              borderRadius: "14px",
              backgroundColor: "#009E60",
              color: "#FFF",
              border: "none",
              fontWeight: "900",
              cursor: "pointer",
              fontSize: "14px",
              boxShadow: "0 4px 12px rgba(0,158,96,0.4)"
            }}
          >
            {t.close}
          </button>
        </div>
      </div>
    </div>
  );
};
