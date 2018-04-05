@Override
    public void run() {
	try {
	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("lccCode", lccCode);
	    paramMap.put("sDate", sDate);
	    paramMap.put("bak_mpp2", bak_mpp2);
	    paramMap.put("bak_mpp3", bak_mpp3);
	    if ("001".equals(projectId)) {
		paramMap.put("projectId", "MPP1");
	    } else if ("006".equals(projectId)) {
		paramMap.put("projectId", "MPP2");
	    } else if ("007".equals(projectId)) {
		paramMap.put("projectId", "MPP3");
	    }

	    List<Map<String, String>> tubeList = scmExportMapper.getAllTubeList(paramMap);
	    if (tubeList.size() == 0) {
		return;
	    }
		//本地有模板excl 文件
	    HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(templatePath));
	    HSSFSheet sheet = wb.getSheetAt(0);

	    // 黄色背景CELLSTYLE
	    CellStyle cellStyle = wb.createCellStyle();
	    cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);

	    int i = 1;
	    checkSame(tubeList);
	    for (Map<String, String> m : tubeList) {
		i++;
		HSSFRow row = sheet.createRow(i);
		row.createCell(0).setCellValue(lccCode);
		row.createCell(1).setCellValue(m.get("BOX_CODE"));
		row.createCell(2).setCellValue(m.get("POSITION"));
		row.createCell(3).setCellValue(m.get("BOX_CODE") + "A");
		HSSFCell cell4 = row.createCell(4);
		cell4.setCellValue(m.get("TUBE_CODE_A"));
		if (!isEmpty(m.get("ERROR_A"))) {
		    cell4.setCellStyle(cellStyle);
		}
		row.createCell(5).setCellValue(m.get("ERROR_A"));
		row.createCell(6).setCellValue(m.get("ERROR_A_303"));
		row.createCell(7).setCellValue(m.get("ERROR_A_304"));
		row.createCell(8).setCellValue(m.get("ERROR_A_213"));
		row.createCell(9).setCellValue(m.get("ERROR_A_206"));
		row.createCell(10).setCellValue(m.get("ERROR_A_207"));
		row.createCell(11).setCellValue(m.get("ERROR_A_901"));

		row.createCell(12).setCellValue(m.get("BOX_CODE") + "E");
		HSSFCell cell7 = row.createCell(13);
		cell7.setCellValue(m.get("TUBE_CODE_E"));
		if (!isEmpty(m.get("ERROR_E"))) {
		    cell7.setCellStyle(cellStyle);
		}
		row.createCell(14).setCellValue(m.get("ERROR_E"));
		row.createCell(15).setCellValue(m.get("ERROR_E_303"));
		row.createCell(16).setCellValue(m.get("ERROR_E_304"));
		row.createCell(17).setCellValue(m.get("ERROR_E_213"));
		row.createCell(18).setCellValue(m.get("ERROR_E_206"));
		row.createCell(19).setCellValue(m.get("ERROR_E_207"));
		row.createCell(20).setCellValue(m.get("ERROR_E_901"));

		row.createCell(21).setCellValue(m.get("BOX_CODE") + "R");
		HSSFCell cell10 = row.createCell(22);
		cell10.setCellValue(m.get("TUBE_CODE_R"));
		if (!isEmpty(m.get("ERROR_R"))) {
		    cell10.setCellStyle(cellStyle);
		}
		row.createCell(23).setCellValue(m.get("ERROR_R"));
		row.createCell(24).setCellValue(m.get("ERROR_R_303"));
		row.createCell(25).setCellValue(m.get("ERROR_R_304"));
		row.createCell(26).setCellValue(m.get("ERROR_R_213"));
		row.createCell(27).setCellValue(m.get("ERROR_R_206"));
		row.createCell(28).setCellValue(m.get("ERROR_R_207"));
		row.createCell(29).setCellValue(m.get("ERROR_R_901"));

		row.createCell(30).setCellValue(m.get("BOX_CODE") + "W");
		HSSFCell cell13 = row.createCell(31);
		cell13.setCellValue(m.get("TUBE_CODE_W"));
		if (!isEmpty(m.get("ERROR_W"))) {
		    cell13.setCellStyle(cellStyle);
		}
		row.createCell(32).setCellValue(m.get("ERROR_W"));
		row.createCell(33).setCellValue(m.get("ERROR_W_303"));
		row.createCell(34).setCellValue(m.get("ERROR_W_304"));
		row.createCell(35).setCellValue(m.get("ERROR_W_213"));
		row.createCell(36).setCellValue(m.get("ERROR_W_206"));
		row.createCell(37).setCellValue(m.get("ERROR_W_207"));
		row.createCell(38).setCellValue(m.get("ERROR_W_901"));

		row.createCell(39).setCellValue(m.get("CHECKSAME"));

	    }

	    FileOutputStream fout = new FileOutputStream(zipDir + "/" + projectName + "_" + lccCode + "-" + lccName
		    + "-" + sDate + "扫码-" + tubeList.size() + "人份.xls");
	    wb.write(fout);
	    fout.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
