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
        def aux, auxIndex, auxTotal = [], errorResponse = [:]
        boolean err = false
        try {
            def jsonSlurper = new JsonSlurper()
            similarity = jsonSlurper.parseText(message)
        } catch (Exception e) {
            errorResponse['__errMSG'] = errorResponse['__errMSG']? errorResponse['__errMSG'] : []
            errorResponse['__status'] = "FAIL"
            errorResponse['__errMSG'] << "Invalid JSON format"
            return  JsonOutput.toJson(errorResponse)
        }
        println similarity
        println similarity.size()
        if (!similarity || similarity.size() == 0) {
            err = true
            errorResponse['__errMSG'] = errorResponse['__errMSG']? errorResponse['__errMSG'] : []
            errorResponse['__status'] = "FAIL"
            errorResponse['__errMSG'] << "Empty msg"
        } else if (similarity.size() == 1 && !similarity[0].code) {
            err = true
            errorResponse['__errMSG'] = errorResponse['__errMSG']? errorResponse['__errMSG'] : []
            errorResponse['__status'] = "FAIL"
            errorResponse['__errMSG'] << "Field 'code' cannot be empty"
        } else if (similarity.size() == 1) {
            return JsonOutput.toJson(similarity)
        } else {
            similarity.each {
                if(!it.code) {
                    err = true
                    errorResponse['__errMSG'] = errorResponse['__errMSG']? errorResponse['__errMSG'] : []
                    errorResponse['__status'] = "FAIL"
                    errorResponse['__errMSG'] << "Field 'code' cannot be empty"
                } else if (it.code.size() > 510) {
                    err = true
                    errorResponse['__errMSG'] = errorResponse['__errMSG']? errorResponse['__errMSG'] : []
                    errorResponse['__status'] = "FAIL"
                    errorResponse['__errMSG'] << "Field 'code' cannot be greather then 510 caracters"
                }
            }
        }

        try {
             if (!err) {
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

             }
        } catch (Exception e) {
            e.printStackTrace()
            errorResponse['__status'] = "FAIL"
            errorResponse['__response'] = "INVALID_FORMAT"
        }
        if (err) {
            return  JsonOutput.toJson(errorResponse)
        }
        return  JsonOutput.toJson(similarity)



    }

    public static void main(String[] argv) {
        SimilarityVerifier evaluatorServer = new SimilarityVerifier("EXCHANGE", "similarity");


    }


}






