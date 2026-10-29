package uz.ttpu.permissionslab

import org.junit.Test

class PermissionModel {
    var granted: Boolean = false
        private set
    var rationale: Boolean = false
        private set
    var dialogShows: Boolean = true
        private set

    private var denialCount = 0

    fun request(userAllows: Boolean) {
        if (!dialogShows) {
            println("no dialog, denied immediately")
            return
        }

        if (userAllows) {
            granted = true
            rationale = false
            dialogShows = false
        } else {
            denialCount++
            granted = false

            when (denialCount) {
                1 -> {
                    rationale = true
                    dialogShows = true
                }
                2 -> {
                    rationale = false
                    dialogShows = false
                }
            }
        }
    }

    fun printState(step: String) {
        println("[$step] Granted: $granted, Rationale: $rationale, DialogShows: $dialogShows")
    }
}

class PermissionModelTest {

    @Test
    fun runPermissionSimulation() {
        val model = PermissionModel()
        model.printState("Initial")

        println("\n--- Step 1: Deny ---")
        model.request(userAllows = false)
        model.printState("Step 1")

        println("\n--- Step 2: Deny ---")
        model.request(userAllows = false)
        model.printState("Step 2")

        println("\n--- Step 3: Request again ---")
        model.request(userAllows = false)
        model.printState("Step 3")

        println("\n--- Step 4: Allow ---")
        model.request(userAllows = true)
        model.printState("Step 4")
    }
}
