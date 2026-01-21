import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGame } from '../context/GameContext';
import { APP_CONSTANTS } from '../utils/Constants';
import { db, auth, signInAnonymouslyUser } from '../firebase';
import { doc, getDoc } from 'firebase/firestore';
import clsx from 'clsx';
import './EnterGameId.css';

const EnterGameId = () => {
    const { setGameId, setGameMode } = useGame();
    const navigate = useNavigate();

    const [inputId, setInputId] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [statusMsg, setStatusMsg] = useState('');
    const [isStatusError, setIsStatusError] = useState(false);

    const inputRef = useRef(null);
    const submitBtnRef = useRef(null);
    const [focusedElem, setFocusedElem] = useState('input'); // 'input' or 'submit'

    useEffect(() => {
        // Auto sign-in on mount
        const initAuth = async () => {
            if (!auth.currentUser) {
                try {
                    setStatusMsg("Initializing...");
                    setIsStatusError(false);
                    await signInAnonymouslyUser();
                    setStatusMsg("");
                } catch (e) {
                    console.error("Auth failed", e);
                    setStatusMsg("Authentication failed. Check connection.");
                    setIsStatusError(true);
                }
            }
        };
        initAuth();

        // Focus input
        if (inputRef.current) inputRef.current.focus();
    }, []);

    const handleSubmit = async () => {
        if (!inputId || inputId.length < 3) {
            setStatusMsg("Invalid Game ID");
            setIsStatusError(true);
            return;
        }

        setIsLoading(true);
        setStatusMsg("Verifying ID...");
        setIsStatusError(false);

        try {
            const formattedId = inputId.trim().toUpperCase();
            const docRef = doc(db, "games", formattedId);
            const docSnap = await getDoc(docRef);

            if (docSnap.exists()) {
                // Success
                setGameId(formattedId);
                setGameMode('MODERATED');
                navigate('/rules');
            } else {
                setStatusMsg("Game ID not found.");
                setIsStatusError(true);
            }
        } catch (e) {
            console.error("Verification failed", e);
            setStatusMsg("Verification error. Try again.");
            setIsStatusError(true);
        } finally {
            setIsLoading(false);
        }
    };

    // TV Navigation
    useEffect(() => {
        const handleKeyDown = (e) => {
            if (e.key === 'ArrowDown') {
                if (focusedElem === 'input') {
                    setFocusedElem('submit');
                    submitBtnRef.current?.focus();
                }
            } else if (e.key === 'ArrowUp') {
                if (focusedElem === 'submit') {
                    setFocusedElem('input');
                    inputRef.current?.focus();
                }
            } else if (e.key === 'Enter') {
                // Native form submission or click
            } else if (e.key === 'Back' || e.key === 'Escape') {
                navigate('/game-mode');
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [focusedElem, navigate]);


    return (
        <div className="enter-game-id-page">
            <h1 className="app-main-header">Tambola Board</h1>
            <h2 className="id-page-header">Enter Game ID</h2>

            <div className="id-input-container">
                <input
                    ref={inputRef}
                    type="text"
                    className="game-id-input"
                    value={inputId}
                    onChange={(e) => {
                        setInputId(e.target.value.toUpperCase());
                        setStatusMsg("");
                    }}
                    onFocus={() => setFocusedElem('input')}
                    placeholder="ID"
                    maxLength={6}
                    disabled={isLoading}
                />

                <div className={clsx("status-message", { info: !isStatusError })}>
                    {statusMsg}
                </div>

                <button
                    ref={submitBtnRef}
                    className={clsx("submit-btn", { focused: focusedElem === 'submit' })}
                    onClick={handleSubmit}
                    onFocus={() => setFocusedElem('submit')}
                    disabled={isLoading}
                >
                    {isLoading ? "Verifying..." : "Submit"}
                </button>
            </div>

            <div className="copyright-text" style={{ marginTop: 'auto' }}>
                {APP_CONSTANTS.COPYRIGHT}
            </div>
        </div>
    );
};

export default EnterGameId;
