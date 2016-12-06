//package com.xuebang.o2o.business.controller;
//
//import com.xuebang.o2o.business.dao.KnowledgeDao;
//import com.xuebang.o2o.business.dao.SysnKnowledgeDao;
//import com.xuebang.o2o.business.entity.Knowledge;
//import com.xuebang.o2o.business.entity.SysnKnowledge;
//import org.apache.log4j.Logger;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * Created by Administrator on 2016/7/26.
// */
//@RequestMapping("know")
//@Controller
//public class KnowledgeController {
//
//    private static final Logger logger = Logger.getLogger(KnowledgeController.class);
//
//    @Autowired
//    private KnowledgeDao knowledgeDao;
//
//    @Autowired
//    private SysnKnowledgeDao sysnKnowledgeDao;
//
//    private String path = "F:\\excel\\math.xlsx";
//    private Long startId = 6001l;
//    private String section = "高中";
//    private String subject ="math";
//    private String syncPublishVersion ="人教版";
//    private String syncBookVersion ="必修一";
//    private String knowSheet ="高中数学专题知识点总集 ";//专题的sheet
//    private String syncKnowSheet ="高中化学同步知识点（人教版）";//同步的sheet
//
//
//    @RequestMapping("sysnknow/read")
//    public void readssnExcel(String path,String section,String subject,
//                             String syncPublishVersion,String syncBookVersion, String syncKnowSheet,Long startId) throws Exception{
//        this.section = section;
//        this.subject = subject;
//        this.startId = startId;
//        this.syncPublishVersion = syncPublishVersion;
//        this.syncBookVersion = syncBookVersion;
//        this.syncKnowSheet = syncKnowSheet;
//        sysnreadExcel(path);
//    }
//
//    @RequestMapping("quchongf")
//    public void quchongf() throws Exception{
//        List<SysnKnowledge> knows =  sysnKnowledgeDao.findAll();
//        for(SysnKnowledge know : knows){
//            List<SysnKnowledge> childs = sysnKnowledgeDao.findChild(know.getId());
//            if(childs.isEmpty()){
//                know.setIsLeaf(1);
//            }else {
//                know.setIsLeaf(0);
//            }
////            String num = know.getKnowNumber().substring( know.getKnowNumber().length() - 1 );
////            know.setSort( Integer.valueOf(num));
//            sysnKnowledgeDao.save(know);
//        }
//    }
//
//    @RequestMapping("know/read")
//    public void readExcel(String path,String section,String subject,String knowSheet,Long startId) throws Exception{
//        this.section = section;
//        this.subject = subject;
//        this.startId = startId;
//        this.knowSheet = knowSheet;
//        readExcel(path);
//    }
//
//    @RequestMapping("know/readL8")
//    public void readL8() throws Exception{
//        readExcel8("F:\\excel\\physics.xlsx");
//    }
//
//    @RequestMapping("quchongs")
//    public void quchongs() throws Exception{
//        List<Knowledge> knows =  knowledgeDao.findAll();
//        for(Knowledge know : knows){
//            List<Knowledge> childs = knowledgeDao.findChild(know.getId());
//            if(childs.isEmpty()){
//                know.setIsLeaf(1);
//            }else {
//                know.setIsLeaf(0);
//            }
//            String num = know.getNumber().substring( know.getNumber().length() - 1 );
//            know.setSort( Integer.valueOf(num));
//            knowledgeDao.save(know);
//        }
//    }
//
//    /**
//     * 同步知识点导入
//     * @param fileName
//     * @throws Exception
//     */
//    public  void sysnreadExcel(String fileName) throws Exception{
//
//        //存放父知识点的excel的位置    index 0  行数  index 1 列数
//        int[] one  = new int[2];
//        int[] tow  = new int[2];
//        int[] three  = new int[2];
//        int[] four  = new int[2];
//        int[] five  = new int[2];
//        //记录父知识点在数据库的id star
//        long oneId =0l,towId =0l,threeId=0l,fourId=0l,fiveId=0l;
//        //记录父知识点在数据库的id se
//        List<SysnKnowledge> knowledges = new ArrayList<>();
//        boolean isE2007 = false;    //判断是否是excel2007格式
//        if(fileName.endsWith("xlsx"))
//            isE2007 = true;
//
//        InputStream input = new FileInputStream(fileName);  //建立输入流
//        Workbook wb  = null;
//        //根据文件格式(2003或者2007)来初始化
//        if(isE2007)
//            wb = new XSSFWorkbook(input);
//        else
//            wb = new HSSFWorkbook(input);
//        Sheet sheet = wb.getSheet(syncKnowSheet);
//        long id = startId;
//        for(int i = 1 ; i < sheet.getLastRowNum() + 1; i++){
//            Row row = sheet.getRow(i);  //获得行数据
//
//            //剔除行 为null的情况 begin
//            boolean rowNotNull = isRowNotNull(row);
//            if (!rowNotNull) {
//                continue;
//            }
//            //剔除行 为null的情况 end
//
//            SysnKnowledge knowledge = new SysnKnowledge();
//            knowledge.setId(id);
//            knowledge.setSection(section);
//            knowledge.setSubject(subject);
//            knowledge.setPublishVersion(syncPublishVersion);
//            knowledge.setBookVersion(syncBookVersion);
//            boolean flag = true;
//            for(int j = 5 ; j < 15 ; j++){
//                Cell cell = row.getCell(j);
//                if (cell == null) {
//                    if(flag && knowledge.getName() != null){
//                        flag = false;
//                        if( j - 2 == 5) {Row parentRow =  sheet.getRow(one[0]); Cell parentCell = parentRow.getCell(one[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(oneId);
//                        }
//                        if( j - 2 == 6) {
//                            Row parentRow =  sheet.getRow(tow[0]);
//                            Cell parentCell = parentRow.getCell(tow[1]);
//                            knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(towId);
//                        }
//                        if( j - 2 == 7) {Row parentRow =  sheet.getRow(three[0]); Cell parentCell = parentRow.getCell(three[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(threeId);
//                        }
//                        if( j - 2 == 8) {Row parentRow =  sheet.getRow(four[0]); Cell parentCell = parentRow.getCell(four[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(fourId);
//                        }
//                        if( j - 2 == 9) {Row parentRow =  sheet.getRow(five[0]); Cell parentCell = parentRow.getCell(five[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(fiveId);
//                        }
//                    }
//                    continue;
//                }
//                switch (cell.getCellType()){
//                    case HSSFCell.CELL_TYPE_NUMERIC:
//                        System.out.println(cell.getNumericCellValue());
//                        if( j - 1 == 4) {one[0] = i;one[1] = j; oneId = knowledge.getId();}
//                        if( j - 1 == 5) {tow[0] = i;tow[1] = j;towId = knowledge.getId();}
//                        if( j - 1 == 6) {three[0] = i;three[1] = j;threeId = knowledge.getId();}
//                        if( j - 1 == 9) {four[0] = i;four[1] = j;fourId = knowledge.getId();}
//                        if( j - 1 == 8) {five[0] = i;five[1] = j;fiveId = knowledge.getId();}
//                        if( j - 1 == 9) {knowledge.setName(String.valueOf(cell.getStringCellValue()));}
//                        if( j - 1 == 11) {knowledge.setKnowName(cell.getStringCellValue());}//专题知识点名字
//                        if( j - 1 == 12) {knowledge.setKnowNumber(String.valueOf(cell.getNumericCellValue()));}//专题知识点序号
//                        break;
//                    case HSSFCell.CELL_TYPE_STRING:
//                        System.out.println(cell.getStringCellValue());
//                        if( j - 1 == 4) {one[0] = i;one[1] = j;knowledge.setName(cell.getStringCellValue());flag = false;oneId = knowledge.getId();}
//                        if( j - 1 == 5) {tow[0] = i;tow[1] = j;knowledge.setName(cell.getStringCellValue());towId = knowledge.getId();}
//                        if( j - 1 == 6) {three[0] = i;three[1] = j;knowledge.setName(cell.getStringCellValue());threeId = knowledge.getId();}
//                        if( j - 1 == 7) {four[0] = i;four[1] = j;knowledge.setName(cell.getStringCellValue());fourId = knowledge.getId();}
//                        if( j - 1 == 8) {five[0] = i;five[1] = j;knowledge.setName(cell.getStringCellValue());fiveId = knowledge.getId();}
//                        if( j - 1 == 9) {knowledge.setName(cell.getStringCellValue());}
//                        if( j - 1 == 11) {knowledge.setKnowName(cell.getStringCellValue());}//专题知识点名字
//                        if( j - 1 == 12) {knowledge.setKnowNumber(cell.getStringCellValue());}//专题知识点序号
//                        break;
//                    default:
//                        if(flag && knowledge.getName() != null){
//                            flag = false;
//                            if (j - 2 == 5) {
//                                Row parentRow = sheet.getRow(one[0]);
//                                Cell parentCell = parentRow.getCell(one[1]);
//                                knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(oneId);
//                            }
//                            if( j - 2 == 6) {
//
//                                Row parentRow =  sheet.getRow(tow[0]);
//                                Cell parentCell = parentRow.getCell(tow[1]);
//                                knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(towId);
//                            }
//                            if (j - 2 == 7) {
////                                Row parentRow = sheet.getRow(three[0]);
////                                Cell parentCell = parentRow.getCell(three[1]);
////                                knowledge.setParent(parentCell.getStringCellValue());
////                                knowledge.setParentId(threeId);
//                                knowledge.setParent(sheet.getRow(three[0]).getCell(three[1]).getStringCellValue());
//                                knowledge.setParentId(threeId);
//
//                            }
//                            if( j - 2 == 8) {Row parentRow =  sheet.getRow(four[0]); Cell parentCell = parentRow.getCell(four[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(fourId);
//                            }
//                            if( j - 2 == 9) {Row parentRow =  sheet.getRow(five[0]); Cell parentCell = parentRow.getCell(five[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(fiveId);
//                            }
//                        }
//
//                        break;
//                }
//            }
//            id++;
//            knowledges.add(knowledge);
//        }
//        sysnKnowledgeDao.save(knowledges);
//        System.out.println(knowledges.toString());
//    }
//
//
//    /**
//     * 专题知识点导入
//     * @param fileName
//     * @throws Exception
//     */
//    public  void readExcel(String fileName) throws Exception {
//        int[] one  = new int[2];
//        int[] tow  = new int[2];
//        int[] three  = new int[2];
//        int[] four  = new int[2];
//        int[] five  = new int[2];
//        long oneId =0l,towId =0l,threeId=0l,fourId=0l,fiveId=0l;
//        List<Knowledge> knowledges = new ArrayList<>();
//        boolean isE2007 = false;    //判断是否是excel2007格式
//        if(fileName.endsWith("xlsx"))
//            isE2007 = true;
//
//        InputStream input = new FileInputStream(fileName);  //建立输入流
//        Workbook wb  = null;
//        //根据文件格式(2003或者2007)来初始化
//        if(isE2007)
//            wb = new XSSFWorkbook(input);
//        else
//            wb = new HSSFWorkbook(input);
//        Sheet sheet = wb.getSheet(knowSheet);
//        long id = startId;
//        for(int i = 1 ; i < sheet.getLastRowNum() + 1; i++){
//            Row row = sheet.getRow(i);  //获得行数据
//
//            //剔除行 为null的情况 begin
//            boolean rowNotNull = isRowNotNull(row);
//            if (!rowNotNull) {
//                continue;
//            }
//            //剔除行 为null的情况 end
//            Knowledge knowledge = new Knowledge();
//            knowledge.setId(id);
//            knowledge.setSection(section);
//            knowledge.setSubject(subject);
//            boolean flag = true;
//            for(int j = 2 ; j < 25 ; j++){
//                Cell cell = row.getCell(j);
//                if (cell == null) {
//                    if(flag && knowledge.getName() != null){
//                        flag = false;
//                        if( j - 2 == 2) {Row parentRow =  sheet.getRow(one[0]); Cell parentCell = parentRow.getCell(one[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(oneId);
//                        }
//                        if( j - 2 == 3) {
//                            if(knowledge.getName().equals("<split-tag>原命题")){
//                                System.out.println(1);
//                            }
//                            Row parentRow =  sheet.getRow(tow[0]);
//                            Cell parentCell = parentRow.getCell(tow[1]);
//                            knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(towId);
//                        }
//                        if( j - 2 == 4) {Row parentRow =  sheet.getRow(three[0]); Cell parentCell = parentRow.getCell(three[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(threeId);
//                        }
//                        if( j - 2 == 5) {Row parentRow =  sheet.getRow(four[0]); Cell parentCell = parentRow.getCell(four[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(fourId);
//                        }
//                        if( j - 2 == 6) {Row parentRow =  sheet.getRow(five[0]); Cell parentCell = parentRow.getCell(five[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(fiveId);
//                        }
//                    }
//                    continue;
//                };
//                switch (cell.getCellType()){
//                    case HSSFCell.CELL_TYPE_NUMERIC:
//                        System.out.println(cell.getNumericCellValue());
//                        if( j - 1 == 1) {one[0] = i;one[1] = j; oneId = knowledge.getId();}
//                        if( j - 1 == 2) {tow[0] = i;tow[1] = j;towId = knowledge.getId();}
//                        if( j - 1 == 3) {three[0] = i;three[1] = j;threeId = knowledge.getId();}
//                        if( j - 1 == 4) {four[0] = i;four[1] = j;fourId = knowledge.getId();}
//                        if( j - 1 == 5) {five[0] = i;five[1] = j;fiveId = knowledge.getId();}
//                        if( j - 1 == 6) {knowledge.setName(String.valueOf(cell.getStringCellValue()));}
//                        if( j - 1 == 8) {knowledge.setNumber(String.valueOf( cell.getNumericCellValue()));}
//                        break;
//                    case HSSFCell.CELL_TYPE_STRING:
//                        System.out.println(cell.getStringCellValue());
//                        if( j - 1 == 1) {one[0] = i;one[1] = j;knowledge.setName(cell.getStringCellValue());flag = false;oneId = knowledge.getId();}
//                        if( j - 1 == 2) {tow[0] = i;tow[1] = j;knowledge.setName(cell.getStringCellValue());towId = knowledge.getId();}
//                        if( j - 1 == 3) {three[0] = i;three[1] = j;knowledge.setName(cell.getStringCellValue());threeId = knowledge.getId();}
//                        if( j - 1 == 4) {four[0] = i;four[1] = j;knowledge.setName(cell.getStringCellValue());fourId = knowledge.getId();}
//                        if( j - 1 == 5) {five[0] = i;five[1] = j;knowledge.setName(cell.getStringCellValue());fiveId = knowledge.getId();}
//                        if( j - 1 == 6) {knowledge.setName(cell.getStringCellValue());}
//                        if( j - 1 == 8) {knowledge.setNumber(cell.getStringCellValue());}
//                        break;
//                    case HSSFCell.CELL_TYPE_BOOLEAN:
//                        System.out.println(cell.getBooleanCellValue());
//                        if( j - 1 == 1) {one[0] = i;one[1] = j;}
//                        if( j - 1 == 2) {tow[0] = i;tow[1] = j;}
//                        if( j - 1 == 3) {three[0] = i;three[1] = j;}
//                        if( j - 1 == 4) {four[0] = i;four[1] = j;}
//                        if( j - 1 == 5) {five[0] = i;five[1] = j;}
//                        if( j - 1 == 6) {knowledge.setName(cell.getStringCellValue());}
//                        if( j - 1 == 8) {knowledge.setNumber(cell.getStringCellValue());}
//                        break;
//                    case HSSFCell.CELL_TYPE_FORMULA:
//                        System.out.println(cell.getCellFormula());
//                        if( j - 1 == 1) {one[0] = i;one[1] = j;}
//                        if( j - 1 == 2) {tow[0] = i;tow[1] = j;}
//                        if( j - 1 == 3) {three[0] = i;three[1] = j;}
//                        if( j - 1 == 4) {four[0] = i;four[1] = j;}
//                        if( j - 1 == 5) {five[0] = i;five[1] = j;}
//                        if( j - 1 == 6) {knowledge.setName(cell.getStringCellValue());}
//                        if( j - 1 == 8) {knowledge.setNumber(cell.getStringCellValue());}
//                        break;
//                    default:
//                        if(flag && knowledge.getName() != null){
//                            flag = false;
//                            if( j - 2 == 2) {Row parentRow =  sheet.getRow(one[0]); Cell parentCell = parentRow.getCell(one[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(oneId);
//                            }
//                            if( j - 2 == 3) {
//                                if(knowledge.getName().equals("<split-tag>原命题")){
//                                    System.out.println(1);
//                                }
//                                Row parentRow =  sheet.getRow(tow[0]);
//                                Cell parentCell = parentRow.getCell(tow[1]);
//                                knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(towId);
//                            }
//                            if( j - 2 == 4) {Row parentRow =  sheet.getRow(three[0]); Cell parentCell = parentRow.getCell(three[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(threeId);
//                            }
//                            if( j - 2 == 5) {Row parentRow =  sheet.getRow(four[0]); Cell parentCell = parentRow.getCell(four[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(fourId);
//                            }
//                            if( j - 2 == 6) {Row parentRow =  sheet.getRow(five[0]); Cell parentCell = parentRow.getCell(five[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(fiveId);
//                            }
//                        }
//
//                        break;
//                }
//            }
//            id++;
//            knowledges.add(knowledge);
//        }
//        knowledgeDao.save(knowledges);
//        System.out.println(knowledges.toString());
//    }
//
//    /**
//     *  确认row ！= null
//     * @param row
//     * @return
//     */
//    private boolean isRowNotNull(Row row) {
//        Iterator<Cell> icell = row.cellIterator();
//        while (icell.hasNext()) {
//            Cell ce = icell.next();
//            if (ce.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    /**
//     * 专题知识点导入 （八级知识点）
//     * @param fileName
//     * @throws Exception
//     */
//    public  void readExcel8(String fileName) throws Exception {
//        int[] one  = new int[2];
//        int[] tow  = new int[2];
//        int[] three  = new int[2];
//        int[] four  = new int[2];
//        int[] five  = new int[2];
//        int[] six  = new int[2];
//        int[] seven  = new int[2];
//        long oneId =0l,towId =0l,threeId=0l,fourId=0l,fiveId=0l,sixId=0l,sevenId=0l;
//        List<Knowledge> knowledges = new ArrayList<>();
//        boolean isE2007 = false;    //判断是否是excel2007格式
//        if(fileName.endsWith("xlsx"))
//            isE2007 = true;
//
//        InputStream input = new FileInputStream(fileName);  //建立输入流
//        Workbook wb  = null;
//        //根据文件格式(2003或者2007)来初始化
//        if(isE2007)
//            wb = new XSSFWorkbook(input);
//        else
//            wb = new HSSFWorkbook(input);
//        Sheet sheet = wb.getSheet(knowSheet);
//        long id = startId;
//        for(int i = 1 ; i < sheet.getLastRowNum() + 1; i++){
//
//
//            Row row = sheet.getRow(i);  //获得行数据
//            Knowledge knowledge = new Knowledge();
//            knowledge.setId(id);
//            knowledge.setSection(section);
//            knowledge.setSubject(subject);
//
//
//            //剔除行 为null的情况 begin
//            boolean rowNotNull = isRowNotNull(row);
//            if (!rowNotNull) {
//                continue;
//            }
//            //剔除行 为null的情况 end
//            boolean flag = true;
//            for(int j = 2 ; j < 20 ; j++){
//                Cell cell = row.getCell(j);
//                if (cell == null) {
//                    if(flag && knowledge.getName() != null){
//                        flag = false;
//                        if( j - 2 == 2) {Row parentRow =  sheet.getRow(one[0]); Cell parentCell = parentRow.getCell(one[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(oneId);
//                        }
//                        if( j - 2 == 3) {
//                            if(knowledge.getName().equals("<split-tag>原命题")){
//                                System.out.println(1);
//                            }
//                            Row parentRow =  sheet.getRow(tow[0]);
//                            Cell parentCell = parentRow.getCell(tow[1]);
//                            knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(towId);
//                        }
//                        if( j - 2 == 4) {Row parentRow =  sheet.getRow(three[0]); Cell parentCell = parentRow.getCell(three[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(threeId);
//                        }
//                        if( j - 2 == 5) {Row parentRow =  sheet.getRow(four[0]); Cell parentCell = parentRow.getCell(four[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(fourId);
//                        }
//                        if( j - 2 == 6) {Row parentRow =  sheet.getRow(five[0]); Cell parentCell = parentRow.getCell(five[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(fiveId);
//                        }
//                        if( j - 2 == 7) {Row parentRow =  sheet.getRow(six[0]); Cell parentCell = parentRow.getCell(six[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(sixId);
//                        }
//                        if( j - 2 == 8) {Row parentRow =  sheet.getRow(seven[0]); Cell parentCell = parentRow.getCell(seven[1]);knowledge.setParent(parentCell.getStringCellValue());
//                            knowledge.setParentId(sevenId);
//                        }
//                    }
//                    continue;
//                };
//                switch (cell.getCellType()){
//                    case HSSFCell.CELL_TYPE_NUMERIC:
//                        System.out.println(cell.getNumericCellValue());
//                        if( j - 1 == 1) {one[0] = i;one[1] = j;knowledge.setName(cell.getStringCellValue());flag = false;oneId = knowledge.getId();}
//                        if( j - 1 == 2) {tow[0] = i;tow[1] = j;knowledge.setName(cell.getStringCellValue());towId = knowledge.getId();}
//                        if( j - 1 == 3) {three[0] = i;three[1] = j;knowledge.setName(cell.getStringCellValue());threeId = knowledge.getId();}
//                        if( j - 1 == 4) {four[0] = i;four[1] = j;knowledge.setName(cell.getStringCellValue());fourId = knowledge.getId();}
//                        if( j - 1 == 5) {five[0] = i;five[1] = j;knowledge.setName(cell.getStringCellValue());fiveId = knowledge.getId();}
//                        if( j - 1 == 6) {six[0] = i;six[1] = j;knowledge.setName(cell.getStringCellValue());sixId = knowledge.getId();}
//                        if( j - 1 == 7) {seven[0] = i;seven[1] = j;knowledge.setName(cell.getStringCellValue());sevenId = knowledge.getId();}
//                        if( j - 1 == 8) {knowledge.setName(cell.getStringCellValue());}
//                        if( j - 1 == 10) {knowledge.setNumber(cell.getStringCellValue());}
//                        break;
//                    case HSSFCell.CELL_TYPE_STRING:
//                        System.out.println(cell.getStringCellValue());
//                        if( j - 1 == 1) {one[0] = i;one[1] = j;knowledge.setName(cell.getStringCellValue());flag = false;oneId = knowledge.getId();}
//                        if( j - 1 == 2) {tow[0] = i;tow[1] = j;knowledge.setName(cell.getStringCellValue());towId = knowledge.getId();}
//                        if( j - 1 == 3) {three[0] = i;three[1] = j;knowledge.setName(cell.getStringCellValue());threeId = knowledge.getId();}
//                        if( j - 1 == 4) {four[0] = i;four[1] = j;knowledge.setName(cell.getStringCellValue());fourId = knowledge.getId();}
//                        if( j - 1 == 5) {five[0] = i;five[1] = j;knowledge.setName(cell.getStringCellValue());fiveId = knowledge.getId();}
//                        if( j - 1 == 6) {six[0] = i;six[1] = j;knowledge.setName(cell.getStringCellValue());sixId = knowledge.getId();}
//                        if( j - 1 == 7) {seven[0] = i;seven[1] = j;knowledge.setName(cell.getStringCellValue());sevenId = knowledge.getId();}
//                        if( j - 1 == 8) {knowledge.setName(cell.getStringCellValue());}
//                        if( j - 1 == 10) {knowledge.setNumber(cell.getStringCellValue());}
//                        break;
//                    default:
//                        if(flag && knowledge.getName() != null){
//                            flag = false;
//                            if( j - 2 == 2) {Row parentRow =  sheet.getRow(one[0]); Cell parentCell = parentRow.getCell(one[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(oneId);
//                            }
//                            if( j - 2 == 3) {
//                                if(knowledge.getName().equals("<split-tag>原命题")){
//                                    System.out.println(1);
//                                }
//                                Row parentRow =  sheet.getRow(tow[0]);
//                                Cell parentCell = parentRow.getCell(tow[1]);
//                                knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(towId);
//                            }
//                            if( j - 2 == 4) {Row parentRow =  sheet.getRow(three[0]); Cell parentCell = parentRow.getCell(three[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(threeId);
//                            }
//                            if( j - 2 == 5) {Row parentRow =  sheet.getRow(four[0]); Cell parentCell = parentRow.getCell(four[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(fourId);
//                            }
//                            if( j - 2 == 6) {Row parentRow =  sheet.getRow(five[0]); Cell parentCell = parentRow.getCell(five[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(fiveId);
//                            }
//                            if( j - 2 == 7) {Row parentRow =  sheet.getRow(six[0]); Cell parentCell = parentRow.getCell(six[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(sixId);
//                            }
//                            if( j - 2 == 8) {Row parentRow =  sheet.getRow(seven[0]); Cell parentCell = parentRow.getCell(seven[1]);knowledge.setParent(parentCell.getStringCellValue());
//                                knowledge.setParentId(sevenId);
//                            }
//                        }
//
//                        break;
//                }
//            }
//            id++;
//            knowledges.add(knowledge);
//        }
//        knowledgeDao.save(knowledges);
//        System.out.println(knowledges.toString());
//    }
//
//
//}
