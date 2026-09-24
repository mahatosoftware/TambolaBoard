"use client";

import React, { useState, useEffect } from "react";
import { PrizeItem } from "../lib/storage";
import { jsPDF } from "jspdf";
import { useTranslation } from "../lib/useTranslation";
import { listenToGame } from "../lib/tambola/tickets";

interface ViewWinnersScreenProps {
  gameId: string;
  prizes: PrizeItem[];
  onBack: () => void;
}

export const ViewWinnersScreen: React.FC<ViewWinnersScreenProps> = ({
  gameId,
  prizes,
  onBack
}) => {
  const { t } = useTranslation();
  const [localPrizes, setLocalPrizes] = useState<PrizeItem[]>(prizes);

  useEffect(() => {
    setLocalPrizes(prizes);
  }, [prizes]);

  useEffect(() => {
    if (!gameId) return;
    const unsub = listenToGame(gameId, (gDoc) => {
      if (gDoc && gDoc.prizes && Array.isArray(gDoc.prizes) && gDoc.prizes.length > 0) {
        setLocalPrizes(gDoc.prizes);
      }
    });
    return () => unsub();
  }, [gameId]);

  const handleDownloadPdf = () => {
    const doc = new jsPDF();
    doc.setFontSize(20);
    doc.setTextColor(108, 11, 169);
    doc.text("Tambola Winner Report", 14, 22);

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
    doc.text("WINNER NAME", 110, y + 6);

    y += 12;
    doc.setFont("helvetica", "normal");
    doc.setTextColor(0, 0, 0);

    localPrizes.forEach((prize) => {
      doc.text(prize.ruleName.toUpperCase(), 18, y);
      doc.text(prize.winnerName || t.unclaimedStatus, 110, y);
      y += 10;
    });

    doc.save(`Tambola_Winners_${gameId || "Report"}.pdf`);
  };

  return (
    <div style={{
      minHeight: "100vh",
      backgroundColor: "#6C0BA9",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      padding: "24px"
    }}>
      {/* Header */}
      <div style={{ textAlign: "center", marginBottom: "20px" }}>
        <h1 style={{ fontSize: "26px", fontWeight: "bold" }}>🏆 {t.viewWinners}</h1>
        {gameId && (
          <div style={{ fontSize: "14px", color: "rgba(255,255,255,0.7)", marginTop: "4px" }}>
            Game ID: {gameId}
          </div>
        )}
      </div>

      {/* Content Area */}
      <div style={{
        flex: 1,
        backgroundColor: "rgba(0,0,0,0.2)",
        borderRadius: "16px",
        padding: "16px",
        overflowY: "auto",
        display: "flex",
        flexDirection: "column"
      }}>
        {localPrizes.length === 0 ? (
          <div style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center", color: "rgba(255,255,255,0.6)", fontSize: "16px" }}>
            No active game or winners recorded.
          </div>
        ) : (
          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))", gap: "16px" }}>
            {localPrizes.map((prize) => (
              <div
                key={prize.id}
                style={{
                  backgroundColor: prize.isClaimed ? "#4A007E" : "rgba(74,0,126,0.5)",
                  border: prize.isClaimed ? "2px solid #FFD700" : "1px solid rgba(255,255,255,0.2)",
                  borderRadius: "14px",
                  padding: "16px",
                  display: "flex",
                  flexDirection: "column",
                  gap: "6px"
                }}
              >
                <div style={{ fontWeight: "bold", fontSize: "17px" }}>
                  {prize.ruleName}
                </div>
                <div style={{ fontSize: "14px", color: prize.winnerName ? "#FFD700" : "rgba(255,255,255,0.5)", fontWeight: "bold" }}>
                  {prize.winnerName ? `Winner: ${prize.winnerName}` : t.unclaimedStatus}
                </div>
                <div style={{ fontSize: "12px", color: prize.isClaimed ? "#009E60" : "rgba(255,255,255,0.5)", fontWeight: "bold", alignSelf: "flex-end" }}>
                  {prize.isClaimed ? `${t.claimedStatus.toUpperCase()} ✓` : t.unclaimedStatus.toUpperCase()}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Footer Action Bar */}
      <div style={{ marginTop: "20px", display: "flex", flexDirection: "column", gap: "10px", alignItems: "center" }}>
        {prizes.length > 0 && (
          <button
            onClick={handleDownloadPdf}
            style={{
              width: "100%",
              maxWidth: "400px",
              padding: "14px",
              borderRadius: "20px",
              backgroundColor: "#CD7F32",
              color: "#FFF",
              border: "none",
              fontWeight: "bold",
              fontSize: "16px",
              cursor: "pointer"
            }}
          >
            {t.downloadPdfSummary}
          </button>
        )}

        <button
          onClick={onBack}
          style={{
            width: "100%",
            maxWidth: "400px",
            padding: "14px",
            borderRadius: "20px",
            backgroundColor: "#009E60",
            color: "#FFF",
            border: "none",
            fontWeight: "bold",
            fontSize: "16px",
            cursor: "pointer"
          }}
        >
          ⬅️ {t.mainMenu}
        </button>

        <span style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)", marginTop: "4px" }}>
          Mahato Software • Made with ❤️ in India
        </span>
      </div>
    </div>
  );
};
