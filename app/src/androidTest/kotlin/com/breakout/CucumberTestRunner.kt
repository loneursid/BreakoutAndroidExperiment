package com.breakout

import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.platform.engine.Constants

@io.cucumber.junit.platform.engine.Cucumber
class CucumberTestRunner : CucumberAndroidJUnitRunner() {
    override fun getInstrArguments(): android.os.Bundle {
        return super.getInstrArguments().apply {
            putString(Constants.GLUE_PROPERTY_NAME, "com.breakout.stepdefs")
            putString(Constants.FEATURES_PROPERTY_NAME, "features")
        }
    }
}
