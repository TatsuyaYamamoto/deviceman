package jp.co.fujixerox.deviceman.controller.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LendingResource {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("lending_start_date")
    private Date lendingStartDate;

    @JsonProperty("due_return_date")
    private Date dueReturnDate;

    @JsonProperty("actual_return_date")
    private Date actualReturnDate;

}
