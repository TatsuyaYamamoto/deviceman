package jp.co.fujixerox.deviceman.controller.resource;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResource {
    private String id;
    private String address;
    private Date created;
    private Date updated;
}
