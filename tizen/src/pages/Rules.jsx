import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGame } from '../context/GameContext';
import { rulesData } from '../utils/RulesData';
import RuleCard from '../components/RuleCard';
import RuleDetailModal from '../components/RuleDetailModal';
import './Rules.css';

const Rules = () => {
    const { selectedRules, toggleRule } = useGame();
    const navigate = useNavigate();
    const [focusedId, setFocusedId] = useState(rulesData[0].id);
    const [detailRule, setDetailRule] = useState(null);
    const bottomBtnRef = useRef(null);
    const [isBottonBtnFocused, setIsBottomBtnFocused] = useState(false);

    // Initial load
    useEffect(() => {
        // If we want to restore focus to something specific or start at top logic
    }, []);

    // Handle Keyboard Navigation (TV Remote)
    useEffect(() => {
        const handleKeyDown = (e) => {
            if (detailRule) {
                if (e.key === 'Back' || e.key === 'Escape') {
                    setDetailRule(null);
                    e.preventDefault();
                    e.stopPropagation();
                }
                return;
            }

            if (isBottonBtnFocused) {
                if (e.key === 'ArrowUp') {
                    e.preventDefault();
                    setIsBottomBtnFocused(false);
                    // Return focus to last focused rule item
                }
                if (e.key === 'Enter') {
                    // Submit is handled by button click natively, but just in case
                }
                return;
            }

            // Grid Navigation Logic
            // Assuming 160px min width + gap, approximate columns calculation or strict D-pad logic
            // For simplicity, let's map index movement.
            const currentIndex = rulesData.findIndex(r => r.id === focusedId);

            // We need to know how many columns are actually rendered to do Up/Down correctly.
            // This is tricky in responsive grid without fixed columns. 
            // Simplified approach: Left/Right = -1/+1. Up/Down = -/+ 4 (assuming 4 cols for TV, or determine dynamically)
            const columns = 5; // Approx for default TV width

            let nextIndex = currentIndex;

            switch (e.key) {
                case 'ArrowRight':
                    nextIndex = Math.min(rulesData.length - 1, currentIndex + 1);
                    break;
                case 'ArrowLeft':
                    nextIndex = Math.max(0, currentIndex - 1);
                    break;
                case 'ArrowDown':
                    if (currentIndex + columns >= rulesData.length) {
                        // Move to bottom button
                        e.preventDefault();
                        setIsBottomBtnFocused(true);
                        bottomBtnRef.current?.focus();
                        return;
                    }
                    nextIndex = Math.min(rulesData.length - 1, currentIndex + columns);
                    break;
                case 'ArrowUp':
                    nextIndex = Math.max(0, currentIndex - columns);
                    break;
                case 'Enter':
                case 'OK': // Tizen specific
                    setDetailRule(rulesData[currentIndex]);
                    break;
                case 'Back':
                case 'Escape':
                    navigate('/');
                    break;
                default:
                    return;
            }

            if (nextIndex !== currentIndex) {
                setFocusedId(rulesData[nextIndex].id);
                // Scroll into view if needed logic
                const el = document.getElementById(`rule-${rulesData[nextIndex].id}`);
                el?.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [focusedId, isBottonBtnFocused, navigate, detailRule]);

    const handleConfirm = () => {
        // Navigate to Quantity Selection instead of direct home
        navigate('/quantity');
    };

    return (
        <div className="rules-page">
            <h1 className="app-main-header">Tambola Board</h1>
            <div className="rules-header">Select Game Rules</div>

            <div className="rules-grid-container">
                <div className="rules-grid">
                    {rulesData.map(rule => (
                        <div key={rule.id} id={`rule-${rule.id}`}>
                            <RuleCard
                                rule={rule}
                                isSelected={selectedRules.includes(rule.id)}
                                isFocused={focusedId === rule.id && !isBottonBtnFocused}
                                onToggle={() => setDetailRule(rule)}
                                onFocus={() => {
                                    setFocusedId(rule.id);
                                    setIsBottomBtnFocused(false);
                                }}
                            />
                        </div>
                    ))}
                </div>
            </div>

            <div className="rules-bottom-bar">
                <button
                    ref={bottomBtnRef}
                    className="submit-btn"
                    onClick={handleConfirm}
                    onFocus={() => setIsBottomBtnFocused(true)}
                >
                    Select Quantity ({selectedRules.length})
                </button>
            </div>

            {detailRule && (
                <RuleDetailModal
                    rule={detailRule}
                    isSelected={selectedRules.includes(detailRule.id)}
                    onDismiss={() => setDetailRule(null)}
                    onToggle={toggleRule}
                />
            )}
        </div>
    );
};

export default Rules;
