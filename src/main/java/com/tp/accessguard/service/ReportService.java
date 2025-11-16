package com.tp.accessguard.service;

import com.tp.accessguard.model.AccessEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    List<AccessEvent> listEvents(Long personId, Long sectorId, LocalDateTime from, LocalDateTime to);
}
