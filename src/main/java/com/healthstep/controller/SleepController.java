package com.healthstep.mobile.controller;

import org.springframework.web.bind.annotation.*;

import com.healthstep.mobile.model.SleepLog;
import com.healthstep.mobile.repository.SleepLogRepository;
import com.healthstep.mobile.service.LeaderboardService;
import com.healthstep.mobile.sync.SyncHub;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sleep")
public class SleepController {

    private final SleepLogRepository repo;
    private final LeaderboardService leaderboard;
    private final SyncHub hub;

    public SleepController(SleepLogRepository repo, LeaderboardService leaderboard, SyncHub hub) {
        this.repo = repo;
        this.leaderboard = leaderboard;
        this.hub = hub;
    }

    // POST /sleep/add
    @PostMapping("/add")
    public SleepLog add(@RequestBody SleepLog log){
        // stamp today's date
        log.setDate(LocalDate.now());

        // persist
        SleepLog saved = repo.save(log);

        // Scoring: +2 per full hour slept, capped at 16 (null-safe)
        int mins = Math.max(0, saved.getDuration());             // duration is required now
        int hrs  = mins / 60;
        int pts  = Math.min(16, hrs) * 2;
        leaderboard.addPoints(saved.getUserId(), pts, LeaderboardService.Kind.SLEEP, mins);

        hub.publish(saved.getUserId(), "sleep"); // notify clients via SSE

        return saved;
    }

    // GET /sleep/{userId} — today's sleep logs
    @GetMapping("/{userId}")
    public List<SleepLog> getTodaySleep(@PathVariable Long userId) {
        return repo.findByUserIdAndDate(userId, LocalDate.now());
    }
}