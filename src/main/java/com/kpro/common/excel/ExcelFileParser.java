package com.kpro.common.excel;

import com.amazonaws.util.StringUtils;
import com.kpro.common.exception.BaseException;
import com.kpro.common.exception.ErrorMessage;
import com.kpro.common.exception.ErrorObject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class ExcelFileParser<E extends BatchBaseEntity> extends FileParser<E> {

    protected abstract Iterator<Row> getDataRows(InputStream fis, int maxTransaction) throws IOException;

    @Override
    public ParseResult parse(JpaRepository<E, Object> repository, Function<Row, E> toEntity, String id, String filePath, int maxTransaction, String language) {
        int totalLine = 0;
        int importLine = 0;
        int errorLine = 0;
        int rowId = 1;
        try(InputStream fis = new FileInputStream(filePath);) {
            Row row;
            List<E> entityList = new LinkedList<>();
            Iterator<Row> rowIterator = this.getDataRows(fis, maxTransaction);

            //remove header when file have header 1 line
            if (rowIterator.hasNext()) {
                rowIterator.next();
                totalLine++;
            }

            while (rowIterator.hasNext()) {
                totalLine++;
                row = rowIterator.next();
                E entity = toEntity.apply(row);

                if (entity == null) {
                    continue;
                }

                //save data if max size in list
                if (entityList.size() >= 1000) {
                    repository.saveAll(entityList);
                    importLine += entityList.size();
                    entityList.clear();
                }

                //count line err
                if (ParserStatus.FAIL.toString().equals(entity.getVerifyStatus())) {
                    errorLine++;
                }

                entity.setOrderNum(rowId);
                rowId++;

                entityList.add(entity);
            }
            //if file is empty or only header
            if (!entityList.isEmpty()) {
                repository.saveAll(entityList);
                importLine += entityList.size();
                entityList.clear();
            }
            return buildParseSuccessResult(totalLine, importLine, errorLine);
        } catch (Exception e) {
            return null;
        }
    }

    private ParseResult buildParseSuccessResult(int totalLine, int importLine, int errorLine) {
        ParseResult parseResult = new ParseResult();
        parseResult.setTotalLine(totalLine);
        parseResult.setImportedLine(importLine);
        parseResult.setVerifyTotalCnt(totalLine);
        parseResult.setVerifyFailedCnt(errorLine);
        return parseResult;
    }

    protected String getValueFromCell(Cell cell) {
        if (Objects.isNull(cell)) {
            return null;
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
            case STRING -> StringUtils.trim(cell.getStringCellValue());
            default -> StringUtils.trim(cell.toString());
        };
    }

    protected Iterator<Row> getDataRowsXlsx(InputStream fis, int maxTransaction) throws IOException {
        XSSFSheet sheet =  new XSSFWorkbook(fis).getSheetAt(0);
        validateMaxLine(maxTransaction, sheet.getPhysicalNumberOfRows());
        return sheet.iterator();
    }

    protected Iterator<Row> getDataRowsXls(InputStream fis, int maxTransaction) throws IOException {
        HSSFSheet sheet = new HSSFWorkbook(fis).getSheetAt(0);
        validateMaxLine(maxTransaction, sheet.getPhysicalNumberOfRows());
        return sheet.iterator();
    }

    private void validateMaxLine(int maxLine, int totalLine) {
        if (totalLine > (maxLine + 1)) {
            String message = "error max transaction ...";
            String messageVi = "error max transaction ...";
            ErrorObject error = new ErrorObject();
            error.setErrorCode("PARSER_001");
            error.setErrorMessage(new ErrorMessage(message, messageVi));
            throw new BaseException(error);
        }
    }
}
