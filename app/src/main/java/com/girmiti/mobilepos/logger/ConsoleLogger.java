package com.girmiti.mobilepos.logger;

public class ConsoleLogger extends Logger {


    protected ConsoleLogger() {
    }

    protected ConsoleLogger(String name) {
        super(name);

    }

    @Override
    public void init() {
        //No need to do anything here, so it is empty
    }

    @Override
    public void close() {
        //No need to do anything here, so it is empty
    }

    @Override
    protected void writeLog(String mesg) {
        System.out.println(mesg);
    }

    @Override
    public Logger newLogger(String name) {
        return new ConsoleLogger(name);
    }
}
