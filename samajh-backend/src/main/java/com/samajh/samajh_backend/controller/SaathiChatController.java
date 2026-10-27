package com.samajh.samajh_backend.controller;

import com.samajh.samajh_backend.dto.ChatRequest;
import com.samajh.samajh_backend.dto.ChatResponse;
import com.samajh.samajh_backend.service.CrisisDetector;
import com.samajh.samajh_backend.service.SaathiChatService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class SaathiChatController {

    private final SaathiChatService saathiChatService;
    private final CrisisDetector crisisDetector;

    public SaathiChatController(SaathiChatService saathiChatService,
                                CrisisDetector crisisDetector) {
        this.saathiChatService = saathiChatService;
        this.crisisDetector = crisisDetector;
    }

    // "/chat" matches the Flask route; "/saathi/chat" keeps the earlier Java route working.
    @PostMapping({"/chat", "/saathi/chat"})
    public ChatResponse chat(@RequestBody ChatRequest request) {

        if (request == null
                || request.message() == null
                || request.message().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Message cannot be empty.");
        }

        String message = request.message().strip();

        if (message.length() > 4000) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Message must not exceed 4000 characters.");
        }

        if (crisisDetector.isCrisis(message)) {
            return new ChatResponse(CrisisDetector.CRISIS_REPLY, CrisisDetector.CRISIS_INFO);
        }

        return new ChatResponse(
                saathiChatService.getReply(message, request.history()), null);
    }
}