package com.company.inventory.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.company.inventory.model.Client;
import com.company.inventory.model.Product;

public class ClientExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Client> client;
	
	public ClientExcelExporter (List<Client> clients) {
		this.client = clients;
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Resultado");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		
		createCell(row, 0, "Nombre", style);
		createCell(row, 1, "Apellido", style);
		createCell(row, 2, "Email", style);
		createCell(row, 3, "Documento de Identidad", style);
	}
	
	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if(value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		
		cell.setCellStyle(style);
	}
	
	private void writeDataLines() {
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		
		for(Client result: client) {
			int columnCount = 0;
			Row row = sheet.createRow(rowCount++);
			createCell(row, columnCount++, result.getName(), style);
			createCell(row, columnCount++, result.getLastname(), style);
			createCell(row, columnCount++, result.getEmail(), style);
			createCell(row, columnCount++, String.valueOf(result.getCi()), style);
		}
	}
	
	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine(); // escribir cabecera
		writeDataLines(); // escribir datos
		
		ServletOutputStream servletOutput = response.getOutputStream();
		workbook.write(servletOutput);
		workbook.close();
		servletOutput.close();
	}
}
