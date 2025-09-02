package com.furkankayam.controller;

import com.furkankayam.repository.influx.DeviceInfluxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DeviceController {

    private final DeviceInfluxRepository deviceInfluxRepository;

    @GetMapping("/{deviceId}")
    public ResponseEntity<String> getEvent(
            @PathVariable String deviceId,
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.status(HttpStatus.OK).body(
                deviceInfluxRepository.getTemperatureData(deviceId, from, to)
        );
    }
}
