package com.angelinaprogress.intellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.angelinaprogress.intellij.configuration.AngelinaProgressConfigurable;
import com.angelinaprogress.intellij.configuration.AngelinaProgressState;
import org.jetbrains.annotations.NotNull;

public final class UpdateNotificationSender {
    private static final String NOTIFICATION_GROUP = "Angelina Progress Bar Update";

    @SuppressWarnings("DialogTitleCapitalization")
    public static void sendNotification(final Project project, final String version) {
        final Notification n = NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP)
            .createNotification("You're now using version "
                    + version
                    + " of Angelina Progress Bar! \uD83C\uDF89",
                NotificationType.INFORMATION);
        n.addAction(new DumbAwareAction("Configuration...") {
                @Override
                public void actionPerformed(@NotNull final AnActionEvent e) {
                    ShowSettingsUtil.getInstance().showSettingsDialog(project,
                        AngelinaProgressConfigurable.class);
                }
            })
            .addAction(new DumbAwareAction("Changenotes") {
                @Override
                public void actionPerformed(@NotNull final AnActionEvent e) {

                    new AngelinaProgressChangenotesDialog(project).show();
                }
            })
            .addAction(new DumbAwareAction("Don't show again", "Disable this notification in the future", null) {
                @Override
                public void actionPerformed(@NotNull final AnActionEvent e) {
                    AngelinaProgressState.getInstance().showUpdateNotification = false;
                }
            })
            .notify(project);
    }
}
