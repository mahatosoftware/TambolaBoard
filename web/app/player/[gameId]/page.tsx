"use client";

import { use, useEffect } from "react";
import { useRouter } from "next/navigation";

interface PlayerGamePageProps {
  params: Promise<{ gameId: string }>;
}

export default function PlayerGamePage({ params }: PlayerGamePageProps) {
  const resolvedParams = use(params);
  const router = useRouter();

  useEffect(() => {
    if (resolvedParams.gameId) {
      router.replace(`/join/${resolvedParams.gameId}`);
    }
  }, [resolvedParams.gameId, router]);

  return (
    <div style={{ minHeight: "100vh", backgroundColor: "#1A0033", color: "#FFF", display: "flex", alignItems: "center", justifyContent: "center" }}>
      Redirecting to join game...
    </div>
  );
}
