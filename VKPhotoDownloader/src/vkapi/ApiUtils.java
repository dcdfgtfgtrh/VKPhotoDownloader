package vkapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class ApiUtils {

	public static ArrayList<VKPhoto> getAllPhotos(VKApi api) throws VKException {

		ArrayList<VKPhoto> photos = new ArrayList<VKPhoto>();
		JSONArray photosArray = new JSONArray();
		int offset = 0;
		do {
			try {
				photosArray = api.apipost(ApiMethod.GET_ALL_PHOTOS,
						"?count=100&offset=" + offset).getJSONArray("response");
				for (int i = 1; i < photosArray.length(); i++) {
					photos.add(new VKPhoto(photosArray.getJSONObject(i)));
				}
				offset += 100;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("This is not a photo");
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("No connection?");
				e.printStackTrace();
				return null;
			}
		} while (photosArray.length() >= 100);
		return photos;
	}

	public static LinkedList<VKAlbum> getAlbums(VKApi api) throws VKException {
		LinkedList<VKAlbum> albums = new LinkedList<VKAlbum>();
		try {
			JSONArray albumsArray = api.apipost(ApiMethod.GET_ALBUMS,
					"?need_covers=1").getJSONArray("response");
			for (int i = 0; i < albumsArray.length(); i++) {
				albums.add(new VKAlbum((JSONObject) albumsArray.get(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return albums;
	}

	public static LinkedList<VKPhoto> getPhotosByAlbum(VKApi api, String aid)
			throws VKException {
		LinkedList<VKPhoto> photos = new LinkedList<VKPhoto>();
		try {
			JSONArray photosArray = api.apipost(ApiMethod.GET_PHOTOS_BY_ALBUM,
					"?uid=" + api.uid + "&aid=" + aid).getJSONArray("response");
			for (int i = 0; i < photosArray.length(); i++) {
				photos.add(new VKPhoto((JSONObject) photosArray.get(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return photos;
	}

	public static HashMap<VKAlbum, LinkedList<VKPhoto>> makeAlbumsPhotosMap(
			VKApi api) throws VKException {
		HashMap<VKAlbum, LinkedList<VKPhoto>> map = new HashMap<VKAlbum, LinkedList<VKPhoto>>();
		LinkedList<VKAlbum> albums = getAlbums(api);
		Iterator<VKAlbum> albumIter = albums.iterator();
		while (albumIter.hasNext()) {
			VKAlbum album = albumIter.next();
			LinkedList<VKPhoto> photos = getPhotosByAlbum(api,
					String.valueOf(album.aid));
			map.put(album, photos);
		}
		VKAlbum service = new VKAlbum();
		service.title = "Фотографии со стены";
		LinkedList<VKPhoto> photos = getPhotosByAlbum(api, "profile");
		map.put(service, photos);
		return map;
	}
}
