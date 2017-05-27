package jp.co.fujixerox.deviceman.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@AllArgsConstructor
@ToString
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
