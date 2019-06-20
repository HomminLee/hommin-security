package com.hommin.security.core.validate.code;

import com.hommin.security.core.properties.SecurityConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Hommin
 */
@RestController
public class ValidateCodeController {

    @Autowired
    private Map<String, ValidateCodeProcessor> processors;

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
        ValidateCodeProcessor processor = processors.get(type + ValidateCodeGenerator.class.getSimpleName());
        if (processor == null) {
            throw new RuntimeException("找不到名为[" + type + "CodeProcessor]的验证码执行器");
        }
        processor.create(new ServletWebRequest(request, response));
    }


}
