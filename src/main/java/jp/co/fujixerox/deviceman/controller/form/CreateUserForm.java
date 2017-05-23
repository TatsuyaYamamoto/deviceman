package jp.co.fujixerox.deviceman.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class CreateUserForm {
    @NotBlank
    private String id;
    @NotBlank
    private String address;
}
