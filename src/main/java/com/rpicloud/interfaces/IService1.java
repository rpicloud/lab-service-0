package com.rpicloud.interfaces;

import com.rpicloud.models.Resource1;
import feign.RequestLine;

import java.util.List;

/**
 * Created by mixmox on 01/03/16.
 */
public interface IService1 {
    @RequestLine("GET /resources")
    List<Resource1> resources();
}
