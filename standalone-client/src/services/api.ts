import {
  mockFetchBets,
  mockFetchEvents,
  mockFetchLeaderboard,
  mockFetchTierByUsername,
  mockFetchUserLogs,
  mockLoginUser,
  mockReportCitizen,
  mockUpdateScore,
} from "./mockServer";

export interface ApiEvent {
  id: number;
  title: string;
  description: string;
  eventDate: string;
  reward: number;
  creator: {
    id: number;
    username: string;
    tier: {
      id: number;
      name: string;
    };
  };
}

export interface ApiBet {
  id: number;
  description: string;
  creator: string | { username: string };
  target: string;
  wagerPoints: number;
  payoutPoints: number;
  lossPoints: number;
  actualOutcome: boolean | null;
  time: string;
}

export interface ApiLeaderboardEntry {
  id: number;
  rank: number;
  name: string;
  points: number;
}

export interface LoginRequest {
  citizenNumber: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  rank: number;
  username: string;
  total_points: number;
  tier: string;
}

export interface ReportRequest {
  targetId: number;
  reason: string;
}

export interface ReportResponse {
  target: string;
  reporter: string;
  target_points_lost: number;
  report_points_earned: number;
}

export interface LogEntry {
  id: number;
  username: string;
  details: string;
  logTime: string;
}

export interface TierResponse {
  tier: "dreg" | "citizen" | "elite" | null;
}

export const fetchEvents = async (): Promise<ApiEvent[]> => {
  return mockFetchEvents();
};

export const fetchBets = async (): Promise<ApiBet[]> => {
  return mockFetchBets();
};

export const fetchLeaderboard = async (): Promise<ApiLeaderboardEntry[]> => {
  return mockFetchLeaderboard();
};

export const loginUser = async (
  credentials: LoginRequest,
): Promise<LoginResponse> => {
  return mockLoginUser(credentials);
};

export const reportCitizen = async (
  reporterId: number,
  data: ReportRequest,
): Promise<ReportResponse> => {
  return mockReportCitizen(reporterId, data);
};

export const fetchUserLogs = async (userId: number): Promise<LogEntry[]> => {
  return mockFetchUserLogs(userId);
};

export const fetchTierByUsername = async (
  username: string,
): Promise<TierResponse> => {
  return mockFetchTierByUsername(username);
};

export const updateScore = async (citizenId: number, score: number) => {
  return mockUpdateScore(citizenId, score);
};
