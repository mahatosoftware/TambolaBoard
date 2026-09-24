"use client";

import React, { useState, useEffect, useRef } from "react";
import { useRouter } from "next/navigation";
import { 
  Ticket, 
  ArrowRight, 
  Gamepad2, 
  QrCode, 
  Keyboard, 
  Camera, 
  Upload, 
  AlertCircle, 
  CheckCircle2 
} from "lucide-react";

export default function JoinMainPage() {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState<"SCAN" | "MANUAL">("SCAN");
  const [gameCodeInput, setGameCodeInput] = useState("");
  const [error, setError] = useState<string | null>(null);

  // Camera & QR State
  const [cameraActive, setCameraActive] = useState(false);
  const [cameraError, setCameraError] = useState<string | null>(null);
  const videoRef = useRef<HTMLVideoElement | null>(null);
  const streamRef = useRef<MediaStream | null>(null);

  const extractCodeFromUrlOrString = (raw: string): string | null => {
    const trimmed = raw.trim();
    if (!trimmed) return null;
    // If it's a URL like https://.../join/3NA2RL
    if (trimmed.includes("/join/")) {
      const parts = trimmed.split("/join/");
      const codePart = parts[parts.length - 1].split("?")[0].split("#")[0].trim().toUpperCase();
      return codePart.length >= 4 ? codePart : null;
    }
    // If it's a direct code string
    const clean = trimmed.toUpperCase().replace(/[^A-Z0-9]/g, "");
    return clean.length >= 4 ? clean : null;
  };

  const startCamera = async () => {
    setCameraError(null);
    try {
      if (typeof window !== "undefined" && !window.isSecureContext && window.location.hostname !== "localhost" && window.location.hostname !== "127.0.0.1") {
        throw new Error("Camera streaming requires HTTPS or localhost connection. Please switch to 'Enter Code' or upload a QR image.");
      }
      if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
        throw new Error("Live camera is disabled over unencrypted HTTP. Please switch to 'Enter Code' or upload a QR image.");
      }
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { facingMode: "environment", width: { ideal: 1280 }, height: { ideal: 720 } }
      });
      streamRef.current = stream;
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
        await videoRef.current.play();
      }
      setCameraActive(true);
    } catch (err: any) {
      console.warn("Camera start warning:", err);
      let msg = err.message || "Could not access camera.";
      if (err.name === "NotAllowedError" || err.name === "PermissionDeniedError") {
        msg = "Camera permission denied. Please allow camera access in your browser settings or enter code manually.";
      }
      setCameraError(msg);
      setCameraActive(false);
    }
  };

  const stopCamera = () => {
    if (streamRef.current) {
      streamRef.current.getTracks().forEach((t) => t.stop());
      streamRef.current = null;
    }
    setCameraActive(false);
  };

  useEffect(() => {
    if (activeTab === "SCAN") {
      startCamera();
    } else {
      stopCamera();
    }

    return () => {
      stopCamera();
    };
  }, [activeTab]);

  // QR Scanning Loop using browser BarcodeDetector API
  useEffect(() => {
    if (activeTab !== "SCAN" || !cameraActive || !videoRef.current) return;

    let animId: number;
    let detector: any = null;

    if ("BarcodeDetector" in window) {
      try {
        detector = new (window as any).BarcodeDetector({ formats: ["qr_code"] });
      } catch (e) {
        console.warn("BarcodeDetector init warning:", e);
      }
    }

    const scanFrame = async () => {
      if (videoRef.current && videoRef.current.readyState === videoRef.current.HAVE_ENOUGH_DATA && detector) {
        try {
          const barcodes = await detector.detect(videoRef.current);
          if (barcodes && barcodes.length > 0) {
            const rawValue = barcodes[0].rawValue;
            const code = extractCodeFromUrlOrString(rawValue);
            if (code) {
              stopCamera();
              router.push(`/join/${code}`);
              return;
            }
          }
        } catch (e) {
          // Ignore frame errors
        }
      }
      animId = requestAnimationFrame(scanFrame);
    };

    if (detector) {
      animId = requestAnimationFrame(scanFrame);
    }

    return () => {
      if (animId) cancelAnimationFrame(animId);
    };
  }, [activeTab, cameraActive, router]);

  // File Upload QR parsing fallback
  const handleFileUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    if ("BarcodeDetector" in window) {
      try {
        const imageBitmap = await createImageBitmap(file);
        const detector = new (window as any).BarcodeDetector({ formats: ["qr_code"] });
        const barcodes = await detector.detect(imageBitmap);
        if (barcodes && barcodes.length > 0) {
          const code = extractCodeFromUrlOrString(barcodes[0].rawValue);
          if (code) {
            router.push(`/join/${code}`);
            return;
          }
        }
        setError("Could not find a valid Tambola QR code in this image.");
      } catch (err) {
        setError("Unable to process image. Please enter code manually.");
      }
    } else {
      setError("Image scanner not supported on this browser. Please enter code manually.");
    }
  };

  const handleManualSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const code = extractCodeFromUrlOrString(gameCodeInput);
    if (!code || code.length !== 6) {
      setError("Please enter a valid 6-character Game Code (e.g. 3NA2RL)");
      return;
    }
    router.push(`/join/${code}`);
  };

  return (
    <div style={{
      minHeight: "100vh",
      background: "radial-gradient(circle at 50% 20%, #6C0BA9 0%, #3A0066 50%, #1A0033 100%)",
      color: "#FFFFFF",
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "center",
      padding: "20px",
      fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
    }}>
      <div style={{
        maxWidth: "440px",
        width: "100%",
        backgroundColor: "rgba(255, 255, 255, 0.08)",
        backdropFilter: "blur(20px)",
        padding: "28px 24px",
        borderRadius: "28px",
        border: "1.5px solid rgba(255, 255, 255, 0.2)",
        boxShadow: "0 15px 35px rgba(0,0,0,0.5)",
        textAlign: "center"
      }}>
        <div style={{
          width: "56px",
          height: "56px",
          borderRadius: "18px",
          background: "linear-gradient(135deg, #00E5FF, #0088FF)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          margin: "0 auto 12px auto",
          boxShadow: "0 4px 20px rgba(0,229,255,0.4)"
        }}>
          <Gamepad2 style={{ width: "28px", height: "28px", color: "#FFF" }} />
        </div>

        <h1 style={{ fontSize: "26px", fontWeight: "900", margin: "0 0 6px 0" }}>
          Get Digital Ticket
        </h1>

        <p style={{ fontSize: "13px", color: "rgba(255,255,255,0.75)", marginBottom: "20px" }}>
          Scan host QR Code or enter Game Code manually to claim your ticket
        </p>

        {/* Tab Selection Controls */}
        <div style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr",
          backgroundColor: "rgba(0, 0, 0, 0.3)",
          borderRadius: "16px",
          padding: "4px",
          marginBottom: "20px"
        }}>
          <button
            onClick={() => { setActiveTab("SCAN"); setError(null); }}
            style={{
              padding: "10px",
              borderRadius: "12px",
              backgroundColor: activeTab === "SCAN" ? "#00E5FF" : "transparent",
              color: activeTab === "SCAN" ? "#000" : "rgba(255,255,255,0.7)",
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
            <QrCode style={{ width: "16px", height: "16px" }} />
            Scan QR Code
          </button>

          <button
            onClick={() => { setActiveTab("MANUAL"); setError(null); }}
            style={{
              padding: "10px",
              borderRadius: "12px",
              backgroundColor: activeTab === "MANUAL" ? "#7C4DFF" : "transparent",
              color: activeTab === "MANUAL" ? "#FFF" : "rgba(255,255,255,0.7)",
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
            <Keyboard style={{ width: "16px", height: "16px" }} />
            Enter Code
          </button>
        </div>

        {/* Tab 1: SCAN QR CODE */}
        {activeTab === "SCAN" && (
          <div style={{ display: "flex", flexDirection: "column", gap: "14px" }}>
            <div style={{
              position: "relative",
              width: "100%",
              aspectRatio: "4/3",
              backgroundColor: "#000",
              borderRadius: "20px",
              overflow: "hidden",
              border: "2px solid #00E5FF",
              display: "flex",
              alignItems: "center",
              justifyContent: "center"
            }}>
              <video
                ref={videoRef}
                playsInline
                muted
                style={{
                  width: "100%",
                  height: "100%",
                  objectFit: "cover",
                  display: cameraActive ? "block" : "none"
                }}
              />

              {!cameraActive && (
                <div style={{ padding: "16px", color: "rgba(255,255,255,0.8)", fontSize: "13px", display: "flex", flexDirection: "column", alignItems: "center", gap: "10px" }}>
                  <Camera style={{ width: "36px", height: "36px", opacity: 0.5 }} />
                  <div>{cameraError || "Initializing camera scanner..."}</div>
                  <button
                    type="button"
                    onClick={() => setActiveTab("MANUAL")}
                    style={{
                      marginTop: "6px",
                      padding: "8px 16px",
                      borderRadius: "10px",
                      backgroundColor: "#7C4DFF",
                      color: "#FFF",
                      border: "none",
                      fontWeight: "bold",
                      fontSize: "12px",
                      cursor: "pointer",
                      display: "flex",
                      alignItems: "center",
                      gap: "6px"
                    }}
                  >
                    <Keyboard style={{ width: "14px", height: "14px" }} />
                    Switch to Enter Code Manually
                  </button>
                </div>
              )}

              {/* Scanning Overlay Box */}
              {cameraActive && (
                <div style={{
                  position: "absolute",
                  inset: "15%",
                  border: "2.5px dashed #00E5FF",
                  borderRadius: "16px",
                  boxShadow: "0 0 0 9999px rgba(0, 0, 0, 0.4)",
                  pointerEvents: "none"
                }} />
              )}
            </div>

            {/* Alternative File Upload button */}
            <label style={{
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              gap: "8px",
              padding: "12px",
              borderRadius: "14px",
              backgroundColor: "rgba(255,255,255,0.1)",
              color: "#FFF",
              border: "1px solid rgba(255,255,255,0.2)",
              fontSize: "13px",
              fontWeight: "600",
              cursor: "pointer"
            }}>
              <Upload style={{ width: "16px", height: "16px", color: "#00E5FF" }} />
              Upload QR Image from Gallery
              <input
                type="file"
                accept="image/*"
                capture="environment"
                onChange={handleFileUpload}
                style={{ display: "none" }}
              />
            </label>
          </div>
        )}

        {/* Tab 2: ENTER CODE MANUALLY */}
        {activeTab === "MANUAL" && (
          <form onSubmit={handleManualSubmit} style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
            <input
              type="text"
              placeholder="e.g. 3NA2RL"
              value={gameCodeInput}
              onChange={(e) => {
                const cleaned = e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, "").slice(0, 6);
                setGameCodeInput(cleaned);
                setError(null);
              }}
              maxLength={6}
              required
              style={{
                padding: "16px",
                borderRadius: "16px",
                backgroundColor: "rgba(255,255,255,0.15)",
                color: "#FFF",
                border: error ? "2px solid #FF5252" : "1.5px solid rgba(255,255,255,0.3)",
                fontSize: "24px",
                fontWeight: "900",
                textAlign: "center",
                letterSpacing: "4px",
                outline: "none"
              }}
            />

            <button
              type="submit"
              style={{
                padding: "16px",
                borderRadius: "16px",
                background: "linear-gradient(135deg, #00E676 0%, #00B0FF 100%)",
                color: "#FFF",
                border: "none",
                fontWeight: "900",
                fontSize: "16px",
                cursor: "pointer",
                boxShadow: "0 6px 20px rgba(0, 230, 118, 0.4)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: "8px"
              }}
            >
              CONTINUE TO GET TICKET
              <ArrowRight style={{ width: "20px", height: "20px" }} />
            </button>
          </form>
        )}

        {error && (
          <div style={{ marginTop: "14px", color: "#FF5252", fontSize: "13px", fontWeight: "bold", display: "flex", alignItems: "center", justifyContent: "center", gap: "6px" }}>
            <AlertCircle style={{ width: "16px", height: "16px" }} />
            {error}
          </div>
        )}
      </div>
    </div>
  );
}
