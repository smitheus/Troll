package za.co.sb.Troll.gui.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils 
{
	public static final int COMPLETE = 1;
	public static final int ERRORED = 2;
	public static final int ABORTED = 3;
	public static final int INTERRUPTED = 4;

	public static final String ONLINE_IMAGE = "online.png";
	public static final String OFFLINE_IMAGE = "offline.png";

	public static void loadImages() 
	{
		loadImage(ONLINE_IMAGE);
		loadImage(OFFLINE_IMAGE);
	}

	public static void loadImage(String fileName) 
	{
		try 
		{
			BufferedImage icon = ImageIO.read(ImageUtils.class.getClassLoader().getResourceAsStream(fileName));
			preload(icon);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public static int preload(Image image) 
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// Check if already loaded
		if ((toolkit.checkImage(image, -1, -1, null) & ImageObserver.ALLBITS) != 0)
		{
			return COMPLETE;
		}

		Object lock = new Object();
		synchronized (lock) 
		{
			while (true) 
			{
				ImageLoadObserver observer = new ImageLoadObserver(lock);
				toolkit.prepareImage(image, -1, -1, observer);
				int result = toolkit.checkImage(image, -1, -1, null);
				
				if ((result & ImageObserver.ALLBITS) != 0)
				{
					return COMPLETE;
				}
					
				if ((result & ImageObserver.ERROR) != 0)
				{
					return ERRORED;
				}
				
				if ((result & ImageObserver.ABORT) != 0)
				{
					return ABORTED;
				}

				try 
				{
					lock.wait();
					return observer.getResult();
				} 
				catch (InterruptedException e) 
				{
					return INTERRUPTED;
				}
			}
		}
	}

	/**
	 * Starts loading the given images, returns only when all the images are
	 * done loading. If you just need to preload one Image, use the
	 * preload(Image) method instead.
	 * 
	 * @return An array specifying the loading result of each image. Possible
	 *         values are {@link #COMPLETE}, {@link #ERRORED} and
	 *         {@link #ABORTED}.
	 * 
	 * @see #preload(Image)
	 */
	public static int[] preload(Image[] images, int[] results) 
	{
		Object[] locks = new Object[images.length];
		ImageLoadObserver[] loadObservers = new ImageLoadObserver[images.length];
		
		if ((results == null) || (results.length < images.length))
		{
			results = new int[images.length];
		}
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		for (int i = 0; i < images.length; i++) 
		{
			locks[i] = new Object();
			loadObservers[i] = new ImageLoadObserver(locks[i]);
			toolkit.prepareImage(images[i], -1, -1, loadObservers[i]);
		}

		for (int i = 0; i < images.length; i++) 
		{
			synchronized (locks[i]) 
			{
				int result = toolkit.checkImage(images[i], -1, -1, null);

				if ((result & ImageObserver.ALLBITS) != 0) 
				{
					results[i] = COMPLETE;
					continue;
				}
				
				if ((result & ImageObserver.ERROR) != 0) 
				{
					results[i] = ERRORED;
					continue;
				}
				
				if ((result & ImageObserver.ABORT) != 0) 
				{
					results[i] = ABORTED;
					continue;
				}

				try 
				{
					locks[i].wait();
					results[i] = loadObservers[i].getResult();
				} 
				catch (InterruptedException e) 
				{
					results[i] = INTERRUPTED;
				}
			}
		}

		return results;
	}

	/**
	 * Returns whether the specified image is already fully loaded.
	 */
	public static boolean isLoaded(Image image) 
	{
		return (Toolkit.getDefaultToolkit().checkImage(image, -1, -1, null) & ImageObserver.ALLBITS) != 0;
	}

	/**
	 * This class is an implementation of ImageObserver which notifies a given
	 * lock when loading of the Image is done. This can be used to wait until a
	 * certain Image has finished loading.
	 */
	public static class ImageLoadObserver implements ImageObserver 
	{

		/**
		 * The lock.
		 */
		private final Object lock;

		/**
		 * The loading result.
		 */
		private int result = -1;

		/**
		 * Creates a new ImageLoadObserver which will notify the given lock when
		 * the Image is done loading.
		 */
		public ImageLoadObserver(Object lock) {
			this.lock = lock;
		}

		/**
		 * If infoflags has the ALLBITS flag set, notifies the lock and returns
		 * false, otherwise simply returns true.
		 */
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
		{
			synchronized (lock) 
			{
				if ((infoflags & ALLBITS) != 0) 
				{
					result = ImageUtils.COMPLETE;
					lock.notify();
					return false;
				}
				
				if ((infoflags & ERROR) != 0) 
				{
					result = ImageUtils.ERRORED;
					lock.notify();
					return false;
				}
				
				if ((infoflags & ABORT) != 0) 
				{
					result = ImageUtils.ABORTED;
					lock.notify();
					return false;
				}
			}
			return true;
		}

		/**
		 * Returns the result of the image loading process or -1 if loading
		 * hasn't finished yet. Possible values are
		 * {@link ImageUtilities#COMPLETE}, {@link ImageUtilities#ERRORED} and
		 * {@link ImageUtilities#ABORTED}
		 */
		public int getResult() 
		{
			return result;
		}
	}
}
