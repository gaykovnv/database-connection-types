package com.example.adminservice.service.parse_file;

import com.example.adminservice.repository.HouseParamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@AllArgsConstructor
public class ParserFileService {

    private final HouseParamRepository houseParamRepository;
    private final JdbcTemplate jdbcTemplate;


    public void parseFile() {
        File file = new File("src/main/resources/templates/01/02");
        if (Objects.isNull(file.listFiles())) {
            return;
        }
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            log.info("Start");
            long start = System.currentTimeMillis();
            for (File temp : file.listFiles()) {
                log.info("Файл: " + file.getName());
                parser.parse(temp, new XmlHandler(houseParamRepository, jdbcTemplate, new ArrayList<>()));
            }
            log.info("Finish: " + (System.currentTimeMillis() - start));

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void parsePackageFile() {
        File file = new File("src/main/resources/templates/01/02");
        if (Objects.isNull(file.listFiles())) {
            return;
        }
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            log.info("Start");
            long start = System.currentTimeMillis();
            for (File temp : file.listFiles()) {
                log.info("Файл: " + file.getName());
                XmlHandler xmlHandler = new XmlHandler(houseParamRepository, jdbcTemplate, new ArrayList<>());
                parser.parse(temp, xmlHandler);
                xmlHandler.saveJdbcPackage();
            }
            log.info("Finish: " + (System.currentTimeMillis() - start));

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void parseConcurrentFile() {
        File file = new File("src/main/resources/templates/01/02");
        if (Objects.isNull(file.listFiles())) {
            return;
        }
        try {
            log.info("Start");
            long start = System.currentTimeMillis();
            ExecutorService service = Executors.newFixedThreadPool(3);
            for (File temp : file.listFiles()) {
                log.info("Файл: " + file.getName());
                service.submit(new ConcurrentService(
                        new XmlHandler(houseParamRepository, jdbcTemplate, new ArrayList<>()), temp));
            }
            service.shutdown();
            log.info("Finish: " + (System.currentTimeMillis() - start));

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
