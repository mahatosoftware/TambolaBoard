"use client";

import React, { useState, useEffect } from "react";
import { HomeScreen } from "./components/HomeScreen";
import { GameModeSelectionScreen } from "./components/GameModeSelectionScreen";
import { GameIdInputScreen } from "./components/GameIdInputScreen";
import { RuleSelectionScreen } from "./components/RuleSelectionScreen";
import { PointsDistributionScreen } from "./components/PointsDistributionScreen";
import { GameScreen } from "./components/GameScreen";
import { ViewWinnersScreen } from "./components/ViewWinnersScreen";
import { TambolaRule } from "./lib/rules";
import { PrizeItem, loadGameState, generateGameId, clearGameState, GameState } from "./lib/storage";

type Screen =
  | "HOME"
  | "GAME_MODE_SELECT"
  | "GAME_ID_INPUT"
  | "RULE_SELECTION"
  | "POINTS_DISTRIBUTION"
  | "GAME_BOARD"
  | "VIEW_WINNERS";

export default function Home() {
  const [currentScreen, setCurrentScreen] = useState<Screen>("HOME");
  const [hasSavedGame, setHasSavedGame] = useState(false);

  // Active game state
  const [gameId, setGameId] = useState("");
  const [isModerated, setIsModerated] = useState(false);
  const [selectedRules, setSelectedRules] = useState<TambolaRule[]>([]);
  const [calledNumbers, setCalledNumbers] = useState<number[]>([]);
  const [lastNumber, setLastNumber] = useState<number | null>(null);
  const [prizes, setPrizes] = useState<PrizeItem[]>([]);

  // Check for saved game on load
  useEffect(() => {
    const saved = loadGameState();
    if (saved && (saved.calledNumbers?.length > 0 || saved.selectedRules?.length > 0)) {
      setHasSavedGame(true);
    } else {
      setHasSavedGame(false);
    }
  }, [currentScreen]);

  // Actions
  const handleContinueLastGame = () => {
    const saved = loadGameState();
    if (saved) {
      setGameId(saved.gameId || generateGameId());
      setCalledNumbers(saved.calledNumbers || []);
      setLastNumber(saved.lastNumber ?? null);
      setSelectedRules(saved.selectedRules || []);
      setPrizes(saved.prizes || []);
      setCurrentScreen("GAME_BOARD");
    }
  };

  const handleViewWinners = () => {
    const saved = loadGameState();
    if (saved) {
      setGameId(saved.gameId || "");
      setPrizes(saved.prizes || []);
    }
    setCurrentScreen("VIEW_WINNERS");
  };

  const handleStartUnmoderated = () => {
    setIsModerated(false);
    setGameId(generateGameId());
    setCurrentScreen("RULE_SELECTION");
  };

  const handleStartModerated = () => {
    setIsModerated(true);
    setCurrentScreen("GAME_ID_INPUT");
  };

  const handleSubmitGameId = (id: string) => {
    setGameId(id);
    setCurrentScreen("RULE_SELECTION");
  };

  const handleRulesSelected = (rules: TambolaRule[]) => {
    setSelectedRules(rules);
    setCurrentScreen("POINTS_DISTRIBUTION");
  };

  const handleStartGame = (rulesWithQuantities: TambolaRule[]) => {
    setSelectedRules(rulesWithQuantities);
    setCalledNumbers([]);
    setLastNumber(null);

    // Generate initial prizes list
    const initialPrizesList: PrizeItem[] = [];
    rulesWithQuantities.forEach((rule) => {
      const qty = rule.quantity || 1;
      for (let i = 0; i < qty; i++) {
        initialPrizesList.push({
          id: `${rule.id}-${i}-${Date.now()}`,
          ruleId: rule.id,
          ruleName: qty > 1 ? `${rule.name} #${i + 1}` : rule.name,
          winnerName: "",
          isClaimed: false
        });
      }
    });

    setPrizes(initialPrizesList);
    setCurrentScreen("GAME_BOARD");
  };

  const handleResetGame = () => {
    const newId = generateGameId();
    setGameId(newId);
    setCalledNumbers([]);
    setLastNumber(null);
  };

  return (
    <main style={{ minHeight: "100vh", backgroundColor: "#6C0BA9" }}>
      {currentScreen === "HOME" && (
        <HomeScreen
          onNewGame={() => setCurrentScreen("GAME_MODE_SELECT")}
          onContinue={handleContinueLastGame}
          onViewWinners={handleViewWinners}
          hasSavedGame={hasSavedGame}
        />
      )}

      {currentScreen === "GAME_MODE_SELECT" && (
        <GameModeSelectionScreen
          onSelectModerated={handleStartModerated}
          onSelectUnmoderated={handleStartUnmoderated}
          onBack={() => setCurrentScreen("HOME")}
        />
      )}

      {currentScreen === "GAME_ID_INPUT" && (
        <GameIdInputScreen
          onSubmitGameId={handleSubmitGameId}
          onBack={() => setCurrentScreen("GAME_MODE_SELECT")}
        />
      )}

      {currentScreen === "RULE_SELECTION" && (
        <RuleSelectionScreen
          onContinue={handleRulesSelected}
          onBack={() => setCurrentScreen(isModerated ? "GAME_ID_INPUT" : "GAME_MODE_SELECT")}
          initialSelectedRules={selectedRules}
        />
      )}

      {currentScreen === "POINTS_DISTRIBUTION" && (
        <PointsDistributionScreen
          selectedRules={selectedRules}
          onStartGame={handleStartGame}
          onBack={() => setCurrentScreen("RULE_SELECTION")}
        />
      )}

      {currentScreen === "GAME_BOARD" && (
        <GameScreen
          gameId={gameId}
          initialCalledNumbers={calledNumbers}
          initialLastNumber={lastNumber}
          selectedRules={selectedRules}
          initialPrizes={prizes}
          onReturnToMainMenu={() => setCurrentScreen("HOME")}
          onResetGame={handleResetGame}
        />
      )}

      {currentScreen === "VIEW_WINNERS" && (
        <ViewWinnersScreen
          gameId={gameId}
          prizes={prizes}
          onBack={() => setCurrentScreen("HOME")}
        />
      )}
    </main>
  );
}
