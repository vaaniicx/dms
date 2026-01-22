export type DocumentStatus =
    | "SELECTED"
    | "UPLOADED"
    | "SCANNED"
    | "SUMMARIZED"
    | "INDEXED"
    | "FAILED";

export interface StatusEntry {
    status: DocumentStatus;
    changeDate: string;
}

export interface FileInfo {
    objectKey: string;
    name: string;
    type: string;
    size: number;
    pageCount: number;
    creationDate: string;
    modificationDate: string;
}

export interface AccessHistoryEntry {
    accessDate: string;
    user?: string;
}

export interface DocumentResponse {
    id: number;
    title: string;
    author: string;
    summary: string | null;
    file: FileInfo;
    status: StatusEntry[];
    accessHistory: AccessHistoryEntry[];
}
