import { useEffect } from 'react';

/**
 * Hook to handle TV Remote attributes and key events.
 * Note: React's synthetic events are great, but for global navigation handling
 * sometimes a window listener is needed if focus is lost.
 * However, we will primarily rely on standard focus management.
 */
export const useTVNavigation = () => {
    useEffect(() => {
        const handleKeyDown = (e) => {
            // Map Tizen/TV keys if necessary. 
            // Browsers on TV usually map arrow keys to standard codes.
            // ArrowUp, ArrowDown, ArrowLeft, ArrowRight, Enter, Backspace/Escape (Back)

            // Prevent default scrolling for arrow keys to handle focus manually if needed
            if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(e.key)) {
                // e.preventDefault(); // CAUTION: This might block native focus moving if we don't handle it.
                // For now, let the browser handle focus moving between buttons natively.
                // We just ensure elements are focusable (tabIndex).
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, []);
};
