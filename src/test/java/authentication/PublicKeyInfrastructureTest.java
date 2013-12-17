/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package authentication;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 */
public class PublicKeyInfrastructureTest {
    
    public PublicKeyInfrastructureTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of authenticateUser method, of class PublicKeyInfrastructure.
     */
    @Test
    public void testAuthenticateUser() {
        System.out.println("authenticateUser");
        String user = "";
        PublicKeyInfrastructure instance = new PublicKeyInfrastructure();
        boolean expResult = true;
        boolean result = instance.authenticateUser(user);
        assertEquals(expResult, result);

    }
}
