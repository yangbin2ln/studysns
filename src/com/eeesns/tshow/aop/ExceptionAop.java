package com.eeesns.tshow.aop;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Component;

import com.eeesns.tshow.common.json.EditJson;

@Component
@Aspect
public class ExceptionAop {

	/**
	 * 方面组件：根据要执行的组件类型和方法记录操作信息
	 * 
	 * @author Administrator
	 */

	@Around("within(com.eeesns.tshow.controller..*)")
	public Object logger(ProceedingJoinPoint pjp) {
		// System.out.println("logger");
		EditJson editJson = new EditJson();
		Object retVal = null;
		try {
			retVal = pjp.proceed();
			return retVal;
		} catch (org.hibernate.StaleObjectStateException ee) {
			try {
				System.out.println("同步异常");
				retVal = pjp.proceed();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
			ee.printStackTrace();
			System.out.println("消息：-------" + ee.getMessage());
			editJson.setSuccess(false);
			editJson.getMess().setState("sysFailed");
			editJson.getMess().setMessage("系统繁忙");
			return editJson;
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("消息：-------" + e.getMessage());
			editJson.setSuccess(false);
			editJson.getMess().setState("sysFailed");
			editJson.getMess().setMessage("系统繁忙");
			return editJson;
		}
	}
}
