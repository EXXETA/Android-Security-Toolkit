package com.exxeta.securitytoolkit

import android.content.Context
import com.exxeta.securitytoolkit.internal.EmulatorDetector
import com.exxeta.securitytoolkit.internal.HooksDetection
import com.exxeta.securitytoolkit.internal.RootDetection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ThreatDetectionCenter(private val context: Context) {
    val areRootPrivilegesDetected: Boolean
        get() = RootDetection.threatDetected(context)

    val areHooksDetected: Boolean
        get() = HooksDetection.threatDetected()

    val isSimulatorDetected: Boolean
        get() = EmulatorDetector.threatDetected()

    val threats: Flow<Threat>
        get() = flow {
            if (RootDetection.threatDetected(context)) {
                emit(Threat.ROOT_PRIVILEGES)
            }
            if (HooksDetection.threatDetected()) {
                emit(Threat.HOOKS)
            }
            if (EmulatorDetector.threatDetected()) {
                emit(Threat.SIMULATOR)
            }
        }

    public enum class Threat {
        ROOT_PRIVILEGES,
        HOOKS,
        SIMULATOR
    }
}