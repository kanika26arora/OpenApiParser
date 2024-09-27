package com.chat.openapiParser.parser.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T : Any> slf4j(): Logger = LoggerFactory.getLogger(T::class.java)
