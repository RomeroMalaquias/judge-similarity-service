package br.ufal.ic

import br.ufal.ic.commons.ServerRPC
import org.codehaus.jackson.map.ObjectMapper

import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.SimpleScriptContext


class SimilarityVerifier extends ServerRPC {
    SimilarityVerifier(String exchangeName, String key) {
        super(exchangeName, key)
    }

    def distance(String str1, String str2) {
        def dist = new int[str1.size() + 1][str2.size() + 1]
        (0..str1.size()).each { dist[it][0] = it }
        (0..str2.size()).each { dist[0][it] = it }

        (1..str1.size()).each { i ->
            (1..str2.size()).each { j ->
                dist[i][j] = [dist[i - 1][j] + 1, dist[i][j - 1] + 1, dist[i - 1][j - 1] + ((str1[i - 1] == str2[j - 1]) ? 0 : 1)].min()
            }
        }
        return dist[str1.size()][str2.size()]
    }

    String doWork (String message){
        Similarity similarity =  new ObjectMapper().readValue(message, Similarity.class);
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        StringWriter writer = new StringWriter(); //ouput will be stored here


        ScriptContext context = new SimpleScriptContext();
        context.setWriter(writer); //configures output redirection

       return distance(similarity.code1, similarity.code2)

    }

    public static void main(String[] argv) {
        SimilarityVerifier evaluatorServer = new SimilarityVerifier("EXCHANGE", "evaluator");


    }


}






