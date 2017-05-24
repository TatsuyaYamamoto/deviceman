
package jp.co.fujixerox.deviceman.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class ApplyLendingForm {
    @NotBlank
    private String userId;
    @NotNull
    private Date dueReturnDate;
}
