import { createContext, useContext, useState, useEffect } from 'react';
import { GameStorage } from '../utils/GameStorage';

const GameContext = createContext();

// eslint-disable-next-line react-refresh/only-export-components
export const useGame = () => useContext(GameContext);

export const GameProvider = ({ children }) => {
  // Helper to load from storage
  const loadState = () => {
    try {
      const saved = localStorage.getItem('tambola_game_state');
      return saved ? JSON.parse(saved) : {};
    } catch (e) {
      console.error("Failed to load game state", e);
      return {};
    }
  };

  const initialState = loadState();

  const [calledNumbers, setCalledNumbers] = useState(initialState.calledNumbers || []);
  const [lastNumber, setLastNumber] = useState(initialState.lastNumber || null);
  const [isAutoCalling, setIsAutoCalling] = useState(false);
  const [gameId, setGameId] = useState(initialState.gameId || '');
  const [selectedRules, setSelectedRules] = useState(initialState.selectedRules || []);
  const [ruleQuantities, setRuleQuantities] = useState(initialState.ruleQuantities || {});
  const [gameMode, setGameMode] = useState(initialState.gameMode || 'MODERATED');
  const [prizes, setPrizes] = useState(initialState.prizes || []);

  // Anonymous Auth
  useEffect(() => {
    import('../firebase').then(({ signInAnonymouslyUser }) => {
      signInAnonymouslyUser().then(() => {
        console.log("Signed in anonymously");
      }).catch((error) => {
        console.error("Error signing in anonymously", error);
      });
    });

    // Init DB
    GameStorage.init().catch(e => console.error("DB Init failed", e));
  }, []);

  const [players, setPlayers] = useState([]);

  // Subscribe to players (tickets) for Moderated Game
  useEffect(() => {
    let unsubscribe;
    if (gameId && gameMode !== 'UNMODERATED') {
      import('../firebase').then(async ({ db }) => {
        const { collection, onSnapshot, query } = await import('firebase/firestore');
        // Assuming path: games/{gameId}/tickets
        const ticketsRef = collection(db, 'games', gameId, 'tickets');

        // Listen to all tickets (or filter by non-empty name?)
        // Let's get all and filter in callback
        const q = query(ticketsRef);

        unsubscribe = onSnapshot(q, (snapshot) => {
          const loadedPlayers = [];
          snapshot.forEach(doc => {
            const data = doc.data();
            if (data.name) {
              loadedPlayers.push({
                id: doc.id,
                name: data.name,
                ticketNumber: data.ticketNumber // Optional if needed
              });
            }
          });
          // Sort alphabetically
          loadedPlayers.sort((a, b) => a.name.localeCompare(b.name));
          setPlayers(loadedPlayers);
        }, (error) => {
          console.error("Error listening to players:", error);
        });
      });
    }

    return () => {
      if (unsubscribe) unsubscribe();
      setPlayers([]); // Clear players on unmount or id change
    };
  }, [gameId, gameMode]);

  // Save to localStorage AND IndexedDB on change
  useEffect(() => {
    const state = { calledNumbers, lastNumber, gameId, selectedRules, ruleQuantities, gameMode, prizes };
    localStorage.setItem('tambola_game_state', JSON.stringify(state));

    // Save to IndexedDB if we have a gameId
    if (gameId) {
      GameStorage.saveGame(state).catch(e => console.error("Failed to save to DB", e));
    }
  }, [calledNumbers, lastNumber, gameId, selectedRules, ruleQuantities, gameMode, prizes]);

  const generateGameId = () => {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let result = '';
    for (let i = 0; i < 5; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
  };

  // Firestore Sync Helper
  const syncGameState = async (id, data, isNew = false) => {
    if (!id) return;
    try {
      const { db } = await import('../firebase');
      const { doc, setDoc, updateDoc, serverTimestamp } = await import('firebase/firestore');
      const gameRef = doc(db, 'games', id);

      if (isNew) {
        await setDoc(gameRef, {
          ...data,
          createdAt: serverTimestamp(),
          lastUpdated: serverTimestamp()
        });
      } else {
        await updateDoc(gameRef, {
          ...data,
          lastUpdated: serverTimestamp()
        });
      }
    } catch (e) {
      console.error("Firestore sync error:", e);
    }
  };

  const startNewGame = (mode) => {
    // If mode passed, use it, otherwise fallback to state (though state might be stale if just set)
    const activeMode = mode || gameMode;

    // Only if mode is passed, update state too? 
    // Usually setGameMode is called externally, but let's ensure consistency.
    if (mode) setGameMode(mode);

    const newId = generateGameId();
    setGameId(newId);
    setCalledNumbers([]);
    setLastNumber(null);
    setIsAutoCalling(false);

    if (activeMode === 'UNMODERATED') {
      console.log("Unmoderated game started - Skipping Firebase creation.");
      return;
    }

    // Sync Initial State
    syncGameState(newId, {
      gameId: newId,
      status: 'WAITING',
      calledNumbers: [],
      lastNumber: null,
      gameMode: activeMode
    }, true);
  };

  const resetBoard = () => {
    // Keep same ID or new ID? Android generates new ID on resetBoard via logic comment.
    // "Let's generate a NEW ID using Util to maintain 'Reset' behavior"
    startNewGame();
  };

  const callNumber = () => {
    const allNumbers = Array.from({ length: 90 }, (_, i) => i + 1);
    const available = allNumbers.filter(n => !calledNumbers.includes(n));

    if (available.length === 0) {
      setIsAutoCalling(false);
      return null;
    }

    const randomIndex = Math.floor(Math.random() * available.length);
    const newNumber = available[randomIndex];

    const newCalledList = [...calledNumbers, newNumber];
    setCalledNumbers(newCalledList);
    setLastNumber(newNumber);

    // Sync Update
    if (gameMode !== 'UNMODERATED') {
      syncGameState(gameId, {
        calledNumbers: newCalledList,
        lastNumber: newNumber,
        status: 'IN_PROGRESS'
      });
    }

    return newNumber;
  };

  const toggleRule = (ruleId) => {
    setSelectedRules(prev => {
      const isSelected = prev.includes(ruleId);
      let newRules;
      let newQuantities = { ...ruleQuantities };

      if (isSelected) {
        delete newQuantities[ruleId];
        newRules = prev.filter(id => id !== ruleId);
      } else {
        newQuantities[ruleId] = 1;
        newRules = [...prev, ruleId];
      }

      setRuleQuantities(newQuantities); // Need to update state here manually if not using functional update for both?
      // Actually, standard setState batching might be better, but let's calculate new state for sync.

      if (gameMode !== 'UNMODERATED') {
        syncGameState(gameId, {
          selectedRules: newRules,
          ruleQuantities: newQuantities
        });
      }

      return newRules;
    });
  };

  const updateRuleQuantity = (ruleId, change) => {
    setRuleQuantities(prev => {
      const current = prev[ruleId] || 1;
      const newValue = Math.max(1, current + change);
      const newQuantities = { ...prev, [ruleId]: newValue };

      if (gameMode !== 'UNMODERATED') {
        syncGameState(gameId, {
          ruleQuantities: newQuantities
        });
      }

      return newQuantities;
    });
  };



  const initializePrizes = (rules, quantities) => {
    // Import here to avoid circular dependency if RulesData uses context (unlikely but safe)
    // Actually RulesData is static, so we can import at top, but for now let's assume valid inputs
    import('../utils/RulesData').then(({ rulesData }) => {
      const newPrizes = [];

      rules.forEach(ruleId => {
        const ruleParams = rulesData.find(r => r.id === ruleId);
        if (!ruleParams) return;

        const qty = quantities[ruleId] || 1;
        for (let i = 0; i < qty; i++) {
          newPrizes.push({
            id: `${ruleId}_${i}`,
            ruleId: ruleId,
            ruleName: ruleParams.name,
            label: `${ruleParams.name} ${i + 1}`,
            winners: [], // Array of strings
            status: 'OPEN', // 'OPEN' | 'CLAIMED'
            timestamp: Date.now()
          });
        }
      });

      setPrizes(newPrizes);

      if (gameMode !== 'UNMODERATED') {
        syncGameState(gameId, { prizes: newPrizes });
      }
    });
  };

  const updatePrize = (prizeId, updates) => {
    setPrizes(prev => {
      const newPrizes = prev.map(p =>
        p.id === prizeId ? { ...p, ...updates } : p
      );

      if (gameMode !== 'UNMODERATED') {
        syncGameState(gameId, { prizes: newPrizes });
      }
      return newPrizes;
    });
  };

  const resumeGame = (gameState) => {
    setGameId(gameState.gameId);
    setCalledNumbers(gameState.calledNumbers || []);
    setLastNumber(gameState.lastNumber);
    setSelectedRules(gameState.selectedRules || []);
    setRuleQuantities(gameState.ruleQuantities || {});
    setGameMode(gameState.gameMode || 'MODERATED');
    setPrizes(gameState.prizes || []);
    setIsAutoCalling(false);
  };

  return (
    <GameContext.Provider value={{
      calledNumbers,
      lastNumber,
      isAutoCalling,
      setIsAutoCalling,
      gameId,
      setGameId,
      startNewGame,
      resetBoard,
      callNumber,
      selectedRules,
      toggleRule,
      ruleQuantities,
      updateRuleQuantity,
      gameMode,
      setGameMode,
      resumeGame,
      prizes,
      initializePrizes,
      updatePrize,
      players
    }}>
      {children}
    </GameContext.Provider>
  );
};
