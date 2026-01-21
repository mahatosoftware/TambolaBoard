import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGame } from '../context/GameContext';
import { APP_CONSTANTS } from '../utils/Constants';
import { GameStorage } from '../utils/GameStorage';
import './Home.css';

const Home = () => {
    const navigate = useNavigate();
    const { startNewGame, resumeGame } = useGame();
    const [hasSavedGame, setHasSavedGame] = useState(false);
    const [savedGameData, setSavedGameData] = useState(null);

    // Auto-focus first button on mount for TV
    useEffect(() => {
        const firstBtn = document.querySelector('.menu-button');
        if (firstBtn) firstBtn.focus();

        // Check for saved game
        const checkSavedGame = async () => {
            try {
                const lastGame = await GameStorage.getLatestGame();
                if (lastGame) {
                    setHasSavedGame(true);
                    setSavedGameData(lastGame);
                    console.log("Found saved game:", lastGame.gameId);
                }
            } catch (e) {
                console.error("Error checking saved game", e);
            }
        };

        checkSavedGame();
    }, []);

    const handleNewGame = () => {
        startNewGame();
        navigate('/game-mode');
    };

    const handleContinue = () => {
        if (savedGameData) {
            resumeGame(savedGameData);
            navigate('/game');
        }
    };

    const handleExit = () => {
        // Tizen app exit logic
        try {
            if (window.tizen) {
                window.tizen.application.getCurrentApplication().exit();
            } else {
                window.close();
            }
        } catch {
            console.log("Exit not supported in browser mode");
        }
    };

    return (
        <div className="home-container">
            <h2 className="welcome-text">Welcome to Tambola Board</h2>

            <button className="menu-button" onClick={handleNewGame}>
                Start New Game
            </button>
            <button
                className="menu-button"
                onClick={handleContinue}
                disabled={!hasSavedGame}
                style={{ opacity: hasSavedGame ? 1 : 0.5 }}
            >
                {hasSavedGame ? "Continue Last Game" : "No Saved Game"}
            </button>
            <button className="menu-button" onClick={() => navigate('/winners')}>
                View Winners
            </button>
            <button className="menu-button" onClick={handleExit}>
                Exit
            </button>

            <div className="copyright-text">
                {APP_CONSTANTS.COPYRIGHT}
            </div>
        </div>
    );
};

export default Home;
