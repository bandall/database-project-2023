//package com.ajousw.spring.domain.parser;
//
//import com.ajousw.spring.domain.timetable.repository.Subject;
//import com.ajousw.spring.domain.timetable.repository.SubjectTime;
//import com.ajousw.spring.domain.timetable.repository.TimeTable;
//import com.ajousw.spring.domain.timetable.parser.ClassTime;
//import com.ajousw.spring.domain.timetable.parser.SubjectParse;
//import com.ajousw.spring.domain.timetable.parser.TableInfo;
//import com.ajousw.spring.domain.timetable.parser.TableParse;
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import java.io.IOException;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//
//@Slf4j
//class TableInfoTest {
//
//    @Test
//    public void parseTest() {
//
//        try {
//            XmlMapper xmlMapper = new XmlMapper();
//            TableInfo tableInfo = xmlMapper.readValue(getData(), TableInfo.class);
//            printData(tableInfo);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
////    private void printData(TableInfo tableInfo) {
////        TableParse parsedTable = tableInfo.getTables().get(0);
////
////        TimeTable timeTable = new TimeTable();
////        timeTable.setIdentifier(parsedTable.getIdentifier());
////        timeTable.setYear(parsedTable.getYear());
////        timeTable.setSemester(parsedTable.getSemester());
////
////        for (SubjectParse subjectParse : parsedTable.getSubjects()) {
////            Subject subject = new Subject();
////            subject.setId(subjectParse.getId());
////            subject.setName(subjectParse.getName());
////            subject.setCode(subjectParse.getInternal());
////            subject.setProfessor(subjectParse.getProfessor());
////            for (ClassTime classTime : subjectParse.getClassTimes()) {
////                SubjectTime subjectTime = new SubjectTime();
////                subjectTime.setDay(classTime.getDay());
////                subjectTime.setPlace(classTime.getPlace());
////                subjectTime.setStartTime(classTime.getStartTime());
////                subjectTime.setEndTime(classTime.getEndTime());
////
////                subject.addSubjectTime(subjectTime);
////            }
////
////            timeTable.addSubject(subject);
////        }
////
////        log.info("{}", timeTable);
////    }
//
//    private String getData() {
//        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//                + "<response>\n"
//                + "    <table year=\"2023\" semester=\"2\" status=\"1\" identifier=\"50HePCkWpfmOvrVaA4A3\">\n"
//                + "        <subject id=\"6249511\">\n"
//                + "            <internal value=\"X562\"/>\n"
//                + "            <name value=\"이공계열 취업역량개발\"/>\n"
//                + "            <professor value=\"정광식\"/>\n"
//                + "            <time value=\"\"/>\n"
//                + "            <place value=\"\"/>\n"
//                + "            <credit value=\"1\"/>\n"
//                + "            <closed value=\"0\"/>\n"
//                + "        </subject>\n"
//                + "        <subject id=\"6249957\">\n"
//                + "            <internal value=\"F050\"/>\n"
//                + "            <name value=\"인간과컴퓨터상호작용\"/>\n"
//                + "            <professor value=\"이환용\"/>\n"
//                + "            <time value=\"화D(팔309) 목C(팔309)\">\n"
//                + "                <data day=\"1\" starttime=\"162\" endtime=\"177\" place=\"팔309\"/>\n"
//                + "                <data day=\"3\" starttime=\"144\" endtime=\"159\" place=\"팔309\"/>\n"
//                + "            </time>\n"
//                + "            <place value=\"\"/>\n"
//                + "            <credit value=\"3\"/>\n"
//                + "            <closed value=\"0\"/>\n"
//                + "        </subject>\n"
//                + "        <subject id=\"6249964\">\n"
//                + "            <internal value=\"F057\"/>\n"
//                + "            <name value=\"오픈소스SW입문\"/>\n"
//                + "            <professor value=\"이환용\"/>\n"
//                + "            <time value=\"화B(원251) 목A(원251)\">\n"
//                + "                <data day=\"1\" starttime=\"126\" endtime=\"141\" place=\"원251\"/>\n"
//                + "                <data day=\"3\" starttime=\"108\" endtime=\"123\" place=\"원251\"/>\n"
//                + "            </time>\n"
//                + "            <place value=\"\"/>\n"
//                + "            <credit value=\"3\"/>\n"
//                + "            <closed value=\"0\"/>\n"
//                + "        </subject>\n"
//                + "        <subject id=\"6249979\">\n"
//                + "            <internal value=\"F073\"/>\n"
//                + "            <name value=\"데이터베이스\"/>\n"
//                + "            <professor value=\"정태선\"/>\n"
//                + "            <time value=\"수B(팔325) 금B(팔325)\">\n"
//                + "                <data day=\"2\" starttime=\"126\" endtime=\"141\" place=\"팔325\"/>\n"
//                + "                <data day=\"4\" starttime=\"126\" endtime=\"141\" place=\"팔325\"/>\n"
//                + "            </time>\n"
//                + "            <place value=\"\"/>\n"
//                + "            <credit value=\"3\"/>\n"
//                + "            <closed value=\"0\"/>\n"
//                + "        </subject>\n"
//                + "        <subject id=\"6249992\">\n"
//                + "            <internal value=\"F088\"/>\n"
//                + "            <name value=\"SW산업세미나\"/>\n"
//                + "            <professor value=\"강경란\"/>\n"
//                + "            <time value=\"수8.5(원251)\">\n"
//                + "                <data day=\"2\" starttime=\"198\" endtime=\"208\" place=\"원251\"/>\n"
//                + "            </time>\n"
//                + "            <place value=\"\"/>\n"
//                + "            <credit value=\"1\"/>\n"
//                + "            <closed value=\"0\"/>\n"
//                + "        </subject>\n"
//                + "        <subject id=\"6251213\">\n"
//                + "            <internal value=\"X537\"/>\n"
//                + "            <name value=\"생각의 예술적 표현\"/>\n"
//                + "            <professor value=\"안지연\"/>\n"
//                + "            <time value=\"화A(성305) 금A(성305)\">\n"
//                + "                <data day=\"1\" starttime=\"108\" endtime=\"123\" place=\"성305\"/>\n"
//                + "                <data day=\"4\" starttime=\"108\" endtime=\"123\" place=\"성305\"/>\n"
//                + "            </time>\n"
//                + "            <place value=\"\"/>\n"
//                + "            <credit value=\"3\"/>\n"
//                + "            <closed value=\"0\"/>\n"
//                + "        </subject>\n"
//                + "    </table>\n"
//                + "    <user name=\"정선문\"/>\n"
//                + "    <primaryTables>\n"
//                + "        <primaryTable year=\"2023\" semester=\"겨울\" identifier=\"EONrEUv2bYZ6CD4dmQmQ\"/>\n"
//                + "        <primaryTable year=\"2023\" semester=\"2\" identifier=\"50HePCkWpfmOvrVaA4A3\"/>\n"
//                + "        <primaryTable year=\"2023\" semester=\"여름\" identifier=\"C0D9W6fggikdbWBd0U9o\"/>\n"
//                + "        <primaryTable year=\"2023\" semester=\"1\" identifier=\"vcXmYgUfYNWTyicW2M1z\"/>\n"
//                + "        <primaryTable year=\"2022\" semester=\"2\" identifier=\"GScta1Yna4zuyK7FIDgD\"/>\n"
//                + "        <primaryTable year=\"2022\" semester=\"1\" identifier=\"hWg9QRGwzGjpusmAqljk\"/>\n"
//                + "        <primaryTable year=\"2021\" semester=\"2\" identifier=\"mD6vXNXSkwNrfOfjONq8\"/>\n"
//                + "        <primaryTable year=\"2021\" semester=\"여름\" identifier=\"5Io7Y6bF25YuDhZTTkdO\"/>\n"
//                + "        <primaryTable year=\"2021\" semester=\"1\" identifier=\"Aue90nRnhyVJMzbYMYAM\"/>\n"
//                + "        <primaryTable year=\"2020\" semester=\"겨울\" identifier=\"H9yjrCjBD9txB4IriqA7\"/>\n"
//                + "        <primaryTable year=\"2020\" semester=\"2\" identifier=\"tvUdIEkKj2Av1oBCe8OA\"/>\n"
//                + "        <primaryTable year=\"2020\" semester=\"여름\" identifier=\"XfO0Io0fLhPEE9icGVM7\"/>\n"
//                + "        <primaryTable year=\"2020\" semester=\"1\" identifier=\"krm8xKgC6LlNt6FDXvXA\"/>\n"
//                + "        <primaryTable year=\"2019\" semester=\"겨울\" identifier=\"XT7uJwGZXHVPfCU0uSVy\"/>\n"
//                + "        <primaryTable year=\"2019\" semester=\"2\" identifier=\"c1azUABldygjipotitUI\"/>\n"
//                + "        <primaryTable year=\"2019\" semester=\"여름\" identifier=\"vRnvH6HMYK62jaSJf2Ri\"/>\n"
//                + "        <primaryTable year=\"2019\" semester=\"1\" identifier=\"9jyDW12dfbVSHZqBpeIb\"/>\n"
//                + "    </primaryTables>\n"
//                + "</response>";
//    }
//}