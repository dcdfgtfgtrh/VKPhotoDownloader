package com.vaka.vkdownloader.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.vaka.vkapi.VKAlbum;
import com.vaka.vkapi.VKApi;
import com.vaka.vkapi.VKException;
import com.vaka.vkapi.VKPhoto;
import com.vaka.vkdownloader.gui.ProgressFrame;


public class Utils {
	public final static VKApi api;
	// public static Color blue = new Color(44,110,213);
	public static Color blue = new Color(68, 126, 218);

	static {
		api = new VKApi();
	}

	//TODO: Get rid of these 2 methods!
	public static boolean doLogin(String login, String pass) throws IOException,
			VKException {
		api.doLogin(login, pass);
		return api.isLogged();
	}

	public static boolean doLogout() {
		api.doLogout();
		return api.isLogged();
	}

	/* TODO
	 * Separate GUI code!
	 */
	public static void savePhotosToDrive(File dir,
			Map<VKAlbum, List<VKPhoto>> map) throws IOException {
		ProgressFrame frame = new ProgressFrame(getTotalPhotos(map));
		for (VKAlbum album : map.keySet()) {
			List<VKPhoto> photos = map.get(album);
			File albumDir = new File(dir.getPath() + "/"
					+ fixString(album.title));
			albumDir.mkdirs();
			
			for (VKPhoto photo : photos) {
				File file = new File(albumDir.getPath() + "/" + "photo_"
						+ photo.pid + ".jpg");
				
				if (file.exists()) {
					frame.next();
					try{
						Thread.sleep(10);
					}catch (InterruptedException e){}
					continue;
				}
				
				String photoURL;
				if (photo.src_xxbig != null) {
					photoURL = photo.src_xxbig;
				} else {
					photoURL = photo.src_big;
				}
				
				try {
					frame.next();
					URL url = new URL(photoURL);
					InputStream is = url.openStream();
					OutputStream os = new FileOutputStream(file);
					byte[] b = new byte[1024];
					int length;
					while ((length = is.read(b)) != -1) {
						os.write(b, 0, length);
					}
					is.close();
					os.close();
				} catch (MalformedURLException e) {
					System.out.println("Not proper url!");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		frame.setVisible(false);
	}

	private static String fixString(String str) {
		char[] chars = new char[] { '/', '\\', '*', ':', '?', '"', '<', '>',
				'|' };
		for (char c : chars) {
			str = str.replace(c, ' ');
		}
		return str;
	}
	
	private static int getTotalPhotos(Map<VKAlbum, List<VKPhoto>> map) {
		int count = 0;
		for (VKAlbum album : map.keySet()) {
			List<VKPhoto> photos = map.get(album);
			count+=photos.size();
		}
		return count;
	}
}
