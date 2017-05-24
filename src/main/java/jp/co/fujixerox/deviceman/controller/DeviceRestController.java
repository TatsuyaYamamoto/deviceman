package jp.co.fujixerox.deviceman.controller;

import jp.co.fujixerox.deviceman.controller.form.CreateDeviceForm;
import jp.co.fujixerox.deviceman.controller.resource.DeviceResource;
import jp.co.fujixerox.deviceman.persistence.entity.DeviceEntity;
import jp.co.fujixerox.deviceman.service.DeviceService;
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
@RequestMapping("/api/devices")
@Log4j2
public class DeviceRestController extends BaseRestController {
    private HttpServletRequest request;
    private DeviceService deviceService;
    private ModelMapper modelMapper;

    @Autowired
    public DeviceRestController(
            HttpServletRequest request,
            DeviceService deviceService,
            ModelMapper modelMapper) {
        this.request = request;
        this.deviceService = deviceService;
        this.modelMapper = modelMapper;
    }

    /**
     * 登録済み端末リストをすべて取得する
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

        List<DeviceEntity> deviceEntityList = deviceService.search(query);

        // entity -> dto mapping.
        List<DeviceResource> devices = deviceEntityList.stream()
                .map((deviceEntity) -> modelMapper.map(deviceEntity, DeviceResource.class))
                .collect(Collectors.toList());

        Map<String, List> response = Collections.emptyMap();
        response.put("devices", devices);

        log.info("END - {}", request.getRequestURI());
        return ResponseEntity.ok().body(response);
    }

    /**
     * 端末を新規登録する
     *
     * @param deviceForm
     * @param bindingResult
     * @param uriBuilder
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registerUser(
            @RequestBody @Valid CreateDeviceForm deviceForm,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder) {

        log.info("START - {}:{}", request.getMethod(), request.getRequestURI());
        log.info("INPUT form={}", deviceForm.toString());

        checkBindingResult(bindingResult);

        DeviceEntity created = deviceService.create(
                deviceForm.getName(),
                deviceForm.getManufacturer(),
                deviceForm.getOsName(),
                deviceForm.getOsVersion(),
                deviceForm.getImei(),
                deviceForm.getWifiMacAddress(),
                deviceForm.getPhoneNumber());

        log.info("SUCCESS creation device resource.");

        /* response */
        URI location = uriBuilder
                .path(request.getRequestURI())
                .path(String.valueOf(created.getId()))
                .build()
                .toUri();

        log.info("END - {} localtion: {}", request.getRequestURI(), location.toString());
        return ResponseEntity.created(location).build();
    }
}
