package com.lionzxy.trex_library

import java.io.File

interface ITRexZipProvider {
    /**
     * Method call from background thread
     */
    fun getTRexZip(): File

    fun dropCache()
}