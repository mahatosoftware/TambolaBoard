import { initializeApp, getApps, getApp } from "firebase/app";
import { 
  getFirestore, 
  doc, 
  collection, 
  addDoc,
  getDoc, 
  getDocs, 
  setDoc, 
  updateDoc, 
  runTransaction, 
  serverTimestamp, 
  query, 
  where, 
  onSnapshot,
  writeBatch,
  increment,
  Timestamp
} from "firebase/firestore";
import { 
  getAuth, 
  signInAnonymously, 
  signInWithPopup, 
  GoogleAuthProvider, 
  onAuthStateChanged, 
  User 
} from "firebase/auth";
import { getAnalytics, isSupported } from "firebase/analytics";

const firebaseConfig = {
  apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY || "",
  authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN || "tambola-board-ticket-prod.firebaseapp.com",
  projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID || "tambola-board-ticket-prod",
  storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET || "tambola-board-ticket-prod.firebasestorage.app",
  messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID || "243416223664",
  appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID || "1:243416223664:web:7ff6139de58b543fc91075",
  measurementId: process.env.NEXT_PUBLIC_FIREBASE_MEASUREMENT_ID || "G-M6WV4600TM"
};

// Initialize Firebase singleton
const app = getApps().length > 0 ? getApp() : initializeApp(firebaseConfig);
const db = getFirestore(app);
const auth = getAuth(app);

let analytics: any = null;
if (typeof window !== "undefined") {
  isSupported().then((supported) => {
    if (supported) {
      analytics = getAnalytics(app);
    }
  }).catch(() => {});
}

/**
 * Ensures user is authenticated (signs in anonymously if guest)
 */
export async function ensureAnonymousAuth(): Promise<User | null> {
  if (typeof window === "undefined") return null;
  if (auth.currentUser) return auth.currentUser;
  try {
    const cred = await signInAnonymously(auth);
    return cred.user;
  } catch (err) {
    console.warn("Anonymous auth failed/disabled:", err);
    return null;
  }
}

/**
 * Authenticates host using Google Auth & creates/updates users/{uid} doc
 */
export async function signInWithGoogleHost(): Promise<User | null> {
  if (typeof window === "undefined") return null;
  
  // If user is already signed in with Google (non-anonymous)
  if (auth.currentUser && !auth.currentUser.isAnonymous) {
    return auth.currentUser;
  }

  const provider = new GoogleAuthProvider();
  try {
    const result = await signInWithPopup(auth, provider);
    const user = result.user;

    if (user) {
      const userRef = doc(db, "users", user.uid);
      const userSnap = await getDoc(userRef);

      if (!userSnap.exists()) {
        const counterRef = doc(db, "config", "counters");
        let nextPlayerId = 1390;
        try {
          await runTransaction(db, async (tx) => {
            const cSnap = await tx.get(counterRef);
            let lastId = 2828;
            if (cSnap.exists()) {
              lastId = Number(cSnap.data().lastPlayerId) || 2828;
            }
            nextPlayerId = lastId + 1;
            tx.set(counterRef, { lastPlayerId: nextPlayerId }, { merge: true });
          });
        } catch (e) {
          console.warn("Counter update warning:", e);
        }

        await setDoc(userRef, {
          uid: user.uid,
          displayName: user.displayName || "Host Player",
          email: user.email || "",
          photoURL: user.photoURL || "",
          playerId: String(nextPlayerId),
          lastLogin: serverTimestamp()
        }, { merge: true });
      } else {
        await setDoc(userRef, {
          lastLogin: serverTimestamp(),
          displayName: user.displayName || userSnap.data()?.displayName || "Host Player",
          photoURL: user.photoURL || userSnap.data()?.photoURL || ""
        }, { merge: true });
      }
    }

    return user;
  } catch (err: any) {
    console.error("Google Auth error:", err);
    throw err;
  }
}

export { 
  app, 
  db, 
  auth, 
  analytics,
  signInAnonymously,
  signInWithPopup,
  GoogleAuthProvider,
  onAuthStateChanged,
  doc, 
  collection, 
  addDoc,
  getDoc, 
  getDocs, 
  setDoc, 
  updateDoc, 
  runTransaction, 
  serverTimestamp, 
  query, 
  where, 
  onSnapshot, 
  writeBatch, 
  increment,
  Timestamp
};
