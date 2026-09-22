"use client";

import React, { useState } from "react";
import { TambolaRule } from "../lib/rules";
import { useTranslation } from "../lib/useTranslation";

interface PointsDistributionScreenProps {
  selectedRules: TambolaRule[];
  onStartGame: (rulesWithQuantities: TambolaRule[]) => void;
  onBack: () => void;
}

export const PointsDistributionScreen: React.FC<PointsDistributionScreenProps> = ({
  selectedRules,
  onStartGame,
  onBack
}) => {
  const { t } = useTranslation();
  const [rules, setRules] = useState<TambolaRule[]>(() =>
    selectedRules.map((r) => ({ ...r, quantity: r.quantity || 1 }))
  );

  const updateQuantity = (index: number, delta: number) => {
    setRules((prev) =>
      prev.map((r, i) => {
        if (i === index) {
          const newQty = Math.max(1, (r.quantity || 1) + delta);
          return { ...r, quantity: newQty };
        }
        return r;
      })
    );
  };

  const handleSaveAndStart = () => {
    onStartGame(rules);
  };

  return (
    <div style={{
      minHeight: "100vh",
      background: "linear-gradient(180deg, #4A148C 0%, #7B1FA2 100%)",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      padding: "16px"
    }}>
      {/* Header */}
      <div style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginBottom: "16px",
        paddingBottom: "8px",
        borderBottom: "1px solid rgba(255,255,255,0.2)"
      }}>
        <button
          onClick={onBack}
          style={{
            backgroundColor: "transparent",
            color: "#FFF",
            border: "1px solid rgba(255,255,255,0.4)",
            borderRadius: "8px",
            padding: "6px 12px",
            cursor: "pointer"
          }}
        >
          ⬅️ {t.back}
        </button>
        <h1 style={{ fontSize: "20px", fontWeight: "bold" }}>{t.setPrizeQuantitiesTitle}</h1>
        <div style={{ width: "60px" }} />
      </div>

      {/* Rules Quantity Card */}
      <div style={{
        flex: 1,
        backgroundColor: "rgba(0,0,0,0.25)",
        borderRadius: "16px",
        padding: "16px",
        display: "flex",
        flexDirection: "column",
        overflow: "hidden"
      }}>
        {/* Table Header */}
        <div style={{
          display: "flex",
          justifyContent: "space-between",
          paddingBottom: "8px",
          borderBottom: "1px solid rgba(255,255,255,0.2)",
          fontSize: "12px",
          fontWeight: "bold",
          color: "rgba(255,255,255,0.7)"
        }}>
          <span>RULE NAME</span>
          <span>{t.quantityLabel.toUpperCase()}</span>
        </div>

        {/* Scrollable list */}
        <div style={{ flex: 1, overflowY: "auto", display: "flex", flexDirection: "column", gap: "10px", marginTop: "10px" }}>
          {rules.map((rule, idx) => (
            <div
              key={rule.id}
              style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "space-between",
                padding: "10px 12px",
                backgroundColor: "rgba(255,255,255,0.1)",
                borderRadius: "10px"
              }}
            >
              <div style={{ fontWeight: "600", fontSize: "15px" }}>{rule.name}</div>

              {/* Stepper controls */}
              <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                <button
                  onClick={() => updateQuantity(idx, -1)}
                  style={{
                    width: "32px",
                    height: "32px",
                    borderRadius: "50%",
                    backgroundColor: "rgba(255,255,255,0.2)",
                    color: "#FFF",
                    border: "none",
                    fontWeight: "bold",
                    fontSize: "16px",
                    cursor: "pointer",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center"
                  }}
                >
                  -
                </button>
                <span style={{ minWidth: "24px", textAlign: "center", fontWeight: "bold", fontSize: "16px" }}>
                  {rule.quantity}
                </span>
                <button
                  onClick={() => updateQuantity(idx, 1)}
                  style={{
                    width: "32px",
                    height: "32px",
                    borderRadius: "50%",
                    backgroundColor: "rgba(255,255,255,0.2)",
                    color: "#FFF",
                    border: "none",
                    fontWeight: "bold",
                    fontSize: "16px",
                    cursor: "pointer",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center"
                  }}
                >
                  +
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Start Game Action Button */}
      <div style={{ marginTop: "16px", display: "flex", flexDirection: "column", gap: "8px", alignItems: "center" }}>
        <button
          onClick={handleSaveAndStart}
          style={{
            width: "100%",
            maxWidth: "400px",
            padding: "16px",
            borderRadius: "25px",
            backgroundColor: "#009E60",
            color: "#FFF",
            border: "none",
            fontSize: "18px",
            fontWeight: "bold",
            cursor: "pointer",
            boxShadow: "0 4px 12px rgba(0,0,0,0.3)"
          }}
        >
          {t.startGameNow}
        </button>
        <span style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>
          Mahato Software • Made with ❤️ in India
        </span>
      </div>
    </div>
  );
};
