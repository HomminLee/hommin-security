package com.hommin.security.core.validate.code;

import com.hommin.security.core.properties.SecurityConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Hommin
 */
@RestController
public class ValidateCodeController {

    @Autowired
    private ValidateCodeProcessorHolder processorHolder;

    /**
     * 创建验证码，根据验证码类型不同
     *
     * @param request  request
     * @param response response
     * @param type     验证码类型
     * @throws Exception
     */
    @GetMapping(SecurityConst.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        ValidateCodeProcessor processor = processorHolder.findValidateCodeProcessor(type);
        processor.create(new ServletWebRequest(request, response));
    }


}
