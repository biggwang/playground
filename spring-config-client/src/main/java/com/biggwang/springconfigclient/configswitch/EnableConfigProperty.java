package com.biggwang.springconfigclient.configswitch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties("transfer.asset")
@RefreshScope
@ToString
public class EnableConfigProperty {

    private boolean enable = false;
//    private AssetProperty asset;

}

@NoArgsConstructor
@Getter
@Setter
class AssetProperty {
    private boolean enable = false;
}