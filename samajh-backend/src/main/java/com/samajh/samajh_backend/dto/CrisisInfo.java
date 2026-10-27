package com.samajh.samajh_backend.dto;

import java.util.List;

public record CrisisInfo(String message, List<Helpline> helplines) {

}