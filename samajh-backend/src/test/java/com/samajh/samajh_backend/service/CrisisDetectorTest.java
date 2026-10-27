package com.samajh.samajh_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CrisisDetectorTest {

    private final CrisisDetector detector = new CrisisDetector();

    @Test
    void detectsClearSelfHarmLanguage() {
        assertThat(detector.isCrisis("I want to die")).isTrue();
        assertThat(detector.isCrisis("I DON’T WANT TO LIVE anymore")).isTrue();
        assertThat(detector.isCrisis("thinking about self-harm")).isTrue();
    }

    @Test
    void ignoresOrdinaryMessages() {
        assertThat(detector.isCrisis("I have been feeling overwhelmed")).isFalse();
        assertThat(detector.isCrisis("How do I sleep better?")).isFalse();
    }
}