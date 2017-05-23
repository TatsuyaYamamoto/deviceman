package jp.co.fujixerox.deviceman.controller;

import jp.co.fujixerox.deviceman.ApplicationException;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

/**
 * {@link BaseRestController#checkBindingResult(BindingResult)}
 */
@Getter
public class BindingResultException extends ApplicationException {
    private BindingResult result;

    public BindingResultException(BindingResult result) {
        super(Arrays.toString(result.getSuppressedFields()));
        this.result = result;
    }
}
