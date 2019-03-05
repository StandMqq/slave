package com.cegz.sys.controller.exception;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cegz.sys.util.Constant;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.ServerException;

/**
 * 控制层异常处理类，控制层所有抛出异常都由该类进行处理
 * 
 * @author lijiaxin
 * @date 2018/07/19
 */
@ControllerAdvice
public class ExceptionController {

	/**
	 * 异常处理方法
	 * 
	 * @author lijiaxin
	 * @date 2018/07/20
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResultData restExceptionHandle(Exception e, HttpServletResponse response) {
		if (e instanceof ServerException) {
			ServerException businessException = (ServerException) e;
			return businessException.getResultData();
		} else if (e instanceof UnauthorizedException) {
			return new ResultData().setSuccess(false).setCode(Constant.RETURN_CODE_AUTHORITY_INSUFFICIENT)
					.setMessage("权限不足，请联系管理员");
		}
		e.printStackTrace();
		return new ResultData().setSuccess(false).setCode(Constant.RETURN_CODE_DATABASE_EXCEPTION)
				.setMessage("服务器繁忙，请稍后重试");
	}
}
