package com.girmiti.mobilepos.logger;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileLogger extends Logger {
    private Logger logger = getNewLogger(FileLogger.class.getName());
    static final String LOG_FILE_NAME = "wallet.log";

    File file = null;
    FileOutputStream out = null;

    protected FileLogger(String name) {
        super(name);
    }

    @Override
    protected Logger newLogger(String name) {
        return new FileLogger(name);
    }

    public void init() {
        try {
            file = new File(LOG_FILE_NAME);
            if (checkNull(file)) {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                out = new FileOutputStream(file);
            }
        } catch (IOException ioEx) {
            logger.info("Unable to open File I/O connection " + ioEx.getMessage());
            logger.info("I/O Exception" + ioEx);
        }
    }


    @Override
    protected void writeLog(String mesg) {
        if (out != null) {
            try {
                out.write((mesg + "\n").getBytes());
                out.flush();
                logger.info("write message" + mesg);
            } catch (IOException e) {
                logger.info("Error in writing the logs to file " + LOG_FILE_NAME);
                logger.info("Inputoutput Exception" + e);

            }

        } else {
            logger.info("NO Filelogger " + mesg);
        }

    }

    @Override
    public void close() {

        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            Log.d("ERROR", "FileLogger, closing");
            Log.d("ERROR", "" + e);
        }
    }

    private boolean checkNull(File inputFile) {
        return inputFile != null;
    }

}
