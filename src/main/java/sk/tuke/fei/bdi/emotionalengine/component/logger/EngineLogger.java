package sk.tuke.fei.bdi.emotionalengine.component.logger;

import sk.tuke.fei.bdi.emotionalengine.component.Engine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tomáš Herich
 */

public class EngineLogger {

    private Engine engine;
    private File logFile;

    public EngineLogger(Integer loggingDelayMillis, Engine engine) {

        this.engine = engine;
        createLogFile();

        LogFileStructureCreator creator = new LogFileStructureCreator(logFile, engine);
        creator.createLogFileStructure();

        EmotionIntensityLogger emotionIntensityLogger = new EmotionIntensityLogger(loggingDelayMillis, logFile, engine);
        emotionIntensityLogger.createEmotionIntensityLogger();

    }

    private void createLogFile() {

        try {

            // Create file abstract path name
            File file = new File("logs/" + createFileNameTime() + engine.getAgentName() + ".xml");

            // Check if file exist
            if (!file.exists()) {

                // Create new file if it doesn't exist
                file.getParentFile().mkdirs();
                file.createNewFile();
            }


            // Assign created file to instance variable
            logFile = file;

        } catch (Exception e) {
            System.out.println(getClass().getSimpleName() + " - " + e.getMessage());
        }

    }

    private String createFileNameTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");

        return simpleDateFormat.format(new Date());

    }


}
