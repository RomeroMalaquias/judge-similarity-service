package br.ufal.ic

import br.ufal.ic.commons.ServerRPC
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
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
        def similarity
        try {
            def jsonSlurper = new JsonSlurper()
            similarity = jsonSlurper.parseText(message)
            if(similarity.code1 && similarity.code2 && similarity.threshold && similarity.threshold >= 0 && similarity.threshold <= 1) {
                def count1 = similarity.code1.split(' ').length
                def count2 = similarity.code2.split(' ').length
                def distance = distance(similarity.code1, similarity.code2)
                def result = ((count1 * count2 * distance)/count1) / 100
                if (1 - result > similarity.threshold) {
                    similarity['__response'] = 'SUSPECT'
                }
                similarity['__response'] = 'NOT_SUSPECT'
            } else {
                similarity['__response'] = "INVALID_FORMAT"
            }
        } catch (Exception e) {
            e.printStackTrace()
            similarity['__response'] = "INVALID_FORMAT"
        }
        return  JsonOutput.toJson(similarity)



    }

    public static void main(String[] argv) {
        SimilarityVerifier evaluatorServer = new SimilarityVerifier("EXCHANGE", "evaluator");


    }


}






