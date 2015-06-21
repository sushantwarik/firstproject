package com.atos.servlet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: FileUploadServlet
 *
 */
 public class FileUploadServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public FileUploadServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		System.out.println("Post Method Called");
		PrintWriter out = response.getWriter();
		String contentType = request.getContentType();
		String saveFile = "";
		try{
			if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
		 		DataInputStream in = new DataInputStream(request.getInputStream());
				//we are taking the length of Content type data
				int formDataLength = request.getContentLength();
				byte dataBytes[] = new byte[formDataLength];
				int byteRead = 0;
				int totalBytesRead = 0;
				//this loop converting the uploaded file into byte code
				while (totalBytesRead < formDataLength) {
					byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
					totalBytesRead += byteRead;
				}

				String file = new String(dataBytes);
				//for saving the file name
				saveFile = file.substring(file.indexOf("filename=\"") + 10);
				saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
				saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,saveFile.indexOf("\""));
				int lastIndex = contentType.lastIndexOf("=");
				String boundary = contentType.substring(lastIndex + 1,contentType.length());
				int pos;
				//extracting the index of file 
				pos = file.indexOf("filename=\"");
				pos = file.indexOf("\n", pos) + 1;
				pos = file.indexOf("\n", pos) + 1;
				pos = file.indexOf("\n", pos) + 1;
				int boundaryLocation = file.indexOf(boundary, pos) - 4;
				int startPos = ((file.substring(0, pos)).getBytes()).length;
				int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;

				ServletContext sc = getServletContext();
				String webInfFolderPath = sc.getRealPath("WEB-INF");
				String realFilePath = webInfFolderPath + File.separator + saveFile;
				if(new File(realFilePath).exists()){
					realFilePath += System.currentTimeMillis(); 
				}
				FileOutputStream fileOut = new FileOutputStream(realFilePath);
				fileOut.write(dataBytes, startPos, (endPos - startPos));
				fileOut.flush();
				fileOut.close();
			}
		} catch(IOException e){
			out.print("Error : " + e.getMessage());
		} catch (Exception e){
			out.print("Error : " + e.getMessage());
		}
	
	}   	  	    
}