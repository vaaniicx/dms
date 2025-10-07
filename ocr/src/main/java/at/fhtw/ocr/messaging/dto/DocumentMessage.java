package at.fhtw.ocr.messaging.dto;

public record DocumentMessage(Long documentId, byte[] content) {}
