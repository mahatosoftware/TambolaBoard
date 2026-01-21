import React from 'react';
import { X } from 'lucide-react';
import { useGame } from '../context/GameContext';
import './ClaimPrizeModal.css';

const ClaimPrizeModal = ({ onClose }) => {
    const { prizes, updatePrize, gameMode, players } = useGame();

    // Auto-focus logic for TV/Keyboard
    React.useEffect(() => {
        const firstInput = document.querySelector('.modal-content input, .modal-content select, .modal-content button');
        if (firstInput) firstInput.focus();
    }, []);

    const handleUpdateWinner = (prizeId, value) => {
        // Keeps existing logic for text input (Unmoderated)
        const winnersArray = value.split(',').map(s => s.trim()).filter(s => s);
        updatePrize(prizeId, { winners: winnersArray });
    };

    const addWinner = (prize, playerName) => {
        if (!playerName) return;
        const currentWinners = prize.winners || [];
        if (!currentWinners.includes(playerName)) {
            updatePrize(prize.id, { winners: [...currentWinners, playerName] });
        }
    };

    const removeWinner = (prize, playerName) => {
        const currentWinners = prize.winners || [];
        const newWinners = currentWinners.filter(w => w !== playerName);
        updatePrize(prize.id, { winners: newWinners });
    };

    const toggleClaim = (prize) => {
        const newStatus = prize.status === 'CLAIMED' ? 'OPEN' : 'CLAIMED';
        updatePrize(prize.id, { status: newStatus });
    };

    // Helper to join array for input value
    const getWinnersString = (winners) => {
        if (!winners) return '';
        return winners.join(', ');
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Manage Prizes</h2>
                    <button className="close-btn" onClick={onClose}>
                        <X size={24} />
                    </button>
                </div>

                <div className="prizes-list">
                    {prizes.length === 0 ? (
                        <div className="no-prizes">
                            No rules selected for this game.
                        </div>
                    ) : (
                        // Sort: Open first, Claimed last
                        [...prizes]
                            .sort((a, b) => {
                                if (a.status === b.status) return 0;
                                return a.status === 'CLAIMED' ? 1 : -1;
                            })
                            .map((prize) => (
                                <div
                                    key={prize.id}
                                    className={`prize-item ${prize.status === 'CLAIMED' ? 'claimed' : ''}`}
                                >
                                    <div className="prize-info">
                                        <span className="prize-label">{prize.label}</span>
                                        <span className="prize-status">{prize.status}</span>
                                    </div>

                                    <div className="prize-actions-container">
                                        {/* Winner Input Section */}
                                        <div className="winner-section">
                                            {gameMode === 'MODERATED' ? (
                                                <div className="moderated-input">
                                                    {/* Selected Winners Chips */}
                                                    <div className="chips-container">
                                                        {(prize.winners || []).map((winner) => (
                                                            <span key={winner} className="winner-chip">
                                                                {winner}
                                                                {prize.status !== 'CLAIMED' && (
                                                                    <button
                                                                        className="chip-remove"
                                                                        onClick={() => removeWinner(prize, winner)}
                                                                    >
                                                                        ×
                                                                    </button>
                                                                )}
                                                            </span>
                                                        ))}
                                                    </div>

                                                    <select
                                                        className="winner-select"
                                                        onChange={(e) => {
                                                            addWinner(prize, e.target.value);
                                                            e.target.value = ""; // Reset dropdown
                                                        }}
                                                        disabled={prize.status === 'CLAIMED'}
                                                        defaultValue=""
                                                    >
                                                        <option value="" disabled>Select Winner</option>
                                                        {players.map(p => (
                                                            <option key={p.id} value={p.name}>{p.name}</option>
                                                        ))}
                                                    </select>
                                                </div>
                                            ) : (
                                                <input
                                                    type="text"
                                                    className="winner-input"
                                                    placeholder="Enter Winner Name(s)..."
                                                    value={getWinnersString(prize.winners)}
                                                    onChange={(e) => handleUpdateWinner(prize.id, e.target.value)}
                                                    readOnly={prize.status === 'CLAIMED'}
                                                />
                                            )}
                                        </div>

                                        <button
                                            className={`claim-btn ${prize.status === 'CLAIMED' ? 'undo-btn' : 'mark-btn'}`}
                                            onClick={() => toggleClaim(prize)}
                                        >
                                            {prize.status === 'CLAIMED' ? 'Undo' : 'Claim'}
                                        </button>
                                    </div>
                                </div>
                            ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default ClaimPrizeModal;
