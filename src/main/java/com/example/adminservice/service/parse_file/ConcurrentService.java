package com.example.adminservice.service.parse_file;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.concurrent.Callable;

public class ConcurrentService implements Callable<Void> {

    private final XmlHandler xmlHandler;
    private final File file;


    public ConcurrentService(XmlHandler xmlHandler, File file) {
        this.xmlHandler = xmlHandler;
        this.file = file;
    }

    @Override
    public Void call() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(file, xmlHandler);
        return null;
    }
}
