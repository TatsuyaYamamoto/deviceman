
package jp.co.fujixerox.deviceman.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
public class ApplyLendingForm {
    @NotBlank
    private String userId;
    @NotNull
    private Date dueReturnDate;
}
