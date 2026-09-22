"use client";

import React from "react";

interface TicketVisualizerProps {
  highlightedIndices: number[];
}

export const TicketVisualizer: React.FC<TicketVisualizerProps> = ({ highlightedIndices }) => {
  // Fixed 3x9 Tambola sample ticket layout
  const ticket = [
    [1, null, 23, null, 41, 52, null, 78, null],
    [null, 15, null, 34, null, 56, 63, null, 82],
    [9, null, 27, 38, null, null, null, 74, 90]
  ];

  return (
    <div style={{
      width: "100%",
      backgroundColor: "#E0F7FA",
      border: "2px solid #006064",
      borderRadius: "10px",
      padding: "8px",
      color: "#000"
    }}>
      <div style={{
        backgroundColor: "#006064",
        color: "#FFF",
        textAlign: "center",
        fontWeight: "bold",
        fontSize: "14px",
        padding: "6px 0",
        borderRadius: "6px",
        letterSpacing: "1px",
        marginBottom: "8px"
      }}>
        TAMBOLA TICKET PATTERN
      </div>

      <div style={{ display: "flex", flexDirection: "column", gap: "4px" }}>
        {ticket.map((row, rowIndex) => (
          <div key={rowIndex} style={{ display: "grid", gridTemplateColumns: "repeat(9, 1fr)", gap: "4px" }}>
            {row.map((val, colIndex) => {
              const cellIndex = rowIndex * 9 + colIndex;
              const isHighlighted = highlightedIndices.includes(cellIndex) && val !== null;
              return (
                <div
                  key={colIndex}
                  style={{
                    aspectRatio: "1/1",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    backgroundColor: val === null ? "transparent" : isHighlighted ? "#00BCD4" : "#FFFFFF",
                    color: isHighlighted ? "#FFFFFF" : "#000000",
                    border: val !== null ? "1px solid #B0BEC5" : "none",
                    borderRadius: "4px",
                    fontWeight: isHighlighted ? "bold" : "normal",
                    fontSize: "13px"
                  }}
                >
                  {val}
                </div>
              );
            })}
          </div>
        ))}
      </div>
    </div>
  );
};
