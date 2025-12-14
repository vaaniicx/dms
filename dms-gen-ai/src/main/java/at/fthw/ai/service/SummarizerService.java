package at.fthw.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SummarizerService {

    private final ChatClient chatClient;

    public SummarizerService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String summarize(String summaryInput) {
        return chatClient.prompt()
                .system("""
                    You are given the full text of a PDF document.

                    Write a concise, neutral summary of approximately 50 to 100 words.
                    Focus on the main topic, purpose, key arguments, and conclusions.
                    Preserve important technical or factual details where relevant.
                    Do not include opinions, commentary, or information not present in the text.

                    Constraints:
                    - Do NOT start the text with phrases like “Here is the summary”, “This document”, or similar meta introductions.
                    - Do NOT address the reader directly.
                    - Do NOT ask questions or suggest further discussion.
                    - Output only the summary text, nothing else.
                """)
                .user(summaryInput)
                .call()
                .content();
    }
}
