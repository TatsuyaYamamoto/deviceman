package jp.co.fujixerox.deviceman.controller;

import jp.co.fujixerox.deviceman.controller.form.ApplyLendingForm;
import jp.co.fujixerox.deviceman.controller.form.CreateDeviceForm;
import jp.co.fujixerox.deviceman.controller.resource.DeviceResource;
import jp.co.fujixerox.deviceman.persistence.entity.DeviceEntity;
import jp.co.fujixerox.deviceman.service.DeviceService;
import jp.co.fujixerox.deviceman.service.LendingService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@Log4j2
public class DeviceRestController extends BaseRestController {
    private HttpServletRequest request;
    private DeviceService deviceService;
    private ModelMapper modelMapper;
    private LendingService lendingService;
    private Type deviceResourceListType;

    @Autowired
    public DeviceRestController(
            HttpServletRequest request,
            DeviceService deviceService,
            ModelMapper modelMapper,
            LendingService lendingService) {

        this.request = request;
        this.deviceService = deviceService;
        this.modelMapper = modelMapper;
        this.lendingService = lendingService;

        deviceResourceListType = new TypeToken<List<DeviceResource>>() {
        }.getType();
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

        List<DeviceResource> devices = modelMapper.map(deviceService.search(query), deviceResourceListType);

        Map<String, List<DeviceResource>> response = new HashMap<>();
        response.put("devices", devices);

        log.info("END - {}", request.getRequestURI());
        return ResponseEntity.ok().body(response);
    }

    /**
     * @api {post} /api/devices/ Create a device.
     * @apiName CreateDevice
     * @apiGroup Device
     * @apiParam {Number} name              Device name.
     * @apiParam {String} manufacturer      Name of device manufacturer.
     * @apiParam {String} os_name OS        name.
     * @apiParam {String} osVersion         OS version under semantic versioning.
     * @apiParam {String} imei              IMEI of the device
     * @apiParam {String} wifi_mac_address  WiFi Mac Address.
     * @apiParam {String} phoneNumber       phone number
     * @apiSuccessExample {json} Response:
     * <pre>
     *      HTTP/1.1 201 Created
     * </pre>
     * @apiErrorExample {json} Response if provided device has been created.
     * <pre>
     *      HTTP/1.1 409 Conflict
     * </pre>
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

    /**
     * @api {put} /api/devices/:deviceId/lending Lend a device.
     * @apiName LendDevice
     * @apiGroup Device
     * @apiParam {Number} deviceId          Device unique ID.
     * @apiParam {String} user_id           User unique ID.
     * @apiParam {Date}   due_return_date   Date that provided user will return the device.
     * @apiSuccessExample {json} Response:
     * <pre>
     *      HTTP/1.1 204 No Content
     * </pre>
     * @apiErrorExample {json} Response if provided due return date is invalid.
     * <pre>
     *      HTTP/1.1 400 Bad Request
     * </pre>
     * @apiErrorExample {json} Response if provided device has been lent.
     * <pre>
     *      HTTP/1.1 409 Conflict
     * </pre>
     */
    @RequestMapping(
            value = "{deviceId}/lending",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity applyCheckout(
            @PathVariable("deviceId")
                    Integer deviceId,
            @RequestBody @Valid
                    ApplyLendingForm lendingForm,
            BindingResult bindingResult) {

        log.info("START - {}:{}", request.getMethod(), request.getRequestURI());
        log.info("INPUT form={}", lendingForm.toString());

        checkBindingResult(bindingResult);

        lendingService.applyLending(
                deviceId,
                lendingForm.getUserId(),
                lendingForm.getDueReturnDate());

        log.info("Success to checkout.");

        /* response */
        log.info("END - {}", request.getRequestURI());
        return ResponseEntity.noContent().build();
    }

    /**
     * @api {delete} /api/devices/:deviceId/lending Return a device.
     * @apiName ReturnDevice
     * @apiGroup Device
     * @apiParam {Number} deviceId  ID of target device.
     * @apiSuccessExample {json} Response:
     * <pre>
     *      HTTP/1.1 204 No Content
     * </pre>
     * @apiErrorExample {json} Response if provided device is NOT lent.
     * <pre>
     *      HTTP/1.1 404 Not Found
     * </pre>
     */
    @RequestMapping(
            value = "{deviceId}/lending",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity applyReturn(
            @PathVariable("deviceId")
                    Integer deviceId) {

        log.info("START - {}:{}", request.getMethod(), request.getRequestURI());

        lendingService.applyReturn(deviceId);
        log.info("Success to return device.");

        log.info("END - {}", request.getRequestURI());
        return ResponseEntity.noContent().build();
    }
}
