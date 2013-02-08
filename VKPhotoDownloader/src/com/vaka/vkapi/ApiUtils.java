package com.vaka.vkapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class ApiUtils {

	public static List<VKPhoto> getAllPhotos(VKApi api) throws VKException {

		List<VKPhoto> photos = new LinkedList<VKPhoto>();
		JSONArray photosArray = new JSONArray();
		
		int offset = 0;
		do {
			try {
				photosArray = api.executeApiMethod(ApiMethod.GET_ALL_PHOTOS,
						"?count=100&offset=" + offset).getJSONArray("response");
				for (int i = 1; i < photosArray.length(); i++) {
					photos.add(new VKPhoto(photosArray.getJSONObject(i)));
				}
				offset += 100;
			} catch (JSONException e) {
				System.out.println("This is not a photo");
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				System.out.println("No connection?");
				e.printStackTrace();
				return null;
			}
		} while (photosArray.length() >= 100);
		
		return photos;
	}

	public static List<VKAlbum> getAlbums(VKApi api) throws VKException {
		
		List<VKAlbum> albums = new LinkedList<VKAlbum>();
		
		try {
			JSONArray albumsArray = api.executeApiMethod(ApiMethod.GET_ALBUMS,
					"?need_covers=1").getJSONArray("response");
			for (int i = 0; i < albumsArray.length(); i++) {
				albums.add(new VKAlbum((JSONObject) albumsArray.get(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return albums;
	}

	public static List<VKPhoto> getPhotosByAlbum(VKApi api, String aid)
			throws VKException {
		
		List<VKPhoto> photos = new LinkedList<VKPhoto>();
		
		try {
			JSONArray photosArray = api.executeApiMethod(ApiMethod.GET_PHOTOS_BY_ALBUM,
					"?uid=" + api.uid + "&aid=" + aid).getJSONArray("response");
			for (int i = 0; i < photosArray.length(); i++) {
				photos.add(new VKPhoto((JSONObject) photosArray.get(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return photos;
	}

	public static Map<VKAlbum, List<VKPhoto>> makeAlbumsPhotosMap(VKApi api) 
			throws VKException {
		
		Map<VKAlbum, List<VKPhoto>> map = new HashMap<VKAlbum, List<VKPhoto>>();
		List<VKAlbum> albums = getAlbums(api);
		
		Iterator<VKAlbum> albumIter = albums.iterator();
		while (albumIter.hasNext()) {
			VKAlbum album = albumIter.next();
			
			List<VKPhoto> photos = getPhotosByAlbum(api,
					String.valueOf(album.aid));
			map.put(album, photos);
		}
		//Add photos from wall
		VKAlbum service = new VKAlbum();
		service.title = "Фотографии со стены";
		List<VKPhoto> photos = getPhotosByAlbum(api, "profile");
		map.put(service, photos);
		
		return map;
	}
}
