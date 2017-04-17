import greenfoot.*;

/**
 * A rocket that can be controlled by the arrowkeys: up, left, right.
 * The gun is fired by hitting the 'space' key. 'z' releases a proton wave.
 * 
 * @author Poul Henriksen
 * @author Michael Kölling
 * 
 * @version 1.1
 */
public class Rocket extends SmoothMover
{
    private static final int gunReloadTime = 5;         // The minimum delay between firing the gun.
    private static final int WAVE_RELOAD_TIME = 500;
    
    private int reloadDelayCount;               // How long ago we fired the gun the last time.
    private int waveDelayCount;
    private int hitDelay;
    private int healDelayCount;
    
    private GreenfootImage rocket = new GreenfootImage("rocket.png");    
    private GreenfootImage rocketWithThrust = new GreenfootImage("rocketWithThrust.png");
    
    /**
     * Initialise this rocket.
     */
    public Rocket()
    {
        Vector startMotion = new Vector( getRotation(), 0.7 );
        addToVelocity(startMotion);
        
        reloadDelayCount = 5;
        
        waveDelayCount = 500;
        
        hitDelay = 30;
        
        healDelayCount = 500;
    }

    /**
     * Do what a rocket's gotta do. (Which is: mostly flying about, and turning,
     * accelerating and shooting when the right keys are pressed.)
     */
    public void act()
    {
        HealthBar healthBar = getWorld().getObjects(HealthBar.class).get(0);
        
        move();
        checkKeys();
        reloadDelayCount++;
        waveDelayCount++;
        hitDelay++;
        healDelayCount++;
        
        int index = 0;
        if ( isTouching(Asteroid.class) )
        {
            if(hitDelay >= 60)
            {
                healthBar.add(-200);
                hitDelay = 0;
            }
        }
        
        if( isTouching(HealthPack.class))
        {
            healthBar.add(100);
            removeTouching(HealthPack.class);
        }
        
        
        checkCollision();
    }
    
    /**
     * Check whether there are any key pressed and react to them.
     */
    private void checkKeys() 
    {
        if( Greenfoot.isKeyDown("space") ) 
        {
            fire();
        }
        
        if( Greenfoot.isKeyDown("left") )
        {
            turn(-5);
        }
        
        if( Greenfoot.isKeyDown("right") )
        {
            turn(5);
        }
        
        if(Greenfoot.isKeyDown("z"))
        {
            startProtonWave();
        }
        
        if(Greenfoot.isKeyDown("x"))
        {
            startHeal();
        }
        ignite( Greenfoot.isKeyDown("up") );
    }
    
    /**
     * Fire a bullet if the gun is ready.
     */
    private void fire() 
    {
        if (reloadDelayCount >= gunReloadTime) 
        {
            Bullet bullet = new Bullet (getVelocity(), getRotation());
            getWorld().addObject (bullet, getX(), getY());
            bullet.move ();
            reloadDelayCount = 0;
        }
    }
    
    /**
     * startProtonWave creates a new ProtonWave object and adds it to the world if the charge has been reloaded.
     * @param There are no parameters.
     * @return Nothing is returned.
     */
    private void startProtonWave()
    {
        if( waveDelayCount >= WAVE_RELOAD_TIME)
        {
            ProtonWave wave = new ProtonWave();
            getWorld().addObject( wave, getX(), getY() );
            waveDelayCount = 0;
        }
    }
    
    /**
     * startHeal adds health points to your healthbar when x is pressed.
     * @param There are no parameters.
     * @return Nothing is returned.
     */
    private void startHeal()
    {
         if( healDelayCount >= WAVE_RELOAD_TIME)
        {
            HealthPack pack = new HealthPack();
            getWorld().addObject( pack, getX(), getY() );
            healDelayCount = 0;
        }
    }
    
    /**
     * ignite will change the image of the rocket to show boost when we are moving forward.
     * @param boosterOn tells us whether the booster image should be shown or not.
     * @return Nothing is returned.
     */
    private void ignite( boolean boosterOn )
    {
        if( boosterOn == true )
        {
            setImage( rocketWithThrust );
            addToVelocity( new Vector( getRotation(), 0.15 ) );
        }
        else
        {
            setImage( rocket );
        }
    }
    
    /**
     * checkCollision will check if an asteroid has hit our rocket and create an explosion accordingly.
     * @param There are no parameters.
     * @return Nothing is returned.
     */
    private void checkCollision()
    {
        Space world = (Space)getWorld();
        
        Asteroid currentAsteroid = (Asteroid)getOneIntersectingObject(Asteroid.class);
        
        HealthBar healthBar = getWorld().getObjects(HealthBar.class).get(0);
        if( healthBar.getCurrent() ==  0)
        {
            world.addObject( new Explosion(), getX(), getY() );
            world.removeObject( this );
            world.gameOver();
        }
    }
    
    
}