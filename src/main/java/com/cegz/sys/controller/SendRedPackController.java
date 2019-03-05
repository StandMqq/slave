package com.cegz.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.util.ResultData;

/**
 * 紅包后台控制类
 * 
 * @author tenglong
 * @date 2018年9月14日
 */
@RestController
@RequestMapping("/sendRedPack")
public class SendRedPackController {
	@Autowired
	private ServerAck serverAck;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

	@Value("${accountant.phone}")
	private String accountant;

	/**
	 * 
	 * @return
	 */
	@PostMapping("sendRedPack")
	public ResultData sendRedPack() {

		return serverAck.getSuccess();
	}

}
