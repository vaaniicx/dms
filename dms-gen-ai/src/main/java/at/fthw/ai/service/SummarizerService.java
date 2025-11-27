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
                        Summarize the following text.
                        
                        HARD RULES:
                        • Output must be EXACTLY ONE paragraph.
                        • NO titles, headings, bullet points, numbers, or labels.
                        • NO section names like "General Goals:" or "Structure:".
                        • NO line breaks. NO formatting. NO explanations.
                        • Output ONLY the paragraph of summary text. Nothing before or after it.
                        """)
                .user(summaryInput)
                .call()
                .content();
    }
}
