import React from 'react';
import { HashRouter, Routes, Route } from 'react-router-dom';
import { GameProvider } from './context/GameContext';
import { useTVNavigation } from './hooks/useTVNavigation';
import Home from './pages/Home';
import GameModeSelection from './pages/GameModeSelection';
import EnterGameId from './pages/EnterGameId';
import Game from './pages/Game';
import Rules from './pages/Rules';
import QuantitySelection from './pages/QuantitySelection';
import ViewWinners from './pages/ViewWinners';

const AppContent = () => {
  useTVNavigation(); // Initialize global key listeners if needed

  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/game-mode" element={<GameModeSelection />} />
      <Route path="/enter-game-id" element={<EnterGameId />} />
      <Route path="/game" element={<Game />} />
      <Route path="/rules" element={<Rules />} />
      <Route path="/quantity" element={<QuantitySelection />} />
      <Route path="/winners" element={<ViewWinners />} />
    </Routes>
  );
};

const App = () => {
  return (
    <GameProvider>
      <HashRouter>
        <AppContent />
      </HashRouter>
    </GameProvider>
  );
};

export default App;
