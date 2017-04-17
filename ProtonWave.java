import greenfoot.*;
import java.util.*;
/**
 * A proton wave that expands and destroys asteroids in its path.
 * 
 * @author Michael Kölling
 * @version 0.1
 */
public class ProtonWave extends Actor
{
    /** The damage this wave will deal */
    private static final int DAMAGE = 30;
    
    /** How many images should be used in the animation of the wave */
    private static final int NUMBER_IMAGES= 30;
    
    private int currentImage = 0;
    
    /** 
     * The images of the wave. This is static so the images are not
     * recreated for every object (improves performance significantly).
     */
    private static GreenfootImage[] images;
    
    /**
     * Create a new proton wave.
     */
    public ProtonWave() 
    {
        initializeImages();
        
        setImage( images[0] );
        Greenfoot.playSound("proton.wav");
    }
    
    /** 
     * Create the images for expanding the wave.
     */
    public static void initializeImages() 
    {
        if(images == null) 
        {
            GreenfootImage baseImage = new GreenfootImage("wave.png");
            images = new GreenfootImage[NUMBER_IMAGES];
            int i = 0;
            while (i < NUMBER_IMAGES) 
            {
                int size = (i+1) * ( baseImage.getWidth() / NUMBER_IMAGES );
                images[i] = new GreenfootImage(baseImage);
                images[i].scale(size, size);
                i++;
            }
        }
    }
    
    /**
     * grow will increase the size of the proton wave until it reaches maximum size,
     * at which point it will remove itself from the world.
     * @param There are no parameters.
     * @return Nothing is returned.
     */
    private void grow()
    {
        setImage( images[ currentImage] );
        currentImage++;
        
        if( currentImage >= NUMBER_IMAGES )
        {
            getWorld().removeObject(this);
        }
    }
    
    /**
     * Act for the proton wave is: grow and check whether we hit anything.
     */
    public void act()
    {
        checkCollision();
        grow();
    }
    
    /**
     * chechCollision sees if our proton wave has touch an asteroid and deals damage to each asteroid it touches.
     * @param There are no parameters.
     * @return Nothing is returned.
     */
    private void checkCollision()
    {
        int range = getImage().getWidth()/2;
        List<Asteroid> allAsteroids = getObjectsInRange( range, Asteroid.class );
        
        for(int i = 0; i < allAsteroids.size();i++)
        {
            allAsteroids.get(i).hit(DAMAGE);
        }
    }
}
