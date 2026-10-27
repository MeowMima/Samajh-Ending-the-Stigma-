package com.samajh.samajh_backend.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.samajh.samajh_backend.dto.CrisisInfo;
import com.samajh.samajh_backend.dto.Helpline;

@Component
public class CrisisDetector {

    private static final List<String> PHRASES = List.of(
            "kill myself", "end my life", "want to die", "wanna die",
            "suicidal", "commit suicide", "take my own life", "hurt myself",
            "self harm", "self-harm", "don't want to live", "do not want to live",
            "better off dead", "no reason to live", "can't go on",
            "khudkushi", "marna chahta", "marna chahti"
    );

    public static final String CRISIS_REPLY =
            "I'm really glad you told me, and I'm so sorry you're in this much pain. "
            + "You deserve support right now, from a real person. "
            + "If you might act on these thoughts or are in immediate danger, please call 112 "
            + "or go to the nearest emergency room, and try to be with someone you trust. "
            + "I'm here to keep talking with you too. What's weighing on you most right now?";

    public static final CrisisInfo CRISIS_INFO = new CrisisInfo(
            "I hear you, and I'm glad you're talking. Please reach out to a crisis helpline right now.",
            List.of(
                    new Helpline("Tele-MANAS (India)", "14416"),
                    new Helpline("iCall (India)", "9152987821"),
                    new Helpline("Vandrevala Foundation", "1860-2662-345"),
                    new Helpline("AASRA", "9820466627")
            ));

    public boolean isCrisis(String message) {
        String m = message.toLowerCase(Locale.ROOT).replace('\u2019', '\'');
        return PHRASES.stream().anyMatch(m::contains);
    }
}