import React, { useEffect } from 'react';
import Board from '../components/Board';
import GameControls from '../components/GameControls';
import './Game.css';

const Game = () => {

    // Auto focus Call button on mount
    useEffect(() => {
        // A bit of a hack to focus the main action button
        const callBtn = document.querySelector('.game-btn');
        if (callBtn) callBtn.focus();
    }, []);

    return (
        <div className="game-page">
            <h1 className="app-main-header">Tambola Board</h1>
            <div className="game-main-content">
                <div className="game-left-panel">
                    <GameControls />
                </div>
                <div className="game-right-panel">
                    <Board />
                </div>
            </div>
        </div>
    );
};

export default Game;
