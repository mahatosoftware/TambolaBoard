"use client";

import React, { useState } from "react";
import { TAMBOLA_RULES, TambolaRule } from "../lib/rules";
import { TicketVisualizer } from "./TicketVisualizer";
import { useTranslation } from "../lib/useTranslation";

interface RuleSelectionScreenProps {
  onContinue: (selectedRules: TambolaRule[]) => void;
  onBack: () => void;
  initialSelectedRules?: TambolaRule[];
}

export const RuleSelectionScreen: React.FC<RuleSelectionScreenProps> = ({
  onContinue,
  onBack,
  initialSelectedRules = []
}) => {
  const { t } = useTranslation();
  const [selectedRuleIds, setSelectedRuleIds] = useState<number[]>(() => {
    if (initialSelectedRules.length > 0) {
      return initialSelectedRules.map((r) => r.id);
    }
    // Default to Full House, Early Five, Top Line, Middle Line, Bottom Line
    return [1, 4, 5, 6, 7];
  });

  const [detailRule, setDetailRule] = useState<TambolaRule | null>(null);

  const toggleRule = (ruleId: number) => {
    setSelectedRuleIds((prev) =>
      prev.includes(ruleId) ? prev.filter((id) => id !== ruleId) : [...prev, ruleId]
    );
  };

  const handleContinue = () => {
    const selected = TAMBOLA_RULES.filter((r) => selectedRuleIds.includes(r.id));
    onContinue(selected);
  };

  return (
    <div style={{
      minHeight: "100vh",
      backgroundColor: "#6C0BA9",
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
        <h1 style={{ fontSize: "22px", fontWeight: "bold" }}>{t.selectRulesTitle}</h1>
        <div style={{ width: "60px" }} />
      </div>

      {/* Rules Grid */}
      <div style={{
        flex: 1,
        display: "grid",
        gridTemplateColumns: "repeat(auto-fill, minmax(160px, 1fr))",
        gap: "14px",
        overflowY: "auto",
        paddingBottom: "16px"
      }}>
        {TAMBOLA_RULES.map((rule) => {
          const isSelected = selectedRuleIds.includes(rule.id);
          return (
            <div
              key={rule.id}
              onClick={() => toggleRule(rule.id)}
              style={{
                position: "relative",
                backgroundColor: isSelected ? "#CD7F32" : "#4A007E",
                border: isSelected ? "3px solid #FFD700" : "1px solid rgba(255,255,255,0.2)",
                borderRadius: "16px",
                padding: "14px",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                cursor: "pointer",
                textAlign: "center",
                minHeight: "140px",
                transition: "transform 0.15s ease, background-color 0.2s"
              }}
            >
              {/* Checkmark indicator */}
              {isSelected && (
                <div style={{
                  position: "absolute",
                  top: "8px",
                  left: "8px",
                  backgroundColor: "#009E60",
                  color: "#FFF",
                  width: "24px",
                  height: "24px",
                  borderRadius: "50%",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  fontWeight: "bold",
                  fontSize: "14px"
                }}>
                  ✓
                </div>
              )}

              {/* Info Icon Button */}
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  setDetailRule(rule);
                }}
                style={{
                  position: "absolute",
                  top: "8px",
                  right: "8px",
                  backgroundColor: "rgba(255,255,255,0.2)",
                  color: "#FFF",
                  border: "none",
                  borderRadius: "50%",
                  width: "26px",
                  height: "26px",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  cursor: "pointer",
                  fontSize: "13px",
                  fontWeight: "bold"
                }}
                title="View Rule Detail & Pattern"
              >
                ℹ️
              </button>

              <div style={{ fontSize: "28px", marginBottom: "8px" }}>
                {rule.type.includes("HOUSE") ? "🏠" : rule.type.includes("LINE") ? "➖" : rule.type.includes("FIVE") ? "🖐️" : "🎯"}
              </div>

              <div style={{ fontWeight: "bold", fontSize: "15px", lineHeight: "1.2" }}>
                {rule.name}
              </div>
            </div>
          );
        })}
      </div>

      {/* Footer Distribute Points Action */}
      <div style={{
        marginTop: "8px",
        paddingTop: "12px",
        borderTop: "1px solid rgba(255,255,255,0.2)",
        display: "flex",
        flexDirection: "column",
        gap: "8px",
        alignItems: "center"
      }}>
        <button
          onClick={handleContinue}
          disabled={selectedRuleIds.length === 0}
          style={{
            width: "100%",
            maxWidth: "400px",
            padding: "16px",
            borderRadius: "25px",
            backgroundColor: selectedRuleIds.length > 0 ? "#009E60" : "rgba(0,158,96,0.4)",
            color: "#FFF",
            border: "none",
            fontSize: "18px",
            fontWeight: "bold",
            cursor: selectedRuleIds.length > 0 ? "pointer" : "not-allowed",
            boxShadow: selectedRuleIds.length > 0 ? "0 4px 12px rgba(0,0,0,0.3)" : "none"
          }}
        >
          {t.continueToPoints} ({selectedRuleIds.length}) ➔
        </button>

        <span style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>
          Mahato Software • Made with ❤️ in India
        </span>
      </div>

      {/* Detail Dialog Modal */}
      {detailRule && (
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
            padding: "24px",
            maxWidth: "420px",
            width: "100%",
            border: "2px solid rgba(255,255,255,0.3)",
            boxShadow: "0 10px 30px rgba(0,0,0,0.5)"
          }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "16px" }}>
              <h2 style={{ fontSize: "20px", fontWeight: "bold" }}>{detailRule.name}</h2>
              <button
                onClick={() => setDetailRule(null)}
                style={{
                  backgroundColor: "transparent",
                  color: "#FFF",
                  border: "none",
                  fontSize: "20px",
                  cursor: "pointer"
                }}
              >
                ✕
              </button>
            </div>

            <TicketVisualizer highlightedIndices={detailRule.winningPattern} />

            <p style={{ marginTop: "16px", fontSize: "14px", lineHeight: "1.5", textAlign: "center", color: "rgba(255,255,255,0.9)" }}>
              {detailRule.description}
            </p>

            <button
              onClick={() => setDetailRule(null)}
              style={{
                marginTop: "20px",
                width: "100%",
                padding: "12px",
                backgroundColor: "#009E60",
                color: "#FFF",
                border: "none",
                borderRadius: "12px",
                fontWeight: "bold",
                fontSize: "15px",
                cursor: "pointer"
              }}
            >
              {t.close}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
