package com.angelinaprogress.intellij

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.angelinaprogress.intellij.configuration.AngelinaProgressState

/**
 * See https://plugins.jetbrains.com/docs/intellij/plugin-components.html#project-open, must be implemented in Kotlin.
 */
class ProjectStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        AngelinaProgressListener.updateProgressBarUi() // ensure the plugin is properly initialized after project open
        sendUpdateNotificationIfRequired(project, AngelinaProgressListener.getPluginDescriptor())
    }


    private fun sendUpdateNotificationIfRequired(project: Project, descriptor: IdeaPluginDescriptor?) {
        val state = AngelinaProgressState.getInstance()
        if (descriptor != null && state != null) {
            val version = descriptor.version
            if (version != state.version) {
                state.version = descriptor.version
                if (state.showUpdateNotification) {
                    UpdateNotificationSender.sendNotification(project, version)
                }
            }
        }
    }
}