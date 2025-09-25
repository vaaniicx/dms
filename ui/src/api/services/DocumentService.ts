import api from "../api";
import type { DocumentResponse } from "../types/DocumentResponse";

export async function getDocuments(): Promise<DocumentResponse[]> {
    const { data } = await api.get<DocumentResponse[]>('/v1/documents');
    return data;
}

export async function deleteDocument(id: number): Promise<void> {
    await api.delete(`/v1/documents/${id}`);
}

export async function uploadDocuments(files: File[]): Promise<void> {
    files.forEach(async (file) => await uploadDocument(file));
}

async function uploadDocument(file: File): Promise<DocumentResponse[]> {
    const formData = new FormData();
    formData.append("file", file);

    const { data } = await api.post<DocumentResponse[]>('/v1/documents', formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
    return data;
}