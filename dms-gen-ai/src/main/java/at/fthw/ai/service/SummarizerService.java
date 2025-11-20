package at.fthw.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SummarizerService {

    private final ChatClient chatClient;

    public SummarizerService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String summarize(String extractedText) {
        return chatClient.prompt()
                .system("Summarize the following text. Only print the summarized paragraph.")
                .user(extractedText)
                .call()
                .content();
    }
}
