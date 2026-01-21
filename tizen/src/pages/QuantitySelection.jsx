import React, { useEffect, useState, useRef, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Minus, Plus } from 'lucide-react';
import clsx from 'clsx';
import { useGame } from '../context/GameContext';
import { rulesData } from '../utils/RulesData';
import { APP_CONSTANTS } from '../utils/Constants';
import './QuantitySelection.css';

const QuantitySelection = () => {
    const {
        selectedRules,
        ruleQuantities,
        updateRuleQuantity,
        initializePrizes // Destructure here
    } = useGame();
    const navigate = useNavigate();

    // Focus management for TV
    const [focusedIndex, setFocusedIndex] = useState(0); // 0 to rules.length * 2 + 1 (last is Save btn)
    // Indexes:
    // 0 = Rule 1 Minus
    // 1 = Rule 1 Plus
    // 2 = Rule 2 Minus
    // 3 = Rule 2 Plus
    // ...
    // Last = Save Button

    // Filter rules data based on selection
    const activeRules = rulesData.filter(r => selectedRules.includes(r.id));
    const totalItems = activeRules.length * 2 + 1; // 2 buttons per rule + 1 save button

    const saveBtnRef = useRef(null);

    const handleSave = useCallback(() => {
        // Initialize prizes based on selection
        initializePrizes(selectedRules, ruleQuantities);

        // Then navigate
        navigate('/game');
    }, [initializePrizes, selectedRules, ruleQuantities, navigate]);

    const handleAction = useCallback((index) => {
        if (index === totalItems - 1) {
            handleSave();
            return;
        }

        const ruleIndex = Math.floor(index / 2);
        const isPlus = index % 2 === 1;
        const ruleId = activeRules[ruleIndex].id;

        if (isPlus) {
            updateRuleQuantity(ruleId, 1);
        } else {
            updateRuleQuantity(ruleId, -1);
        }
    }, [totalItems, handleSave, activeRules, updateRuleQuantity]);

    useEffect(() => {
        const handleKeyDown = (e) => {
            let nextIndex = focusedIndex;

            switch (e.key) {
                case 'ArrowUp':
                    if (focusedIndex === totalItems - 1) {
                        // From Save Button to last rule Plus
                        nextIndex = (activeRules.length - 1) * 2 + 1;
                    } else {
                        // Move up one rule row (minus 2)
                        nextIndex = Math.max(0, focusedIndex - 2);
                    }
                    break;
                case 'ArrowDown':
                    if (focusedIndex >= (activeRules.length - 1) * 2) {
                        // If in last row, go to Save Button
                        nextIndex = totalItems - 1;
                    } else {
                        nextIndex = Math.min(totalItems - 1, focusedIndex + 2);
                    }
                    break;
                case 'ArrowLeft':
                    // Just toggle between Minus/Plus in same row?
                    if (focusedIndex < totalItems - 1) {
                        if (focusedIndex % 2 === 1) nextIndex = focusedIndex - 1; // Plus -> Minus
                    }
                    break;
                case 'ArrowRight':
                    if (focusedIndex < totalItems - 1) {
                        if (focusedIndex % 2 === 0) nextIndex = focusedIndex + 1; // Minus -> Plus
                    }
                    break;
                case 'Enter':
                case 'OK':
                    handleAction(focusedIndex);
                    break;
                case 'Back':
                case 'Escape':
                    navigate('/rules');
                    break;
                default:
                    return;
            }

            if (nextIndex !== focusedIndex) {
                setFocusedIndex(nextIndex);
                e.preventDefault();

                // Scroll logic if needed
                const el = document.getElementById(`qty-row-${Math.floor(nextIndex / 2)}`);
                el?.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [focusedIndex, activeRules, totalItems, navigate, handleAction]);



    return (
        <div className="quantity-page">
            <h1 className="app-main-header">Tambola Board</h1>
            <div className="quantity-header">Select Quantity</div>

            <div className="quantity-content">
                <div className="quantity-list-header">
                    <div className="header-rule">RULE</div>
                    <div className="header-qty">QTY</div>
                </div>

                {activeRules.map((rule, idx) => {
                    const qty = ruleQuantities[rule.id] || 1;
                    const minusFocus = focusedIndex === idx * 2;
                    const plusFocus = focusedIndex === idx * 2 + 1;

                    return (
                        <div key={rule.id} className="quantity-row" id={`qty-row-${idx}`}>
                            <div className="rule-name">{rule.name}</div>
                            <div className="stepper-container">
                                <button
                                    className={clsx('stepper-btn', { focused: minusFocus })}
                                    onClick={() => updateRuleQuantity(rule.id, -1)}
                                    onMouseEnter={() => setFocusedIndex(idx * 2)}
                                >
                                    <Minus size={20} />
                                </button>

                                <div className="qty-value">{qty}</div>

                                <button
                                    className={clsx('stepper-btn', { focused: plusFocus })}
                                    onClick={() => updateRuleQuantity(rule.id, 1)}
                                    onMouseEnter={() => setFocusedIndex(idx * 2 + 1)}
                                >
                                    <Plus size={20} />
                                </button>
                            </div>
                        </div>
                    );
                })}
            </div>

            <div className="quantity-footer">
                <button
                    ref={saveBtnRef}
                    className={clsx('save-btn', { focused: focusedIndex === totalItems - 1 })}
                    onClick={handleSave}
                    onMouseEnter={() => setFocusedIndex(totalItems - 1)}
                >
                    Start Game
                </button>
                <div className="copyright">{APP_CONSTANTS.COPYRIGHT}</div>
            </div>
        </div>
    );
};

export default QuantitySelection;
