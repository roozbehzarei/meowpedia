package com.roozbehzarei.meowpedia

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The base application class for the Meowpedia application.
 * It serves as the entry point for Hilt dependency injection.
 */
@HiltAndroidApp
class BaseApplication: Application()