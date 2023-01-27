package com.example.adminservice.service.parse_file;

import com.example.adminservice.entity.HousesParam;
import com.example.adminservice.repository.HouseParamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class XmlHandler extends DefaultHandler {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private HouseParamRepository houseParamRepository;
    private JdbcTemplate jdbcTemplate;

    private final static String SQL_INSERT_PERSON =
            "insert into houses_param("
                    + "id, id_object, kd_type, vl, change_id, change_id_end, update_date, start_date, end_date) "
                    + "values(?,?,?,?,?,?,?,?,?)";
    private List<HousesParam> cstmtList;

    public XmlHandler(HouseParamRepository houseParamRepository,
                      JdbcTemplate jdbcTemplate,
                      List<HousesParam> cstmtList) {
        this.houseParamRepository = houseParamRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.cstmtList = cstmtList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        int attributeLength = attributes.getLength();
        if (qName.equals("PARAM")) {
            HousesParam entity = new HousesParam();
            for (int i = 0; i < attributeLength; i++) {
                entity.setId(System.nanoTime());
                entity.setObjectid(toNumber(attributes.getValue("OBJECTID")));
                entity.setType(toNumber(attributes.getValue("TYPEID")));
                entity.setValue(attributes.getValue("VALUE"));
                entity.setChangeId(attributes.getValue("CHANGEID"));
                entity.setChangeIdEnd(attributes.getValue("CHANGEIDEND"));
                entity.setUpdateDate(toDate(attributes.getValue("UPDATEDATE")));
                entity.setStartDate(toDate(attributes.getValue("STARTDATE")));
                entity.setEndDate(toDate(attributes.getValue("ENDDATE")));
            }
            // JDBC_SIMPLE
//            jdbcTemplate.update(SQL_INSERT_PERSON,
//                    new Date().getTime(),
//                    entity.getObjectid(),
//                    entity.getType(),
//                    entity.getValue(),
//                    entity.getChangeId(),
//                    entity.getChangeIdEnd(),
//                    entity.getUpdateDate(),
//                    entity.getStartDate(),
//                    entity.getEndDate());
                // JPA_SIMPLE
                houseParamRepository.save(entity);
            // JDBC_PACKAGE
//            cstmtList.add(entity);
        }

    }

    private void jdbcPackage(HousesParam entity, CallableStatement cstmt) {
        try {
            cstmt.setLong(1, entity.getId()); //(2)
            cstmt.setLong(2, entity.getObjectid()); //(2)
            cstmt.setLong(3, entity.getType()); //(2)
            cstmt.setString(4, entity.getValue()); //(2)
            cstmt.setString(5, entity.getChangeId()); //(2)
            cstmt.setString(6, entity.getChangeIdEnd()); //(2)
            cstmt.setDate(7, java.sql.Date.valueOf(entity.getUpdateDate())); //(2)
            cstmt.setDate(8, java.sql.Date.valueOf(String.valueOf(entity.getStartDate()))); //(2)
            cstmt.setDate(9, java.sql.Date.valueOf(String.valueOf(entity.getEndDate()))); //(2)
            cstmt.addBatch(); //(3)

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveJdbcPackage() {
        try (CallableStatement cstmt = jdbcTemplate.getDataSource()
                .getConnection().prepareCall(SQL_INSERT_PERSON)) {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);
            cstmtList.forEach(i -> {
                try {
                    jdbcPackage(i, cstmt);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            });
            cstmt.executeBatch();
            log.info("executeBatch");
            connection.commit();
            log.info("commit");
            cstmtList.clear();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private Long toNumber(String value) {
        if (value == null) {
            return null;
        }
        return Long.valueOf(value);
    }

    private LocalDate toDate(String value) {
        if (value == null) {
            return null;
        }
        return LocalDate.parse(value, dateTimeFormatter);
    }
}
