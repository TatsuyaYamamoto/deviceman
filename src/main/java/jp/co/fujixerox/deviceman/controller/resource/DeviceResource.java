package jp.co.fujixerox.deviceman.controller.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DeviceResource {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("manufacturer")
    private String manufacturer;

    @JsonProperty("name")
    private String name;

    @JsonProperty("imei")
    private String imei;

    @JsonProperty("wifi_mac_address")
    private String wifiMacAddress;

    @JsonProperty("created_at")
    private Date created;

    @JsonProperty("updated_at")
    private Date updated;
}
