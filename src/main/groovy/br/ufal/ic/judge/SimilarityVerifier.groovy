package br.ufal.ic.judge

import br.ufal.ic.judge.commons.ServerRPC
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

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
            def aux, auxIndex, auxTotal = []

            similarity = jsonSlurper.parseText(message)
            similarity.eachWithIndex { element1, i ->
                auxIndex = []
                aux = []
                similarity.eachWithIndex { element2, j ->
                    if (i != j) {
                        def count1 = element1.code.split(' ').length
                        def count2 = element2.code.split(' ').length
                        def distance = distance(element1.code, element2.code)
                        def result = ((count1 * count2 * distance)/count1) / 100
                        result = 1 - result
                        aux = element2.clone()
                        aux['__rate'] = result
                        auxIndex.add(aux)
                    }
                }
                auxTotal.add(auxIndex)
            }
            auxTotal.eachWithIndex {it, index ->
                similarity[index].put('__similarities', it)
            }


        } catch (Exception e) {
            e.printStackTrace()
            similarity['__response'] = "INVALID_FORMAT"
        }
        return  JsonOutput.toJson(similarity)



    }

    public static void main(String[] argv) {
        SimilarityVerifier evaluatorServer = new SimilarityVerifier("EXCHANGE", "similarity");


    }


}






