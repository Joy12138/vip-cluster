package com.lz.vip.controller

import com.lz.vip.service.UserService
import com.lz.vip.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.RequestParam
import com.lz.vip.utils.JSONResult
import com.lz.vip.utils.okay
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Suppress("UseExpressionBody")
@Controller
class UserController {

    @Autowired private lateinit var userService: UserService

    private val logger by logger()

    /**
     * 初始化用户数据
     */
    @RequestMapping("/init", produces = [(MimeTypeUtils.APPLICATION_JSON_VALUE)])
    fun saveFbFriends(
            @RequestParam("uid")
            uid: String
    ): JSONResult {
        logger.info("初始化用户 {} 数据", uid)
        userService.initUserStaticInfo(uid)
        logger.info("初始化用户数据结束")
        return okay {}
    }
}