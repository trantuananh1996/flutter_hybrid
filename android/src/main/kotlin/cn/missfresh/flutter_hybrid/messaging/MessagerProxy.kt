package cn.missfresh.flutter_hybrid.messaging

import cn.missfresh.flutter_hybrid.Logger
import io.flutter.plugin.common.MethodChannel

/**
 * Created by sjl
 * on 2019-09-03
 */
class MessagerProxy {

    private var mMethodChannel: MethodChannel

    private var messagerSet: HashSet<Messager> = hashSetOf()

    constructor(methodChannel: MethodChannel) {
        mMethodChannel = methodChannel
        setMethodCallHandler()
    }

    /**
     * Native accepts flutter call communication protocol, processing parameters and response
     */
    private fun setMethodCallHandler() {
        mMethodChannel.setMethodCallHandler { methodCall, result ->

            var components = methodCall.method.split(".")

            if (components.size != 2) {
                throw Exception("method name is invalid: " + methodCall.method)
            }

            val messagerName = components[0]
            val methodName = components[1]

            messagerSet.forEach {
                if (it.name == messagerName) {
                    Logger.d("messagerName=$messagerName,methodName=$methodName")
                    it.handleMethodCall(methodName, methodCall.arguments, result)
                }
            }
        }
    }

    /**
     * Add a Messager to the messagerSet
     */
    fun addMessager(messager: Messager) {
        messagerSet.add(messager)
        messager.setMethodChannel(mMethodChannel)
    }

    /**
     * Remove a Messager form the messagerSet
     */
    fun removeMessager(messager: Messager) {
        messagerSet.remove(messager)
    }
}