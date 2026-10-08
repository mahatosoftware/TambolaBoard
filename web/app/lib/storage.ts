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

export function generateGameId(length: number = 5): string {
  const chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
  let result = "";
  for (let i = 0; i < length; i++) {
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

function setCookie(name: string, value: string, days: number = 365): void {
  if (typeof document === "undefined") return;
  const expires = new Date(Date.now() + days * 864e5).toUTCString();
  document.cookie = `${encodeURIComponent(name)}=${encodeURIComponent(value)}; expires=${expires}; path=/; SameSite=Lax`;
}

function getCookie(name: string): string | null {
  if (typeof document === "undefined") return null;
  const matches = document.cookie.match(new RegExp(`(?:^|; )${encodeURIComponent(name).replace(/[\-\.\+\*]/g, "\\$&")}=([^;]*)`));
  return matches ? decodeURIComponent(matches[1]) : null;
}

export function saveSelectedLanguage(lang: string): void {
  if (typeof window === "undefined") return;
  try {
    localStorage.setItem(LANGUAGE_KEY, lang);
  } catch (e) {
    console.error("Failed to save language to localStorage:", e);
  }
  setCookie(LANGUAGE_KEY, lang, 365);
  window.dispatchEvent(new CustomEvent("tambola_language_changed", { detail: lang }));
}

export function loadSelectedLanguage(): string {
  if (typeof window === "undefined") return "en";
  const cookieLang = getCookie(LANGUAGE_KEY);
  if (cookieLang) return cookieLang;
  try {
    return localStorage.getItem(LANGUAGE_KEY) || "en";
  } catch {
    return "en";
  }
}

export function loadVoiceGender(): "female" {
  return "female";
}
