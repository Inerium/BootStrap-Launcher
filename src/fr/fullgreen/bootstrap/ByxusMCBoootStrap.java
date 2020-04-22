package fr.fullgreen.bootstrap;

import fr.theshark34.openlauncherlib.bootstrap.Bootstrap;
import fr.theshark34.openlauncherlib.bootstrap.LauncherClasspath;
import fr.theshark34.openlauncherlib.bootstrap.LauncherInfos;
import fr.theshark34.openlauncherlib.util.ErrorUtil;
import fr.theshark34.openlauncherlib.util.GameDir;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;

import java.io.File;
import java.io.IOException;


public class ByxusMCBoootStrap {

    private static SplashScreen splash;
    private static SColoredBar bar;
    private static Thread barThread;
    private static final LauncherInfos BINFOS = new LauncherInfos("ByxusMC", "fr.fullgreen.LauncherFrame");
    private static final File DIR = GameDir.createGameDir("ByxusMC");
    private static final LauncherClasspath BCP = new LauncherClasspath(new File(DIR, "Launcher/launcher.jar"), new File(DIR, "Launcher/Libs/"));
    private static ErrorUtil errorUtil = new ErrorUtil(new File(DIR, "Launcher/crashes"));


    public static void main(String[] args) {
        Swinger.setResourcePath("/fr/fullgreen/bootstrap/res");
            displaySplash();
            try {
                doUpdate();
            }catch (Exception e) {
                errorUtil.catchError(e, "Impossible de mettre a jour le launcher !");
                barThread.interrupt();
            }
            try {
                launchLauncher();
            }catch (IOException e) {
                errorUtil.catchError(e, "Impossible de mettre lancer le launcher !");

            }
    }

    private static void displaySplash() {
        splash = new SplashScreen("ByxusMC", Swinger.getResource("by.png"));
        splash.setBackground(Swinger.TRANSPARENT);
        splash.getContentPane();
        bar = new SColoredBar(Swinger.getTransparentWhite(100), Swinger.getTransparentWhite(175));
        bar.setBounds(0,490,350,20);
        splash.add(bar);
        splash.setVisible(true);
    }

    private static void doUpdate() throws Exception {
        SUpdate su = new SUpdate("https://supdate.byxus.fr/bootstrap", new File(DIR, "Launcher"));

        barThread = new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    bar.setValue((int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000));
                    bar.setMaximum((int) (BarAPI.getNumberOfTotalBytesToDownload() / 100));

                }
            }
        };
        barThread.start();
        su.start();
        barThread.interrupt();
    }

    private static void launchLauncher() throws IOException {
        Bootstrap bootstrap = new Bootstrap(BCP, BINFOS);
        Process p = bootstrap.launch();

        splash.setVisible(false);
        try {
            p.waitFor();
        }catch (InterruptedException e) {
        }
        System.exit(0);

    }
}
