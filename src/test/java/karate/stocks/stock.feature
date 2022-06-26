Feature: Integration Tests for Stocks
  Background:
    * url baseUrl
    * def create = read('../data/create.json')
    * def create_response = read('../data/create-result.json')
    * header Accept = 'application/json'
    	
    
  Scenario: Create a person

    Given path 'api/stocks'
    And request create
    And header Accept = 'application/json'
    When method post
    Then status 200
    And match response == create_response
    
  Scenario: Get stock we just created

    Given path 'api/stocks/1'
    When method GET
    Then status 200
    And match response == create_response