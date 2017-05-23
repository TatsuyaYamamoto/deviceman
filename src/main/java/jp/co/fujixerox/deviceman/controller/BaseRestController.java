package jp.co.fujixerox.deviceman.controller;

import jp.co.fujixerox.deviceman.controller.BindingResultException;
import org.springframework.validation.BindingResult;

public abstract class BaseRestController {

    /**
     * {@link BindingResult}の内容を確認し、Invalidの場合、{@lnik BindingResultException}をスローする
     *
     * @param bindingResult 検証対象の{@link BindingResult}
     * @throws BindingResultException {@link BindingResult}がエラーを持っている
     */
    void checkBindingResult(BindingResult bindingResult) throws BindingResultException {
        if (bindingResult.hasErrors()) {
            throw new BindingResultException(bindingResult);
        }
    }
}
