package jp.co.fujixerox.deviceman.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class CreateDeviceForm {
    @NotBlank
    private String name;
    @NotBlank
    private String manufacturer;
    @NotBlank
    private String osName;
    @NotBlank
    private String osVersion;
    @NotBlank
    private String imei;
    @NotBlank
    private String wifiMacAddress;

    private String phoneNumber;
}
