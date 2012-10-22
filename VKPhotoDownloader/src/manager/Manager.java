package manager;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream.GetField;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vkapi.ApiMethod;
import vkapi.VKAlbum;
import vkapi.VKApi;
import vkapi.VKException;
import vkapi.VKPhoto;

public class Manager
{
    public final VKApi api;

    public Manager(String login, String pass)
    {
	api = new VKApi(login, pass);
    }

    /*
     * public static void main(String... args) throws IOException { Manager
     * manager = new Manager(); String token = manager.api.doLogin();
     * System.out.println("ACCESS_TOKEN=" + token); ArrayList<VKPhoto> photos =
     * manager.getAllPhotos();
     * 
     * manager.savePhotosToDrive("download/", photos);
     * 
     * }
     */
    public void savePhotosToDrive(File dir,
	    HashMap<VKAlbum, LinkedList<VKPhoto>> map)
    {
	JFrame frame = new JFrame("Сохраняю...");
	JLabel label = new JLabel("Логин успешен!");
	frame.add(label);
	frame.setLocation(frame.getToolkit().getScreenSize().width / 2, frame
		.getToolkit().getScreenSize().height / 2);
	frame.setSize(new Dimension(200, 100));
	frame.setVisible(true);
	Set<VKAlbum> albums = map.keySet();
	for (VKAlbum album : albums)
	{
	    LinkedList<VKPhoto> photos = map.get(album);
	    File albumDir = new File(dir.getPath()+"/"+fixString(album.title));
	    albumDir.mkdirs();
	    for (VKPhoto photo : photos)
	    {
		File file = new File(albumDir.getPath() + "/"
			    + "photo_" + photo.pid + ".jpg");
		if (file.exists()){System.out.println("Skipping: "+file.toString());continue;}
		String photoURL;
		URL url = null;
		if (photo.src_xxbig != null)
		{
		    photoURL = photo.src_xxbig;
		} else
		{
		    photoURL = photo.src_big;
		}
		try
		{
		    label.setText("Сохраняю фото" + photos.indexOf(photo)
			    + " из " + photos.size());
		    url = new URL(photoURL);
		    InputStream is = url.openStream();
		    OutputStream os = new FileOutputStream(file);
		    byte[] b = new byte[1024];
		    int length;
		    while ((length = is.read(b)) != -1)
		    {
			os.write(b, 0, length);
		    }
		    is.close();
		    os.close();
		} catch (MalformedURLException e)
		{
		    System.out.println("Not proper url!");
		    e.printStackTrace();
		} catch (IOException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	frame.setVisible(false);
	frame = null;

    }
    private String fixString(String str)
    {
	char[] chars = new char[]{'/','\\','*',':','?','"','<','>','|'};
	for (char c : chars)
	{
	    str = str.replace(c, ' ');
	}
	return str;
    }
}
