package com.samajh.samajh_backend.dto;

import java.util.List;

public record ChatRequest(String message, List<ChatTurn> history) {

}