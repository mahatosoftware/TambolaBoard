/**
 * Indian Female English Text-to-Speech
 * -----------------------------------
 * ✔ Strong preference: en-IN + Female
 * ✔ Avoids male voices unless unavoidable
 * ✔ Chrome / Android WebView safe
 * ✔ Clean fallback strategy
 */

let voicesLoaded = false;

/**
 * Load voices safely (Chrome & WebView fix)
 */
const loadVoices = () => {
    return new Promise((resolve) => {
        const voices = window.speechSynthesis.getVoices();
        if (voices.length) {
            voicesLoaded = true;
            resolve(voices);
            return;
        }

        window.speechSynthesis.onvoiceschanged = () => {
            voicesLoaded = true;
            resolve(window.speechSynthesis.getVoices());
        };
    });
};

/**
 * Pick the best Indian English FEMALE voice
 */
const selectIndianFemaleVoice = (voices) => {
    // 1️⃣ Indian English – Female (highest priority)
    const indianFemale = voices.find(v =>
        v.lang === "en-IN" &&
        /female|woman|zira|priya|neural/i.test(v.name)
    );

    // 2️⃣ Any Indian English Female (even if mislabeled)
    const indianFemaleLoose = voices.find(v =>
        v.lang === "en-IN" &&
        !/rishi|male|man/i.test(v.name)
    );

    // 3️⃣ Google English (India) – often female but mislabeled
    const googleIndia = voices.find(v =>
        /google/i.test(v.name) && /india/i.test(v.name)
    );

    // 4️⃣ Any Female English voice
    const femaleEnglish = voices.find(v =>
        v.lang.startsWith("en") &&
        /female|woman|zira|samantha/i.test(v.name)
    );

    // 5️⃣ Absolute fallback (English only)
    const fallbackEnglish = voices.find(v =>
        v.lang.startsWith("en")
    );

    return (
        indianFemale ||
        indianFemaleLoose ||
        googleIndia ||
        femaleEnglish ||
        fallbackEnglish ||
        voices[0]
    );
};

/**
 * Speak text using Indian English Female voice
 */
export const speak = async (text, options = {}) => {
    if (!("speechSynthesis" in window)) return;

    // Stop any existing speech
    window.speechSynthesis.cancel();

    // Load voices safely
    const voices = voicesLoaded
        ? window.speechSynthesis.getVoices()
        : await loadVoices();

    const utterance = new SpeechSynthesisUtterance(text);

    // Voice selection
    utterance.voice = selectIndianFemaleVoice(voices);

    // Voice tuning (pleasant female narration)
    utterance.rate = options.rate ?? 0.9;
    utterance.pitch = options.pitch ?? 1.1;
    utterance.volume = options.volume ?? 1;

    // Optional lifecycle hooks
    utterance.onstart = options.onStart || null;
    utterance.onend = options.onEnd || null;
    utterance.onerror = options.onError || null;

    window.speechSynthesis.speak(utterance);
};

/**
 * Stop speaking immediately
 */
export const stopSpeak = () => {
    if ("speechSynthesis" in window) {
        window.speechSynthesis.cancel();
    }
};
