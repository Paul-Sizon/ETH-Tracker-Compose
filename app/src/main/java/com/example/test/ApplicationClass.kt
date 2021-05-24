package com.example.test


import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.BuildConfig
import com.adjust.sdk.LogLevel
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.AppsFlyerLibCore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {
    private val TAG = "Application"


    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext





        /** Adjust */
        val appToken = "IDs.adjustId"

        var environment = AdjustConfig.ENVIRONMENT_PRODUCTION;
        if (BuildConfig.DEBUG) {
            environment = AdjustConfig.ENVIRONMENT_SANDBOX;
        }

        val configAdjust = AdjustConfig(applicationContext, appToken, environment);
        if (BuildConfig.DEBUG) {
            configAdjust.setLogLevel(LogLevel.VERBOSE);
        }

        class AdjustLifecycleCallbacks : ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                Adjust.onResume()
            }

            override fun onActivityPaused(activity: Activity) {
                Adjust.onPause()
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }
        }

        Adjust.onCreate(configAdjust)
        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())




        /** AppsFlyer block */
        val devKey = "IDs.appsFlyerDevKey"
        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                Log.i(TAG, "onConversionDataSuccess")
                data?.let { cvData ->
                    val mapToString = cvData
                        .toString()
                        .replace(", ", "&")
                        .replace(Regex("""[{,}]"""), "")

                    var result = ""
                    val organicLink = "app://&" + mapToString
                    if (cvData.containsKey("campaign")) {
                        val campaignValue = cvData.getValue("campaign")
                        if (campaignValue == "null") {

                            result = organicLink
                            Log.i(TAG, organicLink)
                        } else if (campaignValue != "null") {
                            val nonOrganicLink =
                                "app://&sub1=$campaignValue&" + mapToString
                            result = nonOrganicLink
                            Log.i(TAG, nonOrganicLink)
                        }
                    } else {
                        result = organicLink
                        Log.i(TAG, organicLink)
                    }

                    cvData.map {
                        Log.i(
                            AppsFlyerLibCore.LOG_TAG,
                            "conversion_attribute:  ${it.key} = ${it.value}"
                        )
                    }
                    Log.i(TAG, result)
                }

            }

            override fun onConversionDataFail(error: String?) {
                Log.e(TAG, "error onAttributionFailure :  $error")
            }

            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                data?.map {
                    Log.d(AppsFlyerLibCore.LOG_TAG, "onAppOpen_attribute: ${it.key} = ${it.value}")
                }
            }

            override fun onAttributionFailure(error: String?) {
                Log.e(AppsFlyerLibCore.LOG_TAG, "error onAttributionFailure :  $error")
            }
        }
        AppsFlyerLib.getInstance().init(devKey, conversionDataListener, this)
        AppsFlyerLib.getInstance().startTracking(this)
        // appsFlyerDeviceId
        val appsFlyerDeviceId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)


    }


    //getting context
    companion object {
        var appContext: Context? = null
            private set
    }
}




