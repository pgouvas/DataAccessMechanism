/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

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
public class PolicyRepositoryTest {
    
    public PolicyRepositoryTest() {
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
     * Test of performCredentialsAuthorization method, of class PolicyRepository.
     */
    //@Test
    public void testPerformCredentialsAuthorization() {
        System.out.println("performCredentialsAuthorization");
        PolicyRepository instance = new PolicyRepository();
        instance.setSparql("select ?namedgraph where { ?accpolicy ?b ?c. }");
        instance.performCredentialsAuthorization();

    }
}
