const DB_NAME = 'TambolaDB';
const DB_VERSION = 1;
const STORE_NAME = 'games';

export const GameStorage = {
    db: null,

    init() {
        return new Promise((resolve, reject) => {
            if (this.db) return resolve(this.db);

            const request = indexedDB.open(DB_NAME, DB_VERSION);

            request.onerror = (event) => {
                console.error("IndexedDB error:", event.target.error);
                reject(event.target.error);
            };

            request.onsuccess = (event) => {
                this.db = event.target.result;
                resolve(this.db);
            };

            request.onupgradeneeded = (event) => {
                const db = event.target.result;
                if (!db.objectStoreNames.contains(STORE_NAME)) {
                    db.createObjectStore(STORE_NAME, { keyPath: 'gameId' });
                }
            };
        });
    },

    async saveGame(gameData) {
        if (!this.db) await this.init();
        return new Promise((resolve, reject) => {
            const transaction = this.db.transaction([STORE_NAME], 'readwrite');
            const store = transaction.objectStore(STORE_NAME);
            const request = store.put({
                ...gameData,
                timestamp: Date.now()
            });

            request.onsuccess = () => resolve(true);
            request.onerror = (e) => reject(e.target.error);
        });
    },

    async getLatestGame() {
        if (!this.db) await this.init();
        return new Promise((resolve, reject) => {
            const transaction = this.db.transaction([STORE_NAME], 'readonly');
            const store = transaction.objectStore(STORE_NAME);

            // Fetch all and sort in memory
            const getAllRequest = store.getAll();

            getAllRequest.onsuccess = () => {
                const games = getAllRequest.result;
                if (games.length === 0) return resolve(null);

                // Sort by timestamp desc
                games.sort((a, b) => b.timestamp - a.timestamp);
                resolve(games[0]);
            };

            getAllRequest.onerror = (e) => reject(e.target.error);
        });
    }
};
