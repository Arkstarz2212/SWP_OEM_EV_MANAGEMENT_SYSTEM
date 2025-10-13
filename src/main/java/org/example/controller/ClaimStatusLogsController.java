package org.example.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.example.models.core.ClaimStatusHistory;
import org.example.service.IService.IClaimStatusLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/claim-status-logs")
public class ClaimStatusLogsController {

    @Autowired
    private IClaimStatusLogService service;

    @PostMapping
    public ClaimStatusHistory create(@RequestBody ClaimStatusHistory log) {
        return service.log(log);
    }

    @GetMapping
    public List<ClaimStatusHistory> list(@RequestParam(required = false) Long claimId,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (claimId != null)
            return service.getByClaim(claimId);
        if (start != null && end != null)
            return service.getByChangedBetween(OffsetDateTime.parse(start), OffsetDateTime.parse(end));
        return List.of();
    }
}
