"use client";

import React, { useState } from "react";
import { PrizeItem } from "../lib/storage";
import { jsPDF } from "jspdf";
import { useTranslation } from "../lib/useTranslation";

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

  const handleNameChange = (id: string, newName: string) => {
    const updated = localPrizes.map((p) => (p.id === id ? { ...p, winnerName: newName } : p));
    setLocalPrizes(updated);
    onUpdatePrize(updated);
  };

  const handleClaimToggle = (id: string, isClaimed: boolean) => {
    const updated = localPrizes.map((p) => (p.id === id ? { ...p, isClaimed } : p));
    setLocalPrizes(updated);
    onUpdatePrize(updated);
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
      backgroundColor: "rgba(0,0,0,0.75)",
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
        padding: "20px",
        maxWidth: "520px",
        width: "100%",
        maxHeight: "85vh",
        display: "flex",
        flexDirection: "column",
        boxShadow: "0 10px 30px rgba(0,0,0,0.5)",
        border: "1px solid rgba(255,255,255,0.2)"
      }}>
        {/* Title */}
        <div style={{ textAlign: "center", marginBottom: "12px" }}>
          <h2 style={{ fontSize: "22px", fontWeight: "800" }}>🏆 {t.winnerBoardTitle}</h2>
          {gameId && (
            <div style={{ fontSize: "13px", color: "rgba(255,255,255,0.8)", fontWeight: "bold" }}>
              Game ID: {gameId}
            </div>
          )}
        </div>

        {/* Table Header */}
        <div style={{
          display: "grid",
          gridTemplateColumns: "1.2fr 1.5fr 0.6fr",
          gap: "8px",
          padding: "8px 12px",
          backgroundColor: "#4A007E",
          borderRadius: "8px",
          fontSize: "12px",
          fontWeight: "bold"
        }}>
          <span>PRIZE</span>
          <span>{t.enterWinnerName.toUpperCase()}</span>
          <span style={{ textAlign: "center" }}>{t.claimedStatus.toUpperCase()}</span>
        </div>

        {/* Scrollable list */}
        <div style={{ flex: 1, overflowY: "auto", display: "flex", flexDirection: "column", gap: "8px", margin: "10px 0" }}>
          {localPrizes.length === 0 ? (
            <div style={{ textAlign: "center", padding: "20px", color: "rgba(255,255,255,0.7)" }}>
              No rules or prizes configured for this game.
            </div>
          ) : (
            localPrizes.map((prize) => (
              <div
                key={prize.id}
                style={{
                  display: "grid",
                  gridTemplateColumns: "1.2fr 1.5fr 0.6fr",
                  gap: "8px",
                  alignItems: "center",
                  padding: "8px 12px",
                  backgroundColor: "rgba(255,255,255,0.1)",
                  borderRadius: "10px"
                }}
              >
                <div style={{ fontWeight: "bold", fontSize: "13px", wordBreak: "break-word" }}>
                  {prize.ruleName.toUpperCase()}
                </div>

                <input
                  type="text"
                  value={prize.winnerName}
                  onChange={(e) => handleNameChange(prize.id, e.target.value)}
                  placeholder={t.enterWinnerName}
                  disabled={prize.isClaimed}
                  style={{
                    padding: "6px 10px",
                    borderRadius: "6px",
                    border: "1px solid rgba(255,255,255,0.3)",
                    backgroundColor: prize.isClaimed ? "rgba(255,255,255,0.1)" : "rgba(255,255,255,0.2)",
                    color: "#FFF",
                    outline: "none",
                    fontSize: "13px"
                  }}
                />

                <div style={{ display: "flex", justifyContent: "center" }}>
                  <input
                    type="checkbox"
                    checked={prize.isClaimed}
                    onChange={(e) => handleClaimToggle(prize.id, e.target.checked)}
                    style={{ width: "20px", height: "20px", cursor: "pointer" }}
                  />
                </div>
              </div>
            ))
          )}
        </div>

        {/* Action Buttons */}
        <div style={{ display: "flex", gap: "10px", marginTop: "8px" }}>
          {localPrizes.length > 0 && (
            <button
              onClick={handleDownloadPdf}
              style={{
                flex: 1,
                padding: "12px",
                borderRadius: "10px",
                backgroundColor: "#CD7F32",
                color: "#FFF",
                border: "none",
                fontWeight: "bold",
                cursor: "pointer",
                fontSize: "14px"
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
              borderRadius: "10px",
              backgroundColor: "#009E60",
              color: "#FFF",
              border: "none",
              fontWeight: "bold",
              cursor: "pointer",
              fontSize: "14px"
            }}
          >
            {t.close}
          </button>
        </div>
      </div>
    </div>
  );
};
