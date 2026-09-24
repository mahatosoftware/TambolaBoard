"use client";

import React, { use } from "react";
import { HostTicketDistribution } from "../../../components/HostTicketDistribution";

interface HostGamePageProps {
  params: Promise<{ gameId: string }>;
}

export default function HostGamePage({ params }: HostGamePageProps) {
  const resolvedParams = use(params);
  const gameId = resolvedParams.gameId || "";

  return <HostTicketDistribution gameId={gameId} />;
}
