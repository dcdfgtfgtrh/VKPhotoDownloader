package com.vaka.vkapi;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class VKAlbum {
    //"aid":"16178xxx"
    public String aid;
    //"thumb_id":"96509xxx"
    public int thumb_id;
    //"owner_id":"6xxx"
    public int owner_id;
    //"title":""
    public String title;
    //"description":""
    public String description;
    //"created":"1203967xxx"
    public int created;
    //"updated":"1238072xxx"
    public int updated;
    //"size":"3"
    public int size;
    //"privacy":"3"
    public int privacy;
    //"thumb_src":"http:\/\/cs9231.vkontakte.ru\/u06492\/100001227\/m_7875dxxx.jpg"
    public String thumb_src;
    
    public VKAlbum()
    {
	// TODO Auto-generated constructor stub
    }

    public VKAlbum(JSONObject object) throws JSONException
    {
	aid = object.getString("aid");
	owner_id = Integer.parseInt(object.getString("owner_id"));
	thumb_id = Integer.parseInt(object.getString("thumb_id"));
	title = object.getString("title");
	description = object.getString("description");
	created = Integer.parseInt(object.getString("created"));
	updated = Integer.parseInt(object.getString("updated"));
	privacy = Integer.parseInt(object.getString("privacy"));
	size = Integer.parseInt(object.getString("size"));
	thumb_src = object.has("thumb_src") ? object.getString("thumb_src") : null;
    }
}
