package pers.xzj.fastdfs.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xzj
 * @version 1.0
 * @description
 * @date 2020/11/18 16:55
 */

@RestController
@CrossOrigin
public class TestController {


    @PostMapping("/test")
    public Map<String, String> test(@RequestBody HashMap<String, String> map2) {

        Map<String, String> map = new HashMap<>();
        map.put("name", map2.get("name") + 123);
        map.put("pass", map2.get("pass") + 456);
        return map;
    }

}
