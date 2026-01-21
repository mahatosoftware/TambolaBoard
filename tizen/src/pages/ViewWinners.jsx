import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { GameStorage } from '../utils/GameStorage';
import './ViewWinners.css';

const ViewWinners = () => {
    const navigate = useNavigate();
    const [gameData, setGameData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchGame = async () => {
            try {
                const latest = await GameStorage.getLatestGame();
                console.log("Loaded game:", latest);
                setGameData(latest);
            } catch (e) {
                console.error("Error loading winners:", e);
            } finally {
                setLoading(false);
            }
        };

        fetchGame();
    }, []);

    // Auto-focus back button on mount
    useEffect(() => {
        if (!loading) {
            const backBtn = document.querySelector('.back-btn');
            if (backBtn) backBtn.focus();
        }
    }, [loading]);

    if (loading) {
        return <div className="winners-container loading">Loading...</div>;
    }

    if (!gameData || !gameData.prizes || gameData.prizes.length === 0) {
        return (
            <div className="winners-container">
                <div className="no-data">
                    <h2>No Winners Record Found</h2>
                    <p>Play a game to see winners here.</p>
                    <button className="back-btn" onClick={() => navigate('/')}>
                        Back to Home
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="winners-container">
            <header className="winners-header">
                <h1>Game Winners</h1>
                <div className="game-meta">
                    <span>Game ID: {gameData.gameId}</span>
                    <span>Date: {new Date(gameData.timestamp || Date.now()).toLocaleDateString()}</span>
                </div>
            </header>

            <div className="winners-grid">
                {gameData.prizes.map((prize) => (
                    <div key={prize.id} className={`winner-card ${prize.status === 'CLAIMED' ? 'claimed' : 'unclaimed'}`}>
                        <div className="card-header">
                            <span className="prize-name">{prize.label}</span>
                            <span className={`status-badge ${prize.status.toLowerCase()}`}>
                                {prize.status}
                            </span>
                        </div>

                        <div className="card-body">
                            {prize.winners && prize.winners.length > 0 ? (
                                <ul className="winners-list">
                                    {prize.winners.map((winner, idx) => (
                                        <li key={idx} className="winner-name">
                                            {winner}
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <span className="no-winner">Not Claimed Yet</span>
                            )}
                        </div>
                    </div>
                ))}
            </div>

            <footer className="winners-footer">
                <button className="back-btn" onClick={() => navigate('/')}>
                    Back to Main Menu
                </button>
            </footer>
        </div>
    );
};

export default ViewWinners;
