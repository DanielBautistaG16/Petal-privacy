package com.petal.handsfree.client

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient
