package com.healthstepcontroller;

import org.springframework.web.bind.annotation.*;

import com.healthstep.mobile.repository.LeaderboardRepository.TodayRow;
import com.healthstep.mobile.service.LeaderboardService;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboard;

    public LeaderboardController(LeaderboardService leaderboard) {
        this.leaderboard = leaderboard;
    }

    @GetMapping("/today")
    public List<TodayRow> today() {
        return leaderboard.topToday(20); // top 20
    }
}