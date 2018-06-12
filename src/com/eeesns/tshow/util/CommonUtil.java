package com.eeesns.tshow.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

	public static void imgScaleRequest(String inPath, String outPath, int width,
			HttpServletRequest request) {
		String rootPath = request.getServletContext().getRealPath("/");
		File file = new File(rootPath + inPath);// 读入文件
		Image img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			RuntimeException re = new RuntimeException("缩放图片异常");
			throw re;
		} // 构造Image对象
			// 写入图片
		if (img.getWidth(null) < width) {
			width = img.getWidth(null);
		}
		resize(width, img, rootPath + outPath);
	}

	/**
	 * 按照给定的宽度等比例缩放图片
	 * 
	 * @param inPath
	 *            原图路径
	 * @param outPath
	 *            缩放后的图片路径
	 * @param width
	 *            给定的宽度
	 */
	public static void imgScale(String inPath, String outPath, int width) {
		File file = new File(inPath);// 读入文件
		Image img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			RuntimeException re = new RuntimeException("缩放图片异常");
			throw re;
		} // 构造Image对象
			// 写入图片
		resize(width, img, outPath);
	}

	/**
	 * 根据宽度等比例缩放图片
	 * 
	 * @param width
	 * @param img
	 */
	private static void resize(int w, Image img, String outImgPath) {
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		int h = height * w / width;
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		outImage(outImgPath, image);
	}

	private static void outImage(String outImgPath, BufferedImage newImg) {
		// 判断输出的文件夹路径是否存在，不存在则创建
		File file = new File(outImgPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}// 输出到文件流
		try {
			ImageIO.write(newImg, outImgPath.substring(outImgPath.lastIndexOf(".") + 1), new File(
					outImgPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
