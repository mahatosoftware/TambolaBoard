import { TambolaRule } from "./rules";

export interface PrizeItem {
  id: string;
  ruleId: number;
  ruleName: string;
  winnerName: string;
  isClaimed: boolean;
}

export interface GameState {
  gameId: string;
  calledNumbers: number[];
  lastNumber: number | null;
  selectedRules: TambolaRule[];
  prizes: PrizeItem[];
  isModerated?: boolean;
}

const GAME_STATE_KEY = "tambola_game_state";
const LANGUAGE_KEY = "tambola_selected_lang";

export function generateGameId(): string {
  const chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
  let result = "";
  for (let i = 0; i < 6; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}

export function saveGameState(state: GameState): void {
  if (typeof window === "undefined") return;
  try {
    localStorage.setItem(GAME_STATE_KEY, JSON.stringify(state));
  } catch (e) {
    console.error("Failed to save game state:", e);
  }
}

export function loadGameState(): GameState | null {
  if (typeof window === "undefined") return null;
  try {
    const raw = localStorage.getItem(GAME_STATE_KEY);
    if (!raw) return null;
    return JSON.parse(raw) as GameState;
  } catch (e) {
    console.error("Failed to load game state:", e);
    return null;
  }
}

export function clearGameState(): void {
  if (typeof window === "undefined") return;
  localStorage.removeItem(GAME_STATE_KEY);
}

export function saveSelectedLanguage(lang: string): void {
  if (typeof window === "undefined") return;
  localStorage.setItem(LANGUAGE_KEY, lang);
  window.dispatchEvent(new CustomEvent("tambola_language_changed", { detail: lang }));
}

export function loadSelectedLanguage(): string {
  if (typeof window === "undefined") return "en";
  return localStorage.getItem(LANGUAGE_KEY) || "en";
}

export function loadVoiceGender(): "female" {
  return "female";
}
