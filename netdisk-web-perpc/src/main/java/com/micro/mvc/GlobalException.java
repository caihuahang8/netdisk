package com.micro.mvc;

import com.micro.common.Result;
import com.micro.common.ResultUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 全局异常
 * @author zwy
 * 2018年10月16日
 */
@ControllerAdvice
public class GlobalException {
	@ExceptionHandler(Exception.class) //异常类型
	@ResponseBody
	public Result defaultExceptionHandler(HttpServletRequest req, Exception e){

		return ResultUtils.error(e.getMessage());
	}
}
