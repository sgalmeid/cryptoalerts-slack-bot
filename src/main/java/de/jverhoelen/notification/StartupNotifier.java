package de.jverhoelen.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StartupNotifier {

    @Autowired
    private SlackService slackService;

    @PostConstruct
    public void alertOnStartup() {
        slackService.sendChannelMessage("general", "ACHTUNG! Alerting-Service wurde neugestartet. Alle Kursaufzeichnungen wurden vergessen und müssen neu gelernt werden. Solang werden Angaben nur den Zeitraum bis *jetzt* berücksichtigen können.");
    }
}
