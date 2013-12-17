package repository;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import java.util.LinkedList;

public class UpdateCategories
{
    private String url = "http://127.0.0.1:3030/l2s/query";
    private LinkedList<String> dimensions = null;
    private String sparql = null;

    public UpdateCategories()
    {
        this.sparql = new String("select ?a ?b\n" +
                "where\n" +
                "{\n" +
                " ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2000/01/rdf-schema#Datatype>.\n" +
                " ?a <http://www.w3.org/2000/01/rdf-schema#comment> ?b.\n" +
        "}");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }    
    
    public String getSparql() 
    {
        return sparql;        
    }


    public ResultSet retrieveCategories()
    {
        ResultSet results = null;
        
        System.out.println(this.sparql);
        Query qr = QueryFactory.create(this.sparql);
        QueryExecution x = QueryExecutionFactory.sparqlService(this.url, qr);
        results = x.execSelect();
        
        while(results.hasNext())
        {
            QuerySolution binding = results.nextSolution();
            System.out.println(" instance :" + binding.get("b").toString() );
        }
        
        return null;
    }
    
    public static void main(String[] args)
    {
        UpdateCategories u = new UpdateCategories();
        u.retrieveCategories();
    }
}
