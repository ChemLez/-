package cn.lizhi.serviceMsm.service;

import org.springframework.stereotype.Service;

import java.util.Map;

public interface MsmService {

    boolean send(String phoneNumbers, Map<String, Object> param);
}
