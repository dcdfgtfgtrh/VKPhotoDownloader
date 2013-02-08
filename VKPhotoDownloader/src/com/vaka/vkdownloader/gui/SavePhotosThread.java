package com.vaka.vkdownloader.gui;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaka.vkapi.ApiUtils;
import com.vaka.vkapi.VKAlbum;
import com.vaka.vkapi.VKException;
import com.vaka.vkapi.VKPhoto;
import com.vaka.vkdownloader.utils.Utils;

public class SavePhotosThread extends Thread {
	public SavePhotosThread(final List<Album> albums,final File pathFile){
		super(new Runnable(){

			@Override
			public void run() {
				Map<VKAlbum, List<VKPhoto>> map = new HashMap<VKAlbum, List<VKPhoto>>();
				for (Album alb : albums) {
					if (alb.isChecked) {
						try {
							map.put(alb.getAlbum(), ApiUtils
									.getPhotosByAlbum(Utils.api,
											alb.getAlbumID()));
						} catch (VKException e) {
							e.printStackTrace();
						}
					}
				}
				Utils.savePhotosToDrive(pathFile, map);		
			}
		});
	}
}
