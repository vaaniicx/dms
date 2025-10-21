import api from "../api";
import type { DocumentResponse } from "../types/DocumentResponse";

export async function getDocuments(): Promise<DocumentResponse[]> {
    const { data } = await api.get<DocumentResponse[]>("/v1/documents");
    return data;
}

export async function getDocument(id: number): Promise<DocumentResponse> {
    const { data } = await api.get<DocumentResponse>(`/v1/documents/${id}`);
    return data;
}

export async function deleteDocument(id: number): Promise<void> {
    await api.delete(`/v1/documents/${id}`);
}

export async function uploadDocument(file: File): Promise<number> {
    const formData = new FormData();
    formData.append("file", file);

    const response = await api.post<void>("/v1/documents", formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });

    const locationHeader =
        response.headers?.location ?? response.headers?.Location;

    if (!locationHeader) {
        throw new Error(
            "Upload succeeded but no document location was returned",
        );
    }

    const match = locationHeader.match(/\/(\d+)(?:\/)?$/);

    if (!match) {
        throw new Error(`Failed to parse documentId from ${locationHeader}`);
    }

    return Number(match[1]);
}
