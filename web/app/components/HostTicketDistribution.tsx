"use client";

import React, { useState, useEffect } from "react";
import QRCode from "qrcode";
import { 
  TicketDoc, 
  GameDoc, 
  TicketStats, 
  JoinRequestDoc,
  listenToTickets, 
  listenToGame, 
  listenToJoinRequests,
  approveJoinRequest,
  rejectJoinRequest,
  generateDigitalTickets, 
  generatePaperTickets, 
  issuePaperTicket, 
  releaseTicket, 
  updateGameSettings 
} from "../lib/tambola/tickets";
import { 
  QrCode, 
  Printer, 
  PlusCircle, 
  UserCheck, 
  RefreshCw, 
  CheckCircle2, 
  X, 
  Sliders, 
  Ticket, 
  Tv, 
  FileText,
  Minus,
  Plus,
  Clock,
  UserX,
  Check
} from "lucide-react";
import { HostQrScreen } from "./HostQrScreen";

interface HostTicketDistributionProps {
  gameId: string;
  onClose?: () => void;
}

export const HostTicketDistribution: React.FC<HostTicketDistributionProps> = ({
  gameId,
  onClose
}) => {
  const [tickets, setTickets] = useState<TicketDoc[]>([]);
  const [stats, setStats] = useState<TicketStats>({
    digitalGenerated: 0,
    digitalAssigned: 0,
    digitalAvailable: 0,
    paperGenerated: 0,
    paperIssued: 0,
    paperAvailable: 0,
    totalGenerated: 0,
    totalDistributed: 0,
    totalAvailable: 0
  });
  const [gameDoc, setGameDoc] = useState<GameDoc | null>(null);
  const [joinRequests, setJoinRequests] = useState<JoinRequestDoc[]>([]);

  // Active view tab: "SUMMARY" | "REQUESTS" | "DIGITAL" | "PAPER" | "PRINT"
  const [activeTab, setActiveTab] = useState<"SUMMARY" | "REQUESTS" | "DIGITAL" | "PAPER" | "PRINT">("SUMMARY");
  
  // Modals & Overlay state
  const [showQrScreen, setShowQrScreen] = useState(false);
  const [showGenDigitalModal, setShowGenDigitalModal] = useState(false);
  const [showGenPaperModal, setShowGenPaperModal] = useState(false);
  const [genCount, setGenCount] = useState<number>(1);
  const [isGenerating, setIsGenerating] = useState(false);

  // Paper Issue Modal state
  const [issueTicketId, setIssueTicketId] = useState<string | null>(null);
  const [issuePlayerName, setIssuePlayerName] = useState<string>("");

  // Print filter selection
  const [printFilter, setPrintFilter] = useState<"ALL" | "AVAILABLE" | "ISSUED">("ALL");

  // Real-time listener for tickets, game state, and join requests
  useEffect(() => {
    const unsubTickets = listenToTickets(gameId, (tList, tStats) => {
      setTickets(tList);
      setStats(tStats);
    });

    const unsubGame = listenToGame(gameId, (g) => {
      setGameDoc(g);
    });

    const unsubReqs = listenToJoinRequests(gameId, (reqs) => {
      setJoinRequests(reqs);
    });

    return () => {
      unsubTickets();
      unsubGame();
      unsubReqs();
    };
  }, [gameId]);

  const handleGenerateDigital = async () => {
    if (genCount <= 0) return;
    setIsGenerating(true);
    try {
      await generateDigitalTickets(gameId, genCount);
      setShowGenDigitalModal(false);
    } catch (e) {
      console.error("Failed to generate digital tickets:", e);
      alert("Failed to generate tickets. Please try again.");
    } finally {
      setIsGenerating(false);
    }
  };

  const handleGeneratePaper = async () => {
    if (genCount <= 0) return;
    setIsGenerating(true);
    try {
      await generatePaperTickets(gameId, genCount);
      setShowGenPaperModal(false);
    } catch (e) {
      console.error("Failed to generate paper tickets:", e);
      alert("Failed to generate tickets. Please try again.");
    } finally {
      setIsGenerating(false);
    }
  };

  const handleConfirmIssuePaper = async () => {
    if (!issueTicketId) return;
    try {
      await issuePaperTicket(gameId, issueTicketId, issuePlayerName);
      setIssueTicketId(null);
      setIssuePlayerName("");
    } catch (e) {
      console.error("Failed to issue paper ticket:", e);
    }
  };

  const handleReleaseTicket = async (tId: string) => {
    if (window.confirm("Release this ticket back to AVAILABLE status?")) {
      try {
        await releaseTicket(gameId, tId);
      } catch (e) {
        console.error("Failed to release ticket:", e);
      }
    }
  };

  const handleToggleDigitalJoin = async () => {
    if (!gameDoc) return;
    const nextVal = !gameDoc.allowDigitalJoin;
    await updateGameSettings(gameId, { allowDigitalJoin: nextVal });
  };

  const handleToggleLateJoin = async () => {
    if (!gameDoc) return;
    const nextVal = !gameDoc.allowLateJoin;
    await updateGameSettings(gameId, { allowLateJoin: nextVal });
  };

  const handleToggleRequireApproval = async () => {
    if (!gameDoc) return;
    const nextVal = gameDoc.requireApproval === undefined ? false : !gameDoc.requireApproval;
    await updateGameSettings(gameId, { requireApproval: nextVal });
  };

  const handleApproveRequest = async (reqUid: string) => {
    try {
      await approveJoinRequest(gameId, reqUid);
    } catch (e: any) {
      console.error("Approve request failed:", e);
      alert(e.message || "Failed to approve request. Please ensure digital tickets are available.");
    }
  };

  const handleRejectRequest = async (reqUid: string) => {
    try {
      await rejectJoinRequest(gameId, reqUid);
    } catch (e: any) {
      console.error("Reject request failed:", e);
    }
  };

  const handleApproveAllRequests = async () => {
    const pending = joinRequests.filter((r) => r.status === "PENDING");
    if (pending.length === 0) return;
    
    for (const req of pending) {
      try {
        await approveJoinRequest(gameId, req.uid);
      } catch (e) {
        console.error("Batch approve error for", req.name, e);
      }
    }
  };

  // Filtered lists
  const digitalTickets = tickets.filter((t) => t.type === "Digital");
  const paperTickets = tickets.filter((t) => t.type === "Paper");

  const printableTickets = paperTickets.filter((t) => {
    if (printFilter === "AVAILABLE") return t.status === "AVAILABLE";
    if (printFilter === "ISSUED") return t.status === "ASSIGNED" || t.status === "ACTIVE";
    return true;
  });

  if (showQrScreen) {
    return <HostQrScreen gameId={gameId} joinedCount={stats.digitalAssigned} onClose={() => setShowQrScreen(false)} />;
  }

  return (
    <div style={{
      minHeight: "100vh",
      backgroundColor: "#1A0033",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
    }}>
      {/* Header */}
      <header className="no-print" style={{
        backgroundColor: "rgba(42, 0, 74, 0.9)",
        backdropFilter: "blur(12px)",
        padding: "16px 24px",
        borderBottom: "1.5px solid rgba(255, 255, 255, 0.15)",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between"
      }}>
        <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          <Ticket style={{ width: "28px", height: "28px", color: "#FFD700" }} />
          <div>
            <h1 style={{ fontSize: "20px", fontWeight: "900", margin: 0, color: "#FFD700" }}>
              TICKET DISTRIBUTION DASHBOARD
            </h1>
            <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.7)" }}>
              Game Code: <strong style={{ color: "#00E5FF" }}>{gameId.toUpperCase()}</strong>
            </div>
          </div>
        </div>

        <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          <button
            onClick={() => setShowQrScreen(true)}
            style={{
              padding: "10px 18px",
              borderRadius: "12px",
              background: "linear-gradient(135deg, #00E5FF 0%, #0088FF 100%)",
              color: "#000",
              border: "none",
              fontWeight: "800",
              fontSize: "14px",
              cursor: "pointer",
              display: "flex",
              alignItems: "center",
              gap: "8px",
              boxShadow: "0 4px 15px rgba(0, 229, 255, 0.4)"
            }}
          >
            <Tv style={{ width: "18px", height: "18px" }} />
            Display Join QR
          </button>

          {onClose && (
            <button
              onClick={onClose}
              style={{
                padding: "8px",
                borderRadius: "50%",
                backgroundColor: "rgba(255,255,255,0.1)",
                color: "#FFF",
                border: "none",
                cursor: "pointer"
              }}
            >
              <X style={{ width: "20px", height: "20px" }} />
            </button>
          )}
        </div>
      </header>

      {/* Main Area */}
      <main style={{ flex: 1, padding: "20px", maxWidth: "1280px", margin: "0 auto", width: "100%" }}>
        
        {/* Navigation Tabs */}
        <div className="no-print" style={{
          display: "flex",
          gap: "10px",
          marginBottom: "20px",
          borderBottom: "1px solid rgba(255,255,255,0.1)",
          paddingBottom: "10px",
          overflowX: "auto"
        }}>
          {[
            { id: "SUMMARY", label: "📊 Summary Stats" },
            { 
              id: "REQUESTS", 
              label: `📥 Join Queue ${joinRequests.filter((r) => r.status === "PENDING").length > 0 ? `(${joinRequests.filter((r) => r.status === "PENDING").length})` : ""}`,
              badge: joinRequests.filter((r) => r.status === "PENDING").length
            },
            { id: "DIGITAL", label: `📱 Digital Tickets (${stats.digitalGenerated})` },
            { id: "PAPER", label: `🎟️ Paper Tickets (${stats.paperGenerated})` },
            { id: "PRINT", label: "🖨️ Print Layout" }
          ].map((tab) => {
            const isPendingTab = tab.id === "REQUESTS" && (tab.badge || 0) > 0;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as any)}
                style={{
                  padding: "10px 18px",
                  borderRadius: "12px",
                  backgroundColor: activeTab === tab.id ? "#7C4DFF" : isPendingTab ? "rgba(255, 152, 0, 0.2)" : "rgba(255,255,255,0.06)",
                  color: activeTab === tab.id ? "#FFF" : isPendingTab ? "#FFB74D" : "rgba(255,255,255,0.7)",
                  border: activeTab === tab.id ? "1.5px solid #00E676" : isPendingTab ? "1.5px solid #FF9800" : "1px solid rgba(255,255,255,0.1)",
                  fontWeight: "700",
                  fontSize: "14px",
                  cursor: "pointer",
                  whiteSpace: "nowrap"
                }}
              >
                {tab.label}
              </button>
            );
          })}
        </div>

        {/* Tab 1: SUMMARY STATS */}
        {activeTab === "SUMMARY" && (
          <div style={{ display: "flex", flexDirection: "column", gap: "24px" }}>
            
            {/* Real-time Stats Cards Grid */}
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(320px, 1fr))", gap: "20px" }}>
              
              {/* Digital Tickets Card */}
              <div style={{
                backgroundColor: "rgba(42, 0, 74, 0.7)",
                borderRadius: "20px",
                padding: "20px",
                border: "1.5px solid rgba(0, 229, 255, 0.4)",
                display: "flex",
                flexDirection: "column",
                gap: "16px"
              }}>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                  <h3 style={{ margin: 0, fontSize: "18px", color: "#00E5FF", fontWeight: "bold", display: "flex", alignItems: "center", gap: "8px" }}>
                    📱 DIGITAL TICKETS
                  </h3>
                  <button
                    onClick={() => { setGenCount(1); setShowGenDigitalModal(true); }}
                    style={{
                      padding: "6px 14px",
                      borderRadius: "10px",
                      backgroundColor: "#00E5FF",
                      color: "#000",
                      border: "none",
                      fontWeight: "bold",
                      fontSize: "13px",
                      cursor: "pointer",
                      display: "flex",
                      alignItems: "center",
                      gap: "6px"
                    }}
                  >
                    <PlusCircle style={{ width: "16px", height: "16px" }} />
                    + Generate
                  </button>
                </div>

                <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: "10px", textAlign: "center" }}>
                  <div style={{ backgroundColor: "rgba(255,255,255,0.06)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "22px", fontWeight: "900", color: "#FFF" }}>{stats.digitalGenerated}</div>
                    <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>Generated</div>
                  </div>
                  <div style={{ backgroundColor: "rgba(0, 230, 118, 0.15)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "22px", fontWeight: "900", color: "#69F0AE" }}>{stats.digitalAssigned}</div>
                    <div style={{ fontSize: "11px", color: "#69F0AE" }}>Assigned</div>
                  </div>
                  <div style={{ backgroundColor: "rgba(255, 215, 0, 0.15)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "22px", fontWeight: "900", color: "#FFD700" }}>{stats.digitalAvailable}</div>
                    <div style={{ fontSize: "11px", color: "#FFD700" }}>Available</div>
                  </div>
                </div>

                <button
                  onClick={() => setActiveTab("DIGITAL")}
                  style={{
                    width: "100%",
                    padding: "10px",
                    borderRadius: "12px",
                    backgroundColor: "rgba(255,255,255,0.08)",
                    color: "#FFF",
                    border: "1px solid rgba(255,255,255,0.2)",
                    fontWeight: "600",
                    fontSize: "13px",
                    cursor: "pointer"
                  }}
                >
                  View Digital Tickets List
                </button>
              </div>

              {/* Paper Tickets Card */}
              <div style={{
                backgroundColor: "rgba(42, 0, 74, 0.7)",
                borderRadius: "20px",
                padding: "20px",
                border: "1.5px solid rgba(255, 152, 0, 0.4)",
                display: "flex",
                flexDirection: "column",
                gap: "16px"
              }}>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                  <h3 style={{ margin: 0, fontSize: "18px", color: "#FF9800", fontWeight: "bold", display: "flex", alignItems: "center", gap: "8px" }}>
                    🎟️ PAPER TICKETS
                  </h3>
                  <button
                    onClick={() => { setGenCount(1); setShowGenPaperModal(true); }}
                    style={{
                      padding: "6px 14px",
                      borderRadius: "10px",
                      backgroundColor: "#FF9800",
                      color: "#000",
                      border: "none",
                      fontWeight: "bold",
                      fontSize: "13px",
                      cursor: "pointer",
                      display: "flex",
                      alignItems: "center",
                      gap: "6px"
                    }}
                  >
                    <PlusCircle style={{ width: "16px", height: "16px" }} />
                    + Generate
                  </button>
                </div>

                <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: "10px", textAlign: "center" }}>
                  <div style={{ backgroundColor: "rgba(255,255,255,0.06)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "22px", fontWeight: "900", color: "#FFF" }}>{stats.paperGenerated}</div>
                    <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>Generated</div>
                  </div>
                  <div style={{ backgroundColor: "rgba(0, 230, 118, 0.15)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "22px", fontWeight: "900", color: "#69F0AE" }}>{stats.paperIssued}</div>
                    <div style={{ fontSize: "11px", color: "#69F0AE" }}>Issued</div>
                  </div>
                  <div style={{ backgroundColor: "rgba(255, 215, 0, 0.15)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "22px", fontWeight: "900", color: "#FFD700" }}>{stats.paperAvailable}</div>
                    <div style={{ fontSize: "11px", color: "#FFD700" }}>Available</div>
                  </div>
                </div>

                <div style={{ display: "flex", gap: "10px" }}>
                  <button
                    onClick={() => setActiveTab("PAPER")}
                    style={{
                      flex: 1,
                      padding: "10px",
                      borderRadius: "12px",
                      backgroundColor: "rgba(255,255,255,0.08)",
                      color: "#FFF",
                      border: "1px solid rgba(255,255,255,0.2)",
                      fontWeight: "600",
                      fontSize: "13px",
                      cursor: "pointer"
                    }}
                  >
                    View / Issue Paper
                  </button>
                  <button
                    onClick={() => setActiveTab("PRINT")}
                    style={{
                      padding: "10px 14px",
                      borderRadius: "12px",
                      backgroundColor: "#CD7F32",
                      color: "#FFF",
                      border: "none",
                      fontWeight: "bold",
                      fontSize: "13px",
                      cursor: "pointer",
                      display: "flex",
                      alignItems: "center",
                      gap: "6px"
                    }}
                  >
                    <Printer style={{ width: "16px", height: "16px" }} />
                    Print
                  </button>
                </div>
              </div>

              {/* Overall Total Summary */}
              <div style={{
                backgroundColor: "rgba(42, 0, 74, 0.7)",
                borderRadius: "20px",
                padding: "20px",
                border: "1.5px solid rgba(255, 215, 0, 0.4)",
                display: "flex",
                flexDirection: "column",
                justifyContent: "space-between",
                gap: "16px"
              }}>
                <h3 style={{ margin: 0, fontSize: "18px", color: "#FFD700", fontWeight: "bold" }}>
                  📈 TOTAL DISTRIBUTION
                </h3>

                <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: "10px", textAlign: "center" }}>
                  <div style={{ backgroundColor: "rgba(255,255,255,0.06)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "24px", fontWeight: "900", color: "#FFF" }}>{stats.totalGenerated}</div>
                    <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>Total Tickets</div>
                  </div>
                  <div style={{ backgroundColor: "rgba(0, 230, 118, 0.15)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "24px", fontWeight: "900", color: "#69F0AE" }}>{stats.totalDistributed}</div>
                    <div style={{ fontSize: "11px", color: "#69F0AE" }}>Distributed</div>
                  </div>
                  <div style={{ backgroundColor: "rgba(255, 215, 0, 0.15)", padding: "12px", borderRadius: "12px" }}>
                    <div style={{ fontSize: "24px", fontWeight: "900", color: "#FFD700" }}>{stats.totalAvailable}</div>
                    <div style={{ fontSize: "11px", color: "#FFD700" }}>Available</div>
                  </div>
                </div>

                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.7)", textAlign: "center" }}>
                  Combined Mixed Mode: Digital & Paper Tickets running in <strong>Game {gameId}</strong>.
                </div>
              </div>
            </div>

            {/* Game Settings Toggles Panel */}
            <div style={{
              backgroundColor: "rgba(255, 255, 255, 0.05)",
              borderRadius: "20px",
              padding: "20px",
              border: "1px solid rgba(255, 255, 255, 0.15)",
              display: "flex",
              flexDirection: "column",
              gap: "14px"
            }}>
              <h3 style={{ margin: 0, fontSize: "16px", fontWeight: "bold", color: "#FFF", display: "flex", alignItems: "center", gap: "8px" }}>
                <Sliders style={{ width: "18px", height: "18px", color: "#7C4DFF" }} />
                Host Game Controls
              </h3>

              <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))", gap: "16px" }}>
                <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", backgroundColor: "rgba(0,0,0,0.3)", padding: "12px 16px", borderRadius: "14px" }}>
                  <div>
                    <div style={{ fontWeight: "bold", fontSize: "14px" }}>Allow Digital Join</div>
                    <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>Players can join via QR code or Game Code</div>
                  </div>
                  <button
                    onClick={handleToggleDigitalJoin}
                    style={{
                      padding: "6px 14px",
                      borderRadius: "20px",
                      backgroundColor: gameDoc?.allowDigitalJoin ? "#00E676" : "rgba(255,255,255,0.2)",
                      color: gameDoc?.allowDigitalJoin ? "#000" : "#FFF",
                      border: "none",
                      fontWeight: "bold",
                      cursor: "pointer"
                    }}
                  >
                    {gameDoc?.allowDigitalJoin ? "ENABLED" : "DISABLED"}
                  </button>
                </div>

                <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", backgroundColor: "rgba(0,0,0,0.3)", padding: "12px 16px", borderRadius: "14px" }}>
                  <div>
                    <div style={{ fontWeight: "bold", fontSize: "14px" }}>Allow Late Join</div>
                    <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>Permit joining after numbers draw has started</div>
                  </div>
                  <button
                    onClick={handleToggleLateJoin}
                    style={{
                      padding: "6px 14px",
                      borderRadius: "20px",
                      backgroundColor: gameDoc?.allowLateJoin ? "#00E676" : "rgba(255,255,255,0.2)",
                      color: gameDoc?.allowLateJoin ? "#000" : "#FFF",
                      border: "none",
                      fontWeight: "bold",
                      cursor: "pointer"
                    }}
                  >
                    {gameDoc?.allowLateJoin ? "ALLOWED" : "DISALLOWED"}
                  </button>
                </div>

                <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", backgroundColor: "rgba(0,0,0,0.3)", padding: "12px 16px", borderRadius: "14px" }}>
                  <div>
                    <div style={{ fontWeight: "bold", fontSize: "14px" }}>Require Host Approval</div>
                    <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>Host approves each digital ticket request from queue</div>
                  </div>
                  <button
                    onClick={handleToggleRequireApproval}
                    style={{
                      padding: "6px 14px",
                      borderRadius: "20px",
                      backgroundColor: gameDoc?.requireApproval !== false ? "#00E676" : "rgba(255,255,255,0.2)",
                      color: gameDoc?.requireApproval !== false ? "#000" : "#FFF",
                      border: "none",
                      fontWeight: "bold",
                      cursor: "pointer"
                    }}
                  >
                    {gameDoc?.requireApproval !== false ? "ENABLED" : "DISABLED"}
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Tab: PENDING JOIN REQUESTS QUEUE */}
        {activeTab === "REQUESTS" && (
          <div style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <div>
                <h2 style={{ margin: 0, fontSize: "20px" }}>📥 Host Approval Queue ({joinRequests.length})</h2>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.6)" }}>
                  Approve or reject digital ticket requests from waiting players
                </div>
              </div>

              {joinRequests.filter((r) => r.status === "PENDING").length > 0 && (
                <button
                  onClick={handleApproveAllRequests}
                  style={{
                    padding: "8px 16px",
                    borderRadius: "12px",
                    backgroundColor: "#00E676",
                    color: "#000",
                    border: "none",
                    fontWeight: "900",
                    fontSize: "13px",
                    cursor: "pointer",
                    display: "flex",
                    alignItems: "center",
                    gap: "6px"
                  }}
                >
                  <CheckCircle2 style={{ width: "16px", height: "16px" }} />
                  Approve All Pending ({joinRequests.filter((r) => r.status === "PENDING").length})
                </button>
              )}
            </div>

            {joinRequests.length === 0 ? (
              <div style={{
                backgroundColor: "rgba(255,255,255,0.05)",
                borderRadius: "20px",
                padding: "32px",
                textAlign: "center",
                color: "rgba(255,255,255,0.6)"
              }}>
                <Clock style={{ width: "36px", height: "36px", margin: "0 auto 10px auto", opacity: 0.5 }} />
                No join requests yet. When players scan the QR / enter Game Code, their requests will appear here for your approval.
              </div>
            ) : (
              <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))", gap: "14px" }}>
                {joinRequests.map((req) => (
                  <div
                    key={req.uid}
                    style={{
                      backgroundColor: "rgba(255,255,255,0.06)",
                      borderRadius: "16px",
                      padding: "16px",
                      border: req.status === "PENDING" ? "1.5px solid #FF9800" : req.status === "APPROVED" ? "1px solid #00E676" : "1px solid rgba(255,255,255,0.15)",
                      display: "flex",
                      flexDirection: "column",
                      gap: "12px"
                    }}
                  >
                    <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
                      <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                        {req.photoURL ? (
                          <img src={req.photoURL} alt="" style={{ width: "36px", height: "36px", borderRadius: "50%", border: "1.5px solid #00E5FF" }} />
                        ) : (
                          <div style={{ width: "36px", height: "36px", borderRadius: "50%", backgroundColor: "#7C4DFF", color: "#FFF", fontWeight: "bold", display: "flex", alignItems: "center", justifyContent: "center" }}>
                            {req.name.charAt(0)}
                          </div>
                        )}
                        <div>
                          <div style={{ fontWeight: "bold", fontSize: "15px", color: "#FFF" }}>{req.name}</div>
                          <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.6)" }}>
                            {req.email || "Google Player"} {req.playerId ? `• ID: #${req.playerId}` : ""}
                          </div>
                        </div>
                      </div>

                      <span style={{
                        padding: "4px 10px",
                        borderRadius: "10px",
                        fontSize: "11px",
                        fontWeight: "bold",
                        backgroundColor: req.status === "PENDING" ? "rgba(255,152,0,0.2)" : req.status === "APPROVED" ? "rgba(0,230,118,0.2)" : "rgba(255,82,82,0.2)",
                        color: req.status === "PENDING" ? "#FFB74D" : req.status === "APPROVED" ? "#69F0AE" : "#FF8A80"
                      }}>
                        {req.status}
                      </span>
                    </div>

                    {req.status === "APPROVED" && (
                      <div style={{ fontSize: "12px", color: "#69F0AE", fontWeight: "600" }}>
                        Assigned Ticket #{req.ticketId}
                      </div>
                    )}

                    {req.status === "PENDING" && (
                      <div style={{ display: "flex", gap: "8px", marginTop: "4px" }}>
                        <button
                          onClick={() => handleApproveRequest(req.uid)}
                          style={{
                            flex: 2,
                            padding: "8px",
                            borderRadius: "10px",
                            backgroundColor: "#00E676",
                            color: "#000",
                            border: "none",
                            fontWeight: "bold",
                            fontSize: "13px",
                            cursor: "pointer",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            gap: "6px"
                          }}
                        >
                          <Check style={{ width: "16px", height: "16px" }} />
                          Approve & Assign
                        </button>

                        <button
                          onClick={() => handleRejectRequest(req.uid)}
                          style={{
                            flex: 1,
                            padding: "8px",
                            borderRadius: "10px",
                            backgroundColor: "rgba(255, 82, 82, 0.2)",
                            color: "#FF8A80",
                            border: "1px solid rgba(255, 82, 82, 0.4)",
                            fontSize: "12px",
                            cursor: "pointer",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            gap: "4px"
                          }}
                        >
                          <UserX style={{ width: "14px", height: "14px" }} />
                          Reject
                        </button>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* Tab 2: DIGITAL TICKETS LIST */}
        {activeTab === "DIGITAL" && (
          <div style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <h2>📱 Digital Tickets ({digitalTickets.length})</h2>
              <button
                onClick={() => { setGenCount(1); setShowGenDigitalModal(true); }}
                style={{
                  padding: "8px 16px",
                  borderRadius: "12px",
                  backgroundColor: "#00E5FF",
                  color: "#000",
                  border: "none",
                  fontWeight: "bold",
                  cursor: "pointer"
                }}
              >
                + Generate Digital Tickets
              </button>
            </div>

            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(260px, 1fr))", gap: "14px" }}>
              {digitalTickets.map((t) => (
                <div
                  key={t.id}
                  style={{
                    backgroundColor: "rgba(255,255,255,0.06)",
                    borderRadius: "16px",
                    padding: "14px",
                    border: t.status === "ASSIGNED" ? "1.5px solid #00E676" : "1px solid rgba(255,255,255,0.15)",
                    display: "flex",
                    flexDirection: "column",
                    gap: "8px"
                  }}
                >
                  <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <span style={{ fontWeight: "bold", color: "#FFD700" }}>Ticket #{t.ticketNumber}</span>
                    <span style={{
                      padding: "3px 10px",
                      borderRadius: "10px",
                      fontSize: "11px",
                      fontWeight: "bold",
                      backgroundColor: t.status === "ASSIGNED" ? "rgba(0,230,118,0.2)" : "rgba(255,215,0,0.2)",
                      color: t.status === "ASSIGNED" ? "#69F0AE" : "#FFD700"
                    }}>
                      {t.status}
                    </span>
                  </div>

                  <div style={{ fontSize: "14px", fontWeight: "700" }}>
                    {t.name ? `👤 ${t.name}` : <em style={{ color: "rgba(255,255,255,0.5)" }}>Unassigned</em>}
                  </div>

                  <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.5)" }}>
                    ID: {t.id} {t.playerId ? `• PlayerID: ${t.playerId}` : ""}
                  </div>

                  {t.status === "ASSIGNED" && (
                    <button
                      onClick={() => handleReleaseTicket(t.id)}
                      style={{
                        marginTop: "6px",
                        padding: "6px",
                        borderRadius: "8px",
                        backgroundColor: "rgba(255, 82, 82, 0.2)",
                        color: "#FF8A80",
                        border: "1px solid rgba(255, 82, 82, 0.4)",
                        fontSize: "12px",
                        cursor: "pointer"
                      }}
                    >
                      Release Ticket
                    </button>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Tab 3: PAPER TICKETS LIST */}
        {activeTab === "PAPER" && (
          <div style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <h2>🎟️ Paper Tickets ({paperTickets.length})</h2>
              <button
                onClick={() => { setGenCount(1); setShowGenPaperModal(true); }}
                style={{
                  padding: "8px 16px",
                  borderRadius: "12px",
                  backgroundColor: "#FF9800",
                  color: "#000",
                  border: "none",
                  fontWeight: "bold",
                  cursor: "pointer"
                }}
              >
                + Generate Paper Tickets
              </button>
            </div>

            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(260px, 1fr))", gap: "14px" }}>
              {paperTickets.map((t) => (
                <div
                  key={t.id}
                  style={{
                    backgroundColor: "rgba(255,255,255,0.06)",
                    borderRadius: "16px",
                    padding: "14px",
                    border: t.status === "ASSIGNED" ? "1.5px solid #FF9800" : "1px solid rgba(255,255,255,0.15)",
                    display: "flex",
                    flexDirection: "column",
                    gap: "8px"
                  }}
                >
                  <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <span style={{ fontWeight: "bold", color: "#FFD700" }}>Ticket #{t.ticketNumber}</span>
                    <span style={{
                      padding: "3px 10px",
                      borderRadius: "10px",
                      fontSize: "11px",
                      fontWeight: "bold",
                      backgroundColor: t.status === "ASSIGNED" ? "rgba(255,152,0,0.2)" : "rgba(255,215,0,0.2)",
                      color: t.status === "ASSIGNED" ? "#FF9800" : "#FFD700"
                    }}>
                      {t.status}
                    </span>
                  </div>

                  <div style={{ fontSize: "14px", fontWeight: "700" }}>
                    {t.name ? `👤 ${t.name}` : <em style={{ color: "rgba(255,255,255,0.5)" }}>Available for Issue</em>}
                  </div>

                  <div style={{ fontSize: "11px", color: "rgba(255,255,255,0.5)" }}>
                    ID: {t.id}
                  </div>

                  {t.status === "AVAILABLE" ? (
                    <button
                      onClick={() => { setIssueTicketId(t.id); setIssuePlayerName(""); }}
                      style={{
                        marginTop: "6px",
                        padding: "8px",
                        borderRadius: "10px",
                        backgroundColor: "#FF9800",
                        color: "#000",
                        border: "none",
                        fontWeight: "bold",
                        fontSize: "13px",
                        cursor: "pointer"
                      }}
                    >
                      ISSUE TICKET
                    </button>
                  ) : (
                    <button
                      onClick={() => handleReleaseTicket(t.id)}
                      style={{
                        marginTop: "6px",
                        padding: "6px",
                        borderRadius: "8px",
                        backgroundColor: "rgba(255, 82, 82, 0.2)",
                        color: "#FF8A80",
                        border: "1px solid rgba(255, 82, 82, 0.4)",
                        fontSize: "12px",
                        cursor: "pointer"
                      }}
                    >
                      Release Ticket
                    </button>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Tab 4: PRINT LAYOUT */}
        {activeTab === "PRINT" && (
          <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
            <div className="no-print" style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              backgroundColor: "rgba(255,255,255,0.06)",
              padding: "16px",
              borderRadius: "16px"
            }}>
              <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
                <span style={{ fontWeight: "bold" }}>Filter Tickets to Print:</span>
                {(["ALL", "AVAILABLE", "ISSUED"] as const).map((f) => (
                  <button
                    key={f}
                    onClick={() => setPrintFilter(f)}
                    style={{
                      padding: "6px 14px",
                      borderRadius: "10px",
                      backgroundColor: printFilter === f ? "#FF9800" : "rgba(255,255,255,0.1)",
                      color: printFilter === f ? "#000" : "#FFF",
                      border: "none",
                      fontWeight: "bold",
                      fontSize: "12px",
                      cursor: "pointer"
                    }}
                  >
                    {f} ({paperTickets.filter((t) => f === "ALL" ? true : f === "AVAILABLE" ? t.status === "AVAILABLE" : t.status !== "AVAILABLE").length})
                  </button>
                ))}
              </div>

              <button
                onClick={() => window.print()}
                style={{
                  padding: "10px 20px",
                  borderRadius: "12px",
                  backgroundColor: "#00E676",
                  color: "#000",
                  border: "none",
                  fontWeight: "900",
                  fontSize: "15px",
                  cursor: "pointer",
                  display: "flex",
                  alignItems: "center",
                  gap: "8px",
                  boxShadow: "0 4px 15px rgba(0,230,118,0.4)"
                }}
              >
                <Printer style={{ width: "20px", height: "20px" }} />
                PRINT NOW
              </button>
            </div>

            {/* Printable Paper Tickets Grid (Fits multiple tickets per page) */}
            <div className="printable-tickets-grid" style={{
              display: "grid",
              gridTemplateColumns: "repeat(2, 1fr)",
              gap: "20px",
              padding: "10px"
            }}>
              {printableTickets.map((ticket) => {
                const gridRows = [ticket.grid.row0 || [], ticket.grid.row1 || [], ticket.grid.row2 || []];
                return (
                  <div
                    key={ticket.id}
                    className="printable-ticket"
                    style={{
                      backgroundColor: "#FFFFFF",
                      color: "#000000",
                      border: "2px solid #000000",
                      borderRadius: "10px",
                      padding: "10px",
                      pageBreakInside: "avoid",
                      boxSizing: "border-box"
                    }}
                  >
                    {/* Ticket Print Header */}
                    <div style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                      borderBottom: "2px solid #000",
                      paddingBottom: "4px",
                      marginBottom: "8px"
                    }}>
                      <div>
                        <div style={{ fontSize: "14px", fontWeight: "900", letterSpacing: "1px" }}>TAMBOLA</div>
                        <div style={{ fontSize: "10px" }}>GAME: <strong>{gameId.toUpperCase()}</strong></div>
                      </div>
                      <div style={{ textAlign: "right" }}>
                        <div style={{ fontSize: "16px", fontWeight: "900" }}>Ticket #{ticket.ticketNumber}</div>
                        <div style={{ fontSize: "9px" }}>ID: {ticket.id}</div>
                      </div>
                    </div>

                    {/* 3x9 Grid */}
                    <div style={{ display: "flex", flexDirection: "column", gap: "2px" }}>
                      {gridRows.map((row, rIdx) => (
                        <div key={rIdx} style={{ display: "grid", gridTemplateColumns: "repeat(9, 1fr)", gap: "2px" }}>
                          {row.map((val, cIdx) => (
                            <div
                              key={cIdx}
                              style={{
                                aspectRatio: "1/1",
                                border: val > 0 ? "1px solid #000" : "1px dashed #DDD",
                                backgroundColor: val > 0 ? "#FFF" : "#F9F9F9",
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                                fontWeight: val > 0 ? "bold" : "normal",
                                fontSize: "14px"
                              }}
                            >
                              {val > 0 ? val : ""}
                            </div>
                          ))}
                        </div>
                      ))}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}
      </main>

      {/* Modal: Generate Digital Tickets */}
      {showGenDigitalModal && (
        <div style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.8)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000, padding: "16px" }}>
          <div style={{ backgroundColor: "#2A004A", borderRadius: "20px", padding: "24px", maxWidth: "380px", width: "100%", border: "1.5px solid #00E5FF" }}>
            <h3 style={{ margin: "0 0 12px 0", color: "#00E5FF" }}>Generate Digital Tickets</h3>
            <p style={{ fontSize: "13px", color: "rgba(255,255,255,0.8)" }}>How many digital tickets do you want to generate?</p>
            
            <div style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: "12px", margin: "20px 0" }}>
              <button
                type="button"
                onClick={() => setGenCount(Math.max(1, genCount - 1))}
                disabled={isGenerating || genCount <= 1}
                style={{
                  width: "48px",
                  height: "48px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  color: "#FFF",
                  border: "1px solid rgba(255,255,255,0.3)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  cursor: genCount <= 1 ? "not-allowed" : "pointer",
                  opacity: genCount <= 1 ? 0.4 : 1
                }}
              >
                <Minus style={{ width: "22px", height: "22px" }} />
              </button>

              <input
                type="number"
                value={genCount}
                onChange={(e) => {
                  const val = parseInt(e.target.value);
                  setGenCount(isNaN(val) ? 1 : Math.max(1, Math.min(100, val)));
                }}
                min={1}
                max={100}
                style={{
                  flex: 1,
                  padding: "10px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(255,255,255,0.1)",
                  color: "#FFF",
                  border: "1.5px solid #00E5FF",
                  fontSize: "24px",
                  fontWeight: "bold",
                  textAlign: "center"
                }}
              />

              <button
                type="button"
                onClick={() => setGenCount(Math.min(100, genCount + 1))}
                disabled={isGenerating || genCount >= 100}
                style={{
                  width: "48px",
                  height: "48px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  color: "#FFF",
                  border: "1px solid rgba(255,255,255,0.3)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  cursor: genCount >= 100 ? "not-allowed" : "pointer",
                  opacity: genCount >= 100 ? 0.4 : 1
                }}
              >
                <Plus style={{ width: "22px", height: "22px" }} />
              </button>
            </div>

            <div style={{ display: "flex", gap: "10px" }}>
              <button onClick={() => setShowGenDigitalModal(false)} disabled={isGenerating} style={{ flex: 1, padding: "10px", borderRadius: "10px", backgroundColor: "rgba(255,255,255,0.1)", color: "#FFF", border: "none" }}>Cancel</button>
              <button onClick={handleGenerateDigital} disabled={isGenerating} style={{ flex: 1, padding: "10px", borderRadius: "10px", backgroundColor: "#00E5FF", color: "#000", border: "none", fontWeight: "bold" }}>{isGenerating ? "Generating..." : "Generate"}</button>
            </div>
          </div>
        </div>
      )}

      {/* Modal: Generate Paper Tickets */}
      {showGenPaperModal && (
        <div style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.8)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000, padding: "16px" }}>
          <div style={{ backgroundColor: "#2A004A", borderRadius: "24px", padding: "24px", maxWidth: "380px", width: "100%", border: "1.5px solid #FF9800" }}>
            <h3 style={{ margin: "0 0 12px 0", color: "#FF9800" }}>Generate Paper Tickets</h3>
            <p style={{ fontSize: "13px", color: "rgba(255,255,255,0.8)" }}>How many paper tickets do you want to generate?</p>
            
            <div style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: "12px", margin: "20px 0" }}>
              <button
                type="button"
                onClick={() => setGenCount(Math.max(1, genCount - 1))}
                disabled={isGenerating || genCount <= 1}
                style={{
                  width: "48px",
                  height: "48px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  color: "#FFF",
                  border: "1px solid rgba(255,255,255,0.3)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  cursor: genCount <= 1 ? "not-allowed" : "pointer",
                  opacity: genCount <= 1 ? 0.4 : 1
                }}
              >
                <Minus style={{ width: "22px", height: "22px" }} />
              </button>

              <input
                type="number"
                value={genCount}
                onChange={(e) => {
                  const val = parseInt(e.target.value);
                  setGenCount(isNaN(val) ? 1 : Math.max(1, Math.min(100, val)));
                }}
                min={1}
                max={100}
                style={{
                  flex: 1,
                  padding: "10px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(255,255,255,0.1)",
                  color: "#FFF",
                  border: "1.5px solid #FF9800",
                  fontSize: "24px",
                  fontWeight: "bold",
                  textAlign: "center"
                }}
              />

              <button
                type="button"
                onClick={() => setGenCount(Math.min(100, genCount + 1))}
                disabled={isGenerating || genCount >= 100}
                style={{
                  width: "48px",
                  height: "48px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  color: "#FFF",
                  border: "1px solid rgba(255,255,255,0.3)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  cursor: genCount >= 100 ? "not-allowed" : "pointer",
                  opacity: genCount >= 100 ? 0.4 : 1
                }}
              >
                <Plus style={{ width: "22px", height: "22px" }} />
              </button>
            </div>

            <div style={{ display: "flex", gap: "10px" }}>
              <button onClick={() => setShowGenPaperModal(false)} disabled={isGenerating} style={{ flex: 1, padding: "10px", borderRadius: "10px", backgroundColor: "rgba(255,255,255,0.1)", color: "#FFF", border: "none" }}>Cancel</button>
              <button onClick={handleGeneratePaper} disabled={isGenerating} style={{ flex: 1, padding: "10px", borderRadius: "10px", backgroundColor: "#FF9800", color: "#000", border: "none", fontWeight: "bold" }}>{isGenerating ? "Generating..." : "Generate"}</button>
            </div>
          </div>
        </div>
      )}

      {/* Modal: Issue Paper Ticket */}
      {issueTicketId && (
        <div style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.8)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000, padding: "16px" }}>
          <div style={{ backgroundColor: "#2A004A", borderRadius: "20px", padding: "24px", maxWidth: "380px", width: "100%", border: "1.5px solid #FFD700" }}>
            <h3 style={{ margin: "0 0 12px 0", color: "#FFD700" }}>Issue Paper Ticket {issueTicketId}</h3>
            <p style={{ fontSize: "13px", color: "rgba(255,255,255,0.8)" }}>Optionally enter player name for tracking:</p>
            <input
              type="text"
              placeholder="Player Name (Optional)"
              value={issuePlayerName}
              onChange={(e) => setIssuePlayerName(e.target.value)}
              style={{ width: "100%", padding: "12px", borderRadius: "10px", backgroundColor: "rgba(255,255,255,0.1)", color: "#FFF", border: "1px solid rgba(255,255,255,0.3)", fontSize: "16px", margin: "12px 0" }}
            />
            <div style={{ display: "flex", gap: "10px" }}>
              <button onClick={() => setIssueTicketId(null)} style={{ flex: 1, padding: "10px", borderRadius: "10px", backgroundColor: "rgba(255,255,255,0.1)", color: "#FFF", border: "none" }}>Cancel</button>
              <button onClick={handleConfirmIssuePaper} style={{ flex: 1.5, padding: "10px", borderRadius: "10px", backgroundColor: "#FF9800", color: "#000", border: "none", fontWeight: "bold" }}>Mark as Issued</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
