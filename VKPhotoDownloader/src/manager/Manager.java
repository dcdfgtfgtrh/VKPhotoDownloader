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

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.me.JSONArray;
import org.json.me.JSONException;

import vkapi.ApiMethod;
import vkapi.VKApi;
import vkapi.VKException;
import vkapi.VKPhoto;

public class Manager
{
    VKApi api;
    String photosTotal;
    
    public Manager(String login,String pass)
    {
	api = new VKApi(login, pass);
    }
 /*   public static void main(String... args) throws IOException
    {
	Manager manager = new Manager();
	String token = manager.api.doLogin();
	System.out.println("ACCESS_TOKEN=" + token);
	ArrayList<VKPhoto> photos = manager.getAllPhotos();
	
	manager.savePhotosToDrive("download/", photos);

    }
*/
    public ArrayList<VKPhoto> getAllPhotos() throws VKException
    {
	if (api.isLogged()!=true)
	{
	    try
	    {
		api.doLogin();
	    } catch (IOException e)
	    {
		System.out.println("Internet?");
		e.printStackTrace();
	    }
	}
	ArrayList<VKPhoto> photos = new ArrayList<VKPhoto>();
	JSONArray photosArray = new JSONArray();
	int offset = 0;
	do
	{
	    try
	    {
		photosArray = api.apipost(ApiMethod.GET_ALL_PHOTOS,
			"?count=100&offset=" + offset).getJSONArray("response");
		if (offset==0){photosTotal=photosArray.getString(0);}
		for (int i = 1; i < photosArray.length(); i++)
		{
		    photos.add(new VKPhoto(photosArray.getJSONObject(i)));
		}
		offset += 100;
	    } catch (JSONException e)
	    {
		// TODO Auto-generated catch block
		System.out.println("This is not a photo");
		e.printStackTrace();
		return null;
	    } catch (IOException e)
	    {
		// TODO Auto-generated catch block
		System.out.println("No connection?");
		e.printStackTrace();
		return null;
	    }
	} while (photosArray.length() >= 100);
	return photos;
    }
    
    public void savePhotosToDrive (File dir, ArrayList<VKPhoto> photos)
    {
	JFrame frame = new JFrame("Сохраняю...");
	JLabel label = new JLabel("Логин успешен!");
	frame.add(label);
	frame.setLocation(frame.getToolkit().getScreenSize().width / 2, frame.getToolkit()
		.getScreenSize().height / 2);
	frame.setSize(new Dimension(200,100));
	frame.setVisible(true);
	for (VKPhoto photo : photos)
	{
	    String photoURL;
	    URL url=null;
	    if (photo.src_xxbig!=null)
	    {
		photoURL=photo.src_xxbig;
	    }else {photoURL=photo.src_big;}
	    try
	    {
		label.setText("Сохраняю фото"+photos.indexOf(photo)+" из "+photosTotal);
		url = new URL(photoURL);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(dir.getPath()+"/"+"photo_"+photo.pid+".jpg");
		byte[] b = new byte[1024];
		int length;
		while ((length = is.read(b))!=-1)
		{
		    os.write(b,0,length);
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
	frame.setVisible(false);
	frame=null;
    }
}
