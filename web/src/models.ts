export interface LeaderboardEntry {
    id: string,
    teamname: string,
    score: {
        challengePoints: number,
        picturesApproved: number,
        points: number
    }
}

export interface SubmissionEntry {
    id: string,
    teamName: string,
    teamId: string
}

export interface ChallengeSubmissionEntry {
    id: string,
    teamName: string,
    teamId: string,
    title: string
}

export interface ChallengeSubmission {
    title: string;
    challenge: string;
    defaultPoints: number;
    submittedBy: string;
    files: string[];
}

export interface Picture {
    id: string;
    state: null | "PENDING" | "DENIED" | "APPROVED"
}

export interface Challenge {
    id: string;
    title: string;
    challenge: string;
    points: number;
    state: null | "PENDING" | "DENIED" | "APPROVED"
}

export interface TeamMeta {
    meta: {
        teamName: string,
        score: {
            challengePoints: number,
            picturesApproved: number,
            points: number,
        }
    };
    joinCode: string;
    challenges: Challenge[];
    pictures: Picture[];
}