package com.bdcor.util.test;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.mortbay.log.Log;

import com.bdcor.util.pdf2img.ServiceMain;
import com.bdcor.util.pdf2img.client.Template2PdfClient;
public class Template2PdfServlet extends HttpServlet {

	public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

   
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8"); 
        // 这个路径相对当前应用的目录
        String fileName = null;
        String filePath = null;
        String mapvalue = null;
        String df= System.getProperty("user.dir");
        File uploadDir = new File(df);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        try {
            // 解析请求的内容提取文件数据
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                	if(item.isFormField()){ //普通域,获取页面参数
                        String field = item.getFieldName();
	                     if(field.equals("mapvalue")){
	                    	 mapvalue = item.getString();
	                         continue;
	                     }
                	}
                    if (!item.isFormField()) {
                        fileName = new File(item.getName()).getName();
                        filePath = df + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                    }
                }
            }
        } catch (Exception ex) {
        	Log.debug(ex.toString());
        }
	    String[] strArray=mapvalue.split("\\;");
	    Map<CharSequence, CharSequence> map = new HashMap<CharSequence, CharSequence>();
	    for (String temp : strArray) {
	        String[] tempArray=temp.split("=");
	        map.put(tempArray[0].trim(), tempArray[1].trim());
	    }
	    String ip = StartJetty.servletip;
		Template2PdfClient client = new Template2PdfClient(ip, null);
		ByteBuffer bytes = client.convert(new File( filePath), map);
		byte[] bufferp = bytes.array();
		//FileUtils.writeByteArrayToFile(new File(fileName), bytes.array());
		response.setHeader("content-disposition", "attachment;filename="+fileName);
        OutputStream out = response.getOutputStream();
        out.write(bufferp);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
    	
    }
	
}
