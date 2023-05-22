package com.biggwang.springconfigclient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigController {

    private final AssetTransferSwitch assetTransferSwitch;
    private final FriendTransferSwitch friendTransferSwitch;

    @GetMapping("/veronica")
    public ResponseEntity<String> config() {
        System.out.println(assetTransferSwitch);
        System.out.println(assetTransferSwitch);
        return ResponseEntity.ok(assetTransferSwitch.toString() + friendTransferSwitch.toString());
    }

}