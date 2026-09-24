"use client";

import React, { useState, useEffect, use } from "react";
import { 
  GameDoc, 
  TicketDoc, 
  JoinRequestDoc,
  listenToGame, 
  assignDigitalTicketAtomically,
  getOrCreateGame,
  submitJoinRequest,
  listenToMyJoinRequest
} from "../../lib/tambola/tickets";
import { db, doc, getDoc, auth, onAuthStateChanged, signInWithGoogleHost } from "../../lib/firebase";
import type { User as FirebaseUser } from "firebase/auth";
import { DigitalTicketView } from "../../components/DigitalTicketView";
import { Ticket, AlertCircle, ArrowRight, User, LogIn, CheckCircle2, Clock, RefreshCw } from "lucide-react";

interface JoinPageProps {
  params: Promise<{ gameCode: string }>;
}

export default function JoinGamePage({ params }: JoinPageProps) {
  const resolvedParams = use(params);
  const rawCode = resolvedParams.gameCode || "";
  const gameCode = rawCode.trim().toUpperCase();

  const [gameDoc, setGameDoc] = useState<GameDoc | null>(null);
  const [ticketDoc, setTicketDoc] = useState<TicketDoc | null>(null);
  const [playerId, setPlayerId] = useState<string>("");

  const [authUser, setAuthUser] = useState<FirebaseUser | null>(null);
  const [userProfile, setUserProfile] = useState<{ displayName?: string; playerId?: string } | null>(null);
  const [playerNameInput, setPlayerNameInput] = useState<string>("");
  const [myJoinRequest, setMyJoinRequest] = useState<JoinRequestDoc | null>(null);

  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isSigningIn, setIsSigningIn] = useState<boolean>(false);
  const [isAssigning, setIsAssigning] = useState<boolean>(false);

  // Session Key
  const sessionKey = `tambola_player_session_${gameCode}`;

  // 1. Observe Firebase Auth state & fetch user profile doc
  useEffect(() => {
    const unsub = onAuthStateChanged(auth, async (user) => {
      if (user && !user.isAnonymous) {
        setAuthUser(user);
        setPlayerNameInput(user.displayName || "");

        // Fetch user profile from users/{uid}
        try {
          const uSnap = await getDoc(doc(db, "users", user.uid));
          if (uSnap.exists()) {
            const data = uSnap.data();
            setUserProfile({
              displayName: data.displayName || user.displayName || "",
              playerId: data.playerId || ""
            });
          }
        } catch (e) {
          console.warn("User profile fetch warning:", e);
        }
      } else {
        setAuthUser(null);
        setUserProfile(null);
      }
    });

    return () => unsub();
  }, []);

  // 2. Listen to real-time status of current user's join request
  useEffect(() => {
    if (!authUser?.uid || !gameCode) return;

    const unsubReq = listenToMyJoinRequest(gameCode, authUser.uid, async (req) => {
      setMyJoinRequest(req);

      // If approved, fetch assigned ticket automatically
      if (req && req.status === "APPROVED" && req.ticketId) {
        try {
          const tRef = doc(db, "games", gameCode, "tickets", req.ticketId);
          const tSnap = await getDoc(tRef);
          if (tSnap.exists()) {
            const data = tSnap.data() as TicketDoc;
            setTicketDoc(data);
            setPlayerId(data.playerId || req.playerId || "");
            if (typeof window !== "undefined") {
              localStorage.setItem(
                sessionKey,
                JSON.stringify({
                  ticketId: data.id,
                  playerId: data.playerId || req.playerId || "",
                  playerName: req.name,
                  userUid: authUser.uid
                })
              );
            }
          }
        } catch (e) {
          console.error("Fetch approved ticket error:", e);
        }
      }
    });

    return () => unsubReq();
  }, [gameCode, authUser?.uid, sessionKey]);

  // 3. Check existing session and listen to game state
  useEffect(() => {
    if (!gameCode) {
      setErrorMsg("Invalid Game Code");
      setIsLoading(false);
      return;
    }

    let isMounted = true;

    // Check if session exists in localStorage
    if (typeof window !== "undefined") {
      try {
        const savedSession = localStorage.getItem(sessionKey);
        if (savedSession) {
          const parsed = JSON.parse(savedSession);
          if (parsed.ticketId) {
            // Fetch saved ticket doc
            const tRef = doc(db, "games", gameCode, "tickets", parsed.ticketId);
            getDoc(tRef).then((snap) => {
              if (snap.exists() && isMounted) {
                const data = snap.data() as TicketDoc;
                if (data.status === "ASSIGNED" || data.status === "ACTIVE") {
                  setTicketDoc(data);
                  setPlayerId(data.playerId || parsed.playerId || "");
                }
              }
            }).catch(console.error);
          }
        }
      } catch (e) {
        console.error("Session restore error:", e);
      }
    }

    // Ensure game document exists in Firestore and listen to updates
    getOrCreateGame(gameCode)
      .then((gDoc) => {
        if (isMounted && gDoc) {
          setGameDoc(gDoc);
          setIsLoading(false);
        }
      })
      .catch((err) => {
        console.warn("getOrCreateGame on mount warning:", err);
      });

    // Listen to real-time Game doc updates
    const unsubGame = listenToGame(gameCode, (gDoc, err) => {
      if (!isMounted) return;
      if (gDoc) {
        setGameDoc(gDoc);
      }
      if (err) {
        console.warn("listenToGame warning:", err);
      }
      setIsLoading(false);
    });

    return () => {
      isMounted = false;
      unsubGame();
    };
  }, [gameCode, sessionKey]);

  // Handle Google Auth Sign-In
  const handleGoogleSignIn = async () => {
    setIsSigningIn(true);
    setErrorMsg(null);
    try {
      const user = await signInWithGoogleHost();
      if (user) {
        setAuthUser(user);
        setPlayerNameInput(user.displayName || "");
      }
    } catch (err: any) {
      console.error("Google Auth Join Error:", err);
      if (err.code === "auth/unauthorized-domain") {
        setErrorMsg("Unauthorized Domain: Please add your domain/IP to Firebase Console -> Authentication -> Settings -> Authorized Domains.");
      } else {
        setErrorMsg("Google Sign-In failed or was cancelled. Please try again.");
      }
    } finally {
      setIsSigningIn(false);
    }
  };

  // Handle Joining & Atomic Ticket Assignment / Request
  const handleJoinGame = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!authUser || authUser.isAnonymous) {
      setErrorMsg("Google Authentication required to claim ticket.");
      return;
    }

    const name = playerNameInput.trim() || authUser.displayName || "Player";

    setIsAssigning(true);
    setErrorMsg(null);

    try {
      let currentDoc = gameDoc;
      if (!currentDoc) {
        currentDoc = await getOrCreateGame(gameCode);
        setGameDoc(currentDoc);
      }

      if (!currentDoc) {
        throw new Error("Game not found");
      }
      if (currentDoc.status === "COMPLETED" || currentDoc.status === "CANCELLED") {
        throw new Error("This game has ended");
      }
      if (currentDoc.allowDigitalJoin === false) {
        throw new Error("Digital ticket joining is closed for this game");
      }
      if (currentDoc.status === "STARTED" && currentDoc.allowLateJoin === false) {
        throw new Error("The host has disabled late joining");
      }

      if (currentDoc.requireApproval !== false) {
        // Submit request to Host Approval Queue
        await submitJoinRequest(gameCode, name, {
          uid: authUser.uid,
          displayName: name,
          email: authUser.email,
          photoURL: authUser.photoURL,
          playerId: userProfile?.playerId
        });
      } else {
        // Direct Ticket Assignment if Host Approval is disabled
        const authPayload = {
          uid: authUser.uid,
          displayName: name,
          playerId: userProfile?.playerId
        };

        const { ticket, playerId: assignedId } = await assignDigitalTicketAtomically(
          gameCode,
          name,
          authPayload
        );

        if (typeof window !== "undefined") {
          localStorage.setItem(
            sessionKey,
            JSON.stringify({
              ticketId: ticket.id,
              playerId: assignedId,
              playerName: name,
              userUid: authUser.uid
            })
          );
        }

        setTicketDoc(ticket);
        setPlayerId(assignedId);
      }
    } catch (err: any) {
      console.error("Join error:", err);
      if (err.message === "NO_AVAILABLE_TICKETS") {
        setErrorMsg("No digital tickets are currently available. Please ask the host to generate more.");
      } else {
        setErrorMsg(err.message || "Failed to join game. Please try again.");
      }
    } finally {
      setIsAssigning(false);
    }
  };

  // If player already has assigned ticket doc, render DigitalTicketView
  if (ticketDoc) {
    return (
      <DigitalTicketView
        gameId={gameCode}
        ticket={ticketDoc}
        playerId={playerId}
        onExit={() => {
          if (window.confirm("Leave this game screen? Your ticket will remain saved to your Google Account.")) {
            setTicketDoc(null);
          }
        }}
      />
    );
  }

  return (
    <div style={{
      minHeight: "100vh",
      background: "radial-gradient(circle at 50% 20%, #6C0BA9 0%, #3A0066 50%, #1A0033 100%)",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "center",
      padding: "24px",
      fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
    }}>
      <div style={{
        maxWidth: "440px",
        width: "100%",
        backgroundColor: "rgba(255, 255, 255, 0.08)",
        backdropFilter: "blur(20px)",
        padding: "32px 24px",
        borderRadius: "28px",
        border: "1.5px solid rgba(255, 255, 255, 0.2)",
        boxShadow: "0 15px 35px rgba(0,0,0,0.5)",
        textAlign: "center"
      }}>
        
        {/* Header */}
        <div style={{
          width: "64px",
          height: "64px",
          borderRadius: "20px",
          background: "linear-gradient(135deg, #FFD700, #FF6F00)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          margin: "0 auto 16px auto",
          boxShadow: "0 4px 20px rgba(255,215,0,0.4)"
        }}>
          <Ticket style={{ width: "32px", height: "32px", color: "#FFF" }} />
        </div>

        <h1 style={{ fontSize: "28px", fontWeight: "900", margin: "0 0 6px 0", color: "#FFF" }}>
          Get Digital Ticket
        </h1>

        <div style={{
          display: "inline-block",
          padding: "4px 14px",
          borderRadius: "14px",
          backgroundColor: "rgba(0, 229, 255, 0.15)",
          color: "#00E5FF",
          fontWeight: "800",
          fontSize: "14px",
          border: "1px solid rgba(0, 229, 255, 0.4)",
          marginBottom: "24px"
        }}>
          GAME CODE: {gameCode}
        </div>

        {isLoading ? (
          <div style={{ fontSize: "14px", color: "rgba(255,255,255,0.7)" }}>
            Loading game details...
          </div>
        ) : !gameDoc ? (
          <div style={{ color: "#FF5252", fontWeight: "bold", fontSize: "15px" }}>
            <AlertCircle style={{ width: "24px", height: "24px", margin: "0 auto 8px auto" }} />
            Game not found! Please check the game code and try again.
          </div>
        ) : gameDoc.status === "COMPLETED" || gameDoc.status === "CANCELLED" ? (
          <div style={{ color: "#FF8A80", fontWeight: "bold", fontSize: "15px" }}>
            This game has ended.
          </div>
        ) : gameDoc.allowDigitalJoin === false ? (
          <div style={{ color: "#FFD54F", fontWeight: "bold", fontSize: "15px" }}>
            Digital ticket joining is currently closed by the host.
          </div>
        ) : (
          <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>

            {/* Google Authentication Step */}
            {!authUser || authUser.isAnonymous ? (
              <div style={{
                backgroundColor: "rgba(255, 255, 255, 0.06)",
                borderRadius: "20px",
                padding: "20px",
                border: "1.5px solid rgba(0, 229, 255, 0.3)",
                display: "flex",
                flexDirection: "column",
                gap: "14px"
              }}>
                <div style={{ fontSize: "14px", fontWeight: "bold", color: "#00E5FF" }}>
                  🔐 Google Sign-In Required
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.75)", lineHeight: "1.5" }}>
                  Please sign in with Google to claim your ticket. Your ticket will be linked securely so you can access it anytime.
                </div>

                <button
                  type="button"
                  onClick={handleGoogleSignIn}
                  disabled={isSigningIn}
                  style={{
                    width: "100%",
                    padding: "14px",
                    borderRadius: "14px",
                    backgroundColor: "#FFFFFF",
                    color: "#000000",
                    border: "none",
                    fontWeight: "800",
                    fontSize: "15px",
                    cursor: isSigningIn ? "not-allowed" : "pointer",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    gap: "10px",
                    boxShadow: "0 4px 15px rgba(255, 255, 255, 0.2)"
                  }}
                >
                  <svg width="20" height="20" viewBox="0 0 24 24">
                    <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                    <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                    <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
                    <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
                  </svg>
                  {isSigningIn ? "Signing In..." : "Sign in with Google"}
                </button>
              </div>
            ) : myJoinRequest?.status === "PENDING" ? (
              <div style={{
                backgroundColor: "rgba(255, 152, 0, 0.12)",
                borderRadius: "20px",
                padding: "24px",
                border: "1.5px solid #FF9800",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: "14px",
                textAlign: "center"
              }}>
                <div style={{
                  width: "56px",
                  height: "56px",
                  borderRadius: "50%",
                  backgroundColor: "rgba(255, 152, 0, 0.2)",
                  color: "#FFB74D",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center"
                }}>
                  <Clock style={{ width: "32px", height: "32px" }} />
                </div>

                <div style={{ fontSize: "18px", fontWeight: "900", color: "#FFB74D" }}>
                  ⏳ Waiting for Host Approval
                </div>

                <div style={{ fontSize: "13px", color: "rgba(255,255,255,0.8)", lineHeight: "1.5" }}>
                  Your request has been sent to the Host! Please hold on while the Host approves your request and assigns your digital ticket.
                </div>

                <div style={{
                  padding: "10px 16px",
                  borderRadius: "12px",
                  backgroundColor: "rgba(0,0,0,0.3)",
                  fontSize: "12px",
                  color: "#FFF"
                }}>
                  Player: <strong>{myJoinRequest.name}</strong> {myJoinRequest.email ? `(${myJoinRequest.email})` : ""}
                </div>
              </div>
            ) : myJoinRequest?.status === "REJECTED" ? (
              <div style={{
                backgroundColor: "rgba(255, 82, 82, 0.15)",
                borderRadius: "20px",
                padding: "20px",
                border: "1.5px solid #FF5252",
                textAlign: "center"
              }}>
                <AlertCircle style={{ width: "32px", height: "32px", color: "#FF5252", margin: "0 auto 10px auto" }} />
                <div style={{ fontSize: "16px", fontWeight: "bold", color: "#FF8A80", marginBottom: "8px" }}>
                  Join Request Rejected
                </div>
                <div style={{ fontSize: "12px", color: "rgba(255,255,255,0.7)", marginBottom: "16px" }}>
                  The host declined your join request. You can try submitting a new request or ask the host.
                </div>
                <button
                  onClick={() => setMyJoinRequest(null)}
                  style={{
                    padding: "10px 20px",
                    borderRadius: "12px",
                    backgroundColor: "#FF5252",
                    color: "#FFF",
                    border: "none",
                    fontWeight: "bold",
                    cursor: "pointer"
                  }}
                >
                  Try Again
                </button>
              </div>
            ) : (
              /* Signed In User Card & Ticket Claim Form */
              <form onSubmit={handleJoinGame} style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
                
                {/* Google User Profile Card */}
                <div style={{
                  backgroundColor: "rgba(0, 230, 118, 0.12)",
                  borderRadius: "16px",
                  padding: "14px",
                  border: "1.5px solid rgba(0, 230, 118, 0.4)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "space-between"
                }}>
                  <div style={{ display: "flex", alignItems: "center", gap: "12px", textAlign: "left" }}>
                    {authUser.photoURL ? (
                      <img
                        src={authUser.photoURL}
                        alt="Profile"
                        style={{ width: "40px", height: "40px", borderRadius: "50%", border: "2px solid #00E676" }}
                      />
                    ) : (
                      <div style={{ width: "40px", height: "40px", borderRadius: "50%", backgroundColor: "#00E676", color: "#000", fontWeight: "bold", display: "flex", alignItems: "center", justifyContent: "center" }}>
                        {authUser.displayName ? authUser.displayName.charAt(0) : "P"}
                      </div>
                    )}
                    <div>
                      <div style={{ fontWeight: "bold", fontSize: "14px", color: "#FFF" }}>
                        {authUser.displayName || "Google User"}
                      </div>
                      <div style={{ fontSize: "11px", color: "#69F0AE" }}>
                        {authUser.email || "Authenticated"} {userProfile?.playerId ? `• ID: #${userProfile.playerId}` : ""}
                      </div>
                    </div>
                  </div>

                  <CheckCircle2 style={{ width: "20px", height: "20px", color: "#00E676" }} />
                </div>

                <div style={{ textAlign: "left" }}>
                  <label style={{ fontSize: "12px", fontWeight: "700", color: "rgba(255,255,255,0.8)", marginBottom: "6px", display: "block" }}>
                    CONFIRM YOUR PLAYER NAME
                  </label>
                  <div style={{ position: "relative" }}>
                    <input
                      type="text"
                      placeholder="e.g. Rahul"
                      value={playerNameInput}
                      onChange={(e) => {
                        setPlayerNameInput(e.target.value);
                        setErrorMsg(null);
                      }}
                      maxLength={30}
                      required
                      style={{
                        width: "100%",
                        padding: "14px 16px 14px 44px",
                        borderRadius: "14px",
                        backgroundColor: "rgba(255,255,255,0.12)",
                        color: "#FFF",
                        border: errorMsg ? "2px solid #FF5252" : "1.5px solid rgba(255,255,255,0.3)",
                        fontSize: "16px",
                        fontWeight: "600",
                        outline: "none",
                        boxSizing: "border-box"
                      }}
                    />
                    <User style={{ position: "absolute", left: "14px", top: "50%", transform: "translateY(-50%)", width: "18px", height: "18px", color: "rgba(255,255,255,0.5)" }} />
                  </div>
                </div>

                <button
                  type="submit"
                  disabled={isAssigning}
                  style={{
                    padding: "16px",
                    borderRadius: "16px",
                    background: "linear-gradient(135deg, #00E676 0%, #00B0FF 100%)",
                    color: "#FFF",
                    border: "none",
                    fontWeight: "900",
                    fontSize: "16px",
                    cursor: isAssigning ? "not-allowed" : "pointer",
                    boxShadow: "0 6px 20px rgba(0, 230, 118, 0.4)",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    gap: "8px"
                  }}
                >
                  {isAssigning ? "Assigning Ticket..." : "CLAIM & VIEW TICKET"}
                  <ArrowRight style={{ width: "20px", height: "20px" }} />
                </button>

                <button
                  type="button"
                  onClick={handleGoogleSignIn}
                  style={{
                    fontSize: "12px",
                    color: "rgba(255,255,255,0.6)",
                    background: "none",
                    border: "none",
                    cursor: "pointer",
                    textDecoration: "underline"
                  }}
                >
                  Sign in with a different Google account
                </button>
              </form>
            )}

            {errorMsg && (
              <div style={{ color: "#FF5252", fontSize: "13px", fontWeight: "bold" }}>
                {errorMsg}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
