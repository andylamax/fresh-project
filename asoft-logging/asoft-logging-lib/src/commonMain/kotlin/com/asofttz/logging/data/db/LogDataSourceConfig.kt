package com.asofttz.logging.data.db

class LogDataSourceConfig {
    var url = "http://localhost:0000"
    var username = "admin"
    var password = "admin"

    val route_get_logs = "/logs"
    val route_post_log = "/log"

    fun fullUrl(route: String) = "$url$route"
}
