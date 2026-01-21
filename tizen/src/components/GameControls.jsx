import React, { useEffect, useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGame } from '../context/GameContext';
import { getFunnyPhrase } from '../utils/FunnyPhrases';
import { speak } from '../utils/TTS';
import clsx from 'clsx';
import './GameControls.css';
import ClaimPrizeModal from '../components/ClaimPrizeModal'; // Assuming this path

const GameControls = () => {
    const {
        callNumber,
        lastNumber,
        isAutoCalling,
        setIsAutoCalling,
        resetBoard,
        gameId,
        calledNumbers
    } = useGame();

    const navigate = useNavigate();

    const [isClaimModalOpen, setIsClaimModalOpen] = useState(false);

    const handleCall = useCallback(() => {
        const number = callNumber();
        if (number) {
            const phrase = getFunnyPhrase(number);
            speak(phrase);
        }
    }, [callNumber]);

    const toggleAutoCall = () => {
        setIsAutoCalling(!isAutoCalling);
    };

    useEffect(() => {
        let interval;
        if (isAutoCalling) {
            // First call immediately if resuming or starting?
            // Actually Android delays 6000ms loop.
            interval = setInterval(() => {
                if (calledNumbers.length >= 90) {
                    setIsAutoCalling(false);
                    return;
                }
                handleCall();
            }, 6000);
        }
        return () => clearInterval(interval);
    }, [isAutoCalling, calledNumbers, handleCall, setIsAutoCalling]);

    const handleExit = () => {
        setIsAutoCalling(false);
        navigate('/');
    };

    const handleReset = () => {
        if (confirm("Reset current game?")) {
            resetBoard();
        }
    };

    return (
        <div className="controls-container">
            {/* Game ID & QR */}
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <div className="qr-code-placeholder">
                    {gameId ? (
                        <img
                            src={`https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${gameId}`}
                            alt="Game QR"
                            style={{ width: '100%', height: '100%', borderRadius: '8px' }}
                        />
                    ) : (
                        'QR'
                    )}
                </div>
                <span className="game-id-text">ID: {gameId}</span>
            </div>

            {/* Last Number Display */}
            <div className="last-number-card">
                <div className="last-number-label">Last Number</div>
                <div className="last-number-value">{lastNumber || '--'}</div>
            </div>

            {/* Buttons */}
            <div className="buttons-grid">
                <button className="game-btn" onClick={() => handleCall()}>
                    Call Next
                </button>
                <button
                    className={clsx("game-btn", { active: isAutoCalling })}
                    onClick={toggleAutoCall}
                >
                    {isAutoCalling ? "Pause Auto" : "Auto Call"}
                </button>
                <button
                    className="game-btn"
                    onClick={() => setIsClaimModalOpen(true)}
                    style={{ backgroundColor: '#FF9800', color: 'black' }}
                >
                    Claim Prize
                </button>
                <button className="game-btn" onClick={handleReset}>
                    Reset
                </button>
                <button className="game-btn" onClick={handleExit}>
                    Return Main Menu
                </button>
            </div>

            {isClaimModalOpen && (
                <ClaimPrizeModal onClose={() => setIsClaimModalOpen(false)} />
            )}
        </div>
    );
};

export default GameControls;
