package com.healthstep.mobile.controller;

import org.springframework.web.bind.annotation.*;

import com.healthstep.mobile.model.WaterLog;
import com.healthstep.mobile.repository.WaterLogRepository;
import com.healthstep.mobile.service.LeaderboardService;
import com.healthstep.mobile.sync.SyncHub;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/water")
public class WaterController {

    private final WaterLogRepository repo;
    private final LeaderboardService leaderboard;
    private final SyncHub hub;

    public WaterController(WaterLogRepository repo, LeaderboardService leaderboard, SyncHub hub) {
        this.repo = repo;
        this.leaderboard = leaderboard;
        this.hub = hub;
    }

    // POST /water/add — create today's water log and award points
    @PostMapping("/add")
    public WaterLog add(@RequestBody WaterLog log) {
        log.setDate(LocalDate.now());
        WaterLog saved = repo.save(log);

        // Scoring: +1 point per 250 ml (null-safe)
        Integer amtObj = saved.getAmount();
        int ml  = (amtObj != null) ? Math.max(0, amtObj) : 0;  // safe int
        int pts = ml / 250;

        // only award if we actually have a user id
        Long uidObj = saved.getUserId();
        if (uidObj != null) {
            leaderboard.addPoints(uidObj.longValue(), pts, LeaderboardService.Kind.WATER, ml);
            hub.publish(uidObj, "water"); // notify connected clients via SSE
        }

        return saved;
    }

    // GET /water/{userId} — today's water logs
    @GetMapping("/{userId}")
    public List<WaterLog> getToday(@PathVariable Long userId) {
        return repo.findByUserIdAndDate(userId, LocalDate.now());
    }
}