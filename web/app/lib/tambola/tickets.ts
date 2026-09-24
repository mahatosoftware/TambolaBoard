import { 
  db, 
  doc, 
  collection, 
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
  Timestamp,
  ensureAnonymousAuth
} from "../firebase";
import { generateTambolaGrid, TambolaGrid } from "./generator";

export type TicketType = "Digital" | "Paper";
export type TicketStatus = "AVAILABLE" | "ASSIGNED" | "ACTIVE" | "COMPLETED" | "CANCELLED";
export type GameStatus = "WAITING" | "STARTED" | "PAUSED" | "COMPLETED" | "CANCELLED";

export interface TicketDoc {
  id: string;
  ticketNumber: number;
  type: TicketType;
  isManual: boolean;
  isActive: boolean;
  status: TicketStatus;
  name: string | null;
  playerId: string | null;
  playerUid: string | null;
  grid: TambolaGrid;
  createdAt: any;
  assignedAt: any | null;
  claimedAt: any | null;
}

export interface GameDoc {
  gameId: string;
  digitalCount: number;
  manualCount: number;
  createdAt: any;
  expireAt?: any;
  status?: GameStatus;
  allowDigitalJoin?: boolean;
  allowPaperTickets?: boolean;
  allowLateJoin?: boolean;
  requireApproval?: boolean;
  hostUid?: string | null;
  startedAt?: any;
  endedAt?: any;
  calledNumbers?: number[];
  lastNumber?: number | null;
  prizes?: any[];
}

export interface TicketStats {
  digitalGenerated: number;
  digitalAssigned: number;
  digitalAvailable: number;
  paperGenerated: number;
  paperIssued: number;
  paperAvailable: number;
  totalGenerated: number;
  totalDistributed: number;
  totalAvailable: number;
}

/**
 * Ensures a game document exists in Firestore under games/{gameId}.
 * Safely initializes missing fields if legacy doc exists.
 */
export async function getOrCreateGame(gameId: string, hostUid?: string): Promise<GameDoc> {
  const normalizedId = gameId.trim().toUpperCase();
  if (!normalizedId) throw new Error("INVALID_GAME_ID");

  await ensureAnonymousAuth();

  const gameRef = doc(db, "games", normalizedId);
  const snap = await getDoc(gameRef);

  if (!snap.exists()) {
    const expireDate = new Date();
    expireDate.setDate(expireDate.getDate() + 1); // 24h default expiry

    const newGame: GameDoc = {
      gameId: normalizedId,
      digitalCount: 0,
      manualCount: 0,
      createdAt: serverTimestamp(),
      expireAt: Timestamp.fromDate(expireDate),
      status: "WAITING",
      allowDigitalJoin: true,
      allowPaperTickets: true,
      allowLateJoin: true,
      hostUid: hostUid || null,
      calledNumbers: [],
      lastNumber: null
    };

    await setDoc(gameRef, newGame, { merge: true });
    return { ...newGame, createdAt: new Date() };
  } else {
    const data = snap.data() as GameDoc;
    // Interpret missing legacy fields safely
    return {
      gameId: data.gameId || normalizedId,
      digitalCount: data.digitalCount || 0,
      manualCount: data.manualCount || 0,
      createdAt: data.createdAt,
      expireAt: data.expireAt,
      status: data.status || "WAITING",
      allowDigitalJoin: data.allowDigitalJoin !== undefined ? data.allowDigitalJoin : true,
      allowPaperTickets: data.allowPaperTickets !== undefined ? data.allowPaperTickets : true,
      allowLateJoin: data.allowLateJoin !== undefined ? data.allowLateJoin : true,
      hostUid: data.hostUid || null,
      calledNumbers: data.calledNumbers || [],
      lastNumber: data.lastNumber ?? null
    };
  }
}

/**
 * Update game settings / status.
 */
export async function updateGameSettings(gameId: string, settings: Partial<GameDoc>): Promise<void> {
  const normalizedId = gameId.trim().toUpperCase();
  if (!normalizedId) return;
  await ensureAnonymousAuth();
  const gameRef = doc(db, "games", normalizedId);
  await setDoc(gameRef, settings, { merge: true });
}

/**
 * Helper to generate random 3 character alphanumeric string for Ticket IDs
 */
function random3Chars(): string {
  const chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
  let res = "";
  for (let i = 0; i < 3; i++) {
    res += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return res;
}

/**
 * Generate N digital tickets under games/{gameId}/tickets/
 */
export async function generateDigitalTickets(gameId: string, count: number): Promise<number> {
  const normalizedId = gameId.trim().toUpperCase();
  const game = await getOrCreateGame(normalizedId);
  const startNum = (game.digitalCount || 0) + 1;

  const batch = writeBatch(db);
  const ticketsRef = collection(db, "games", normalizedId, "tickets");

  for (let i = 0; i < count; i++) {
    const ticketNum = startNum + i;
    const ticketId = `${normalizedId}-${random3Chars()}`;
    const tRef = doc(ticketsRef, ticketId);
    
    const newTicket: TicketDoc = {
      id: ticketId,
      ticketNumber: ticketNum,
      type: "Digital",
      isManual: false,
      isActive: true,
      status: "AVAILABLE",
      name: null,
      playerId: null,
      playerUid: null,
      grid: generateTambolaGrid(),
      createdAt: serverTimestamp(),
      assignedAt: null,
      claimedAt: null
    };

    batch.set(tRef, newTicket);
  }

  // Update digitalCount on game doc
  const gameRef = doc(db, "games", normalizedId);
  batch.update(gameRef, {
    digitalCount: increment(count)
  });

  await batch.commit();
  return startNum + count - 1;
}

/**
 * Generate N paper tickets under games/{gameId}/tickets/
 */
export async function generatePaperTickets(gameId: string, count: number): Promise<number> {
  const normalizedId = gameId.trim().toUpperCase();
  const game = await getOrCreateGame(normalizedId);
  const startNum = (game.manualCount || 0) + 1;

  const batch = writeBatch(db);
  const ticketsRef = collection(db, "games", normalizedId, "tickets");

  for (let i = 0; i < count; i++) {
    const ticketNum = startNum + i;
    const paddedNum = String(ticketNum).padStart(3, "0");
    const ticketId = `${normalizedId}-P${paddedNum}`;
    const tRef = doc(ticketsRef, ticketId);

    const newTicket: TicketDoc = {
      id: ticketId,
      ticketNumber: ticketNum,
      type: "Paper",
      isManual: true,
      isActive: true,
      status: "AVAILABLE",
      name: null,
      playerId: null,
      playerUid: null,
      grid: generateTambolaGrid(),
      createdAt: serverTimestamp(),
      assignedAt: null,
      claimedAt: null
    };

    batch.set(tRef, newTicket);
  }

  // Update manualCount on game doc
  const gameRef = doc(db, "games", normalizedId);
  batch.update(gameRef, {
    manualCount: increment(count)
  });

  await batch.commit();
  return startNum + count - 1;
}

/**
 * Atomically assigns an AVAILABLE digital ticket to a joining player.
 * Prevents double ticket assignment & uses config/counters.lastPlayerId transaction for guests.
 */
export async function assignDigitalTicketAtomically(
  gameId: string,
  playerName: string,
  authUser?: { uid: string; displayName?: string; playerId?: string } | null,
  existingTicketId?: string | null
): Promise<{ ticket: TicketDoc; playerId: string }> {
  const normalizedId = gameId.trim().toUpperCase();

  // Ensure user is authenticated anonymously if guest
  const anonUser = !authUser ? await ensureAnonymousAuth() : null;
  const effectiveAuthUser = authUser || (anonUser ? { uid: anonUser.uid } : null);

  // 1. If existing ticket ID is passed or stored, verify if it belongs to this player/session
  if (existingTicketId) {
    const existingRef = doc(db, "games", normalizedId, "tickets", existingTicketId);
    const existingSnap = await getDoc(existingRef);
    if (existingSnap.exists()) {
      const data = existingSnap.data() as TicketDoc;
      if (data.status === "ASSIGNED" || data.status === "ACTIVE") {
        return { ticket: data, playerId: data.playerId || "" };
      }
    }
  }

  // 2. Search for any ticket already assigned to this auth user / name in this game
  if (authUser?.uid) {
    const userTicketsQuery = query(
      collection(db, "games", normalizedId, "tickets"),
      where("playerUid", "==", authUser.uid)
    );
    const userTicketsSnap = await getDocs(userTicketsQuery);
    if (!userTicketsSnap.empty) {
      const assigned = userTicketsSnap.docs[0].data() as TicketDoc;
      return { ticket: assigned, playerId: assigned.playerId || "" };
    }
  }

  // 3. Query all tickets in games/{gameId}/tickets
  const ticketsRef = collection(db, "games", normalizedId, "tickets");
  let allTicketsSnap = await getDocs(ticketsRef);

  let availableDocs = allTicketsSnap.docs.filter((d) => {
    const t = d.data() as TicketDoc;
    const isDigital = t.type === "Digital" || t.isManual === false || (!t.type && !t.isManual);
    const isAvailable = (t.status === "AVAILABLE" || !t.status) && !t.playerId && !t.name;
    return isDigital && isAvailable;
  });

  // If no available digital tickets exist, auto-generate 10 more tickets on the fly
  if (availableDocs.length === 0) {
    await generateDigitalTickets(normalizedId, 10);
    allTicketsSnap = await getDocs(ticketsRef);
    availableDocs = allTicketsSnap.docs.filter((d) => {
      const t = d.data() as TicketDoc;
      const isDigital = t.type === "Digital" || t.isManual === false || (!t.type && !t.isManual);
      const isAvailable = (t.status === "AVAILABLE" || !t.status) && !t.playerId && !t.name;
      return isDigital && isAvailable;
    });
  }

  if (availableDocs.length === 0) {
    throw new Error("NO_AVAILABLE_TICKETS");
  }

  // 4. Perform Firestore Transaction to claim ticket & update counter atomically
  for (let attempt = 0; attempt < availableDocs.length; attempt++) {
    const candidateRef = availableDocs[attempt].ref;
    const counterRef = doc(db, "config", "counters");

    try {
      const result = await runTransaction(db, async (transaction) => {
        // Read target ticket inside transaction
        const ticketSnap = await transaction.get(candidateRef);
        if (!ticketSnap.exists()) {
          throw new Error("TICKET_NOT_FOUND");
        }
        const ticketData = ticketSnap.data() as TicketDoc;
        if (ticketData.playerId || ticketData.name || (ticketData.status && ticketData.status !== "AVAILABLE")) {
          throw new Error("TICKET_ALREADY_TAKEN");
        }

        // Determine playerId
        let assignedPlayerId = "";
        if (authUser?.playerId) {
          assignedPlayerId = authUser.playerId;
        } else {
          // Guest player: read config/counters
          const counterSnap = await transaction.get(counterRef);
          let currentLastId = 2828; // Fallback starting ID
          if (counterSnap.exists()) {
            currentLastId = Number(counterSnap.data().lastPlayerId) || 2828;
          }
          const nextPlayerId = currentLastId + 1;
          assignedPlayerId = String(nextPlayerId);

          // Update counter inside transaction
          transaction.set(counterRef, { lastPlayerId: nextPlayerId }, { merge: true });
        }

        // Update ticket inside transaction
        const updatedFields = {
          name: playerName.trim(),
          playerId: assignedPlayerId,
          playerUid: authUser?.uid || null,
          status: "ASSIGNED" as TicketStatus,
          type: "Digital" as TicketType,
          isManual: false,
          assignedAt: serverTimestamp()
        };

        transaction.update(candidateRef, updatedFields);

        return {
          ticket: {
            ...ticketData,
            ...updatedFields,
            assignedAt: new Date()
          } as TicketDoc,
          playerId: assignedPlayerId
        };
      });

      return result;
    } catch (err: any) {
      if (err.message === "TICKET_ALREADY_TAKEN") {
        // Retry next candidate available ticket
        continue;
      }
      throw err;
    }
  }

  throw new Error("NO_AVAILABLE_TICKETS");
}

/**
 * Issue or reassign a Paper Ticket to a player.
 */
export async function issuePaperTicket(
  gameId: string,
  ticketId: string,
  playerName?: string | null,
  playerId?: string | null,
  playerUid?: string | null
): Promise<void> {
  const normalizedId = gameId.trim().toUpperCase();
  const ticketRef = doc(db, "games", normalizedId, "tickets", ticketId);

  await updateDoc(ticketRef, {
    status: "ASSIGNED" as TicketStatus,
    name: playerName ? playerName.trim() : null,
    playerId: playerId || null,
    playerUid: playerUid || null,
    assignedAt: serverTimestamp()
  });
}

/**
 * Release an assigned ticket back to AVAILABLE.
 */
export async function releaseTicket(gameId: string, ticketId: string): Promise<void> {
  const normalizedId = gameId.trim().toUpperCase();
  const ticketRef = doc(db, "games", normalizedId, "tickets", ticketId);

  await updateDoc(ticketRef, {
    status: "AVAILABLE" as TicketStatus,
    name: null,
    playerId: null,
    playerUid: null,
    assignedAt: null
  });
}

/**
 * Listen to real-time changes in game tickets and compute stats.
 */
export function listenToTickets(
  gameId: string,
  onTicketsChanged: (tickets: TicketDoc[], stats: TicketStats) => void
) {
  const normalizedId = gameId.trim().toUpperCase();
  const ticketsRef = collection(db, "games", normalizedId, "tickets");

  return onSnapshot(ticketsRef, (snapshot) => {
    const tickets: TicketDoc[] = [];
    let digitalGen = 0, digitalAssigned = 0, digitalAvail = 0;
    let paperGen = 0, paperIssued = 0, paperAvail = 0;

    snapshot.forEach((docSnap) => {
      const data = docSnap.data() as TicketDoc;
      // Infer status safely for legacy docs
      const status: TicketStatus = data.status || (data.playerId ? "ASSIGNED" : "AVAILABLE");
      const type: TicketType = data.type || (data.isManual ? "Paper" : "Digital");

      const ticket: TicketDoc = {
        ...data,
        id: docSnap.id,
        status,
        type,
        isManual: data.isManual !== undefined ? data.isManual : type === "Paper"
      };

      tickets.push(ticket);

      if (type === "Digital") {
        digitalGen++;
        if (status === "ASSIGNED" || status === "ACTIVE" || status === "COMPLETED") {
          digitalAssigned++;
        } else if (status === "AVAILABLE") {
          digitalAvail++;
        }
      } else {
        paperGen++;
        if (status === "ASSIGNED" || status === "ACTIVE" || status === "COMPLETED") {
          paperIssued++;
        } else if (status === "AVAILABLE") {
          paperAvail++;
        }
      }
    });

    // Sort tickets by ticketNumber
    tickets.sort((a, b) => a.ticketNumber - b.ticketNumber);

    const stats: TicketStats = {
      digitalGenerated: digitalGen,
      digitalAssigned: digitalAssigned,
      digitalAvailable: digitalAvail,
      paperGenerated: paperGen,
      paperIssued: paperIssued,
      paperAvailable: paperAvail,
      totalGenerated: digitalGen + paperGen,
      totalDistributed: digitalAssigned + paperIssued,
      totalAvailable: digitalAvail + paperAvail
    };

    onTicketsChanged(tickets, stats);
  }, (error) => {
    console.warn("Tickets listener warning:", error.message);
    onTicketsChanged([], {
      digitalGenerated: 0, digitalAssigned: 0, digitalAvailable: 0,
      paperGenerated: 0, paperIssued: 0, paperAvailable: 0,
      totalGenerated: 0, totalDistributed: 0, totalAvailable: 0
    });
  });
}

/**
 * Listen to real-time changes in game document.
 */
export function listenToGame(
  gameId: string, 
  onGameChanged: (game: GameDoc | null, error?: Error) => void
) {
  const normalizedId = gameId.trim().toUpperCase();
  const gameRef = doc(db, "games", normalizedId);

  return onSnapshot(gameRef, (snapshot) => {
    if (!snapshot.exists()) {
      onGameChanged(null);
      return;
    }
    const data = snapshot.data() as GameDoc;
    onGameChanged({
      ...data,
      gameId: normalizedId,
      status: data.status || "WAITING",
      allowDigitalJoin: data.allowDigitalJoin !== undefined ? data.allowDigitalJoin : true,
      allowPaperTickets: data.allowPaperTickets !== undefined ? data.allowPaperTickets : true,
      allowLateJoin: data.allowLateJoin !== undefined ? data.allowLateJoin : true,
      requireApproval: data.requireApproval !== undefined ? data.requireApproval : true
    });
  }, (error) => {
    console.warn("Game listener warning:", error.message);
    onGameChanged(null, error);
  });
}

export interface JoinRequestDoc {
  uid: string;
  name: string;
  email?: string | null;
  photoURL?: string | null;
  playerId?: string | null;
  status: "PENDING" | "APPROVED" | "REJECTED";
  ticketId?: string | null;
  requestedAt: any;
  approvedAt?: any;
}

/**
 * Submit a join request for a player.
 */
export async function submitJoinRequest(
  gameId: string,
  playerName: string,
  authUser: { uid: string; displayName?: string; email?: string | null; photoURL?: string | null; playerId?: string }
): Promise<void> {
  const normalizedId = gameId.trim().toUpperCase();
  const reqRef = doc(db, "games", normalizedId, "joinRequests", authUser.uid);
  
  await setDoc(reqRef, {
    uid: authUser.uid,
    name: playerName.trim() || authUser.displayName || "Player",
    email: authUser.email || null,
    photoURL: authUser.photoURL || null,
    playerId: authUser.playerId || null,
    status: "PENDING",
    ticketId: null,
    requestedAt: serverTimestamp()
  }, { merge: true });
}

/**
 * Real-time listener for join requests of a specific user.
 */
export function listenToMyJoinRequest(
  gameId: string,
  uid: string,
  onChanged: (req: JoinRequestDoc | null) => void
) {
  const normalizedId = gameId.trim().toUpperCase();
  const reqRef = doc(db, "games", normalizedId, "joinRequests", uid);

  return onSnapshot(reqRef, (snap) => {
    if (snap.exists()) {
      onChanged(snap.data() as JoinRequestDoc);
    } else {
      onChanged(null);
    }
  });
}

/**
 * Real-time listener for all join requests under games/{gameId}/joinRequests
 */
export function listenToJoinRequests(
  gameId: string,
  onChanged: (requests: JoinRequestDoc[]) => void
) {
  const normalizedId = gameId.trim().toUpperCase();
  const reqsRef = collection(db, "games", normalizedId, "joinRequests");

  return onSnapshot(reqsRef, (snap) => {
    const list: JoinRequestDoc[] = [];
    snap.forEach((d) => {
      list.push(d.data() as JoinRequestDoc);
    });
    // Sort pending requests first
    list.sort((a, b) => {
      if (a.status === "PENDING" && b.status !== "PENDING") return -1;
      if (a.status !== "PENDING" && b.status === "PENDING") return 1;
      return 0;
    });
    onChanged(list);
  });
}

/**
 * Approve a join request and assign a digital ticket to the player.
 */
export async function approveJoinRequest(
  gameId: string,
  requestUid: string,
  targetTicketId?: string | null
): Promise<TicketDoc> {
  const normalizedId = gameId.trim().toUpperCase();
  const reqRef = doc(db, "games", normalizedId, "joinRequests", requestUid);
  const reqSnap = await getDoc(reqRef);

  if (!reqSnap.exists()) {
    throw new Error("JOIN_REQUEST_NOT_FOUND");
  }

  const reqData = reqSnap.data() as JoinRequestDoc;

  // Assign digital ticket atomically to player
  const { ticket, playerId } = await assignDigitalTicketAtomically(
    normalizedId,
    reqData.name,
    { uid: reqData.uid, displayName: reqData.name, playerId: reqData.playerId || undefined },
    targetTicketId || null
  );

  // Update join request status to APPROVED
  await updateDoc(reqRef, {
    status: "APPROVED",
    ticketId: ticket.id,
    playerId: playerId,
    approvedAt: serverTimestamp()
  });

  return ticket;
}

/**
 * Reject a join request.
 */
export async function rejectJoinRequest(gameId: string, requestUid: string): Promise<void> {
  const normalizedId = gameId.trim().toUpperCase();
  const reqRef = doc(db, "games", normalizedId, "joinRequests", requestUid);
  await updateDoc(reqRef, {
    status: "REJECTED"
  });
}
