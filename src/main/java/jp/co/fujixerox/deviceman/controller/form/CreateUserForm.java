package jp.co.fujixerox.deviceman.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@AllArgsConstructor
@ToString
public class CreateUserForm {
    @NotBlank
    private String id;
    @NotBlank
    private String address;
}
