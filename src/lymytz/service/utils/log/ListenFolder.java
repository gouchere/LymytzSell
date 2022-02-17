/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lymytz.service.utils.log;



import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import lymytz.service.utils.ConsUtil;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;

/**
 *
 * @author LENOVO
 */
public class ListenFolder implements Runnable {

    private Path path = null;
    private final String fileName = "log/appslog.txt";

    public ListenFolder() {
        File logFile = new File("log");
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        this.path = Paths.get(logFile.getAbsolutePath());
//        logFile = new File(fileName);
//        if (!logFile.exists()) {
//            try {
//                logFile.createNewFile();
//            } catch (IOException ex) {
//                Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    @Override
    public void run() {
        WatchService service = null;
        try {
            service = this.path.getFileSystem().newWatchService();
            //Enregistrer les opérations à surveiller
            this.path.register(service, StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey watchKey;
            // boucle sans fin
            while (!LymytzService.stopThread) {
                watchKey = service.take();
                //traiter les evenements
                for (WatchEvent ev : watchKey.pollEvents()) {
                    String fileName_ = ev.context().toString();
                    if (StandardWatchEventKinds.ENTRY_MODIFY.equals(ev.kind())) {
                        File logFile = new File(this.path.toFile().getAbsoluteFile() + "\\" + ev.context().toString());
                        if (ev.context().toString().equals(ConsUtil.SOURCE_LOG_FILE_SYNC) || ev.context().toString().equals(ConsUtil.SOURCE_LOG_FILE_EXCEPTION) || ev.context().toString().equals(ConsUtil.SOURCE_LOG_FILE_USER)) {
                            if ((logFile.length() / (1024 * 1024)) > 1) { //1Mo
                                File dest = new File(this.path.toFile().getAbsoluteFile() + "\\"+ev.context().toString().replace(".log", "")+"_log_" + Constantes.dfh.format(new Date()).replaceAll(" ", "").replaceAll(":", "").replaceAll("-", "") + ".log");
                                logFile.setExecutable(true);
                                logFile.setWritable(true);
                                if (logFile.renameTo(dest)) {
                                }
                                //création d'un nouvveau fichier
                                logFile = new File(this.path.toFile().getAbsoluteFile() + "\\" + ev.context().toString());
                                logFile.createNewFile();
                            }
                        }

                    }

                }
                //se place en attente de message
                watchKey.reset();
            }
        } catch (IOException ex) {
            Logger.getLogger(ListenFolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            try {
                service.close();
                Logger.getLogger(ListenFolder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(ListenFolder.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
    }

}