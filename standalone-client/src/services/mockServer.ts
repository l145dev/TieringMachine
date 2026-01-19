import betsData from "../data/mock_db/bets.json";
import eventsData from "../data/mock_db/events.json";
import logsData from "../data/mock_db/logs.json";
import tiersData from "../data/mock_db/tiers.json";
import usersData from "../data/mock_db/users.json";
import type {
  ApiBet,
  ApiEvent,
  ApiLeaderboardEntry,
  LogEntry,
  LoginRequest,
  LoginResponse,
  ReportRequest,
  ReportResponse,
  TierResponse,
} from "./api";

// In-memory state
let currentUsers = [...usersData];
let currentBets = [...betsData];
let currentLogs = [...logsData];
let currentEvents = [...eventsData]; // Events seem read-only in the UI based on api.ts but let's keep it consistent

const getTierName = (tierId: number) => {
  const tier = tiersData.find((t) => t.id === tierId);
  return tier ? tier.name : "unknown";
};

const getUser = (userId: number) => {
  return currentUsers.find((u) => u.id === userId);
};

export const mockFetchEvents = async (): Promise<ApiEvent[]> => {
  return currentEvents.map((event) => {
    const creator = getUser(event.creatorId);
    return {
      id: event.id,
      title: event.title,
      description: event.description,
      eventDate: event.eventDate,
      reward: event.reward,
      creator: {
        id: creator?.id || 0,
        username: creator?.username || "Unknown",
        tier: {
          id: creator?.tierId || 0,
          name: getTierName(creator?.tierId || 0),
        },
      },
    };
  });
};

export const mockFetchBets = async (): Promise<ApiBet[]> => {
  // api.ts expects data with "time" but json has "resolutionDate", need to map if distinct, or just use resolutionDate
  // Looking at SQL, bets have resolution_date. ApiBet has 'time'.
  // In SQL, logs have log_time. ApiBet in api.ts has time: string.
  // Let's assume resolutionDate maps to time for the Bet interface based on the SQL -> API translation likely happening on server.

  return currentBets.map((bet) => {
    const creator = getUser(bet.creatorId);
    const target = getUser(bet.targetId);

    return {
      id: bet.id,
      description: bet.betDetails,
      creator: { username: creator?.username || "Unknown" },
      target: target?.username || "Unknown",
      wagerPoints: bet.wagerPoints,
      payoutPoints: bet.payoutPoints,
      lossPoints: bet.lossPoints,
      actualOutcome: bet.actualOutcome,
      time: bet.resolutionDate,
    };
  });
};

export const mockFetchLeaderboard = async (): Promise<
  ApiLeaderboardEntry[]
> => {
  const sortedUsers = [...currentUsers].sort(
    (a, b) => b.totalPoints - a.totalPoints,
  );
  return sortedUsers.map((user, index) => ({
    id: user.id,
    rank: index + 1,
    name: user.username,
    points: user.totalPoints,
  }));
};

export const mockLoginUser = async (
  credentials: LoginRequest,
): Promise<LoginResponse> => {
  // In a real app we'd hash, but here we compare plain text as per the SQL/JSON
  const user = currentUsers.find(
    (u) =>
      u.username === credentials.citizenNumber &&
      u.password === credentials.password,
  ); // Using username as citizenNumber based on typical login flows or maybe it is just username.
  // Wait, api.ts says citizenNumber. The SQL has usernames like 'CryptoWhale69'.
  // Let's assume citizenNumber === username for this mock.

  if (!user) {
    throw new Error("Invalid credentials");
  }

  const sortedUsers = [...currentUsers].sort(
    (a, b) => b.totalPoints - a.totalPoints,
  );
  const rank = sortedUsers.findIndex((u) => u.id === user.id) + 1;

  return {
    id: user.id,
    rank: rank,
    username: user.username,
    total_points: user.totalPoints,
    tier: getTierName(user.tierId),
  };
};

export const mockReportCitizen = async (
  reporterId: number,
  data: ReportRequest,
): Promise<ReportResponse> => {
  // Logic:
  // - Reporter gets points?
  // - Target loses points?
  // Based on api.ts response: target_points_lost, report_points_earned.
  // Let's implement some dummy logic since we don't have the backend code.
  // 50 points earned, 100 lost.

  const reporter = getUser(reporterId);
  const target = getUser(data.targetId);

  if (!reporter || !target) throw new Error("User not found");

  const pointsEarned = 50;
  const pointsLost = 100;

  reporter.totalPoints += pointsEarned;
  target.totalPoints -= pointsLost;

  // Add log
  currentLogs.push({
    id: currentLogs.length + 1,
    userId: reporterId,
    details: `Reported ${target.username} for ${data.reason}`,
    logTime: new Date().toISOString(),
  });

  return {
    target: target.username,
    reporter: reporter.username,
    target_points_lost: pointsLost,
    report_points_earned: pointsEarned,
  };
};

export const mockFetchUserLogs = async (
  userId: number,
): Promise<LogEntry[]> => {
  return currentLogs
    .filter((log) => log.userId === userId)
    .map((log) => ({
      id: log.id,
      username: getUser(log.userId)?.username || "Unknown",
      details: log.details,
      logTime: log.logTime,
    }))
    .sort(
      (a, b) => new Date(b.logTime).getTime() - new Date(a.logTime).getTime(),
    );
};

export const mockFetchTierByUsername = async (
  username: string,
): Promise<TierResponse> => {
  const user = currentUsers.find((u) => u.username === username);
  if (!user) return { tier: null };

  const tierName = getTierName(user.tierId);
  // Cast to expected union type
  return { tier: tierName as "dreg" | "citizen" | "elite" | null };
};

export const mockUpdateScore = async (citizenId: number, score: number) => {
  const user = getUser(citizenId);
  if (user) {
    user.totalPoints = score;
  }
  return {};
};
