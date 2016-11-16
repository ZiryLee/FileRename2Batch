package me.ziry.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class AlertPhotoUtil {

	public int alterCount = 0;
	public int errorCount = 0;
	public int sum = 0;
	
	public  List<String> errorNameList = new ArrayList<String>();

	/**
	 * 获取文件名以拍摄日期加随机数组成的文件名
	 * 
	 * @param imgFile
	 * @return
	 * @throws JpegProcessingException
	 * @throws IOException 
	 */
	public  String getImgFileDate(File imgFile) {
		
		Directory exif = null;
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(imgFile);
			exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		} catch (JpegProcessingException | IOException e) {
//			e.printStackTrace();
		}
		
		String imgDate = null;
		
		if(exif != null) {
			imgDate = exif.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		}
		
		if(imgDate == null || imgDate.length() == 0 ) {
			
			long time = imgFile.lastModified();//返回文件最后修改时间，是以个long型毫秒数
			imgDate = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss").format(new Date(time));
			
			/*
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			imgDate = format.format(date);
			*/
		}
		imgDate = imgDate.replaceAll(":", "").replaceAll(" ", "_") + "_" + sum;
		return imgDate;
	}

	/**
	 * 获取当前文件名后缀
	 * 
	 * @param imgFile
	 * @return
	 */
	public  String gerImgFileSuffix(File imgFile) {
		String imgname = imgFile.getName();
		return imgname.substring(imgname.lastIndexOf('.') + 1);
	}

	/**
	 * 重名文件名
	 * 
	 * @param imgFile
	 * @param newname
	 * @return
	 */
	public  boolean alterImgFileName(File imgFile, String newname) {
		String rootPath = imgFile.getParent();
		File newFile = new File(rootPath + File.separator + newname);
		return imgFile.renameTo(newFile);
	}

	public void start(File imgFile) {
		if (imgFile.isFile()) {
			sum++;
			try {
				this.alterImgFileName( imgFile, this.getImgFileDate(imgFile)+ "." + this.gerImgFileSuffix(imgFile));
				alterCount++;
			} catch (Exception e) {
				errorNameList.add(imgFile.getName() +" error : "+e);
				e.printStackTrace();
				errorCount++;
			} 
		} else {

			File[] tempList = imgFile.listFiles();
			for (int i = 0; i < tempList.length; i++) {
				this.start(tempList[i]);
			}

		}

	}
}
