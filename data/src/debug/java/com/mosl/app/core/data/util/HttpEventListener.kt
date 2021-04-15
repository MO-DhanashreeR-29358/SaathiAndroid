package com.mosl.app.core.data.util

import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import okhttp3.*
import timber.log.Timber
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.atomic.AtomicLong

class HttpEventListener(
    val callId: Long,
    val callStartNanos: Long,
    val apiStatsRelay: PublishRelay<Long>
) : EventListener() {

    private fun printEvent(name: String) {
        val nowNanos = System.nanoTime()
        val elapsedNanos = nowNanos - callStartNanos
        Timber.tag("OkHttpEvent")
            .d("%04d %.3f %s%n", callId, elapsedNanos / 1000000000.0, name)
    }

    override fun callStart(call: Call) {
        printEvent("callStart")
    }

    override fun dnsStart(
        call: Call,
        domainName: String
    ) {
        printEvent("dnsStart")
    }

    override fun dnsEnd(
        call: Call,
        domainName: String,
        inetAddressList: List<InetAddress>
    ) {
        printEvent("dnsEnd")
    }

    override fun connectStart(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy
    ) {
        printEvent("connectStart")
    }

    override fun secureConnectStart(call: Call) {
        printEvent("secureConnectStart")
    }

    override fun secureConnectEnd(
        call: Call,
        handshake: Handshake?
    ) {
        printEvent("secureConnectEnd")
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        printEvent("connectEnd")
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        printEvent("connectFailed")
    }

    override fun connectionAcquired(
        call: Call,
        connection: Connection
    ) {
        printEvent("connectionAcquired")
    }

    override fun connectionReleased(
        call: Call,
        connection: Connection
    ) {
        printEvent("connectionReleased")
    }

    override fun requestHeadersStart(call: Call) {
        printEvent("requestHeadersStart")
    }

    override fun requestHeadersEnd(
        call: Call,
        request: Request
    ) {
        printEvent("requestHeadersEnd")
    }

    override fun requestBodyStart(call: Call) {
        printEvent("requestBodyStart")
    }

    override fun requestBodyEnd(
        call: Call,
        byteCount: Long
    ) {
        printEvent("requestBodyEnd")
    }

    override fun responseHeadersStart(call: Call) {
        printEvent("responseHeadersStart")
    }

    override fun responseHeadersEnd(
        call: Call,
        response: Response
    ) {
        printEvent("responseHeadersEnd")
    }

    override fun responseBodyStart(call: Call) {
        printEvent("responseBodyStart")
    }

    override fun responseBodyEnd(
        call: Call,
        byteCount: Long
    ) {
        printEvent("responseBodyEnd")
    }

    override fun callEnd(call: Call) {
        apiStatsRelay.accept(System.nanoTime() - callStartNanos)
        printEvent("callEnd")
    }

    override fun callFailed(
        call: Call,
        ioe: IOException
    ) {
        printEvent("callFailed")
    }

}

object HttpListenerFactory : EventListener.Factory {
    private val nextCallId = AtomicLong(1L)
    private val apiStatsRelay: PublishRelay<Long> = PublishRelay.create()

    fun getApiStatsObservable(): Observable<Long> {
        return apiStatsRelay
    }

    override fun create(call: Call): EventListener {
        val callId = nextCallId.getAndIncrement()
        return HttpEventListener(callId, System.nanoTime(), apiStatsRelay)
//        return if (Math.random() < 0.50) {
//            Timber.d("%04d %s%n", callId, call.request().url)
//            HttpEventListener(callId, System.nanoTime(), apiStatsRelay)
//        } else {
//            EventListener.NONE
//        }
    }
}

