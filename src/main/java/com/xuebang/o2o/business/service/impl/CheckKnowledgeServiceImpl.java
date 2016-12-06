package com.xuebang.o2o.business.service.impl;

import com.xuebang.o2o.business.dao.KnowledgeDao;
import com.xuebang.o2o.business.dao.SysnKnowledgeDao;
import com.xuebang.o2o.business.entity.KnowBaseInfo;
import com.xuebang.o2o.business.entity.Knowledge;
import com.xuebang.o2o.business.entity.SyncKnowBaseInfo;
import com.xuebang.o2o.business.entity.SysnKnowledge;
import com.xuebang.o2o.business.service.CheckKnowledgeService;
import com.xuebang.o2o.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2016/11/9.
 */
@Service
@Transactional
public class CheckKnowledgeServiceImpl implements CheckKnowledgeService {

    CellStyle styleYellow = null;


    CellStyle styleRed = null;
    @Autowired
    private KnowledgeDao knowledgeDao;

    @Autowired
    private SysnKnowledgeDao sysnKnowledgeDao ;


    @Override
    public boolean check(String filePath) throws IOException {
        boolean isE2007 = false;    //判断是否是excel2007格式
        if (filePath.endsWith("xlsx"))
            isE2007 = true;

        InputStream input = new FileInputStream(filePath);  //建立输入流
        Workbook wb = null;
        //根据文件格式(2003或者2007)来初始化
        if (isE2007)
            wb = new XSSFWorkbook(input);
        else
            wb = new HSSFWorkbook(input);

        //设置背景色为红色
        CellStyle style = wb.createCellStyle();
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.RED.index);

        styleRed = wb.createCellStyle();
        styleRed.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleRed.setFillForegroundColor(HSSFColor.RED.index);

        styleYellow = wb.createCellStyle();
        styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleYellow.setFillForegroundColor(HSSFColor.YELLOW.index);

        /** 检查结果*/
        Boolean checkRresult = true;

        /** 所有知识点的map*/
        Map<String , List >  allKnows = new HashMap<>();

        boolean flag = true;

        //读取所有的sheet页
        /** 规定第一个sheet页必须是专题知识点  */
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            boolean tempResult = true;
            boolean sheetHidden = wb.isSheetHidden(i);

            if (sheetHidden) {
                continue;
            }
            System.out.println("sheetHidden = " + sheetHidden);

            //存储每一个sheet所有的知识点名称，给规则一 判断有没有重复名字的知识点
            Set<String> sheetAllknows = new HashSet<>();
            //同步知识点的处理
            tempResult = syncKnowledgeCheck(wb.getSheetAt(i), style, allKnows , sheetAllknows );


            if (checkRresult) {
                checkRresult = tempResult;
            }
        }

        /** 确认检查结果是否正确,正确就不输出错误批注的副本*/
        if ( !checkRresult ) {
            String filePathPrefix = filePath.substring( 0,filePath.lastIndexOf(".") );
            String filePathSuffix = filePath.substring(filePath.lastIndexOf(".") );
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(filePathPrefix+"错误"+filePathSuffix);
                wb.write(fout);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            if (PropertiesUtils.getIntValue( "isImportSql") == 1 ) {
                for (Map.Entry<String, List> entry : allKnows.entrySet()) {
                    List<SysnKnowledge> sysnKnowledges = entry.getValue();

                    for (SysnKnowledge sysnKnowledge : sysnKnowledges) {

                        sysnKnowledgeDao.saveAndFlush(sysnKnowledge);
                        sysnKnowledgeDao.flush();
                    }

                }
            }
        }

        return checkRresult;
    }




    /**
     *  获取知识点序号
     *  获取 某行某列的值   因为序号有可能为 数字 类型 所有要做判断
     * @param row  行
     * @param j   列index
     * @return
     */

    private String cellValue(Row row, int j) {
        if( row == null || row.getCell( j )  == null ){
            return null;
        }

        if ( row.getCell(j).getCellType() == HSSFCell.CELL_TYPE_NUMERIC ) {
            return  String.valueOf(  row.getCell( j ).getNumericCellValue() ).trim();
        }
        try {
            return row.getCell(j).getStringCellValue().trim();

        }catch ( Exception e){
            System.out.println("1");
            row.getCell(j).setCellStyle( styleRed );
        }
        return null;
    }

    /**
     * 获取列头的值
     * @param j
     * @param sheet
     * @return
     */
    private String columnHeadValue(int j , Sheet sheet) {
        Row row = sheet.getRow(0);
        if ( row == null || row.getCell(j) == null) {
            return null;
        }

        return row.getCell(j).getStringCellValue().trim();
    }

    private Integer subStringXuhao( String s) {
        String xuhao = s;// 例 ： 1.1.1.4 元素与集合的关系, 获取 尾数4
        String val = xuhao.substring(xuhao.lastIndexOf(".")+1);
        try {
            Integer result =  Integer.valueOf( val );
            return result ;
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }
        return null;
    }


    /**
     * 确实一行的所有知识点名是否都是null
     * @param row
     * @param name2position
     * @return
     */
    private boolean rowIsNull(Row row , Map<String , Integer> name2position){
        for( Map.Entry<String , Integer> entry : name2position.entrySet() ){
            String val =  cellValue( row ,entry.getValue().intValue() );
            if( StringUtils.isNotBlank(val) ){
                return  false;
            }
        }
        return  true;
    }

            /**==================================================以上是公共方法===================================================================*/
            /**====================================================华丽的分割线=====================================================================*/
            /**==================================================以下是同步知识点===================================================================*/
    private Boolean syncKnowledgeCheck(Sheet sheet ,CellStyle style , Map<String , List >  allKnows ,Set<String> sheetAllknows) {
        SyncKnowBaseInfo syncKnowBaseInfo = initSyncKnowBaseInfoParam(sheet, style);

        System.out.println("同步知识点 《列号》 数据注册成功 ------->" + syncKnowBaseInfo.toString());

        List<SysnKnowledge> sysnKnowledges = new ArrayList<>() ;

        //检查结果
        Boolean checkRresult = true;

        /** 检查知识点*/
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i) ;
            if( row == null || rowIsNull(row , syncKnowBaseInfo.getName2position() )){
                continue;
            }

            boolean knowNameCellTotalNull = isNnowNameCellTotalNull(syncKnowBaseInfo.getNameColPostion(), row);
            if( knowNameCellTotalNull ){
                continue;
            }

            //添加check规则
            /** 规则一  start*/
            boolean tempResult = true;
            Map<String , Object>  result =   syncknowNameCount(style, syncKnowBaseInfo, row , sheetAllknows );
            tempResult = (Boolean) result.get( "checkRresult");
            if( checkRresult ){
                checkRresult = tempResult;
            }
            /** 规则一  end*/

            /** 规则二 start*/
//            checkRresult = checkTwo(sheet, style, allKnows, syncKnowBaseInfo, checkRresult, i, row, result);
            /** 规则二 end*/

        }


        if ( checkRresult ) {
            /**导入知识点*/
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);

                if( row == null || rowIsNull(row , syncKnowBaseInfo.getName2position() )){
                    continue;
                }

                boolean knowNameCellTotalNull = isNnowNameCellTotalNull(syncKnowBaseInfo.getNameColPostion(), row);
                if( knowNameCellTotalNull ){
                    continue;
                }

                int flagNumer = 0;
                SysnKnowledge sysnKnowledge = new SysnKnowledge();
                for (int j = 0; j < (int) row.getLastCellNum() + 1; j++) {
                    flagNumer = registerSyncKnowledge(sheet, syncKnowBaseInfo, row, flagNumer, sysnKnowledge, j ,allKnows);
    //                registerKnowledge();
                }
                if( flagNumer != 0 ){
                    syncKnowBaseInfo.getPosition2Objec().put( flagNumer , sysnKnowledge);
                }else {
                    System.out.println( "行位置为 "+ i+1 + "，出现错误，请检查 ");
                }
                sysnKnowledges.add( sysnKnowledge );
            }

            allKnows.put( "syncknows" + sheet.getSheetName() , sysnKnowledges );
        }


        return checkRresult;
    }

    /**
     * 同步知识点check 规则二 ： 如果存在专题知识点则检查本知识点和专题知识点是否都是叶子节点
     * @param sheet    错误的样式
     * @param style     错误的样式
     * @param allKnows         所有对象的map
     * @param syncKnowBaseInfo   同步知识点映射
     * @param row             当前row
     * @param result    规则一 返回的map    存储了 规则一检查的结果  和  知识点名称的位置
     * @return
     */
    private Boolean checkTwo(Sheet sheet, CellStyle style, Map<String, List> allKnows, SyncKnowBaseInfo syncKnowBaseInfo, Boolean checkRresult, int rowIndex, Row row, Map<String, Object> result) {
        boolean tempResult;
        int nextRowIndex = rowIndex +1;
        Row nextRow = sheet.getRow( nextRowIndex );
        while ( isNnowNameCellTotalNull(syncKnowBaseInfo.getNameColPostion() , nextRow )){
            nextRowIndex ++;
            nextRow = sheet.getRow( nextRowIndex );
            if ( nextRow == null ){
                break;
            }
        }
        tempResult = checkKnowledgeNumberAndName( style , syncKnowBaseInfo , row ,nextRow, allKnows , (Integer)result.get( "syncKnowNamePosition") );
        if( checkRresult ){
            checkRresult = tempResult;
        }
        return checkRresult;
    }

    private int registerSyncKnowledge(Sheet sheet, SyncKnowBaseInfo syncKnowBaseInfo, Row row, int flagNumer, SysnKnowledge sysnKnowledge, int j ,Map<String , List >  allKnows) {
        if (j == syncKnowBaseInfo.getSection()) { /**列号  == 学段*/
            sysnKnowledge.setSectionId( cellValue( row , j ));
        } else if (j == syncKnowBaseInfo.getSubject()) {/**列号 == 学科*/
            sysnKnowledge.setSubjectId( cellValue( row , j) );
        } else if (j == syncKnowBaseInfo.getBookVersion()) {/**列号 == 书本*/
            sysnKnowledge.setBookVersion( cellValue( row , j) );
        }else if (j == syncKnowBaseInfo.getPublishVersion()) {/**列号 == 教材版本*/
            sysnKnowledge.setPublishVersion( cellValue( row , j) );
        }else if ( syncKnowBaseInfo.getName2position().containsKey(columnHeadValue( j, sheet)) ) {/**列号 == 知识点名称的列号*/
            String cellValue = cellValue( row , j );
            if ( ! StringUtils.isBlank( cellValue)) {/**列数据不为空*/
                flagNumer = j ;
                if ( cellValue.indexOf( "<split-tag>" ) > -1) {//剔除<split-tag>
                    cellValue = cellValue.replace("<split-tag>","");
                }

                /** 保证行的知识点不全为空  start*/
                int nextRowIndex = row.getRowNum() + 1;
                Row nextRow = sheet.getRow( nextRowIndex );
                while ( isNnowNameCellTotalNull(syncKnowBaseInfo.getNameColPostion() , nextRow )){
                    nextRowIndex ++;
                    nextRow = sheet.getRow( nextRowIndex );
                    if ( nextRow == null ){
                        break;
                    }
                }
                /** 保证行的知识点不全为空  end*/
                //设置是否为叶子节点
                isSyncLeaf(syncKnowBaseInfo, sysnKnowledge, j , nextRow , row);

                //设置排序
                sysnKnowledge.setSort( subStringXuhao(cellValue.split(" ")[0]) );

                String[] cellValArray = cellValue.split(" ");
                if ( cellValArray.length == 1 ){
                    cellValArray = cellValue.split(" ");//中文空格
                }
                sysnKnowledge.setNumber( cellValArray[0] );
                try {
                    sysnKnowledge.setName(cellValArray[1]);
                } catch (Exception e) {
                    System.out.printf("空格，有误");
                    row.getCell(j).setCellStyle(styleRed);

                }
                Integer parentKey = j - 1;
                /**取出父级知识点*/
                if ( syncKnowBaseInfo.getPosition2Objec().containsKey( parentKey ) ){
                    sysnKnowledge.setParent(  syncKnowBaseInfo.getPosition2Objec().get( parentKey) );
                }
            }

        }

        return flagNumer;
    }

    /**
     * 是否是叶子节点
     * @param syncKnowBaseInfo
     * @param sysnKnowledge
     * @param j
     * @param nextRow
     */
    private void isSyncLeaf(SyncKnowBaseInfo syncKnowBaseInfo, SysnKnowledge sysnKnowledge, int j, Row nextRow ,Row row ) {
        //判断是否是叶子节点
        // 当column +　1  不是知识点名字的列时 || 下一行的当前列之前的所有知识点名称的有一个不为null，当前知识点就是叶子节点    前提： 下一列的所有知识点的名称都不为空
        boolean nextRowCellIsNotNull = nextRowCellIsNotNull( syncKnowBaseInfo.getNameColPostion() , nextRow , j );

        //if nextRow > 最后一行 则, nextRowCellIsNotNull = true;
        if( row.getSheet().getLastRowNum() == row.getRowNum() ){
            nextRowCellIsNotNull = true ;
        }

        if( !syncKnowBaseInfo.getNameColPostion().contains(j+1) ||  nextRowCellIsNotNull ){
            sysnKnowledge.setIsLeaf( 1 );
        }else{
            sysnKnowledge.setIsLeaf( 0 );
        }

    }

    /**
     * 判断知识点名称所在的列是否全部是null
     * @param nameColPostion
     * @param row
     * @return
     */
    private boolean isNnowNameCellTotalNull(List <Integer> nameColPostion, Row row) {
//        if ( row == null ) {
//            return false;
//        }
        boolean knowNameCellTotalNull = true;
        for (Integer colPos : nameColPostion){
            String value =  cellValue( row , colPos );
            if ( !StringUtils.isBlank( value) ){
               return   false;
            }
        }
        return  true;
    }

    /**
     * 同步知识点check 规则二 ： 如果存在专题知识点则检查本知识点和专题知识点是否都是叶子节点
     * @param style     错误的样式
     * @param syncKnowBaseInfo   同步知识点映射
     * @param row             当前row
     * @param nextRow         下一行的row
     * @param allKnows         所有对象的map
     * @param syncKnowNamePosition    当前行的知识点名称的列的位置
     * @return
     */
    private boolean checkKnowledgeNumberAndName(CellStyle style, SyncKnowBaseInfo syncKnowBaseInfo, Row row ,Row nextRow,Map<String , List >  allKnows , Integer syncKnowNamePosition) {
       // 1. 如果专题知识点名称和序号都为null，返回true
        String knowNumber =  cellValue( row ,syncKnowBaseInfo.getKnowledgeNumber() );
        String knowName = cellValue( row , syncKnowBaseInfo.getKnowName() );
        if( StringUtils.isBlank( knowNumber ) && StringUtils.isBlank( knowName) ){
            return true;
        }

        // 2. 如果专题知识点序号 和  名称只有其中一个为空，则返回false
        if( StringUtils.isBlank( knowNumber ) && !StringUtils.isBlank( knowName) ){
            bgColorRedCell(style, syncKnowBaseInfo.getKnowName(), row);
            return false;
        }
        if( !StringUtils.isBlank( knowNumber ) && StringUtils.isBlank( knowName) ){
            bgColorRedCell(style, syncKnowBaseInfo.getKnowName(), row);
            return false;
        }

        // 3. （ 当前列为叶子节点【下一行的此列之前的所有知识点列有一个不为null || 当前列列位置+1不是知识点名称】 ） && 专题知识点名称和序号也是叶子节点，return true； 此处有一个前提：保证下一行不是整个知识点名字的列都为 null
        List< Knowledge > knows = allKnows.get( "knows" );
        boolean isKnowLeaf =   isKnowLeaf(knowNumber, knowName, knows);
        if(  "<split-tag>增长率问题".equals( knowName )){
            System.out.println(1);
        }
        boolean nextRowCellIsNotNull = nextRowCellIsNotNull(syncKnowBaseInfo.getNameColPostion(), nextRow, syncKnowNamePosition);

        //  if row 是最后一行  nextRowCellIsNotNull = true
        if ( row.getSheet().getLastRowNum() == row.getRowNum()  ){
            nextRowCellIsNotNull = true;
        }

        if(  (nextRowCellIsNotNull ||  ( ! syncKnowBaseInfo.getNameColPostion().contains( syncKnowNamePosition + 1 )) ) && isKnowLeaf){
            return  true ;
        }else{
            bgColorRedCell(style, syncKnowBaseInfo.getKnowName(), row);
            bgColorRedCell(style, syncKnowBaseInfo.getKnowledgeNumber(), row);
            return  false;
        }

    }

    /***
     * 下一行的此列之前的所有知识点列有一个不为null
     * @param nameColPostion            所有知识点列的  列号
     * @param nextRow                   下一行
     * @param syncKnowNamePosition      当前列
     * @return
     */
    private Boolean nextRowCellIsNotNull(List<Integer> nameColPostion, Row nextRow, Integer syncKnowNamePosition) {
        String nextRowNextCell =  null ;
        Integer cellIndex =  nameColPostion.indexOf( syncKnowNamePosition );
        for( int h = 0 ; h < cellIndex + 1 ; h++ ){
            nextRowNextCell = cellValue( nextRow , nameColPostion.get( h ) );
            if ( !StringUtils.isBlank( nextRowNextCell )){
                return  true;
            }
        }
        return false;
    }

    private void bgColorRedCell(CellStyle style, Integer cellIndex , Row row) {
        try {
            row.getCell( cellIndex).setCellStyle( style);
        } catch (Exception e) {
            row.createCell(  cellIndex ).setCellStyle( style );
        }
    }

    /**
     * 确认专题知识点是否是叶子节点
     * @param knowNumber
     * @param knowName
     * @param knows
     * @return
     */
    private boolean isKnowLeaf(String knowNumber, String knowName, List<Knowledge> knows) {
        for ( Knowledge know : knows ){
            Knowledge parent = know.getParent();
            if ( parent == null ) {
                continue;
            }
            if( knowNumber.equals( parent.getName() ) && knowName.equals( parent.getNumber())){
                return false;
            }
        }
        return  true;
    }

    /**
     * 同步知识点check 规则一 ： 同一行不能有两个知识点名称
     * @param style
     * @param syncKnowBaseInfo
     * @param row
     * @param sheetAllknows 一个sheet里面的所有知识点
     * @return
     */
    private Map<String , Object> syncknowNameCount(CellStyle style, SyncKnowBaseInfo syncKnowBaseInfo, Row row , Set<String> sheetAllknows) {
        Map<String , Object > result = new HashMap<>();

        Boolean checkRresult = true;
        int flag = 0;
        Integer syncKnowNamePosition = 0;//知识点名称的列位置   规则二需要用到
        for (Integer colPos : syncKnowBaseInfo.getNameColPostion()){
            String value =  cellValue( row , colPos);
            if(! StringUtils.isBlank( value)){
                syncKnowNamePosition = colPos;
                flag ++;
                if ( flag > 1 ){
                    bgColorRedCell(style, colPos , row);
//                    row.getCell(colPos).setCellStyle( style );
                    checkRresult = false;
                    System.out.println( " 行row = " + row.getRowNum() + " 有多个知识点，请检查!" );
                }
                if ( sheetAllknows.contains( value ) ) {
                    row.getCell( colPos ).setCellStyle( style );
                    System.out.println( "知识点《"+ value  +"》有重复，请检查" );
                    checkRresult = false ;
                    sheetAllknows.add( value );
                }else {
                    sheetAllknows.add( value );
                }
                value = value.replace("<split-tag>","");
                if( value.indexOf( " ")  == -1  && value.indexOf(" " ) == -1){
                    bgColorRedCell(styleYellow, colPos , row);
                    row.getCell(colPos).setCellStyle( styleYellow );
                    checkRresult = false;
                    System.out.println( " 行row = " + row.getRowNum() + " 序号与知识点中间没有空格" );
                }


            }
        }
        result.put( "checkRresult" , checkRresult );
        result.put( "syncKnowNamePosition" , syncKnowNamePosition);
        return  result;

    }

    private SyncKnowBaseInfo initSyncKnowBaseInfoParam(Sheet sheet, CellStyle style) {
        SyncKnowBaseInfo syncKnowBaseInfo = new SyncKnowBaseInfo();
        Row oneRow = sheet.getRow(0);
        if( oneRow == null ){
            throw new RuntimeException(sheet.getSheetName()+"有误，请检查");
        }
        for (int i = 0; i < oneRow.getLastCellNum(); i++) {
            Cell cell = oneRow.getCell(i);
            if( cell == null ){
                continue;
            }
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    String value = cell.getStringCellValue();

                    syncKnowBaseInfoRegister(syncKnowBaseInfo, i, value);
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    cell.setCellStyle(style);
                    System.out.println("首行出错，不应该有数字类型的数据");
                    break;
            }
        }
        return syncKnowBaseInfo;
    }

    private void syncKnowBaseInfoRegister(SyncKnowBaseInfo syncKnowBaseInfo, int i, String value) {
          if( value.trim() != null ){
            if( value.trim().indexOf("学段") > -1 || value.trim().indexOf("级章节") > -1 || value.trim().indexOf("专题知识点序号") > -1 ||  value.trim().indexOf("科目") > -1 || value.trim().indexOf("编号") > -1
                    || value.trim().indexOf("专题知识点") > -1|| value.trim().indexOf("书本") > -1|| value.trim().indexOf("教材版本") > -1 ){

            }else{
                throw new RuntimeException("列头：<"+value.trim()+">有误,请检查");
            }
        }
        switch (value.trim()) {
            case "学段":
                syncKnowBaseInfo.setSection(i);
                break;
            case "科目":
                syncKnowBaseInfo.setSubject(i);
                break;
            case "书本":
                syncKnowBaseInfo.setBookVersion(i);
                break;
            case "教材版本":
                syncKnowBaseInfo.setPublishVersion(i);
                break;
            case "专题知识点序号":
                syncKnowBaseInfo.setKnowledgeNumber(i);
                break;
            case "专题知识点":
                syncKnowBaseInfo.setKnowName(i);
                break;
            case "一级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "二级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "三级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "四级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "五级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "六级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "七级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "八级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "九级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
            case "十级章节":
                if (syncKnowBaseInfo.getName2position().containsKey(value.trim())) {
                    System.out.println(value.trim() + "---->存在多个");
                } else {
                    syncKnowBaseInfo.getName2position().put(value.trim(), i);
                    syncKnowBaseInfo.getNameColPostion().add( i );
                }
                break;
        }

    }


    public static void main(String[] args) {
        KnowBaseInfo knowBaseInfo = new KnowBaseInfo();
        knowBaseInfo.setSection(33);
        knowBaseInfo.setSubject(33);
        Boolean checkResult = true;
        initaa(knowBaseInfo, 2 ,checkResult);
        System.out.println( checkResult );

        System.out.println(knowBaseInfo.getSection().toString() + "  " + knowBaseInfo.getSubject());

        String filePath = "F:\\excel\\bbb.xlsx";

        String filePathPrefix = filePath.substring( 0,filePath.lastIndexOf(".") );
        String filePathSuffix = filePath.substring(filePath.lastIndexOf(".") );

        System.out.println( filePathPrefix +"----------> "+ filePathSuffix);

        String val = "<split-tag>1.1 集合";
        System.out.println( val.indexOf( "<split-tag>") > -1);
        System.out.println( val.replace( "<split-tag>" ,""));
    }

    private static void initaa(KnowBaseInfo knowBaseInfo, int a ,Boolean checkResult) {
        knowBaseInfo.setSection(72);
        knowBaseInfo.setSubject(22);
        System.out.println(a);
        checkResult = false;

    }

}
