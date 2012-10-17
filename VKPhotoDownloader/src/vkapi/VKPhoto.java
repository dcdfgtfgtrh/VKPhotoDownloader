package vkapi;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class VKPhoto
{

    // "pid":"146771xxx"
    public int    pid;
    // "aid":"100001xxx"
    public int    aid;
    // "owner_id":"6492",
    public int    owner_id;
    // "src":"http:\/\/cs9231.vkontakte.ru\/u06492\/100001227\/m_7875dxxx.jpg",
    public String src;
    // "src_big":"http:\/\/cs9231.vkontakte.ru\/u06492\/100001227\/x_cd563xxx.jpg",
    public String src_big;
    // "src_small":"http:\/\/cs9231.vkontakte.ru\/u06492\/100001227\/s_c3bbaxxx.jpg",
    public String src_small;
    // "src_xbig":"http:\/\/cs9231.vkontakte.ru\/u06492\/100001227\/y_62a74xxx.jpg",
    public String src_xbig;
    // "src_xxbig":"http:\/\/cs9231.vkontakte.ru\/u06492\/100001227\/z_793e9xxx.jpg",
    public String src_xxbig;
    // "text":"test",
    public String text;
    // "created":"1260885xxx"
    public int    created;

    public VKPhoto()
    {
	// TODO Auto-generated constructor stub
    }

    public VKPhoto(JSONObject object) throws JSONException
    {
	pid = Integer.parseInt(object.getString("pid"));
	aid = Integer.parseInt(object.getString("aid"));
	owner_id = Integer.parseInt(object.getString("owner_id"));
	src = object.getString("src");
	src_big = object.getString("src_big");
	src_small = object.getString("src_small");
	src_xbig = object.has("src_xbig") ? object.getString("src_xbig") : null;
	src_xxbig = object.has("src_xxbig") ? object.getString("src_xxbig")
		: null;
	text = (String) object.opt("text");
	created = Integer.parseInt(object.getString("created"));
    }

}
