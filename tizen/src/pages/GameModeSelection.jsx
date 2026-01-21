import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Bot, User } from 'lucide-react';
import clsx from 'clsx';
import { useGame } from '../context/GameContext';
import { APP_CONSTANTS } from '../utils/Constants';
import './GameModeSelection.css';

const GameModeSelection = () => {
    const { setGameMode, startNewGame } = useGame();
    const navigate = useNavigate();
    const [focusedIndex, setFocusedIndex] = useState(0); // 0 = Moderated, 1 = Unmoderated

    const selectMode = useCallback((mode) => {
        setGameMode(mode);

        if (mode === 'UNMODERATED') {
            // User specifically asked: "on click of unModerated generate a 5 char... id"
            startNewGame(mode); // Generates new ID and resets logic, skipping sync if UNMODERATED
            navigate('/rules');
        } else {
            // MODERATED
            // "navigate to a new screen... to enter the Game Id"
            navigate('/enter-game-id');
        }
    }, [setGameMode, startNewGame, navigate]);

    useEffect(() => {
        const handleKeyDown = (e) => {
            if (e.key === 'ArrowRight') {
                setFocusedIndex(1);
            } else if (e.key === 'ArrowLeft') {
                setFocusedIndex(0);
            } else if (e.key === 'Enter' || e.key === 'OK') {
                selectMode(focusedIndex === 0 ? 'MODERATED' : 'UNMODERATED');
            } else if (e.key === 'Back' || e.key === 'Escape') {
                navigate('/');
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [focusedIndex, navigate, selectMode]);



    return (
        <div className="game-mode-page">
            <h1 className="app-main-header">Tambola Board</h1>

            <div className="mode-main-content">
                <h2 className="mode-header">Select Game Mode</h2>

                <div className="mode-container">
                    <div
                        className={clsx('mode-card', { focused: focusedIndex === 0 })}
                        onClick={() => selectMode('MODERATED')}
                        onMouseEnter={() => setFocusedIndex(0)}
                    >
                        <Bot className="mode-icon" size={48} />
                        <div className="mode-title">Moderated</div>
                        <div className="mode-desc">
                            Host controls the ticket distribution via Tambola Tickets Application.
                        </div>
                    </div>

                    <div
                        className={clsx('mode-card', { focused: focusedIndex === 1 })}
                        onClick={() => selectMode('UNMODERATED')}
                        onMouseEnter={() => setFocusedIndex(1)}
                    >
                        <User className="mode-icon" size={48} />
                        <div className="mode-title">Unmoderated</div>
                        <div className="mode-desc">
                            Host does not control the ticket distribution.
                        </div>
                    </div>
                </div>
            </div>

            <div className="copyright-text" style={{ marginTop: 'auto' }}>
                {APP_CONSTANTS.COPYRIGHT}
            </div>
        </div>
    );
};

export default GameModeSelection;
