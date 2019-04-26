package com.trackster.tracksterapp.utils

data class Config(var BASE_URL: String,
                  var AWS_MAIN_URL: String,
                  var AWS_ACCESS_KEY: String,
                  var AWS_ACCESS_SECRET: String,
                  var AWS_BUCKET_NAME: String,
                  var MESSAGE_MAX_LENGTH: Int,
                  var VIDEO_MAX_LENGTH: Int,
                  var AWS_CDN_URL: String)