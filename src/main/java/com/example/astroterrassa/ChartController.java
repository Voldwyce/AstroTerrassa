package com.example.astroterrassa;

import com.example.astroterrassa.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChartController {

    @Autowired
    private ChartService chartService;

    @GetMapping("/chartData")
    public Map<String, Long> getChartData(@RequestParam String dataType, @RequestParam(required = false) String year, @RequestParam(required = false) String month) {
        return chartService.getChartData(dataType, year, month);
    }
}
