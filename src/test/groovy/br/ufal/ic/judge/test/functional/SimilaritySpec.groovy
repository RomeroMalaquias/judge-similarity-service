package br.ufal.ic.judge.test.functional

import groovy.json.JsonSlurper
import spock.lang.Specification

class SimilaritySpec extends Specification {


    def "Quando nenhum código é submetido"() {
        when:
        SimilarityTest similarityTest = new SimilarityTest("EXCHANGE", "similarity");
        similarityTest.start();
        similarityTest.call('[{}]');
        while (similarityTest.getLoop()) {
        }
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(similarityTest.getResponse())
        response['__status'] == 'FAIL'
        response['__errMSG'] != null
    }
    def "Quando se submete um codigo"() {
        when:
        SimilarityTest similarityTest = new SimilarityTest("EXCHANGE", "similarity");
        similarityTest.start();
        similarityTest.call('[{"code":"print \'Ola mundo\';"}]');
        while (similarityTest.getLoop()) {
        }
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(similarityTest.getResponse())
        response[0] != null
    }
    def "Quando se submete mais de um codigo"() {
        when:
        SimilarityTest similarityTest = new SimilarityTest("EXCHANGE", "similarity");
        similarityTest.start();
        similarityTest.call('[{"code":"print \'Ola mundo\';"}, {"code":"print \'Ola mundo\';"}, {"code":"print \'Ola mundo\';"}]');
        while (similarityTest.getLoop()) {
        }
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(similarityTest.getResponse())
        response[0] != null
    }

    def "Quando falta um código na lista"() {
        when:
        SimilarityTest similarityTest = new SimilarityTest("EXCHANGE", "similarity");
        similarityTest.start();
        similarityTest.call('[{"code":""}, {"code":"print \'Ola mundo\';"}, {"code":"print \'Ola mundo\';"}]');
        while (similarityTest.getLoop()) {
        }
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(similarityTest.getResponse())
        response['__status'] == 'FAIL'
        response['__errMSG'] != null
    }

    def "Quando um código na lista ultrapassa 510 caracteres"() {
        when:
        SimilarityTest similarityTest = new SimilarityTest("EXCHANGE", "similarity");
        similarityTest.start();
        similarityTest.call('[{"code":"a2romero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.com"}, {"code":"print \'Ola mundo\';"}, {"code":"print \'Ola mundo\';"}]');
        while (similarityTest.getLoop()) {
        }
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(similarityTest.getResponse())
        response['__status'] == 'FAIL'
        response['__errMSG'] != null
    }
}