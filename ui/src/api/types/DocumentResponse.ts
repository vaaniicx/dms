export type DocumentStatus =
    | "SELECTED"
    | "UPLOADED"
    | "SCANNED"
    | "SUMMARIZED"
    | "INDEXED"
    | "FAILED";

export interface DocumentResponse {
    id: number;
    fileName: string;
    fileSize: number;
    fileSizeUnit: string;
    fileType: string;
    fileExtension: string;
    docPageCount: number;
    docTitle: string;
    docAuthor: string;
    uploaded: boolean;
    scanned: boolean;
    summarized: boolean;
    indexed: boolean;
    docCreatedAt: string;
    summary?: string | null;
    insertedAt: string;
    updatedAt: string;
}
