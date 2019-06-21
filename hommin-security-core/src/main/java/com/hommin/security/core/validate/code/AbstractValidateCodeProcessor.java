package com.hommin.security.core.validate.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
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
        String type = getProcessorType().toString().toLowerCase();
        String name = type + ValidateCodeGenerator.class.getSimpleName();
        ValidateCodeGenerator generator = validateCodeGenerators.get(name);
        if (generator == null) {
            throw new RuntimeException("找不到名为[" + name + "]的验证码生成器, 请检查你的验证码生成器名字是否正确");
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
        new HttpSessionSessionStrategy().setAttribute(request, SESSION_KEY_PREFIX + getProcessorType().toString().toUpperCase(), code);
    }

    /**
     * 根据请求的url获取校验码的类型
     *
     * @return 验证码对象
     */
    private ValidateCodeType getProcessorType() {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), ValidateCodeProcessor.class.getSimpleName());
        return ValidateCodeType.valueOf(type.toUpperCase());
    }


    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    public void validate(ServletWebRequest request) {
        ValidateCodeType type = getProcessorType();

        String sessionKey = ValidateCodeProcessor.SESSION_KEY_PREFIX + type.toString().toUpperCase();
        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(request, sessionKey);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), type.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("找不到验证码的值");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);
    }
}
