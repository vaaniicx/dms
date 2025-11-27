export type DocumentStatus =
    | "SELECTED"
    | "UPLOADED"
    | "SCANNED"
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
    docStatus: DocumentStatus;
    docCreatedAt: string;
    summary?: string | null;
    insertedAt: string;
    updatedAt: string;
}
