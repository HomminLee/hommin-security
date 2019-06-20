package com.hommin.security.core.validate.code;

import com.hommin.security.core.properties.SecurityConst;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * @author Hommin
 * 2019年06月19日 4:19 PM
 */
public abstract class AbstractValidateCodeProcessor<V extends ValidateCode> implements ValidateCodeProcessor {
    @Override
    public void create(ServletWebRequest request) throws Exception {
        V code = this.generate(request);
        this.saveValidateCode(request, code);
        this.sendValidateCode(request, code);
    }

    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;

    /**
     * 生成验证码
     *
     * @param request ServletWebRequest
     * @return 验证码
     */
    @SuppressWarnings("unchecked")
    private V generate(ServletWebRequest request) {
        String type = getProcessorType(request).toLowerCase();
        ValidateCodeGenerator generator = validateCodeGenerators.get(type + SecurityConst.VALIDATE_CODE_GENERATOR_SUFFIX);
        if (generator == null) {
            throw new RuntimeException("找不到名为[" + type + "CodeGenerator]的验证码生成器, 请检查你的验证码生成器名字是否正确");
        }
        return (V) generator.generate(request);
    }

    /**
     * 发送验证码
     *
     * @param request ServletWebRequest
     * @param code    验证码对象
     * @throws Exception e
     */
    protected abstract void sendValidateCode(ServletWebRequest request, V code) throws Exception;

    /**
     * 存储验证码
     *
     * @param request ServletWebRequest
     * @param code    验证码对象
     */
    private void saveValidateCode(ServletWebRequest request, V code) {
        new HttpSessionSessionStrategy().setAttribute(request, SESSION_KEY_PREFIX + getProcessorType(request).toUpperCase(), code);
    }

    /**
     * 根据请求的url获取校验码的类型
     *
     * @param request ServletWebRequest
     * @return 验证码对象
     */
    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringBefore(request.getRequest().getRequestURI(), SecurityConst.VALIDATE_CODE_PROCESSOR_SUFFIX).toLowerCase();
    }

}
