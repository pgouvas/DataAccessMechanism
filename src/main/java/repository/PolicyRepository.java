package repository;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PolicyRepository
{
    private String url = "http://127.0.0.1:3030/policies/query";
    private LinkedList<String> dimensions = null;
    private LinkedList<String> userTriples = null;    
    private String sparql = null;
    private String policySparql = null;     


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }    
    
    public String getPolicySparql() {
        return policySparql;
    }

    public void setPolicySparql(String policySparql) {
        this.policySparql = policySparql;
    }
    
    public String getSparql() 
    {
        return sparql;        
    }

    /*
    public void setSparql(String sparql) 
    {
        String tmpString = null;
        String[] tmpTriples = null;
        this.sparql = new String(sparql);
        
        tmpString = sparql.substring(sparql.indexOf("{") + 1, sparql.indexOf("}"));
        tmpTriples = tmpString.trim().split("dimension");
        dimensions = new LinkedList<String>();
        
        for(int i=1;i<tmpTriples.length;i++)
        {
            System.out.println("---- " + tmpTriples[i] + " " + tmpTriples[i].split(" ")[1].trim());
            dimensions.add(tmpTriples[i].split(" ")[1].trim());
        }
        
    }
   */
    
    public void setSparql(String sparql) 
    {
        String tmpString = null;
        String[] tmpTriples = null;
        this.sparql = new String(sparql);
        
        tmpString = sparql.substring(sparql.indexOf("{") + 1, sparql.indexOf("}"));
        tmpTriples = tmpString.trim().split("l2s-dim");
        dimensions = new LinkedList<String>();
        
        for(int i=1;i<tmpTriples.length;i++)
        {
            System.out.println("---- " + " l2s-dim" + tmpTriples[i].split(" ")[0].trim());
            dimensions.add("l2s-dim" + tmpTriples[i].split(" ")[0].trim());
        }
        
    }    

    public void getUserProfileFromQuery()
    throws MissingUserProfileException
    {
        String tmpString = null;
        String[] tmpTriples = null;
        String property = null;
        String value = null;
        String userProfileString[] = null;

        userProfileString = sparql.split("user_profile");

        if(userProfileString.length<2)
            throw new MissingUserProfileException();
        else
        {
            tmpString = userProfileString[1];
            tmpString = tmpString.substring(tmpString.indexOf("{") + 1, tmpString.indexOf("}"));
            tmpTriples = tmpString.trim().split("lmds");
            userTriples = new LinkedList<String>();

            System.out.println(tmpTriples.length);
            if(tmpTriples.length<=1)
                throw new MissingUserProfileException();
            else
            {
                for(int i=1;i<tmpTriples.length;i++)
                {
                    property = new String(tmpTriples[i].split(" ")[0].trim());
                    //System.out.println(i + " p=" + property);
                    value = new String(tmpTriples[i].split(" ")[1].trim());
                    //System.out.println(i + " v=" + value);
                    value = value.substring(value.indexOf("\""), value.indexOf("."));

                    /*
                    System.out.println("---- " + " lmds " + tmpTriples[i].split(" ")[0].trim() 
                            + tmpTriples[i].split(" ")[1].trim().substring(i, i));
                    */

                    System.out.println(property + " " + value);
                    userTriples.add(property + " " + value);
                }
            }
        }
    }    

    private void incorporateDimensions()
    {
        for(String dim : dimensions)
        {
            this.policySparql += "?condition c:hasDimension " + dim  + ". ";
        }        
    }
    
    private void incorporatePolicy()
    {
        for(String dim : userTriples)
        {
            this.policySparql += "?profile c" + dim  + ". ";
        }        
    }    
    
    private void formPolicingQuery()
    {
        this.policySparql = "prefix c: <http://www.linked2safety.eu/lmds2#> ";
        this.policySparql += "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
        this.policySparql += "prefix acl: <http://www.w3.org/ns/auth/acl#> ";
        this.policySparql += "prefix l2s-dim:<http://www.linked2safety-project.eu/properties/> ";

        this.policySparql += "select ?graphuri ?sparqlendpoint where { ";
        this.policySparql += "?accpolicy a c:AccessPolicy. ";
        this.policySparql += "?accpolicy c:appliesToNamedGraph ?namedgraph. ";
        this.policySparql += "?namedgraph c:hasGraphURI ?graphuri. ";
        this.policySparql += "?namedgraph c:hasSparqlEndpoint ?sparqlendpoint. ";

        
        this.policySparql += "?accpolicy c:grantsAccess acl:Read_l2s. ";

        this.policySparql += "?accpolicy c:hasCondition ?condition. ";
        incorporateDimensions();
        this.policySparql += "?accpolicy c:hasUserProfile ?profile.";
        incorporatePolicy();
        
        this.policySparql += " }";
    }   

    public LinkedList<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(LinkedList<String> dimensions) {
        this.dimensions = dimensions;
    }

    public ResultSet performCredentialsAuthorization()
    {
        ResultSet results = null;
        
        this.formPolicingQuery();
        System.out.println(this.policySparql);
        Query qr = QueryFactory.create(this.policySparql);
        QueryExecution x = QueryExecutionFactory.sparqlService(this.url, qr);
        results = x.execSelect();

        return results;
    }
    
    public static void main(String[] args)
    {
        ResultSet results = null;
        PolicyRepository instance = new PolicyRepository();
        
        instance.setSparql("prefix lmds: <http://www.linked2safety.eu/lmds2#> \n" +
                            "SELECT ?diabetes ?bmi ?hypertension\n" +
                                "WHERE {\n" +
                                "?instance a qb:Observation .\n" +
                                "?instance l2s-dim:hasHypertension ?diabetes.\n" +
                                "?instance l2s-dim:BMI ?bmi.\n" +
                                "?instance l2s-dim:patientHasDiabetes ?hypertension.\n" +
                                "?instance <http://www.linked2safety-project.eu/measure/Cases> ?cases.\n" +
                                "}\n" + 
                                " user_profile = { \n"+
                                "<http://www.l2s.com/userprofile> lmds:hasLocation \"Greece\".\n" +
                                "<http://www.l2s.com/userprofile> lmds:hasWorkingArea \"Oncology\".\n" +
                                " }");
        try {
            instance.getUserProfileFromQuery();
            results = instance.performCredentialsAuthorization();
                    
            while(results.hasNext())
            {
                QuerySolution binding = results.nextSolution();
                System.out.println(" instance :" + binding.get("graphuri").toString() + " " + binding.get("sparqlendpoint").toString());
            }
        
        } catch (MissingUserProfileException ex) {
            Logger.getLogger(PolicyRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
