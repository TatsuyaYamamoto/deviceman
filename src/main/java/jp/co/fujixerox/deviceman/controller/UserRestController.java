package jp.co.fujixerox.deviceman.controller;

import jp.co.fujixerox.deviceman.controller.form.CreateUserForm;
import jp.co.fujixerox.deviceman.controller.resource.UserResource;
import jp.co.fujixerox.deviceman.persistence.entity.UserEntity;
import jp.co.fujixerox.deviceman.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Log4j2
public class UserRestController extends BaseRestController {
    private HttpServletRequest request;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserRestController(
            HttpServletRequest request,
            UserService userService,
            ModelMapper modelMapper) {
        this.request = request;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * ユーザー検索
     *
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity searchUsers(
            @RequestParam(name = "query", required = false, defaultValue = "")
                    String query) {

        log.info("START - {}:{}", request.getMethod(), request.getRequestURI());
        log.info("INPUT query={}", query);

        List<UserEntity> userEntities = userService.search(query);

        Map<String, List> response = Collections.emptyMap();
        List<UserResource> users = userEntities.stream()
                .map((userEntity) -> modelMapper.map(userEntity, UserResource.class))
                .collect(Collectors.toList());
        response.put("users", users);

        log.info("END - {}", request.getRequestURI());
        return ResponseEntity.ok().body(response);
    }

    /**
     * 借り出しユーザーを作成する
     *
     * @param userForm
     * @param uriBuilder
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registerUser(
            @RequestBody @Valid CreateUserForm userForm,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder) {

        log.info("START - {}:{}", request.getMethod(), request.getRequestURI());
        log.info("INPUT form={}", userForm.toString());

        checkBindingResult(bindingResult);

        UserEntity created = userService.create(
                userForm.getId(),
                userForm.getAddress());

        log.info("SUCCESS creation user resource.");

        /* response */
        URI location = uriBuilder
                .path(request.getRequestURI())
                .path(created.getId())
                .build()
                .toUri();

        log.info("END - {} localtion: {}", request.getRequestURI(), location.toString());
        return ResponseEntity.created(location).build();
    }
}
