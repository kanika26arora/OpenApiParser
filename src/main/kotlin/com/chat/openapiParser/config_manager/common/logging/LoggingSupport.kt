package com.chat.openapiParser.config_manager.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T : Any> slf4j(): Logger = LoggerFactory.getLogger(T::class.java)
